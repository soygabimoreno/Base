package soy.gabimoreno.presentation.screen.premium

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.*
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.TextFieldValue
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.first
import soy.gabimoreno.R
import soy.gabimoreno.framework.datastore.getEmail
import soy.gabimoreno.framework.datastore.getPassword
import soy.gabimoreno.framework.toast
import soy.gabimoreno.presentation.screen.ViewModelProvider
import soy.gabimoreno.presentation.screen.premium.view.LoginOutlinedTextField
import soy.gabimoreno.presentation.theme.Black
import soy.gabimoreno.presentation.theme.Spacing
import soy.gabimoreno.presentation.ui.button.PrimaryButton
import soy.gabimoreno.presentation.ui.button.SecondaryButton

@Composable
fun PremiumScreen() {
    val context = LocalContext.current

    val premiumViewModel = ViewModelProvider.premiumViewModel

    var email by remember { mutableStateOf(EMPTY_EMAIL) }
    var password by remember { mutableStateOf(EMPTY_PASSWORD) }

    var showLoading by remember { mutableStateOf(true) }
    var showAccess by remember { mutableStateOf(false) }
    var showInvalidEmailFormatError by remember { mutableStateOf(false) }
    var showInvalidPasswordError by remember { mutableStateOf(false) }
    var showPremium by remember { mutableStateOf(false) }
    var showGenerateAuthCookieError by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        premiumViewModel.onViewScreen(
            email = context.getEmail().first(),
            password = context.getPassword().first()
        )
        premiumViewModel.viewEventFlow.collect { viewEvent ->
            when (viewEvent) {
                is PremiumViewModel.ViewEvent.ShowAccess -> {
                    showLoading = false
                    showAccess = true

                    email = viewEvent.email
                    password = viewEvent.password
                }
                is PremiumViewModel.ViewEvent.ShowAccessAgain -> {
                    showAccess = true
                    showPremium = false
                }
                PremiumViewModel.ViewEvent.ShowLoading -> {
                    showLoading = true
                }
                PremiumViewModel.ViewEvent.HideLoading -> {
                    showLoading = false
                }
                PremiumViewModel.ViewEvent.ShowInvalidEmailFormatError -> {
                    showInvalidPasswordError = false
                    showInvalidEmailFormatError = true
                }
                PremiumViewModel.ViewEvent.ShowInvalidPasswordError -> {
                    showInvalidEmailFormatError = false
                    showInvalidPasswordError = true
                }
                is PremiumViewModel.ViewEvent.ShowPremium -> {
                    showInvalidEmailFormatError = false
                    showInvalidPasswordError = false
                    showPremium = true
                    premiumViewModel.saveCredentialsInDataStore(viewEvent.email, viewEvent.password)
                }
                PremiumViewModel.ViewEvent.ShowGenerateAuthCookieError -> {
                    showGenerateAuthCookieError = true
                }
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
            style = MaterialTheme.typography.h5
        )
        Spacer()
        Text(text = stringResource(id = R.string.premium_description))
        Spacer(modifier = Modifier.height(Spacing.s40))
        if (showAccess && !showPremium) {
            Text(
                text = stringResource(id = R.string.premium_login).uppercase(),
                style = MaterialTheme.typography.h6
            )
            Spacer()
            var emailTextFieldValue by remember { mutableStateOf(TextFieldValue(email)) }
            LoginOutlinedTextField(
                emailTextFieldValue,
                stringResource(id = R.string.premium_email),
                showInvalidEmailFormatError,
                stringResource(id = R.string.premium_email_error_invalid_format)
            ) {
                emailTextFieldValue = it
            }
            Spacer()
            var passwordTextFieldValue by remember { mutableStateOf(TextFieldValue(password)) }
            LoginOutlinedTextField(
                passwordTextFieldValue,
                stringResource(id = R.string.premium_password),
                showInvalidPasswordError,
                stringResource(id = R.string.premium_password_error_invalid)
            ) {
                passwordTextFieldValue = it
            }
            Spacer()
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.fillMaxWidth()
            ) {
                PrimaryButton(
                    text = stringResource(id = R.string.premium_login),
                    height = Spacing.s48
                ) {
                    premiumViewModel.onLoginClicked(
                        emailTextFieldValue.text,
                        passwordTextFieldValue.text
                    )
                }
            }
        }

        if (showPremium) {
            Spacer()
            Text(text = stringResource(id = R.string.premium_premium).uppercase()) // TODO: This is provisional
            Spacer()
            SecondaryButton(
                text = stringResource(id = R.string.premium_logout),
                height = Spacing.s48
            ) {
                premiumViewModel.onLogoutClicked()
            }
        }

        if (showGenerateAuthCookieError) {
            showGenerateAuthCookieError = false
            context.toast(stringResource(id = R.string.premium_error_generate_auth_cookie))
        }
    }

    ShowLoading(showLoading)
}

@Composable
private fun ShowLoading(showLoading: Boolean) {
    if (showLoading) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .background(brush = SolidColor(Black), alpha = 0.2f)
                .fillMaxSize()
                .focusable()
                .clickable {}
        ) {
            CircularProgressIndicator()
        }
    }
}

@Composable
private fun Spacer() {
    Spacer(modifier = Modifier.height(Spacing.s16))
}

private const val EMPTY_EMAIL = ""
private const val EMPTY_PASSWORD = ""
