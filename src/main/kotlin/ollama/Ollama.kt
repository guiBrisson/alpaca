package ollama

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.utils.io.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.serialization.json.Json
import ollama.exceptions.OllamaModelNotFoundException
import ollama.exceptions.OllamaPullModelException
import ollama.exceptions.OllamaRunningException
import ollama.models.*
import ollama.utils.isSuccessful

class Ollama(
    val host: String = "localhost",
    val port: Int = 11434,
) {
    private val _currentState = MutableStateFlow<OllamaState>(OllamaState.IDLE)
    val currentState: StateFlow<OllamaState> = _currentState.asStateFlow()

    private val json = Json { ignoreUnknownKeys = true }

    private val client = HttpClient(CIO) {
        defaultRequest {
            url("http://${this@Ollama.host}:${this@Ollama.port}/api/")
        }

        install(ContentNegotiation) {
            json(json)
        }

        install(HttpTimeout) {
            requestTimeoutMillis = 300000 // 5min
        }
    }

    /**
     * @return All downloaded [Models]
     */
    suspend fun listModels(): Models {
        val response: HttpResponse = client.get(LIST_MODELS_ENDPOINT) {
            contentType(ContentType.Application.Json)
        }
        return response.body()
    }

    /**
     * Generates text based on the provided prompt and model.
     *
     * @param prompt The input prompt.
     * @param model The Ollama model to use for generation.
     * @param onFinish A callback function that is called when the generation is finished.
     * @return A [Flow] of generated text, where each element represents a completion response from the Ollama API.
     * @throws OllamaRunningException When Ollama is already running
     * @throws OllamaModelNotFoundException When model is not found
     */
    suspend fun generate(prompt: String, model: String, onFinish: (String) -> Unit = {}): Flow<String> {
        if (_currentState.value.isRunning()) throw OllamaRunningException()

        _currentState.update { OllamaState.RUNNING }

        val requestBuilder = client.preparePost(GENERATE_ENDPOINT) {
            contentType(ContentType.Application.Json)
            setBody(CompletionRequest(model = model, prompt = prompt))
        }

        return flow {
            var generatedText = ""
            requestBuilder.execute { response ->
                if (!response.isSuccessful() && response.status.value == 404) {
                    throw OllamaModelNotFoundException(model)
                }

                val channel: ByteReadChannel = response.body()
                while (!channel.isClosedForRead) {
                    channel.readUTF8Line()?.let {
                        val completion = json.decodeFromString<CompletionResponse>(it)
                        generatedText += completion.response
                        emit(completion.response)
                    }
                    delay(50)
                }
                onFinish(generatedText)
                _currentState.update { OllamaState.IDLE }
            }
        }
    }

    /**
     * Chat with an assistant using the Ollama API.
     *
     * @param messages The list of messages to send to the assistant.
     * @param model The name of the chatbot model to use.
     * @param onFinish A callback that is called when the chat session finishes.
     * @return A [Flow] of strings representing the chatbot's responses to the user's input.
     * @throws OllamaRunningException When Ollama is already running
     * @throws OllamaModelNotFoundException When model is not found
     */
    suspend fun chat(messages: List<Message>, model: String, onFinish: (List<Message>) -> Unit = {}): Flow<String> {
        if (_currentState.value.isRunning()) throw OllamaRunningException()

        _currentState.update { OllamaState.RUNNING }

        val requestBuilder = client.preparePost(CHAT_ENDPOINT) {
            contentType(ContentType.Application.Json)
            setBody(ChatRequest(model = model, messages = messages))
        }

        return flow {
            var generatedText = ""
            requestBuilder.execute { response ->
                if (!response.isSuccessful() && response.status.value == 404) {
                    throw OllamaModelNotFoundException(model)
                }

                val channel: ByteReadChannel = response.body()
                while (!channel.isClosedForRead) {
                    channel.readUTF8Line()?.let {
                        val chat = json.decodeFromString<ChatResponse>(it)
                        generatedText += chat.message.content
                        emit(chat.message.content)
                    }
                    delay(50)
                }
                val updatedMessages = messages.toMutableList()
                updatedMessages.add(Message(role = Role.ASSISTANT, content = generatedText))
                onFinish(updatedMessages)
                _currentState.update { OllamaState.IDLE }
            }
        }
    }

    /**
     * Show information about a model including details, modelfile, template, parameters, license, and system prompt.
     * @param model The name of the model to show
     * @return [ShowModel] The retrieved show model, or null if no such model exists.
     * @throws OllamaModelNotFoundException When model is not found
     */
    suspend fun show(model: String): ShowModel {
        val response = client.post(SHOW_MODEL_ENDPOINT) {
            contentType(ContentType.Application.Json)
            setBody(mapOf("name" to model))
        }

        if (!response.isSuccessful() && response.status.value == 404) {
            throw OllamaModelNotFoundException(model)
        }

        return response.body()
    }

    /**
     * Pulls a model from the Ollama server and emits events as they arrive.
     *
     * @param model The name of the model to pull.
     * @param onFinish A callback function that is called when the pull operation completes successfully.
     * @return A [Flow] of [PullResponse] objects, where each object represents an event emitted by the Ollama server.
     * @throws OllamaPullModelException if there was a problem pulling the model from the server.
     */
    suspend fun pull(model: String, onFinish: (PullResponse) -> Unit): Flow<PullResponse> {
        if (_currentState.value.isRunning()) throw OllamaRunningException()

        _currentState.update { OllamaState.RUNNING }

        val requestBuilder = client.preparePost(PULL_MODEL_ENDPOINT) {
            contentType(ContentType.Application.Json)
            setBody(mapOf("name" to model))
        }

        return flow {
            requestBuilder.execute { response ->
                val channel: ByteReadChannel = response.body()
                while (!channel.isClosedForRead) {
                    channel.readUTF8Line()?.let {
                        val pull = json.decodeFromString<PullResponse>(it)
                        emit(pull)

                        if (!pull.error.isNullOrEmpty()) throw OllamaPullModelException(message = pull.error)
                        if (pull.status.isSuccessful()) onFinish(pull)
                    }
                }
                _currentState.update { OllamaState.IDLE }
            }
        }
    }

    /**
     * Embeds the given prompt in a given model and returns the resulting embeddings.
     *
     * @param model The name of the Ollama model to use for embedding.
     * @param prompt The input text to be embedded.
     * @return An [EmbeddingResponse] containing the output embeddings.
     * @throws OllamaModelNotFoundException if the given model is not found in the server.
     */
    suspend fun embeddings(model: String, prompt: String): EmbeddingResponse {
        val response = client.post(EMBEDDING_ENDPOINT) {
            contentType(ContentType.Application.Json)
            setBody(EmbeddingRequest(model = model, prompt = prompt))
        }

        if (!response.isSuccessful() && response.status.value == 404) {
            throw OllamaModelNotFoundException(model)
        }

        return response.body()
    }

    /**
     * Closes client's stream and releases any system resources associated with it.
     * If the stream is already closed then invoking this method has no effect.
     */
    fun close() {
        _currentState.update { OllamaState.IDLE }
        client.close()
    }

    companion object {
        private const val LIST_MODELS_ENDPOINT = "tags"
        private const val GENERATE_ENDPOINT = "generate"
        private const val CHAT_ENDPOINT = "chat"
        private const val SHOW_MODEL_ENDPOINT = "show"
        private const val PULL_MODEL_ENDPOINT = "pull"
        private const val EMBEDDING_ENDPOINT = "embeddings"
    }
}

sealed interface OllamaState {
    data object IDLE : OllamaState
    data object RUNNING : OllamaState
}

fun OllamaState.isRunning(): Boolean {
    return this == OllamaState.RUNNING
}
