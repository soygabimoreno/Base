package soy.gabimoreno.presentation.screen.playlist.detail

import soy.gabimoreno.domain.model.content.Playlist
import soy.gabimoreno.domain.model.content.PlaylistAudioItem
import soy.gabimoreno.presentation.screen.playlist.list.EMPTY_STRING

data class PlaylistDetailState(
    val isLoading: Boolean = false,
    val audio: PlaylistAudioItem? = null,
    val playlist: Playlist? = null,
    val playlistAudioItems: List<PlaylistAudioItem> = emptyList(),
    val selectedPlaylistAudioItem: String? = null,
    val shouldIShowDialog: Boolean = false,
    val dialogTitle: String = EMPTY_STRING,
    val dialogDescription: String = EMPTY_STRING,
    val dialogTitleError: Boolean = false,
    val dialogDescriptionError: Boolean = false,
    val dialogType: PlaylistDialogType = PlaylistDialogType.Edit,
)

sealed class PlaylistDialogType {
    data object Delete : PlaylistDialogType()
    data object Edit : PlaylistDialogType()
}
