package ui.components

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun TextInput(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    readOnly: Boolean = false,
    singleLine: Boolean = false,
    maxLines: Int = if (singleLine) 1 else Int.MAX_VALUE,
    minLines: Int = 1,
    innerPaddingValues: PaddingValues = PaddingValues(12.dp),
    hint: @Composable () -> Unit,
    trailingButton: @Composable() (() -> Unit)? = null,
) {
    BasicTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier,
        enabled = enabled,
        readOnly = readOnly,
        singleLine = singleLine,
        maxLines = maxLines,
        minLines = minLines,
        textStyle = MaterialTheme.typography.body1,
        decorationBox = { innerTextField ->
            Row(
                modifier = Modifier
                    .border(
                        width = 1.dp,
                        color = MaterialTheme.colors.onBackground.copy(alpha = 0.3f),
                        shape = RoundedCornerShape(12.dp)
                    )
                    .padding(innerPaddingValues),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.CenterStart) {
                    if (value.isEmpty()) hint()
                    innerTextField()
                }

                trailingButton?.let { it() }
            }
        }
    )
}