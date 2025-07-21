package soy.gabimoreno.presentation.screen.premium.view

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Error
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import soy.gabimoreno.framework.toast
import soy.gabimoreno.presentation.theme.White

@Composable
fun LoginOutlinedTextField(
    value: String,
    placeholderText: String,
    showError: Boolean,
    errorText: String,
    keyboardOptions: KeyboardOptions =
        KeyboardOptions(
            autoCorrectEnabled = false,
            keyboardType = KeyboardType.Email,
            imeAction = ImeAction.Next,
        ),
    keyboardActions: KeyboardActions = KeyboardActions(),
    visualTransformation: VisualTransformation = VisualTransformation.None,
    leadingIcon: ImageVector,
    onValueChange: (String) -> Unit,
) {
    OutlinedTextField(
        value = value,
        onValueChange = { onValueChange(it) },
        placeholder = { Text(text = placeholderText) },
        maxLines = 1,
        leadingIcon = { Icon(leadingIcon, placeholderText) },
        trailingIcon = {
            if (showError) {
                val context = LocalContext.current
                Icon(
                    Icons.Filled.Error,
                    errorText,
                    tint = MaterialTheme.colors.error,
                    modifier =
                        Modifier.clickable {
                            context.toast(errorText)
                        },
                )
            }
        },
        isError = showError,
        visualTransformation = visualTransformation,
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions,
        modifier =
            Modifier
                .fillMaxWidth(),
        colors =
            TextFieldDefaults.outlinedTextFieldColors(
                backgroundColor = White.copy(alpha = 0.1f),
            ),
    )
}
