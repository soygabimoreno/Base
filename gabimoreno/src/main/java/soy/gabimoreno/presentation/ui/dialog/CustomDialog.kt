package soy.gabimoreno.presentation.ui.dialog

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
            modifier = Modifier
                .width(310.dp)
                .height(380.dp)
        ) {
            Column(
                modifier = Modifier
                    .width(310.dp)
                    .height(320.dp)
                    .align(Alignment.BottomCenter),
            ) {
                Spacer(modifier = Modifier.height(Spacing.s32))
                Box(
                    modifier = Modifier
                        .width(310.dp)
                        .height(244.dp)
                        .border(1.dp, White, RoundedCornerShape(Spacing.s16))
                        .background(PurpleLight, RoundedCornerShape(Spacing.s16))
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(Spacing.s16),
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        if (text.isNotEmpty()) {
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
                        } else {
                            Spacer(modifier = Modifier.weight(0.3f))
                            Text(
                                title.uppercase(),
                                style = MaterialTheme.typography.h6, color = White
                            )
                            Spacer(modifier = Modifier.weight(0.3f))
                        }
                        when (typeDialog) {
                            TypeDialog.CONFIRMATION, TypeDialog.CONFIRMATION_ERROR -> {
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

                            TypeDialog.CONFIRMATION_WITH_CHECKBOX -> {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(top = 8.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Checkbox(
                                        checked = isCheckboxChecked,
                                        onCheckedChange = { onCheckboxChanged() },
                                        colors = CheckboxDefaults.colors(
                                            uncheckedColor = White,
                                            checkmarkColor = Orange
                                        )
                                    )
                                    Text(
                                        checkBoxText,
                                        color = White,
                                        style = MaterialTheme.typography.body2
                                    )
                                }
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

                            TypeDialog.INFO, TypeDialog.ERROR, TypeDialog.SUCCESS -> {
                                PrimaryButton(
                                    text = confirmText,
                                    height = Spacing.s48,
                                    onClick = onConfirm,
                                )
                            }
                        }
                    }
                }
            }
            HeaderDialog(
                modifier = Modifier
                    .size(Spacing.s128)
                    .align(Alignment.TopCenter),
                typeDialog = typeDialog
            )
        }
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
            CustomDialog(
                title = "This is an example title",
                text = "",
                confirmText = "Confirm",
                dismissText = "Dismiss",
                onConfirm = {},
                onDismiss = {},
                isCheckboxChecked = false,
                typeDialog = TypeDialog.SUCCESS
            )
        }
    }
}

