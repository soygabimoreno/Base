package soy.gabimoreno.presentation.screen.premium

import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import kotlinx.coroutines.flow.collect
import soy.gabimoreno.R
import soy.gabimoreno.presentation.screen.ViewModelProvider
import soy.gabimoreno.presentation.theme.Spacing

@Composable
fun PremiumScreen() {
    val premiumViewModel = ViewModelProvider.premiumViewModel
    val showLogin = remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        premiumViewModel.onViewScreen()
        premiumViewModel.viewEventFlow.collect { viewEvent ->
            when (viewEvent) {
                is PremiumViewModel.ViewEvent.ShowLogin -> showLogin.value = true
            }
        }
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
        if (showLogin.value) {
            Spacer(modifier = Modifier.height(Spacing.s16))
            Text(text = stringResource(id = R.string.premium_login))
        }
    }
}
