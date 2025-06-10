@file:OptIn(ExperimentalMaterialApi::class)

package soy.gabimoreno.presentation.screen.premium

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
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
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.LibraryMusic
import androidx.compose.material.icons.filled.Person
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import soy.gabimoreno.R
import soy.gabimoreno.domain.model.content.PremiumAudio
import soy.gabimoreno.framework.datastore.isMemberActive
import soy.gabimoreno.framework.toast
import soy.gabimoreno.presentation.screen.ViewModelProvider
import soy.gabimoreno.presentation.theme.Black
import soy.gabimoreno.presentation.theme.Orange
import soy.gabimoreno.presentation.theme.PurpleLight
import soy.gabimoreno.presentation.theme.Spacing
import soy.gabimoreno.presentation.theme.White

@Composable
fun PremiumScreenRoot(
    onRequireAuth: () -> Unit,
    onItemClicked: (premiumAudioId: String) -> Unit,
    onAddToPlaylistClicked: (premiumAudioId: String) -> Unit,
    onPlaylistClicked: () -> Unit,
) {
    val context = LocalContext.current
    val premiumViewModel = ViewModelProvider.premiumViewModel
    LaunchedEffect(Unit) {
        launch {
            context.isMemberActive()
                .distinctUntilChanged()
                .collect { isMemberActive ->
                    premiumViewModel.onViewScreen(isMemberActive)
                }
        }
        launch {
            premiumViewModel.events.collect { event ->
                when (event) {
                    is PremiumEvent.Error -> {
                        context.toast(context.getString(R.string.unexpected_error))
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
    }

    PremiumScreen(
        state = premiumViewModel.state,
        onAction = { action ->
            when (action) {
                is PremiumAction.OnLoginClicked -> onRequireAuth()
                is PremiumAction.OnPlaylistClicked -> onPlaylistClicked()
                is PremiumAction.OnAddToPlaylistClicked -> onAddToPlaylistClicked(action.premiumAudioId)
                else -> Unit
            }
            premiumViewModel.onAction(action)
        }
    )
}

@Composable
fun PremiumScreen(
    state: PremiumState,
    onAction: (PremiumAction) -> Unit,
) {
    val premiumAudiosFlow = state.premiumAudioFlow.collectAsLazyPagingItems()

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .windowInsetsPadding(WindowInsets.safeDrawing.only(WindowInsetsSides.Top))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(Spacing.s16),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(id = R.string.nav_item_premium).uppercase(),
                style = MaterialTheme.typography.h5,
                modifier = Modifier
                    .weight(0.8f)
            )
            Icon(
                imageVector = Icons.Default.LibraryMusic,
                contentDescription = "Playlist",
                modifier = Modifier
                    .padding(end = Spacing.s16)
                    .clickable {
                        onAction(PremiumAction.OnPlaylistClicked)
                    }
            )
            Icon(
                imageVector = if (state.shouldIAccessPremium)
                    Icons.AutoMirrored.Filled.Logout else Icons.Default.Person,
                contentDescription = stringResource(
                    if (state.shouldIAccessPremium)
                        R.string.premium_logout else R.string.premium_login
                ),
                modifier = Modifier
                    .clip(CircleShape)
                    .clickable {
                        onAction(PremiumAction.OnLoginClicked)
                    }
            )
        }
        Box(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            if (state.shouldIAccessPremium) {
                PremiumContent(
                    premiumAudios = premiumAudiosFlow,
                    onItemClicked = { onAction(PremiumAction.OnPremiumItemClicked(it)) },
                    onListenedToggled = { onAction(PremiumAction.OnListenedToggled(it)) },
                    onPullRefreshTriggered = { onAction(PremiumAction.OnRefreshContent) },
                    onAddToPlaylistClicked = { onAction(PremiumAction.OnAddToPlaylistClicked(it)) }
                )
            } else {
                NonPremiumContent()
            }
            ShowLoading(state.isLoading)
        }
    }
}

@Composable
fun NonPremiumContent() {
    val uriHandler = LocalUriHandler.current
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(Spacing.s16),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(R.drawable.ic_premium_logo),
            contentDescription = stringResource(R.string.non_premium_image_cd),
            contentScale = ContentScale.FillWidth,
            modifier = Modifier
                .width(350.dp)
        )
        Spacer()
        val annotatedLoginString = buildAnnotatedString {
            append(stringResource(R.string.non_premium_title))
            append(" ")
            withStyle(
                SpanStyle(color = Orange, fontWeight = FontWeight.Bold)
            ) {
                append(stringResource(R.string.non_premium_title_span))
            }
        }
        Text(
            text = annotatedLoginString,
            style = MaterialTheme.typography.h5,
        )
        Box(modifier = Modifier.weight(0.60f))
        Text(
            stringResource(R.string.non_premium_text),
            style = MaterialTheme.typography.body1,
            textAlign = TextAlign.Justify
        )
        Box(modifier = Modifier.weight(0.05f))
        TextButton(
            onClick = {
                uriHandler.openUri(URL_ANDROIDES_PREMIUM)
            }) {
            Text(
                stringResource(R.string.non_premium_info_link),
                fontWeight = FontWeight.SemiBold,
                textDecoration = TextDecoration.Underline
            )
        }
        Box(modifier = Modifier.weight(0.25f))
    }
}

@Composable
fun PremiumContent(
    premiumAudios: LazyPagingItems<PremiumAudio>,
    onItemClicked: (premiumAudioId: String) -> Unit,
    onListenedToggled: (premiumAudio: PremiumAudio) -> Unit,
    onPullRefreshTriggered: () -> Unit,
    onAddToPlaylistClicked: (premiumAudioId: String) -> Unit,
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
                                onListenedToggled = onListenedToggled,
                                onAddToPlaylistClicked = onAddToPlaylistClicked
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
    onAddToPlaylistClicked: (premiumAudioId: String) -> Unit,
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
        Spacer(modifier = Modifier.width(Spacing.s16))
        IconButton(
            modifier = Modifier.weight(0.10f),
            onClick = { onAddToPlaylistClicked(premiumAudio.id) },
        ) {
            Icon(
                modifier = Modifier
                    .size(Spacing.s32),
                imageVector = Icons.Default.LibraryMusic,
                contentDescription = stringResource(R.string.playlists_add_audio_to_playlist),
                tint = White,
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
private const val URL_ANDROIDES_PREMIUM = "https://gabimoreno.soy/los-androides-premium"
