package soy.gabimoreno.data.cloud.playlist.response

data class CloudPlaylistItemResponse(
    val id: Int = -1,
    val audioItemId: String = "",
    val playlistId: String = "",
    val position: Int = -1,
)
