package soy.gabimoreno.presentation.screen.playlist.view.reorderable

import androidx.compose.ui.semantics.CustomAccessibilityAction
import soy.gabimoreno.domain.model.content.Playlist

internal fun buildAccessibilityActions(
    index: Int,
    listSize: Int,
    onReorder: (List<Playlist>) -> Unit,
    reorderedPlaylists: List<Playlist>,
    labelUp : String,
    labelDown : String
): List<CustomAccessibilityAction> = buildList {
    if (index > 0) {
        add(
            CustomAccessibilityAction(labelUp) {
                onReorder(reorderedPlaylists.moveItemOnList(index, index - 1))
                true
            }
        )
    }
    if (index < listSize - 1) {
        add(
            CustomAccessibilityAction(labelDown) {
                onReorder(reorderedPlaylists.moveItemOnList(index, index + 1))
                true
            }
        )
    }
}
