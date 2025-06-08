package soy.gabimoreno.domain.model.content

data class Playlist(
    val id: Int,
    val title: String,
    val description: String,
    val position: Int,
    val items: List<PlaylistAudioItem>,
)
