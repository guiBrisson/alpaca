import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Menu
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import ollama.presentation.chat.ChatScreen
import ollama.presentation.chat_history.ChatHistoryScreen
import ui.theme.AlpacaTheme

@Composable
fun App() {
    var historyChatExpanded by remember { mutableStateOf(true) }

    AlpacaTheme {
        Surface(color = MaterialTheme.colors.background) {
            Box {
                Row(modifier = Modifier.fillMaxSize()) {
                    ChatHistoryScreen(expanded = historyChatExpanded)
                    ChatScreen(modifier = Modifier.weight(1f))
                }

                // expand/collapse side menu button
                Box(
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .padding(4.dp)
                        .clip(RoundedCornerShape(6.dp))
                        .clickable { historyChatExpanded = !historyChatExpanded },
                    contentAlignment = Alignment.Center,
                ) {
                    Icon(
                        modifier = Modifier.padding(4.dp).size(24.dp),
                        imageVector = Icons.Rounded.Menu,
                        contentDescription = "Expand/collapse side menu",
                        tint = Color(0xff888889),
                    )
                }
            }
        }
    }
}

fun main() = application {
    val state = rememberWindowState()

    Window(onCloseRequest = ::exitApplication, state = state) {
        App()
    }
}
