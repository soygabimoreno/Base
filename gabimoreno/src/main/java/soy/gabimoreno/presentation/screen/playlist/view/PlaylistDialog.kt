package soy.gabimoreno.presentation.screen.playlist.view

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import soy.gabimoreno.R
import soy.gabimoreno.presentation.theme.PurpleLight
import soy.gabimoreno.presentation.theme.Spacing
import soy.gabimoreno.presentation.theme.White
import soy.gabimoreno.presentation.ui.button.PrimaryButton
import soy.gabimoreno.presentation.ui.button.SecondaryButton

@Composable
fun PlaylistDialog(
    titleDialog: String,
    title: String = "",
    description: String = "",
    titleError: Boolean = false,
    onTitleChange: (String) -> Unit = {},
    onDescriptionChange: (String) -> Unit = {},
    onConfirm: () -> Unit = {},
    onDismiss: () -> Unit = {},
) {
    Dialog(onDismissRequest = onDismiss) {
        Column(
            modifier =
                Modifier
                    .width(310.dp)
                    .height(intrinsicSize = IntrinsicSize.Min)
                    .border(1.dp, White, RoundedCornerShape(Spacing.s16))
                    .background(PurpleLight, RoundedCornerShape(Spacing.s16))
                    .padding(Spacing.s16),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                titleDialog.uppercase(),
                style = MaterialTheme.typography.h6,
                color = White,
            )
            Spacer(modifier = Modifier.height(Spacing.s8))
            CustomOutlinedTextField(
                value = title,
                placeholderText = stringResource(id = R.string.playlists_create_title_hint),
                errorText =
                    if (titleError) {
                        stringResource(
                            R.string.playlists_create_error_empty,
                        )
                    } else {
                        null
                    },
                onValueChange = { onTitleChange(it) },
            )
            Spacer(modifier = Modifier.height(Spacing.s8))
            CustomOutlinedTextField(
                value = description,
                placeholderText = stringResource(id = R.string.playlists_create_description_hint),
                errorText = null,
                onValueChange = { onDescriptionChange(it) },
            )
            Spacer(modifier = Modifier.height(Spacing.s24))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                SecondaryButton(
                    text = stringResource(id = R.string.close),
                    height = Spacing.s48,
                    modifier = Modifier.weight(0.45f),
                    onClick = onDismiss,
                )
                Spacer(modifier = Modifier.weight(0.06f))
                PrimaryButton(
                    text = stringResource(id = R.string.playlists_create),
                    height = Spacing.s48,
                    modifier = Modifier.weight(0.45f),
                    onClick = onConfirm,
                )
            }
        }
    }
}
