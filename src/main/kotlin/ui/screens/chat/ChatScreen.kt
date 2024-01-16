package ui.screens.chat

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Send
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import ui.components.IconButton
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

    Column(
        modifier = modifier.fillMaxHeight().padding(top = 4.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        ModelSelector(
            modifier = Modifier.padding(start = expandedStartDpPadding).fillMaxWidth(),
            currentModel = uiState.currentModel,
            models = uiState.models,
            selectedModel = viewModel::updateSelectedModel,
        )

        Column(
            modifier = Modifier
                .fillMaxHeight()
                .padding(start = 20.dp, end = 20.dp, bottom = 20.dp)
                .widthIn(max = 820.dp, min = 360.dp),
        ) {

            // Chat space
            LazyColumn(modifier = Modifier.fillMaxWidth().weight(1f)) {

            }

            // Text input space
            Row(modifier = Modifier.fillMaxWidth()) {
                IconButton(onClick = { /*TODO*/ }) {
                    Icon(
                        modifier = Modifier.padding(4.dp).size(24.dp),
                        imageVector = Icons.Rounded.Send,
                        contentDescription = "Send input",
                    )
                }
            }
        }

    }
}
