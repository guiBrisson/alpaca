package ui.screens.chat

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ollama.Ollama
import ollama.models.Message
import ollama.models.Model
import ollama.models.Role
import utils.ViewModel

class ChatViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(ChatUiState())
    val uiState: StateFlow<ChatUiState> = _uiState.asStateFlow()

    private val ollama = Ollama()
    val ollamaState = ollama.currentState

    fun fetchOllamaModelList() {
        viewModelScope.launch(Dispatchers.IO) {
            val newList = ollama.listModels().models
            _uiState.update {
                it.copy(models = newList)
            }

            if (newList.isNotEmpty()) updateSelectedModel(newList.first())
        }
    }

    fun updateSelectedModel(model: Model) {
        _uiState.update { it.copy(currentModel = model) }
    }

    fun sendPrompt(prompt: String) {
        addMessage(prompt, Role.USER)

        viewModelScope.launch(Dispatchers.IO) {
            _uiState.value.currentModel?.let { model ->
                var generatedText = ""
                addMessage(text = "", role = Role.ASSISTANT)

                ollama.generate(
                    prompt = prompt,
                    model = model.name,
                    onFinish = { text ->
                        val currentMessages = _uiState.value.messages.toMutableList()
                        currentMessages.last().content = text
                        _uiState.update { it.copy(generatedText = null, messages = currentMessages) }
                    },
                ).collect { text ->
                    generatedText += text
                    _uiState.update { it.copy(generatedText = generatedText) }
                }
            }
        }
    }

    private fun addMessage(text: String, role: Role) {
        val message = Message(role = role, content = text)
        val currentMessages = _uiState.value.messages.toMutableList()
        currentMessages.add(message)

        _uiState.update { it.copy(messages = currentMessages) }
    }

    fun stopOllama() {
        ollama.close()
    }

    override fun onDispose() {
        super.onDispose()
        ollama.close()
    }

}
