package soy.gabimoreno.presentation.screen.premium

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Error
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.TextFieldValue
import soy.gabimoreno.framework.toast

@Composable
fun LoginOutlinedTextField(
    value: TextFieldValue,
    placeholderText: String,
    showError: Boolean,
    errorText: String,
    onValueChange: (TextFieldValue) -> Unit
) {
    OutlinedTextField(
        value = value,
        onValueChange = { onValueChange(it) },
        placeholder = { Text(text = placeholderText) },
        trailingIcon = {
            if (showError) {
                val context = LocalContext.current
                Icon(
                    Icons.Filled.Error,
                    errorText,
                    tint = MaterialTheme.colors.error,
                    modifier = Modifier.clickable {
                        context.toast(errorText)
                    }
                )
            }
        },
        isError = showError,
        modifier = Modifier
            .fillMaxWidth()
    )
}
