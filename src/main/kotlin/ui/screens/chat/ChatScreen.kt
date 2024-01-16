package ui.screens.chat

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import ollama.Ollama
import ollama.models.Model
import ui.components.ModelSelector

@Composable
fun ChatScreen(
    modifier: Modifier = Modifier,
    expanded: Boolean,
) {
    val ollama = Ollama()
    val list = remember { mutableListOf<Model>() }
    val expandedStartDpPadding: Dp by animateDpAsState(if (expanded) 40.dp else 0.dp)
    var currentModel by remember { mutableStateOf<Model?>(null) }

    // TODO: move this to a viewModel
    LaunchedEffect(Unit) {
        list.addAll(ollama.listModels().models)

        if (list.isNotEmpty()) currentModel = list.first()
    }

    Column(modifier = modifier.fillMaxHeight().padding(top = 4.dp)) {
        ModelSelector(
            modifier = Modifier.padding(start = expandedStartDpPadding),
            currentModel = currentModel,
            models = list.toList(),
            selectedModel = { currentModel = it }
        )
    }
}
