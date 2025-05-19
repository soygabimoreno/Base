package soy.gabimoreno.presentation.screen.auth

import androidx.compose.material.ModalBottomSheetState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class AuthBottomSheetController(
    private val state: ModalBottomSheetState,
    private val scope: CoroutineScope
) {
    fun show() = scope.launch { state.show() }
    fun hide() = scope.launch { state.hide() }
    fun toggle() = scope.launch { if (state.isVisible) state.hide() else state.show() }
}
