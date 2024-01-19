package ui.screens.chat.components

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowUpward
import androidx.compose.material.icons.rounded.Stop
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.unit.dp
import ollama.OllamaState
import ollama.isRunning
import ui.components.IconButton
import ui.components.TextInput

@Composable
fun ChatInputText(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    ollamaState: OllamaState,
    onStopGenerate: () -> Unit,
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
                style = MaterialTheme.typography.body1.copy(
                    color = MaterialTheme.colors.onBackground.copy(alpha = 0.5f),
                ),
            )
        }
    ) {
        val enabled: Boolean = ollamaState.isRunning() || value.isNotEmpty()
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
            onClick = { if (ollamaState.isRunning()) onStopGenerate() else onSend(value) },
        ) {
            if (ollamaState.isRunning()) {
                Icon(
                    modifier = Modifier.padding(4.dp).size(20.dp),
                    imageVector = Icons.Rounded.Stop,
                    contentDescription = "Stop generating prompt",
                    tint = MaterialTheme.colors.background,
                )
            } else {
                Icon(
                    modifier = Modifier.padding(4.dp).size(20.dp),
                    imageVector = Icons.Rounded.ArrowUpward,
                    contentDescription = "Send input",
                    tint = MaterialTheme.colors.background,
                )
            }
        }
    }
}

