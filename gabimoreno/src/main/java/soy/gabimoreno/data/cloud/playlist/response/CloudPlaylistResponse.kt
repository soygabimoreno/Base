package soy.gabimoreno.data.cloud.playlist.response

data class CloudPlaylistResponse(
    val playlistId: String = "",
    val categoryId: Int = -1,
    val description: String = "",
    val position: Int = -1,
    val title: String = ""
)
