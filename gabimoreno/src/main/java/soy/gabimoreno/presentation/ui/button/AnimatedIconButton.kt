package soy.gabimoreno.presentation.ui.button

import androidx.compose.animation.animateColor
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.VectorConverter
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateValue
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.LocalContentAlpha
import androidx.compose.material.LocalContentColor
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Share
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import soy.gabimoreno.presentation.theme.GabiMorenoTheme
import soy.gabimoreno.presentation.theme.Orange
import soy.gabimoreno.presentation.theme.Spacing
import soy.gabimoreno.presentation.ui.IconButton

@Composable
fun AnimatedIconButton(
    showAnimation: Boolean,
    imageVector: ImageVector,
    contentDescription: String,
    padding: Dp,
    modifier: Modifier = Modifier,
    tint: Color = LocalContentColor.current.copy(alpha = LocalContentAlpha.current),
    transitionTint: Color = Orange,
    onClick: () -> Unit,
) {
    val animations =
        rememberPulseAnimations(showAnimation, tint, transitionTint, contentDescription)

    Box(contentAlignment = Alignment.Center) {
        AnimatedCircleBorder(
            modifier,
            animations.animatedSize,
            animations.animatedCircleColor,
            animations.animatedAlpha,
        )
        PulsingIconButton(
            imageVector = imageVector,
            contentDescription = contentDescription,
            padding = padding,
            modifier = modifier,
            scale = animations.animatedScale,
            tint = animations.animatedTint,
            onClick = onClick,
        )
    }
}

@Composable
private fun rememberPulseAnimations(
    showAnimation: Boolean,
    tint: Color,
    transitionTint: Color,
    label: String,
): PulseAnimations {
    val baseSize = 32.dp
    val infiniteTransition = rememberInfiniteTransition(label = "${label}Pulse")

    val animatedScale by infiniteTransition.animateFloat(
        initialValue = 1.1f,
        targetValue = if (showAnimation) 1.4f else 1.1f,
        animationSpec =
            infiniteRepeatable(
                animation = tween(ANIMATION_DURATION, easing = FastOutSlowInEasing),
                repeatMode = RepeatMode.Reverse,
            ),
        label = "scaleAnim",
    )

    val animatedAlpha by infiniteTransition.animateFloat(
        initialValue = 0.05f,
        targetValue = if (showAnimation) 1f else 0.05f,
        animationSpec =
            infiniteRepeatable(
                animation = tween(ANIMATION_DURATION_COLOR_CIRCLE, easing = FastOutSlowInEasing),
                repeatMode = RepeatMode.Reverse,
            ),
        label = "circleAlpha",
    )

    val animatedSize by infiniteTransition.animateValue(
        initialValue = baseSize * 1.1f,
        targetValue = if (showAnimation) baseSize * 1.5f else baseSize * 1.1f,
        typeConverter = Dp.VectorConverter,
        animationSpec =
            infiniteRepeatable(
                animation = tween(ANIMATION_DURATION, easing = FastOutLinearInEasing),
                repeatMode = RepeatMode.Reverse,
            ),
        label = "pulsingSize",
    )

    val animatedTint by infiniteTransition.animateColor(
        initialValue = tint,
        targetValue = if (showAnimation) transitionTint else tint,
        animationSpec =
            infiniteRepeatable(
                animation = tween(ANIMATION_DURATION),
                repeatMode = RepeatMode.Restart,
            ),
        label = "tint",
    )

    val animatedCircleColor by infiniteTransition.animateColor(
        initialValue = tint,
        targetValue = if (showAnimation) transitionTint else tint,
        animationSpec =
            infiniteRepeatable(
                animation = tween(ANIMATION_DURATION_COLOR_CIRCLE),
                repeatMode = RepeatMode.Restart,
            ),
        label = "circleColor",
    )

    return PulseAnimations(
        animatedScale,
        animatedAlpha,
        animatedSize,
        animatedTint,
        animatedCircleColor,
    )
}

@Composable
private fun AnimatedCircleBorder(
    modifier: Modifier,
    size: Dp,
    color: Color,
    alpha: Float,
) {
    Box(
        modifier =
            modifier
                .size(size)
                .clip(CircleShape)
                .border(2.dp, color.copy(alpha = alpha), CircleShape),
    )
}

@Composable
private fun PulsingIconButton(
    imageVector: ImageVector,
    contentDescription: String,
    padding: Dp,
    modifier: Modifier,
    scale: Float,
    tint: Color,
    onClick: () -> Unit,
) {
    IconButton(
        imageVector = imageVector,
        contentDescription = contentDescription,
        padding = padding,
        modifier =
            modifier
                .padding(end = Spacing.s4)
                .graphicsLayer {
                    scaleX = scale
                    scaleY = scale
                },
        tint = tint,
        onClick = onClick,
    )
}

private data class PulseAnimations(
    val animatedScale: Float,
    val animatedAlpha: Float,
    val animatedSize: Dp,
    val animatedTint: Color,
    val animatedCircleColor: Color,
)

private const val ANIMATION_DURATION = 900
private const val ANIMATION_DURATION_COLOR_CIRCLE = 700

@Preview
@Composable
private fun AnimatedIconButtonPreview() {
    GabiMorenoTheme {
        AnimatedIconButton(
            showAnimation = true,
            imageVector = Icons.Default.Share,
            contentDescription = "Share",
            padding = Spacing.s16,
            onClick = {},
        )
    }
}
