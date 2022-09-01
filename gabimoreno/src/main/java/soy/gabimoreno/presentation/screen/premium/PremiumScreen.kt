package soy.gabimoreno.presentation.screen.premium

import androidx.compose.foundation.layout.*
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.TextFieldValue
import kotlinx.coroutines.flow.collect
import soy.gabimoreno.R
import soy.gabimoreno.presentation.screen.ViewModelProvider
import soy.gabimoreno.presentation.theme.Spacing
import soy.gabimoreno.presentation.ui.PrimaryButton

@Composable
fun PremiumScreen() {
    val premiumViewModel = ViewModelProvider.premiumViewModel
    var showLogin by remember { mutableStateOf(false) }
    var showInvalidEmailFormatError by remember { mutableStateOf(false) }
    var showInvalidPasswordError by remember { mutableStateOf(false) }
    var showPremium by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        premiumViewModel.onViewScreen()
        premiumViewModel.viewEventFlow.collect { viewEvent ->
            when (viewEvent) {
                is PremiumViewModel.ViewEvent.ShowLogin -> {
                    showLogin = true
                }
                PremiumViewModel.ViewEvent.ShowInvalidEmailFormatError -> {
                    showInvalidPasswordError = false
                    showInvalidEmailFormatError = true
                }
                PremiumViewModel.ViewEvent.ShowInvalidPasswordError -> {
                    showInvalidEmailFormatError = false
                    showInvalidPasswordError = true
                }
                PremiumViewModel.ViewEvent.ShowPremium -> {
                    showInvalidEmailFormatError = false
                    showInvalidPasswordError = false
                    showPremium = true
                }
            }
        }
    }

    val showLoading = !showLogin
    ShowLoading(showLoading)

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(Spacing.s16)
    ) {
        Text(
            text = stringResource(id = R.string.nav_item_premium).uppercase(),
            style = MaterialTheme.typography.h5
        )
        Spacer(modifier = Modifier.height(Spacing.s16))
        Text(text = stringResource(id = R.string.premium_description))
        Spacer(modifier = Modifier.height(Spacing.s40))
        Text(
            text = stringResource(id = R.string.premium_login).uppercase(),
            style = MaterialTheme.typography.h6
        )
        if (showLogin && !showPremium) {
            Spacer(modifier = Modifier.height(Spacing.s16))
            var email by remember { mutableStateOf(TextFieldValue("")) }
            LoginOutlinedTextField(
                email,
                stringResource(id = R.string.premium_email),
                showInvalidEmailFormatError,
                stringResource(id = R.string.premium_email_error_invalid_format)
            ) {
                email = it
            }
            Spacer(modifier = Modifier.height(Spacing.s16))
            var password by remember { mutableStateOf(TextFieldValue("")) }
            LoginOutlinedTextField(
                password,
                stringResource(id = R.string.premium_password),
                showInvalidPasswordError,
                stringResource(id = R.string.premium_password_error_invalid)
            ) {
                password = it
            }
            Spacer(modifier = Modifier.height(Spacing.s16))
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.fillMaxWidth()
            ) {
                PrimaryButton(
                    text = stringResource(id = R.string.premium_login),
                    height = Spacing.s48
                ) {
                    premiumViewModel.onLoginClicked(email.text, password.text)
                }
            }
        }

        if (showPremium) {
            Text(text = "PREMIUM")
        }
    }
}

@Composable
private fun ShowLoading(showLoading: Boolean) {
    if (showLoading) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            CircularProgressIndicator()
        }
    }
}
