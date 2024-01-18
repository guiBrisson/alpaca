package ui.screens.chat

import data.repository.ChatHistoryRepository
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
import utils.ChatSingleton
import utils.ViewModel

class ChatViewModel(
    private val chatRepository: ChatHistoryRepository,
) : ViewModel() {
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

    fun fetchChatHistoryMessages(chatHistoryId: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            _uiState.update { it.copy(loadingMessages = true, messages = emptyList()) }

            val (_, messages) = chatRepository.getHistoryById(chatHistoryId)
            _uiState.update {
                it.copy(
                    loadingMessages = false,
                    messages = messages?.map { ms -> Message.fromDataObject(ms) } ?: emptyList()
                )
            }
        }
    }

    fun sendPrompt(prompt: String) {
        addMessage(prompt, Role.USER)
        addMessageToDatabase(prompt, Role.USER)

        viewModelScope.launch(Dispatchers.IO) {
            _uiState.value.currentModel?.let { model ->
                var generatedText = ""
                addMessage("", Role.ASSISTANT)

                val updatedMessages = _uiState.value.messages.toMutableList()
                val newMessage = Message(role = Role.USER, content = prompt)
                updatedMessages.add(newMessage)

                ollama.chat(
                    messages = updatedMessages,
                    model = model.name,
                    onFinish = { lastMessage ->
                        val currentMessages = _uiState.value.messages.toMutableList()
                        val lastIndex = currentMessages.lastIndex
                        currentMessages.removeAt(lastIndex)
                        currentMessages.add(lastMessage)

                        _uiState.update { it.copy(generatedText = null, messages = currentMessages) }
                        addMessageToDatabase(lastMessage.content, Role.ASSISTANT)
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

    private fun addMessageToDatabase(text: String, role: Role) {
        viewModelScope.launch(Dispatchers.IO) {
            // Creating new chat when no chat is selected
            if (ChatSingleton.selectedChat.value == null) {
                _uiState.value.currentModel?.let { selectedModel ->
                    val newChatId = chatRepository.createNewChat(selectedModel.name)
                    val (newChat, _) = chatRepository.getHistoryById(newChatId)
                    ChatSingleton.selectedChat.value = newChat
                }
            }

            // Adding message to selected chat
            ChatSingleton.selectedChat.value?.id?.let { currentChatId ->
                chatRepository.addChatMessage(
                    chatHistoryId = currentChatId,
                    role = role.name.lowercase(),
                    content = text,
                )
                return@launch
            }
        }
    }

    fun stopOllama() {
        ollama.close()
    }

    override fun onDispose() {
        super.onDispose()
        ollama.close()
    }

}
