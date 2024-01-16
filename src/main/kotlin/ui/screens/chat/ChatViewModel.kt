package ui.screens.chat

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ollama.Ollama
import ollama.models.Model
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
                it.copy(models = newList,)
            }

            if (newList.isNotEmpty()) updateSelectedModel(newList.first())
        }
    }

    fun updateSelectedModel(model: Model) {
        _uiState.update { it.copy(currentModel = model) }
    }

}
