package ui.screens.chat_history

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun ChatHistoryScreen(
    modifier: Modifier = Modifier,
    minWidth: Dp = 220.dp,
    expanded: Boolean,
    safePaddingValues: PaddingValues = PaddingValues(top = 36.dp)
) {
    AnimatedVisibility(expanded) {
        LazyColumn(modifier = modifier.fillMaxHeight().width(minWidth).padding(safePaddingValues)) {
            item {
                Text(text = "oi")
            }
        }
    }
}
