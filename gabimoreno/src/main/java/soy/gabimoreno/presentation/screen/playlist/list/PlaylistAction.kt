package soy.gabimoreno.presentation.screen.playlist.list

import soy.gabimoreno.domain.model.content.Playlist

sealed interface PlaylistAction {
    data object OnBackClicked : PlaylistAction
    data class OnItemClicked(val playlistId: Int) : PlaylistAction
    data object OnAddNewPlaylistClicked : PlaylistAction
    data object OnAddPlaylistDialogDismiss : PlaylistAction
    data object OnAddPlaylistDialogConfirm : PlaylistAction
    data class OnDialogTitleChange(val title: String) : PlaylistAction
    data class OnDialogDescriptionChange(val description: String) : PlaylistAction
    data class OnItemDragFinish(val reorderedPlaylists: List<Playlist>) : PlaylistAction
}
