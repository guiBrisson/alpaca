package ui.screens.chat

import ollama.models.Message
import ollama.models.Model

data class ChatUiState(
    val models: List<Model> = emptyList(),
    val currentModel: Model? = null,
    val generatedText: String? = null,

    val loadingMessages: Boolean = false,
    val messages: List<Message> = emptyList(),
)
