package soy.gabimoreno.presentation.screen.playlist.detail

import soy.gabimoreno.domain.model.content.PlaylistAudioItem

sealed interface PlaylistDetailAction {
    data object OnBackClicked : PlaylistDetailAction
    data object OnPlayClicked : PlaylistDetailAction
    data class OnAudioItemClicked(
        val playlistAudioItem: PlaylistAudioItem,
    ) : PlaylistDetailAction
    data class OnAudioItemsReordered(
        val playlistAudioItems: List<PlaylistAudioItem>,
    ) : PlaylistDetailAction
    data class OnRemovePlaylistAudioItem(
        val playlistAudioItemId: String,
    ) : PlaylistDetailAction
    data object OnEditPlaylistClicked : PlaylistDetailAction
    data object OnDismissDialog : PlaylistDetailAction
    data object OnConfirmDialog : PlaylistDetailAction
    data class OnDialogTitleChange(
        val title: String,
    ) : PlaylistDetailAction
    data class OnDialogDescriptionChange(
        val description: String,
    ) : PlaylistDetailAction
    data object OnEditPlaylistConfirmDialog : PlaylistDetailAction
    data object OnEditPlaylistDismissDialog : PlaylistDetailAction
}
