@file:OptIn(ExperimentalMaterialApi::class)

package soy.gabimoreno.presentation.screen.premium

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextOverflow
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import soy.gabimoreno.R
import soy.gabimoreno.domain.model.content.PremiumAudio
import soy.gabimoreno.framework.datastore.getEmail
import soy.gabimoreno.framework.datastore.getPassword
import soy.gabimoreno.framework.toast
import soy.gabimoreno.presentation.screen.ViewModelProvider
import soy.gabimoreno.presentation.screen.premium.view.LoginOutlinedTextField
import soy.gabimoreno.presentation.theme.Black
import soy.gabimoreno.presentation.theme.Orange
import soy.gabimoreno.presentation.theme.PurpleLight
import soy.gabimoreno.presentation.theme.Spacing
import soy.gabimoreno.presentation.ui.button.PrimaryButton

@Composable
fun PremiumScreenRoot(
    onItemClicked: (premiumAudioId: String) -> Unit,
) {
    val context = LocalContext.current
    val premiumViewModel = ViewModelProvider.premiumViewModel

    LaunchedEffect(Unit) {
        val previousEmail = context.getEmail().first()
        val previousPassword = context.getPassword().first()

        premiumViewModel.onAction(PremiumAction.OnViewScreen(previousEmail, previousPassword))

        premiumViewModel.events.collect { event ->
            when (event) {
                is PremiumEvent.Error -> {
                    context.toast(context.getString(R.string.unexpected_error))
                }

                PremiumEvent.ShowLoginError -> {
                    context.toast(context.getString(R.string.premium_error_generate_auth_cookie))
                }

                PremiumEvent.ShowTokenExpiredError -> {
                    context.toast(context.getString(R.string.premium_error_token_expired))
                }

                is PremiumEvent.ShowDetail -> {
                    onItemClicked(event.premiumAudioId)
                }
            }
        }
    }

    PremiumScreen(
        state = premiumViewModel.state,
        onAction = premiumViewModel::onAction
    )
}

@Composable
fun PremiumScreen(
    state: PremiumState,
    onAction: (PremiumAction) -> Unit,
) {
    val focusManager = LocalFocusManager.current
    val premiumAudiosFlow = state.premiumAudioFlow.collectAsLazyPagingItems()

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .windowInsetsPadding(WindowInsets.safeDrawing.only(WindowInsetsSides.Top))
    ) {
        if (state.shouldShowAccess && !state.shouldShowPremium) {
            Column(modifier = Modifier.padding(Spacing.s16)) {
                Text(
                    text = stringResource(id = R.string.premium_login).uppercase(),
                    style = MaterialTheme.typography.h6
                )
                Spacer()
                LoginOutlinedTextField(
                    value = state.email,
                    placeholderText = stringResource(id = R.string.premium_email),
                    showError = state.showInvalidEmailFormatError,
                    errorText = stringResource(id = R.string.premium_email_error_invalid_format),
                    onValueChange = { onAction(PremiumAction.OnEmailChanged(it)) }
                )
                Spacer()
                LoginOutlinedTextField(
                    value = state.password,
                    placeholderText = stringResource(id = R.string.premium_password),
                    showError = state.showInvalidPasswordError,
                    errorText = stringResource(id = R.string.premium_password_error_invalid),
                    visualTransformation = PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(
                        autoCorrectEnabled = false,
                        keyboardType = KeyboardType.Password,
                        imeAction = ImeAction.Done,
                    ),
                    keyboardActions =
                        KeyboardActions(
                            onAny = {
                                focusManager.clearFocus()
                                onAction(PremiumAction.OnLoginClicked)
                            },
                        ),
                    onValueChange = { onAction(PremiumAction.OnPasswordChanged(it)) }
                )
                Spacer()
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    PrimaryButton(
                        text = stringResource(id = R.string.premium_login),
                        height = Spacing.s48
                    ) {
                        onAction(PremiumAction.OnLoginClicked)
                    }
                }
            }
        }

        if (state.shouldShowPremium) {
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
                    Icons.AutoMirrored.Filled.Logout,
                    contentDescription = stringResource(R.string.premium_logout),
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(top = Spacing.s8, end = Spacing.s8)
                        .clip(CircleShape)
                        .clickable {
                            onAction(PremiumAction.OnLogoutClicked)
                        }
                        .padding(Spacing.s8)
                )
            }
            Spacer()
            PremiumContent(
                premiumAudios = premiumAudiosFlow,
                onItemClicked = { onAction(PremiumAction.OnPremiumItemClicked(it)) },
                onListenedToggled = { onAction(PremiumAction.OnListenedToggled(it)) },
                onPullRefreshTriggered = { onAction(PremiumAction.OnRefreshContent) })
        }
    }

    ShowLoading(state.isLoading)
}

@Composable
fun PremiumContent(
    premiumAudios: LazyPagingItems<PremiumAudio>,
    onItemClicked: (premiumAudioId: String) -> Unit,
    onListenedToggled: (premiumAudio: PremiumAudio) -> Unit,
    onPullRefreshTriggered: () -> Unit,
) {
    val refreshScope = rememberCoroutineScope()
    var refreshing by remember { mutableStateOf(false) }

    fun refresh() = refreshScope.launch {
        refreshing = true
        delay(REFRESH_DELAY)
        onPullRefreshTriggered()
        refreshing = false
    }

    val pullRefreshState = rememberPullRefreshState(refreshing, ::refresh)

    Box(
        Modifier
            .fillMaxSize()
            .pullRefresh(pullRefreshState)
    ) {
        when {
            premiumAudios.loadState.refresh is LoadState.Loading || premiumAudios.itemCount == 0 -> {
                CircularProgressIndicator(
                    modifier = Modifier
                        .align(Alignment.Center)
                )
            }

            else ->
                LazyColumn {
                    items(premiumAudios.itemCount) {
                        premiumAudios[it]?.let { premiumAudio ->
                            PremiumItem(
                                premiumAudio = premiumAudio,
                                onItemClicked = onItemClicked,
                                onListenedToggled = onListenedToggled
                            )
                        }
                    }
                }
        }
        PullRefreshIndicator(
            refreshing = refreshing,
            state = pullRefreshState,
            modifier = Modifier.align(Alignment.TopCenter)
        )
    }
}

@Composable
fun PremiumItem(
    premiumAudio: PremiumAudio,
    onItemClicked: (premiumAudioId: String) -> Unit,
    onListenedToggled: (premiumAudio: PremiumAudio) -> Unit,
) {
    val iconColor by animateColorAsState(
        targetValue = if (premiumAudio.hasBeenListened) Orange else Black.copy(alpha = 0.2f),
        animationSpec = tween(durationMillis = CHANGE_COLOR_ANIMATION_DURATION),
        label = "checkIconColorAnimation"
    )
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                onItemClicked(premiumAudio.id)
            }
            .background(brush = run {
                val backgroundColor =
                    if (premiumAudio.id == "5453") { // TODO: This is a provisional patch for visualization
                        Orange
                    } else {
                        PurpleLight
                    }
                SolidColor(backgroundColor)
            }, alpha = 0.5f)
            .padding(horizontal = Spacing.s16, vertical = Spacing.s16),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        Icon(
            imageVector = premiumAudio.category.icon,
            contentDescription = premiumAudio.category.title,
            modifier = Modifier.weight(0.10f),
        )
        Spacer(modifier = Modifier.width(Spacing.s16))
        Text(
            text = premiumAudio.title,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier
                .padding(end = Spacing.s16)
                .weight(0.70f),
        )
        IconButton(
            modifier = Modifier.weight(0.10f),
            onClick = { onListenedToggled(premiumAudio) },
        ) {
            Icon(
                modifier = Modifier
                    .size(Spacing.s32),
                imageVector = Icons.Default.Check,
                contentDescription = stringResource(R.string.premium_audio_listened),
                tint = iconColor,
            )
        }
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

private const val REFRESH_DELAY = 1500L
private const val CHANGE_COLOR_ANIMATION_DURATION = 300
