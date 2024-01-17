package ui.components

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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ollama.models.Model

@Composable
fun ModelSelector(
    modifier: Modifier = Modifier,
    models: List<Model>,
    currentModel: Model? = null,
    selectedModel: (Model) -> Unit,
) {
    val width = 220.dp
    val shape = RoundedCornerShape(6.dp)
    var expandedDropdown by remember { mutableStateOf(false) }

    Column(modifier = modifier) {
        Row(
            modifier = Modifier
                .clip(shape)
                .clickable { expandedDropdown = !expandedDropdown }
                .padding(horizontal = 12.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.Bottom,
        ) {
            val name = currentModel?.name?.split(":")?.first() ?: "Select a model"
            val tag = currentModel?.name?.split(":")?.last() ?: ""

            Text(
                text = name,
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
            )

            Text(
                modifier = Modifier.padding(horizontal = 4.dp),
                text = tag,
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colors.onBackground.copy(alpha = 0.6f),
            )

            Icon(
                modifier = Modifier.size(16.dp),
                tint = Color(0xff888889),
                imageVector = Icons.Rounded.KeyboardArrowDown,
                contentDescription = "Expand model list menu"
            )
        }

        DropdownMenu(
            modifier = Modifier.widthIn(min = width).padding(horizontal = 6.dp),
            expanded = expandedDropdown,
            onDismissRequest = { expandedDropdown = false },
        ) {
            val onModelClick: (Model) -> Unit = {
                selectedModel(it)
                expandedDropdown = false
            }

            Text(
                modifier = Modifier.padding(vertical = 4.dp, horizontal = 6.dp),
                text = "Models",
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colors.onBackground,
            )
            models.forEach { model ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(shape)
                        .clickable { onModelClick(model) }
                        .padding(horizontal = 6.dp, vertical = 10.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        modifier = Modifier,
                        text = model.name,
                        color = MaterialTheme.colors.onBackground,
                    )

                    Checkbox(size = 16.dp, checked = model == currentModel, onCheckedChange = {}, enabled = false)
                }
            }
        }
    }
}
