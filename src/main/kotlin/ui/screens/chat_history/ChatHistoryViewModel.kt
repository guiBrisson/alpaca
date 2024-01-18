package ui.screens.chat_history

import data.model.ChatHistory
import data.repository.ChatHistoryRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import utils.ChatSingleton
import utils.ViewModel

class ChatHistoryViewModel(
    private val chatHistoryRepository: ChatHistoryRepository,
): ViewModel() {
    private val _uiState = MutableStateFlow(ChatHistoryUiState())
    val uiState: StateFlow<ChatHistoryUiState> = _uiState.asStateFlow()

    fun fetchChatHistory() {
        viewModelScope.launch(Dispatchers.IO) {
            val chatHistory = chatHistoryRepository.getHistoryList()
            _uiState.update { it.copy(chatHistory = chatHistory) }
        }
    }

    fun selectChat(chatHistory: ChatHistory) {
        ChatSingleton.selectedChat.value = chatHistory
    }
}
