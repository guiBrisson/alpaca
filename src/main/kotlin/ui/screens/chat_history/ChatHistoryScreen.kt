package ui.screens.chat_history

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import utils.ChatSingleton
import utils.rememberViewModel

@Composable
fun ChatHistoryScreen(
    modifier: Modifier = Modifier,
    minWidth: Dp = 220.dp,
    expanded: Boolean,
    safePaddingValues: PaddingValues = PaddingValues(top = 36.dp)
) {
    val viewModel: ChatHistoryViewModel = rememberViewModel()
    val uiState by viewModel.uiState.collectAsState()
    val selectedChat by remember { ChatSingleton.selectedChat }

    LaunchedEffect(Unit) {
        viewModel.fetchChatHistory()
    }

    AnimatedVisibility(expanded) {
        LazyColumn(modifier = modifier.fillMaxHeight().width(minWidth).padding(safePaddingValues)) {
            items(uiState.chatHistory) { chatHistory ->
                val backgroundColor = if (selectedChat == chatHistory) {
                    MaterialTheme.colors.onBackground.copy(alpha = 0.2f)
                } else {
                    Color.Unspecified
                }

                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 6.dp)
                        .clip(RoundedCornerShape(4.dp))
                        .background(backgroundColor)
                        .clickable { viewModel.selectChat(chatHistory) }
                        .padding(8.dp),
                    text = chatHistory.createdAt.toString(),
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Medium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
            }
        }
    }
}
