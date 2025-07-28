package soy.gabimoreno.presentation.ui.dialog

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Checkbox
import androidx.compose.material.CheckboxDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import soy.gabimoreno.presentation.theme.GabiMorenoTheme
import soy.gabimoreno.presentation.theme.Orange
import soy.gabimoreno.presentation.theme.Percent
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
    checkBoxText: String = "Don't ask again",
    isCheckboxChecked: Boolean = false,
    onCheckboxChanged: () -> Unit = {},
    onConfirm: () -> Unit = {},
    onDismiss: () -> Unit = {},
    typeDialog: TypeDialog,
) {
    Dialog(onDismissRequest = onDismiss) {
        Box(
            modifier =
                Modifier
                    .width(DIALOG_WIDTH.dp)
                    .height(DIALOG_HEIGHT.dp),
        ) {
            DialogContent(
                title = title,
                text = text,
                confirmText = confirmText,
                dismissText = dismissText,
                checkBoxText = checkBoxText,
                isCheckboxChecked = isCheckboxChecked,
                onCheckboxChanged = onCheckboxChanged,
                onConfirm = onConfirm,
                onDismiss = onDismiss,
                typeDialog = typeDialog,
                modifier = Modifier.align(Alignment.BottomCenter),
            )
            HeaderDialog(
                modifier =
                    Modifier
                        .size(Spacing.s128)
                        .align(Alignment.TopCenter),
                typeDialog = typeDialog,
            )
        }
    }
}

@Composable
private fun DialogContent(
    title: String,
    text: String,
    confirmText: String,
    dismissText: String,
    checkBoxText: String,
    isCheckboxChecked: Boolean,
    onCheckboxChanged: () -> Unit,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
    typeDialog: TypeDialog,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier =
            modifier
                .width(DIALOG_WIDTH.dp)
                .height(DIALOG_CONTENT_HEIGHT.dp),
    ) {
        Spacer(modifier = Modifier.height(Spacing.s32))
        Box(
            modifier =
                Modifier
                    .width(DIALOG_WIDTH.dp)
                    .height(CONTENT_COLUMN_HEIGHT.dp)
                    .border(
                        Spacing.s1,
                        White,
                        RoundedCornerShape(Spacing.s16),
                    ).background(PurpleLight, RoundedCornerShape(Spacing.s16)),
        ) {
            Column(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .padding(Spacing.s16),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                DialogTextContent(
                    title = title,
                    text = text,
                )
                DialogActions(
                    confirmText = confirmText,
                    dismissText = dismissText,
                    checkBoxText = checkBoxText,
                    isCheckboxChecked = isCheckboxChecked,
                    onCheckboxChanged = onCheckboxChanged,
                    onConfirm = onConfirm,
                    onDismiss = onDismiss,
                    typeDialog = typeDialog,
                )
            }
        }
    }
}

@Composable
private fun ColumnScope.DialogTextContent(
    title: String,
    text: String,
) {
    if (text.isNotEmpty()) {
        Spacer(modifier = Modifier.height(Spacing.s24))
        Text(
            title.uppercase(),
            style = MaterialTheme.typography.h6,
            color = White,
        )
        Spacer(modifier = Modifier.weight(Percent.THIRTY))
        Text(
            text,
            style = MaterialTheme.typography.subtitle2,
            color = White,
        )
        Spacer(modifier = Modifier.weight(Percent.SEVENTY))
    } else {
        Spacer(modifier = Modifier.weight(Percent.THIRTY))
        Text(
            title.uppercase(),
            style = MaterialTheme.typography.h6,
            color = White,
        )
        Spacer(modifier = Modifier.weight(Percent.THIRTY))
    }
}

@Composable
private fun DialogActions(
    confirmText: String,
    dismissText: String,
    checkBoxText: String,
    isCheckboxChecked: Boolean,
    onCheckboxChanged: () -> Unit,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
    typeDialog: TypeDialog,
) {
    when (typeDialog) {
        TypeDialog.CONFIRMATION, TypeDialog.CONFIRMATION_ERROR -> {
            ConfirmationButtons(
                confirmText = confirmText,
                dismissText = dismissText,
                onConfirm = onConfirm,
                onDismiss = onDismiss,
            )
        }

        TypeDialog.CONFIRMATION_WITH_CHECKBOX -> {
            DialogCheckbox(
                checkBoxText = checkBoxText,
                isCheckboxChecked = isCheckboxChecked,
                onCheckboxChanged = onCheckboxChanged,
            )
            ConfirmationButtons(
                confirmText = confirmText,
                dismissText = dismissText,
                onConfirm = onConfirm,
                onDismiss = onDismiss,
            )
        }

        TypeDialog.INFO, TypeDialog.ERROR, TypeDialog.SUCCESS -> {
            PrimaryButton(
                text = confirmText,
                height = Spacing.s48,
                onClick = onConfirm,
            )
        }
    }
}

@Composable
private fun ConfirmationButtons(
    confirmText: String,
    dismissText: String,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        SecondaryButton(
            text = dismissText,
            height = Spacing.s48,
            modifier = Modifier.weight(Percent.FORTY_FIVE),
            onClick = onDismiss,
        )
        Spacer(modifier = Modifier.weight(Percent.SIX))
        PrimaryButton(
            text = confirmText,
            height = Spacing.s48,
            modifier = Modifier.weight(Percent.FORTY_FIVE),
            onClick = onConfirm,
        )
    }
}

@Composable
private fun DialogCheckbox(
    checkBoxText: String,
    isCheckboxChecked: Boolean,
    onCheckboxChanged: () -> Unit,
) {
    Row(
        modifier =
            Modifier
                .fillMaxWidth()
                .padding(top = Spacing.s8),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Checkbox(
            checked = isCheckboxChecked,
            onCheckedChange = { onCheckboxChanged() },
            colors =
                CheckboxDefaults.colors(
                    uncheckedColor = White,
                    checkmarkColor = Orange,
                ),
        )
        Text(
            checkBoxText,
            color = White,
            style = MaterialTheme.typography.body2,
        )
    }
}

enum class TypeDialog {
    INFO,
    ERROR,
    SUCCESS,
    CONFIRMATION,
    CONFIRMATION_WITH_CHECKBOX,
    CONFIRMATION_ERROR,
}

@Preview(
    showSystemUi = true,
    showBackground = true,
)
@Composable
private fun CustomComposablePreview() {
    GabiMorenoTheme {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            CustomDialog(
                title = "This is an example title",
                text = "",
                confirmText = "Confirm",
                dismissText = "Dismiss",
                onConfirm = {},
                onDismiss = {},
                isCheckboxChecked = false,
                typeDialog = TypeDialog.SUCCESS,
            )
        }
    }
}

private const val CONTENT_COLUMN_HEIGHT = 244
private const val DIALOG_CONTENT_HEIGHT = 320
private const val DIALOG_HEIGHT = 380
private const val DIALOG_WIDTH = 310
