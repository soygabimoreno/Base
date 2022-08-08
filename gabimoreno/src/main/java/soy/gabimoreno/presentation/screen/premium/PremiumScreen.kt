package soy.gabimoreno.presentation.screen.premium

import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import soy.gabimoreno.R
import soy.gabimoreno.presentation.screen.ViewModelProvider
import soy.gabimoreno.presentation.theme.Spacing

@Composable
fun PremiumScreen() {

    val premiumViewModel = ViewModelProvider.premiumViewModel

    LaunchedEffect(Unit) {
        premiumViewModel.onViewScreen()
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(Spacing.s16)
    ) {
        Text(
            text = stringResource(id = R.string.nav_item_premium).uppercase(),
            style = MaterialTheme.typography.h6
        )
        Spacer(modifier = Modifier.height(Spacing.s16))
        Text(text = stringResource(id = R.string.premium_description))
    }
}
