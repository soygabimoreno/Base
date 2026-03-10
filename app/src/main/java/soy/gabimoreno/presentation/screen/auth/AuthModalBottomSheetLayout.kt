package soy.gabimoreno.presentation.screen.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetState
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Password
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import kotlinx.coroutines.flow.first
import soy.gabimoreno.R
import soy.gabimoreno.framework.datastore.getEmail
import soy.gabimoreno.framework.datastore.getPassword
import soy.gabimoreno.framework.toast
import soy.gabimoreno.presentation.screen.ViewModelProvider
import soy.gabimoreno.presentation.screen.premium.view.LoginOutlinedTextField
import soy.gabimoreno.presentation.theme.Orange
import soy.gabimoreno.presentation.theme.Percent
import soy.gabimoreno.presentation.theme.PurpleDark
import soy.gabimoreno.presentation.theme.PurpleLight
import soy.gabimoreno.presentation.theme.Spacing
import soy.gabimoreno.presentation.ui.button.PrimaryButton

@Composable
fun AuthModalBottomSheetRoot(
    modalBottomSheetState: ModalBottomSheetState,
    onHideBottomSheet: () -> Unit,
    content: @Composable () -> Unit,
) {
    val context = LocalContext.current
    val authViewModel = ViewModelProvider.authViewModel

    LaunchedEffect(Unit) {
        val previousEmail = context.getEmail().first()
        val previousPassword = context.getPassword().first()

        authViewModel.onAction(AuthAction.OnViewScreen(previousEmail, previousPassword))
        authViewModel.events.collect { event ->
            when (event) {
                is AuthEvent.Error -> {
                    context.toast(context.getString(R.string.unexpected_error))
                }

                AuthEvent.OnLoginEvent -> {
                    onHideBottomSheet()
                }

                AuthEvent.ShowLoginError -> {
                    context.toast(context.getString(R.string.premium_error_generate_auth_cookie))
                }

                AuthEvent.ShowTokenExpiredError -> {
                    context.toast(context.getString(R.string.premium_error_token_expired))
                }
            }
        }
    }
    AuthModalBottomSheetLayout(
        state = authViewModel.state,
        modalBottomSheetState = modalBottomSheetState,
        onAction = authViewModel::onAction,
        content = content,
    )
}

@Composable
fun AuthModalBottomSheetLayout(
    state: AuthState,
    modalBottomSheetState: ModalBottomSheetState,
    onAction: (AuthAction) -> Unit,
    content: @Composable () -> Unit,
) {
    val focusManager = LocalFocusManager.current

    ModalBottomSheetLayout(
        sheetState = modalBottomSheetState,
        scrimColor = PurpleDark.copy(alpha = Percent.FIFTY),
        sheetBackgroundColor = PurpleLight,
        sheetElevation = Spacing.s4,
        sheetShape =
            RoundedCornerShape(
                topStart = Spacing.s16,
                topEnd = Spacing.s16,
            ),
        sheetContent = {
            Column(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .imePadding()
                        .padding(Spacing.s16)
                        .windowInsetsPadding(
                            WindowInsets.safeDrawing.only(WindowInsetsSides.Bottom),
                        ),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Box(
                    modifier =
                        Modifier
                            .fillMaxWidth(Percent.FIFTY)
                            .height(Spacing.s4)
                            .clip(RoundedCornerShape(Spacing.s16))
                            .background(Orange),
                )
                Spacer(modifier = Modifier.height(Spacing.s24))
                if (state.shouldShowAccess) {
                    Text(
                        text = stringResource(id = R.string.login_title).uppercase(),
                        style = MaterialTheme.typography.h6,
                        modifier = Modifier.fillMaxWidth(),
                    )
                    Spacer(modifier = Modifier.height(Spacing.s16))
                    LoginOutlinedTextField(
                        value = state.email,
                        placeholderText = stringResource(id = R.string.premium_email),
                        showError = state.showInvalidEmailFormatError,
                        errorText =
                            stringResource(
                                id = R.string.premium_email_error_invalid_format,
                            ),
                        leadingIcon = Icons.Filled.Email,
                        onValueChange = { onAction(AuthAction.OnEmailChanged(it)) },
                    )
                    Spacer(modifier = Modifier.height(Spacing.s16))
                    LoginOutlinedTextField(
                        value = state.password,
                        placeholderText = stringResource(id = R.string.premium_password),
                        showError = state.showInvalidPasswordError,
                        errorText = stringResource(id = R.string.premium_password_error_invalid),
                        visualTransformation = PasswordVisualTransformation(),
                        keyboardOptions =
                            KeyboardOptions(
                                autoCorrectEnabled = false,
                                keyboardType = KeyboardType.Password,
                                imeAction = ImeAction.Done,
                            ),
                        keyboardActions =
                            KeyboardActions(
                                onAny = {
                                    focusManager.clearFocus()
                                    onAction(AuthAction.OnLoginClicked)
                                },
                            ),
                        leadingIcon = Icons.Filled.Password,
                        onValueChange = { onAction(AuthAction.OnPasswordChanged(it)) },
                    )
                    Spacer(modifier = Modifier.height(Spacing.s24))
                    PrimaryButton(
                        text = stringResource(id = R.string.premium_login),
                        height = Spacing.s48,
                        isEnabled = !state.isLoading,
                    ) {
                        onAction(AuthAction.OnLoginClicked)
                    }
                    Spacer(modifier = Modifier.height(Spacing.s24))
                } else {
                    Text(
                        text = stringResource(id = R.string.logout_question).uppercase(),
                        style = MaterialTheme.typography.h6,
                    )
                    Spacer(modifier = Modifier.height(Spacing.s24))
                    PrimaryButton(
                        text = stringResource(id = R.string.close),
                        height = Spacing.s48,
                    ) {
                        onAction(AuthAction.OnLogoutClicked)
                    }
                    Spacer(modifier = Modifier.height(Spacing.s24))
                }
            }
        },
        content = {
            if (!state.isLoading) {
                content()
            } else {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }
        },
    )
}
