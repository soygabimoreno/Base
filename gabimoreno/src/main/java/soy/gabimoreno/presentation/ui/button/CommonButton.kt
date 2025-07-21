package soy.gabimoreno.presentation.ui.button

import androidx.annotation.VisibleForTesting
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import soy.gabimoreno.presentation.theme.Spacing
import soy.gabimoreno.presentation.ui.PreviewContent

@Composable
fun CommonButton(
    text: String,
    height: Dp,
    background: Color,
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
) {
    Box(
        modifier =
            modifier
                .defaultMinSize(Spacing.oddSpacing200)
                .height(height)
                .clip(CircleShape)
                .background(background)
                .clickable(onClick = onClick)
                .testTag(COMMON_BUTTON_BOX_ID),
    ) {
        Text(
            text = text,
            color = MaterialTheme.colors.onPrimary,
            style = MaterialTheme.typography.body1.copy(fontWeight = FontWeight.Bold),
            modifier =
                Modifier
                    .padding(horizontal = Spacing.s16)
                    .align(Alignment.Center),
        )
    }
}

@Preview(name = "CommonButton")
@Composable
fun CommonButtonPreview() {
    PreviewContent {
        CommonButton(
            "Play",
            height = Spacing.s48,
            MaterialTheme.colors.primary,
        ) {}
    }
}

@VisibleForTesting
internal const val COMMON_BUTTON_BOX_ID = "COMMON_BUTTON_BOX_ID"
