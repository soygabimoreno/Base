package soy.gabimoreno.presentation.screen.playlist.list.view

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.DragHandle
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.customActions
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import sh.calvin.reorderable.ReorderableItem
import sh.calvin.reorderable.rememberReorderableLazyListState
import soy.gabimoreno.R
import soy.gabimoreno.domain.model.content.Playlist
import soy.gabimoreno.presentation.screen.playlist.view.PlaylistItem
import soy.gabimoreno.presentation.screen.playlist.view.reorderable.ReorderHapticFeedbackType
import soy.gabimoreno.presentation.screen.playlist.view.reorderable.buildAccessibilityActions
import soy.gabimoreno.presentation.screen.playlist.view.reorderable.rememberReorderHapticFeedback
import soy.gabimoreno.presentation.theme.PurpleDark
import soy.gabimoreno.presentation.theme.Spacing

@Composable
fun ReorderablePlaylistColumn(
    playlists: List<Playlist>,
    onItemClick: (Int) -> Unit,
    onDragFinish: (reorderedPlaylists: List<Playlist>) -> Unit,
    modifier: Modifier = Modifier
) {
    val haptic = rememberReorderHapticFeedback()
    var reorderedPlaylists by remember { mutableStateOf(playlists) }

    val lazyListState = rememberLazyListState()
    val reorderableLazyColumnState = rememberReorderableLazyListState(lazyListState) { from, to ->
        reorderedPlaylists = reorderedPlaylists.toMutableList().apply {
            add(to.index, removeAt(from.index))
        }

        haptic.performHapticFeedback(ReorderHapticFeedbackType.MOVE)
    }

    LazyColumn(
        modifier = modifier.fillMaxSize(),
        state = lazyListState,
        contentPadding = PaddingValues(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        itemsIndexed(reorderedPlaylists, key = { _, item -> item.id }) { index, item ->
            ReorderableItem(reorderableLazyColumnState, item.id) {
                val interactionSource = remember { MutableInteractionSource() }
                val labelUp = stringResource(R.string.accessibility_action_up)
                val labelDown = stringResource(R.string.accessibility_action_down)

                Box(
                    modifier = Modifier
                        .height(IntrinsicSize.Min)
                        .semantics {
                            customActions = buildAccessibilityActions(
                                index = index,
                                listSize = reorderedPlaylists.size,
                                onReorder = { newList -> reorderedPlaylists = newList },
                                reorderedPlaylists = reorderedPlaylists,
                                labelUp = labelUp,
                                labelDown = labelDown,
                            )
                        },
                    contentAlignment = Alignment.Center
                ) {
                    PlaylistItem(
                        playlist = reorderedPlaylists[index],
                        onItemClick = {
                            onItemClick(reorderedPlaylists[index].id)
                        }
                    )
                    IconButton(
                        modifier = Modifier
                            .draggableHandle(
                                onDragStarted = {
                                    haptic.performHapticFeedback(ReorderHapticFeedbackType.START)
                                },
                                onDragStopped = {
                                    haptic.performHapticFeedback(ReorderHapticFeedbackType.END)
                                    onDragFinish(reorderedPlaylists.updatePositions())
                                },
                                interactionSource = interactionSource,
                            )
                            .align(Alignment.TopEnd)
                            .clearAndSetSemantics { },
                        onClick = {},
                    ) {
                        Icon(
                            Icons.Rounded.DragHandle,
                            contentDescription = stringResource(R.string.playlists_reorder),
                            modifier = Modifier.size(Spacing.s32),
                            tint = PurpleDark
                        )
                    }
                }
            }
        }
    }
}

private fun List<Playlist>.updatePositions(): List<Playlist> =
    mapIndexed { index, playlist ->
        playlist.copy(position = index)
    }
