package soy.gabimoreno.domain.model.content

data class Playlist(
    val id: Int,
    val category: PlaylistCategory = PlaylistCategory.USER_PLAYLIST,
    val description: String,
    val items: List<PlaylistAudioItem>,
    val position: Int,
    val title: String,
)
