package soy.gabimoreno.presentation.screen.profile.view

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import kotlinx.coroutines.delay
import soy.gabimoreno.R
import soy.gabimoreno.presentation.theme.GabiMorenoTheme
import soy.gabimoreno.presentation.theme.PurpleLight
import soy.gabimoreno.presentation.theme.Spacing
import soy.gabimoreno.presentation.theme.White
import soy.gabimoreno.presentation.ui.button.PrimaryButton
import soy.gabimoreno.presentation.ui.button.SecondaryButton

@Composable
fun CustomDialog(
    title: String = "Title",
    text: String = "Description",
    confirmText: String = "Confirm",
    dismissText: String = "Dismiss",
    onConfirm: () -> Unit = {},
    onDismiss: () -> Unit = {},
) {
    Dialog(onDismissRequest = onDismiss) {
        Box(
            modifier = Modifier
                .width(310.dp)
                .height(440.dp)
        ) {
            Column(
                modifier = Modifier
                    .width(310.dp)
                    .height(320.dp)
            ) {
                Spacer(modifier = Modifier.height(Spacing.s32))
                Box(
                    modifier = Modifier
                        .width(310.dp)
                        .height(224.dp)
                        .border(1.dp, White, RoundedCornerShape(Spacing.s16))
                        .background(PurpleLight, RoundedCornerShape(Spacing.s16))
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(Spacing.s16),
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        Spacer(modifier = Modifier.height(Spacing.s24))
                        Text(
                            title.uppercase(),
                            style = MaterialTheme.typography.h6, color = White
                        )
                        Spacer(modifier = Modifier.weight(0.3f))
                        Text(
                            text,
                            style = MaterialTheme.typography.subtitle2,
                            color = White
                        )
                        Spacer(modifier = Modifier.weight(0.7f))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                        ) {
                            SecondaryButton(
                                text = dismissText,
                                height = Spacing.s48,
                                modifier = Modifier.weight(0.45f),
                                onClick = onDismiss,
                            )
                            Spacer(modifier = Modifier.weight(0.06f))
                            PrimaryButton(
                                text = confirmText,
                                height = Spacing.s48,
                                modifier = Modifier.weight(0.45f),
                                onClick = onConfirm,
                            )
                        }
                    }
                }
            }
            HeaderDialog(
                modifier = Modifier
                    .size(Spacing.s64)
                    .align(Alignment.TopCenter),
            )
        }
    }
}

@Composable
private fun HeaderDialog(
    modifier: Modifier = Modifier,
) {
    var visible by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        delay(150L)
        visible = true
    }
    AnimatedVisibility(
        modifier = modifier,
        visible = visible,
        enter = scaleIn(),
        exit = fadeOut(),
    ) {
        Image(
            painter = painterResource(R.drawable.ic_stat_name),
            contentDescription = "Image info",
            modifier = modifier
        )
    }
}

@Preview(
    showSystemUi = true,
    showBackground = true
)
@Composable
private fun CustomComposablePreview() {
    GabiMorenoTheme {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            CustomDialog()
        }
    }
}

