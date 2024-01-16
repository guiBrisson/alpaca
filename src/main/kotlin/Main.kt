import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Menu
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import di.viewModelModule
import org.koin.core.context.startKoin
import ui.screens.chat.ChatScreen
import ui.screens.chat_history.ChatHistoryScreen
import ui.components.IconButton
import ui.theme.AlpacaTheme

@Composable
fun App() {
    var historyChatExpanded by remember { mutableStateOf(true) }

    AlpacaTheme {
        Surface(color = MaterialTheme.colors.background) {
            Box {
                Row(modifier = Modifier.fillMaxSize()) {
                    ChatHistoryScreen(expanded = historyChatExpanded)
                    ChatScreen(modifier = Modifier.weight(1f), expanded = !historyChatExpanded)
                }

                // expand/collapse side menu button
                IconButton(
                    modifier = Modifier.align(Alignment.TopStart),
                    onClick = { historyChatExpanded = !historyChatExpanded },
                    icon = {
                        Icon(
                            modifier = Modifier.padding(4.dp).size(24.dp),
                            imageVector = Icons.Rounded.Menu,
                            contentDescription = "Expand/collapse side menu",
                            tint = Color(0xff888889),
                        )
                    },
                )
            }
        }
    }
}

fun main() = application {
    val state = rememberWindowState()

    startKoin {
        modules(viewModelModule)
    }

    Window(onCloseRequest = ::exitApplication, state = state) {
        App()
    }
}
