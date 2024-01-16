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

    private val _generatedText = MutableStateFlow("")

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

    fun sendPrompt(prompt: String) {
        viewModelScope.launch(Dispatchers.IO) {
            _uiState.value.currentModel?.let { model ->
                ollama.generate(model = model.name, prompt = prompt).collect { text ->
                    _generatedText.value += text
                    _uiState.update { it.copy(generatedText = _generatedText.value) }
                }
            }
        }
    }

    override fun onDispose() {
        super.onDispose()
        ollama.close()
    }

}
