package soy.gabimoreno.presentation.screen.profile

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DeleteSweep
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import soy.gabimoreno.R
import soy.gabimoreno.framework.datastore.getEmail
import soy.gabimoreno.framework.toast
import soy.gabimoreno.presentation.screen.ViewModelProvider
import soy.gabimoreno.presentation.theme.Orange
import soy.gabimoreno.presentation.theme.Spacing
import soy.gabimoreno.presentation.ui.button.PrimaryButton
import soy.gabimoreno.presentation.ui.dialog.CustomDialog

@Composable
fun ProfileScreenRoot(
    onToggleBottomSheet: () -> Unit
) {
    val context = LocalContext.current
    val profileViewModel = ViewModelProvider.profileViewModel
    LaunchedEffect(Unit) {
        launch {
            context.getEmail()
                .distinctUntilChanged()
                .collect { email ->
                    profileViewModel.onAction(ProfileAction.OnEmailChanged(email))
                }
        }
        launch {
            profileViewModel.events.collect { event ->
                when (event) {
                    is ProfileEvent.Error -> {
                        context.toast(context.getString(R.string.unexpected_error))
                    }

                    is ProfileEvent.ResetSuccess -> {
                        val message = when (event.type) {
                            TypeDialog.PREMIUM -> R.string.profile_reset_success_premium
                            TypeDialog.AUDIOCOURSES -> R.string.profile_reset_success_audiocurses
                        }
                        context.toast(context.getString(message))
                    }
                }
            }
        }
    }

    ProfileScreen(
        state = profileViewModel.state,
        onAction = { action ->
            when (action) {
                is ProfileAction.OnToggleBottomSheet -> onToggleBottomSheet()
                else -> Unit
            }
            profileViewModel.onAction(action)
        }
    )
}

@Composable
fun ProfileScreen(
    state: ProfileState,
    onAction: (ProfileAction) -> Unit,
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .windowInsetsPadding(WindowInsets.safeDrawing.only(WindowInsetsSides.Top))
                .padding(Spacing.s16),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(R.drawable.ic_stat_name),
                contentDescription = stringResource(R.string.app_name),
                contentScale = ContentScale.FillWidth,
                modifier = Modifier
                    .width(120.dp)
            )
            Spacer(modifier = Modifier.padding(vertical = Spacing.s32))
            if (state.email.isBlank()) {
                Text(
                    stringResource(R.string.profile_no_session),
                    modifier = Modifier.fillMaxWidth(),
                    fontWeight = FontWeight.SemiBold
                )
            } else {
                val annotatedLoginString = buildAnnotatedString {
                    append(stringResource(R.string.profile_info_email))
                    append(" ")
                    withStyle(
                        SpanStyle(color = Orange, fontWeight = FontWeight.Bold)
                    ) {
                        append(stringResource(R.string.premium_email))
                    }
                }

                Text(annotatedLoginString, modifier = Modifier.fillMaxWidth())
            }
            OrangeSeparator()
            Text(
                text = state.email,
                modifier = Modifier.fillMaxWidth(),
                style = MaterialTheme.typography.h5,
            )
            Spacer(modifier = Modifier.padding(vertical = Spacing.s32))
            if (state.email.isNotBlank()) {
                ResetAudioItem(
                    text = stringResource(R.string.profile_reset_premium),
                    onItemClicked = {
                        onAction(ProfileAction.OnResetPremiumAudioClicked)
                    }
                )
            }
            ResetAudioItem(
                text = stringResource(R.string.profile_reset_audiocourses),
                onItemClicked = {
                    onAction(ProfileAction.OnResetAudioCoursesClicked)
                }
            )
            Box(modifier = Modifier.weight(0.70f))
            PrimaryButton(
                text = stringResource(
                    if (state.email.isBlank())
                        R.string.login_title else R.string.logout_title
                ),
                height = Spacing.s48,
                onClick = { onAction(ProfileAction.OnToggleBottomSheet) },
                modifier = Modifier.fillMaxWidth()
            )
            Box(modifier = Modifier.weight(0.25f))
        }
    }
    if (state.showResetDialog)
        CustomDialog(
            title = stringResource(R.string.profile_reset_dialog_title),
            text = stringResource(R.string.profile_reset_dialog_text),
            confirmText = stringResource(R.string.profile_reset_dialog_confirm),
            dismissText = stringResource(R.string.close),
            onConfirm = { onAction(ProfileAction.OnConfirmDialog) },
            onDismiss = { onAction(ProfileAction.OnDismissDialog) },
            typeDialog = soy.gabimoreno.presentation.ui.dialog.TypeDialog.CONFIRMATION
        )
}

@Composable
private fun ResetAudioItem(
    modifier: Modifier = Modifier,
    text: String,
    onItemClicked: () -> Unit = {},
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(top = Spacing.s16),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text)
        IconButton(
            onClick = { onItemClicked() }
        ) {
            Icon(
                imageVector = Icons.Default.DeleteSweep,
                contentDescription = null,
                tint = Orange,
            )
        }
    }
    OrangeSeparator()
}

@Composable
private fun OrangeSeparator() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(1.dp)
            .background(Orange)
    )
}
