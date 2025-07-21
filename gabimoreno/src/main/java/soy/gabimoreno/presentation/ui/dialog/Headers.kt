package soy.gabimoreno.presentation.ui.dialog

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import kotlinx.coroutines.delay
import soy.gabimoreno.R

@Composable
internal fun HeaderDialog(
    modifier: Modifier = Modifier,
    typeDialog: TypeDialog,
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
        Box(modifier = modifier) {
            when (typeDialog) {
                TypeDialog.INFO -> InfoHeader()
                TypeDialog.ERROR -> ErrorHeader()
                TypeDialog.SUCCESS -> SuccessHeader()
                TypeDialog.CONFIRMATION -> ConfirmationHeader()
                TypeDialog.CONFIRMATION_WITH_CHECKBOX -> ConfirmationHeader()
                TypeDialog.CONFIRMATION_ERROR -> ErrorHeader()
            }
        }
    }
}

@Composable
fun InfoHeader(modifier: Modifier = Modifier) {
    Image(
        painter = painterResource(R.drawable.ic_gabi_info),
        contentDescription = stringResource(R.string.dialog_info_header_cd),
        modifier = modifier,
    )
}

@Composable
fun SuccessHeader(modifier: Modifier = Modifier) {
    Image(
        painter = painterResource(R.drawable.ic_gabi_success),
        contentDescription = stringResource(R.string.dialog_success_header_cd),
        modifier = modifier,
    )
}

@Composable
fun ErrorHeader(modifier: Modifier = Modifier) {
    Image(
        painter = painterResource(R.drawable.ic_gabi_error),
        contentDescription = stringResource(R.string.dialog_error_header_cd),
        modifier = modifier,
    )
}

@Composable
fun ConfirmationHeader(modifier: Modifier = Modifier) {
    Image(
        painter = painterResource(R.drawable.ic_gabi_confirm),
        contentDescription = stringResource(R.string.dialog_confirmation_header_cd),
        modifier = modifier,
    )
}
