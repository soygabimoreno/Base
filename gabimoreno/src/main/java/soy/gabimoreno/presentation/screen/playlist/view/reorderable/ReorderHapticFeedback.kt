package soy.gabimoreno.presentation.screen.playlist.view.reorderable

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalView
import androidx.core.view.HapticFeedbackConstantsCompat
import androidx.core.view.ViewCompat

enum class ReorderHapticFeedbackType(val hapticConstant: Int) {
    START(HapticFeedbackConstantsCompat.GESTURE_START),
    MOVE(HapticFeedbackConstantsCompat.SEGMENT_FREQUENT_TICK),
    END(HapticFeedbackConstantsCompat.GESTURE_END)
}

fun interface ReorderHapticFeedback {
    fun performHapticFeedback(type: ReorderHapticFeedbackType)
}

@Composable
fun rememberReorderHapticFeedback(): ReorderHapticFeedback {
    val view = LocalView.current

    return remember(view) {
        ReorderHapticFeedback { type ->
            ViewCompat.performHapticFeedback(view, type.hapticConstant)
        }
    }
}
