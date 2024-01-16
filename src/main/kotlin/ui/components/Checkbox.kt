package ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Check
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun Checkbox(
    modifier: Modifier = Modifier,
    size: Dp = 18.dp,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    enabled: Boolean = true,
) {
    val borderStroke = if (!checked) {
        BorderStroke(width = 2.dp, color = MaterialTheme.colors.onBackground.copy(alpha = 0.6f))
    } else {
        BorderStroke(width = 0.dp, color = Color.Unspecified)
    }

    val backgroundColor = if (checked) MaterialTheme.colors.onBackground else Color.Unspecified

    Box(
        modifier = modifier
            .size(size)
            .border(border = borderStroke, shape = CircleShape)
            .clip(CircleShape)
            .background(backgroundColor)
            .clickable { if (enabled) onCheckedChange(!checked) },
        contentAlignment = Alignment.Center,
    ) {
        if (checked) {
            Icon(
                modifier = Modifier.size(size - 2.dp),
                imageVector = Icons.Rounded.Check,
                contentDescription = null,
                tint = MaterialTheme.colors.background,
            )
        }
    }
}
