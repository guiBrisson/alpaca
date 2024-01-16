package ui.screens.chat

import ollama.models.Model

data class ChatUiState(
    val models: List<Model> = emptyList(),
    val currentModel: Model? = null,
    val generatedText: String? = null,
)
