package ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.dp

@Composable
fun IconButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    backgroundColor: Color = Color.Unspecified,
    shape: Shape = RoundedCornerShape(6.dp),
    border: BorderStroke = BorderStroke(width = 0.dp, color = Color.Unspecified),
    enabled: Boolean = true,
    icon: @Composable BoxScope.() -> Unit,
) {
    Box(
        modifier = modifier
            .padding(4.dp)
            .clip(shape)
            .border(border, shape)
            .background(color = backgroundColor)
            .clickable(enabled = enabled) { if (enabled) onClick() },
        contentAlignment = Alignment.Center,
        content =  icon,
    )
}
