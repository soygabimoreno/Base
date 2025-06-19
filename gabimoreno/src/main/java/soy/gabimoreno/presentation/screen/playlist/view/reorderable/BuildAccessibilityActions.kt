package soy.gabimoreno.presentation.screen.playlist.view.reorderable

import androidx.compose.ui.semantics.CustomAccessibilityAction

internal fun <T> buildAccessibilityActions(
    index: Int,
    listSize: Int,
    onReorder: (List<T>) -> Unit,
    reorderedItems: List<T>,
    labelUp: String,
    labelDown: String
): List<CustomAccessibilityAction> = buildList {
    if (index > 0) {
        add(
            CustomAccessibilityAction(labelUp) {
                onReorder(reorderedItems.moveItemOnList(index, index - 1))
                true
            }
        )
    }
    if (index < listSize - 1) {
        add(
            CustomAccessibilityAction(labelDown) {
                onReorder(reorderedItems.moveItemOnList(index, index + 1))
                true
            }
        )
    }
}
