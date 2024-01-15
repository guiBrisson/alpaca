import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import kotlinx.coroutines.launch
import ollama.Ollama
import ollama.exceptions.OllamaModelNotFoundException
import ollama.models.Model
import ui.theme.AlpacaTheme

@Composable
@Preview
fun App() {
    val ollama = Ollama()
    var text by remember { mutableStateOf("") }
    val models = remember { mutableListOf<Model>() }
    val scope = rememberCoroutineScope()

    AlpacaTheme {
        Column(modifier = Modifier.fillMaxSize().background(MaterialTheme.colors.background)) {
            Button(onClick = {
                scope.launch {
                    ollama.generate(
                        prompt = "Write a hello world function in Golang",
                        model = "codellama",
                        onFinish = { println("generatedText: $it") },
                    ).collect {
                        text += it
                    }
                }
            }) {
                Text("Generate")
            }

            Text(text, color = MaterialTheme.colors.onBackground)

            Button(onClick = {
                scope.launch {
                    models.addAll(ollama.listModels().models)
                    println("models: $models")
                }
            }) {
                Text("get models")
            }

            Button(onClick = {
                scope.launch {
                    try {
                        val show = ollama.show("codellama")
                        println("show model: $show")
                    } catch (e: OllamaModelNotFoundException) {
                        e.printStackTrace()
                    }
                }
            }) {
                Text("show model codellama")
            }
        }
    }
}

fun main() = application {
    Window(onCloseRequest = ::exitApplication) {
        App()
    }
}
