package ui.components

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.DropdownMenu
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.KeyboardArrowDown
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ollama.models.Model

@Composable
fun ModelSelector(
    modifier: Modifier = Modifier,
    models: List<Model>,
    selectedModel: (Model) -> Unit,
) {
    var text by remember { mutableStateOf("Select a model") }
    val borderColor = Color(0xffe4e4e7)
    val width = 220.dp
    val shape = RoundedCornerShape(6.dp)
    var expandedDropdown by remember { mutableStateOf(false) }

    Column {
        Row(
            modifier = modifier
                .width(width)
                .pointerHoverIcon(PointerIcon.Hand)
                .clip(shape)
                .clickable { expandedDropdown = !expandedDropdown }
                .border(width = 1.dp, color = borderColor, shape = shape)
                .padding(horizontal = 12.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = text,
                fontWeight = FontWeight.Medium,
                letterSpacing = 0.sp,
            )
            Icon(
                modifier = Modifier.size(16.dp).padding(0.dp),
                imageVector = Icons.Rounded.KeyboardArrowDown,
                contentDescription = "open model selector",
                tint = Color(0xff888889),
            )
        }

        DropdownMenu(
            modifier = Modifier.widthIn(min = width).padding(horizontal = 6.dp),
            expanded = expandedDropdown,
            onDismissRequest = { expandedDropdown = false },
        ) {
            val onModelClick: (Model) -> Unit = {
                selectedModel(it)
                text = it.name
                expandedDropdown = false
            }

            Text(
                modifier = Modifier.padding(vertical = 4.dp, horizontal = 6.dp),
                text = "Models",
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colors.onBackground,
            )
            models.forEach { model ->
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(shape)
                        .clickable { onModelClick(model) }
                        .padding(horizontal = 6.dp, vertical = 10.dp),
                    text = model.name,
                    color = MaterialTheme.colors.onBackground,
                )
            }
        }
    }
}
