package soy.gabimoreno.presentation.screen.home.view

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import soy.gabimoreno.R
import soy.gabimoreno.presentation.theme.Spacing

@Composable
fun ErrorView(
    text: String,
    onClick: () -> Unit,
) {
    Column(
        modifier = Modifier.padding(horizontal = Spacing.s24)
    ) {
        Text(
            text,
            modifier = Modifier
                .padding(horizontal = Spacing.s8)
                .padding(bottom = Spacing.s8)
        )
        TextButton(onClick = onClick) {
            Text(
                stringResource(R.string.try_again),
                style = MaterialTheme.typography.button,
            )
        }
    }
}
