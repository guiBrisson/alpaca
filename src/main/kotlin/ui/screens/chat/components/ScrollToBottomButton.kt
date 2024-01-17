package ui.screens.chat.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowDownward
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ui.components.IconButton

@Composable
fun ScrollToBottomButton(
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