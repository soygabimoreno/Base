package soy.gabimoreno.presentation.screen.playlist.detail.view

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.DeleteForever
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import sh.calvin.reorderable.ReorderableItem
import sh.calvin.reorderable.rememberReorderableLazyListState
import soy.gabimoreno.R
import soy.gabimoreno.domain.model.content.PlaylistAudioItem
import soy.gabimoreno.presentation.screen.playlist.view.reorderable.ReorderHapticFeedbackType
import soy.gabimoreno.presentation.screen.playlist.view.reorderable.buildAccessibilityActions
import soy.gabimoreno.presentation.screen.playlist.view.reorderable.rememberReorderHapticFeedback
import soy.gabimoreno.presentation.theme.Black
import soy.gabimoreno.presentation.theme.Pink
import soy.gabimoreno.presentation.theme.PurpleDark
import soy.gabimoreno.presentation.theme.Spacing
import soy.gabimoreno.presentation.theme.White

@Composable
fun ReorderablePlaylistItemColumn(
    playlistAudioItems: List<PlaylistAudioItem>,
    onItemClicked: (playlistAudioItem: PlaylistAudioItem) -> Unit,
    onDragFinish: (reorderedAudioItems: List<PlaylistAudioItem>) -> Unit,
    onRemoveClicked: (playlistAudioItem: PlaylistAudioItem) -> Unit,
    modifier: Modifier = Modifier
) {
    val haptic = rememberReorderHapticFeedback()
    var reorderedAudioItems by remember(playlistAudioItems) {
        mutableStateOf(playlistAudioItems)
    }
    val lazyListState = rememberLazyListState()
    val reorderableLazyColumnState = rememberReorderableLazyListState(lazyListState) { from, to ->
        reorderedAudioItems = reorderedAudioItems.toMutableList().apply {
            add(to.index, removeAt(from.index))
        }

        haptic.performHapticFeedback(ReorderHapticFeedbackType.MOVE)
    }

    LazyColumn(
        modifier = modifier.fillMaxSize(),
        state = lazyListState,
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        itemsIndexed(reorderedAudioItems, key = { _, item -> item.id }) { index, item ->
            ReorderableItem(reorderableLazyColumnState, item.id) {
                val interactionSource = remember { MutableInteractionSource() }
                val labelUp = stringResource(R.string.accessibility_action_up)
                val labelDown = stringResource(R.string.accessibility_action_down)

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(IntrinsicSize.Min)
                        .semantics {
                            customActions = buildAccessibilityActions(
                                index = index,
                                listSize = reorderedAudioItems.size,
                                onReorder = { newList -> reorderedAudioItems = newList },
                                reorderedItems = reorderedAudioItems,
                                labelUp = labelUp,
                                labelDown = labelDown,
                            )
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onItemClicked(reorderedAudioItems[index]) }
                            .background(White.copy(alpha = 0.95f))
                            .padding(
                                start = Spacing.s8,
                                top = Spacing.s16,
                                bottom = Spacing.s16,
                                end = Spacing.s48
                            ),
                        verticalArrangement = Arrangement.SpaceEvenly
                    ) {
                        Text(
                            reorderedAudioItems[index].title,
                            color = Black,
                            fontWeight = FontWeight.SemiBold
                        )
                        Spacer(modifier = Modifier.height(Spacing.s8))
                        Text(
                            if (reorderedAudioItems[index].id.contains("-"))
                                reorderedAudioItems[index].description
                            else
                                reorderedAudioItems[index].category.title,
                            color = Black.copy(alpha = 0.8f),
                            fontWeight = FontWeight.Light
                        )
                    }
                    IconButton(
                        modifier = Modifier
                            .draggableHandle(
                                onDragStarted = {
                                    haptic.performHapticFeedback(ReorderHapticFeedbackType.START)
                                },
                                onDragStopped = {
                                    haptic.performHapticFeedback(ReorderHapticFeedbackType.END)
                                    onDragFinish(reorderedAudioItems.updatePositions())
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
                    IconButton(
                        modifier = Modifier
                            .align(Alignment.BottomEnd),
                        onClick = {
                            onRemoveClicked(reorderedAudioItems[index])
                        },
                    ) {
                        Icon(
                            Icons.Rounded.DeleteForever,
                            contentDescription = stringResource(R.string.playlists_remove),
                            modifier = Modifier.size(Spacing.s32),
                            tint = Pink
                        )
                    }
                }
            }
        }
    }
}

private fun List<PlaylistAudioItem>.updatePositions(): List<PlaylistAudioItem> =
    mapIndexed { index, playlistAudioItem ->
        playlistAudioItem.copy(position = index)
    }
