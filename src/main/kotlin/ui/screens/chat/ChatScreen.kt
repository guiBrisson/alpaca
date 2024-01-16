package ui.screens.chat

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import ui.components.ModelSelector
import utils.rememberViewModel

@Composable
fun ChatScreen(
    modifier: Modifier = Modifier,
    expanded: Boolean,
) {
    val viewModel: ChatViewModel = rememberViewModel()
    val uiState by viewModel.uiState.collectAsState()
    val ollamaState by viewModel.ollamaState.collectAsState()

    val expandedStartDpPadding: Dp by animateDpAsState(if (expanded) 40.dp else 0.dp)

    LaunchedEffect(Unit) {
        viewModel.fetchOllamaModelList()
    }

    Column(modifier = modifier.fillMaxHeight().padding(top = 4.dp)) {
        ModelSelector(
            modifier = Modifier.padding(start = expandedStartDpPadding),
            currentModel = uiState.currentModel,
            models = uiState.models,
            selectedModel = viewModel::updateSelectedModel,
        )
    }
}
