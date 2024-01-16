package ui.screens.chat

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowDownward
import androidx.compose.material.icons.rounded.ArrowUpward
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import ollama.OllamaState
import ollama.models.Model
import ui.components.IconButton
import ui.components.ModelSelector
import ui.components.TextInput
import utils.isScrolledToEnd
import utils.rememberViewModel
import utils.scrollToTheBottom

@Composable
fun ChatScreen(
    modifier: Modifier = Modifier,
    expanded: Boolean,
) {
    val viewModel: ChatViewModel = rememberViewModel()
    val uiState by viewModel.uiState.collectAsState()
    val ollamaState by viewModel.ollamaState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.fetchOllamaModelList()
    }

    ChatScreen(
        modifier = modifier,
        expanded = expanded,
        uiState = uiState,
        ollamaState = ollamaState,
        onSelectedModel = viewModel::updateSelectedModel,
        onSendPrompt = viewModel::sendPrompt,
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
) {
    val scrollState = rememberLazyListState()
    val endOfListReached by remember { derivedStateOf { scrollState.isScrolledToEnd() } }

    val expandedStartDpPadding: Dp by animateDpAsState(if (expanded) 40.dp else 0.dp)
    var prompt by remember { mutableStateOf("") }

    val scope = rememberCoroutineScope()

    LaunchedEffect(uiState.messages, uiState.generatedText) {
        if (endOfListReached) scrollToTheBottom(uiState.messages.size + 1, scrollState)
    }

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

            // Chat space
            Box(modifier = Modifier.fillMaxWidth().weight(1f)) {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    state = scrollState,
                    verticalArrangement = Arrangement.Bottom
                ) {
                    items(uiState.messages) { message ->
                        Row(
                            modifier = Modifier.padding(vertical = 8.dp),
                            horizontalArrangement = Arrangement.spacedBy(4.dp),
                            verticalAlignment = Alignment.Top,
                        ) {
                            Text(text = "${message.role.name}: ")

                            val content = message.content.ifEmpty {
                                uiState.generatedText ?: ""
                            }

                            Text(text = content)
                        }
                    }

                    item {
                        Spacer(modifier = Modifier.padding(bottom = 20.dp))
                    }
                }

                if (!endOfListReached) {
                    ScrollToBottomButton(
                        modifier = Modifier.align(Alignment.BottomCenter),
                        onClick = { scope.launch { scrollToTheBottom(uiState.messages.size + 1, scrollState) } },
                    )
                }
            }

            // Text input space
            ChatInputText(
                value = prompt,
                onValueChange = { prompt = it },
                onSend = { onSendPrompt(it); prompt = "" },
            )

        }

    }
}

@Composable
private fun ChatInputText(
    modifier: Modifier = Modifier,
    value: String,
    onValueChange: (String) -> Unit,
    onSend: (String) -> Unit,
) {
    TextInput(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier.fillMaxWidth(),
        maxLines = 8,
        innerPaddingValues = PaddingValues(vertical = 8.dp, horizontal = 10.dp),
        hint = {
            Text(
                text = "Message to model...",
                color = MaterialTheme.colors.onBackground.copy(alpha = 0.5f),
            )
        }
    ) {
        val enabled: Boolean = value.isNotEmpty()
        val backgroundColor = if (enabled) {
            MaterialTheme.colors.onBackground
        } else {
            MaterialTheme.colors.onBackground.copy(alpha = 0.1f)
        }
        val pointerIcon = if (enabled) PointerIcon.Hand else PointerIcon.Default

        IconButton(
            modifier = Modifier.pointerHoverIcon(icon = pointerIcon),
            enabled = enabled,
            backgroundColor = backgroundColor,
            onClick = { onSend(value) },
        ) {
            Icon(
                modifier = Modifier.padding(4.dp).size(20.dp),
                imageVector = Icons.Rounded.ArrowUpward,
                contentDescription = "Send input",
                tint = MaterialTheme.colors.background,
            )
        }
    }
}

@Composable
private fun ScrollToBottomButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
) {
    IconButton(
        modifier = modifier,
        onClick = onClick,
        border = BorderStroke(1.dp, color = MaterialTheme.colors.onBackground.copy(alpha = 0.2f)),
        backgroundColor = MaterialTheme.colors.background,
    ) {
        Icon(
            modifier = Modifier.padding(4.dp).size(20.dp),
            imageVector = Icons.Rounded.ArrowDownward,
            contentDescription = "Scroll to bottom of the chat",
            tint = MaterialTheme.colors.onBackground,
        )
    }
}

