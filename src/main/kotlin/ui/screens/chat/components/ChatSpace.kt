package ui.screens.chat.components

import androidx.compose.foundation.VerticalScrollbar
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollbarAdapter
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material.ProvideTextStyle
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import com.halilibo.richtext.markdown.Markdown
import com.halilibo.richtext.ui.CodeBlockStyle
import com.halilibo.richtext.ui.RichTextStyle
import com.halilibo.richtext.ui.material.RichText
import com.halilibo.richtext.ui.resolveDefaults
import kotlinx.coroutines.launch
import ui.screens.chat.ChatUiState
import utils.isScrolledToEnd
import utils.scrollToTheBottom

@Composable
fun ChatSpace(
    modifier: Modifier = Modifier,
    uiState: ChatUiState,
) {
    val scope = rememberCoroutineScope()

    val scrollState = rememberLazyListState()
    val endOfListReached by remember { derivedStateOf { scrollState.isScrolledToEnd() } }

    //TODO: finish styling
    val richTextStyle by remember {
        mutableStateOf(
            RichTextStyle(
                codeBlockStyle = CodeBlockStyle(wordWrap = true),
                paragraphSpacing = 20.sp,
            ).resolveDefaults()
        )
    }

    LaunchedEffect(uiState.messages, uiState.generatedText) {
        if (endOfListReached) scrollToTheBottom(uiState.messages.size + 1, scrollState)
    }

    Box(modifier = modifier.fillMaxWidth()) {
        VerticalScrollbar(
            modifier = Modifier.align(Alignment.CenterEnd).offset(x = 10.dp),
            adapter = rememberScrollbarAdapter(scrollState),
        )
        SelectionContainer {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                state = scrollState,
                verticalArrangement = Arrangement.Bottom,
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

                        ProvideTextStyle(TextStyle(lineHeight = 1.3.em)) {
                            RichText(
                                style = richTextStyle,
                            ) {
                                Markdown(content = content)
                            }
                        }
                    }
                }

                item {
                    Spacer(modifier = Modifier.padding(bottom = 20.dp))
                }
            }
        }

        if (!endOfListReached) {
            ScrollToBottomButton(
                modifier = Modifier.align(Alignment.BottomCenter),
                onClick = { scope.launch { scrollToTheBottom(uiState.messages.size + 1, scrollState) } },
            )
        }
    }
}
