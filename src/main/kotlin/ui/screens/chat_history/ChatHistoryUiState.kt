package ui.screens.chat_history

import data.model.ChatHistory

data class ChatHistoryUiState(
    val chatHistory: List<ChatHistory> = emptyList(),
)
