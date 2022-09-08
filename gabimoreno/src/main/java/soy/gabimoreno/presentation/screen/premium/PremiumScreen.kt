package soy.gabimoreno.presentation.screen.premium

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Logout
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextOverflow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.first
import soy.gabimoreno.R
import soy.gabimoreno.data.network.mapper.toPremiumAudios
import soy.gabimoreno.domain.model.content.Post
import soy.gabimoreno.domain.model.content.PremiumAudio
import soy.gabimoreno.framework.datastore.getEmail
import soy.gabimoreno.framework.datastore.getPassword
import soy.gabimoreno.framework.toast
import soy.gabimoreno.presentation.screen.ViewModelProvider
import soy.gabimoreno.presentation.screen.premium.view.LoginOutlinedTextField
import soy.gabimoreno.presentation.theme.Black
import soy.gabimoreno.presentation.theme.PurpleLight
import soy.gabimoreno.presentation.theme.Spacing
import soy.gabimoreno.presentation.ui.button.PrimaryButton

@Composable
fun PremiumScreen(
    onItemClicked: (premiumAudioId: String) -> Unit,
) {
    val context = LocalContext.current

    val premiumViewModel = ViewModelProvider.premiumViewModel

    var email by remember { mutableStateOf(EMPTY_EMAIL) }
    var password by remember { mutableStateOf(EMPTY_PASSWORD) }
    var posts by remember { mutableStateOf(EMPTY_POSTS) }

    var showLoading by remember { mutableStateOf(true) }
    var showAccess by remember { mutableStateOf(false) }
    var showInvalidEmailFormatError by remember { mutableStateOf(false) }
    var showInvalidPasswordError by remember { mutableStateOf(false) }
    var showPremium by remember { mutableStateOf(false) }
    var showLoginError by remember { mutableStateOf(false) }

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
                    posts = viewEvent.posts
                }
                is PremiumViewModel.ViewEvent.ShowLoginError -> {
                    showLoginError = true
                    showAccess = true

                    email = viewEvent.email
                    password = viewEvent.password
                }
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Row(modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = Spacing.s16)
        ) {
            Text(
                text = stringResource(id = R.string.nav_item_premium).uppercase(),
                style = MaterialTheme.typography.h5
            )
        }
        Spacer()
        Text(
            text = stringResource(id = R.string.premium_description),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = Spacing.s16)
        )
        Spacer(modifier = Modifier.height(Spacing.s40))
        if (showAccess && !showPremium) {
            Text(
                text = stringResource(id = R.string.premium_login).uppercase(),
                style = MaterialTheme.typography.h6
            )
            Spacer()
            var emailTextFieldValue by remember { mutableStateOf(TextFieldValue(email)) }
            LoginOutlinedTextField(
                value = emailTextFieldValue,
                placeholderText = stringResource(id = R.string.premium_email),
                showError = showInvalidEmailFormatError,
                errorText = stringResource(id = R.string.premium_email_error_invalid_format)
            ) {
                emailTextFieldValue = it
            }
            Spacer()
            var passwordTextFieldValue by remember { mutableStateOf(TextFieldValue(password)) }
            LoginOutlinedTextField(
                value = passwordTextFieldValue,
                placeholderText = stringResource(id = R.string.premium_password),
                showError = showInvalidPasswordError,
                errorText = stringResource(id = R.string.premium_password_error_invalid),
                visualTransformation = PasswordVisualTransformation()
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
            Box(
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = stringResource(id = R.string.nav_item_premium).uppercase(),
                    style = MaterialTheme.typography.h5,
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .padding(top = Spacing.s16, start = Spacing.s16)
                )
                Icon(
                    Icons.Default.Logout,
                    contentDescription = stringResource(R.string.premium_logout),
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(top = Spacing.s8, end = Spacing.s8)
                        .clip(CircleShape)
                        .clickable {
                            premiumViewModel.onLogoutClicked()
                        }
                        .padding(Spacing.s8)
                )
            }
            Spacer()
            PremiumContent(posts, onItemClicked)
        }

        if (showLoginError) {
            showLoginError = false
            context.toast(stringResource(id = R.string.premium_error_generate_auth_cookie))
        }
    }

    ShowLoading(showLoading)
}

@Composable
fun PremiumContent(
    posts: List<Post>,
    onItemClicked: (premiumAudioId: String) -> Unit,
) {
    LazyColumn {
        items(posts.toPremiumAudios()) { premiumAudio ->
            PremiumItem(premiumAudio, onItemClicked)
        }
    }
}

@Composable
fun PremiumItem(
    premiumAudio: PremiumAudio,
    onItemClicked: (premiumAudioId: String) -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                onItemClicked(premiumAudio.id)
            }
            .background(brush = SolidColor(PurpleLight), alpha = 0.5f)
            .padding(horizontal = Spacing.s16, vertical = Spacing.s24)
    ) {
        Icon(
            imageVector = premiumAudio.category.icon,
            contentDescription = premiumAudio.category.title
        )
        Spacer(modifier = Modifier.width(Spacing.s16))
        Text(
            text = premiumAudio.title,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.padding(end = Spacing.s16)
        )
    }
}

@Composable
private fun ShowLoading(showLoading: Boolean) {
    if (showLoading) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .background(brush = SolidColor(Black), alpha = 0.2f)
                .fillMaxSize()
                .clickable(false) {}
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
private val EMPTY_POSTS = emptyList<Post>()
