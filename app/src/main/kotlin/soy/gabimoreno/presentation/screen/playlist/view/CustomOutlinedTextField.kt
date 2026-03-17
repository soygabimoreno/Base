package soy.gabimoreno.presentation.screen.playlist.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import soy.gabimoreno.presentation.theme.GabiMorenoTheme
import soy.gabimoreno.presentation.theme.PurpleDark
import soy.gabimoreno.presentation.theme.Spacing
import soy.gabimoreno.presentation.theme.White

@Composable
fun CustomOutlinedTextField(
    value: String,
    placeholderText: String,
    errorText: String?,
    onValueChange: (String) -> Unit,
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Text(
            modifier = Modifier.fillMaxWidth(),
            text = errorText ?: "",
            color = MaterialTheme.colors.error,
        )
        OutlinedTextField(
            value = value,
            modifier = Modifier.fillMaxWidth(),
            onValueChange = { onValueChange(it) },
            placeholder = { Text(text = placeholderText) },
            maxLines = 1,
            isError = errorText != null,
            colors =
                TextFieldDefaults.outlinedTextFieldColors(
                    backgroundColor = White.copy(alpha = 0.95f),
                    focusedBorderColor = White,
                    unfocusedBorderColor = White.copy(alpha = 0.5f),
                    textColor = PurpleDark,
                    placeholderColor = PurpleDark.copy(alpha = 0.8f),
                ),
        )
    }
}

@Preview(
    showBackground = true,
    showSystemUi = true,
)
@Composable
private fun CustomOutlinedTextFieldPreview() {
    GabiMorenoTheme {
        Column(
            modifier =
                Modifier
                    .fillMaxSize()
                    .padding(Spacing.s16),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            CustomOutlinedTextField(
                value = "",
                placeholderText = "Title",
                errorText = "No puede estar vacio",
                onValueChange = {},
            )
        }
    }
}
