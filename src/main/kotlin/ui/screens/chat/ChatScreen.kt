package ui.screens.chat

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import ollama.OllamaState
import ollama.models.Model
import ui.components.ModelSelector
import ui.screens.chat.components.ChatInputText
import ui.screens.chat.components.ChatSpace
import utils.ChatSingleton
import utils.rememberViewModel

@Composable
fun ChatScreen(
    modifier: Modifier = Modifier,
    expanded: Boolean,
) {
    val viewModel: ChatViewModel = rememberViewModel()
    val uiState by viewModel.uiState.collectAsState()
    val ollamaState by viewModel.ollamaState.collectAsState()
    val selectedChat by remember { ChatSingleton.selectedChat }

    LaunchedEffect(Unit) {
        viewModel.fetchOllamaModelList()
    }

    LaunchedEffect(selectedChat) {
        selectedChat?.id?.let { viewModel.fetchChatHistoryMessages(it) }
    }

    ChatScreen(
        modifier = modifier,
        expanded = expanded,
        uiState = uiState,
        ollamaState = ollamaState,
        onSelectedModel = viewModel::updateSelectedModel,
        onSendPrompt = viewModel::sendPrompt,
        onStopGenerate = viewModel::stopOllama,
    )
}

@Composable
internal fun ChatScreen(
    modifier: Modifier = Modifier,
    expanded: Boolean,
    uiState: ChatUiState,
    ollamaState: OllamaState,
    onSelectedModel: (Model) -> Unit,
    onSendPrompt: (String) -> Unit,
    onStopGenerate: () -> Unit,
) {
    val expandedStartDpPadding: Dp by animateDpAsState(if (expanded) 40.dp else 0.dp)
    var prompt by remember { mutableStateOf("") }

    Column(
        modifier = modifier.fillMaxHeight().padding(top = 4.dp).requiredWidthIn(min = 400.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        ModelSelector(
            modifier = Modifier.padding(start = expandedStartDpPadding).fillMaxWidth(),
            currentModel = uiState.currentModel,
            models = uiState.models,
            selectedModel = onSelectedModel,
        )

        Column(
            modifier = Modifier
                .fillMaxHeight()
                .padding(start = 20.dp, end = 20.dp, bottom = 20.dp)
                .widthIn(max = 820.dp),
        ) {

            ChatSpace(modifier = Modifier.weight(1f), uiState = uiState)

            ChatInputText(
                ollamaState = ollamaState,
                value = prompt,
                onValueChange = { prompt = it },
                onSend = { onSendPrompt(it); prompt = "" },
                onStopGenerate = onStopGenerate,
            )

        }

    }
}

