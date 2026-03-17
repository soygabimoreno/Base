package soy.gabimoreno.data.cloud.playlist.response

data class CloudPlaylistItemResponse(
    val id: String = EMPTY_STRING,
    val audioItemId: String = EMPTY_STRING,
    val playlistId: String = EMPTY_STRING,
    val position: Int = DEFAULT_INT,
)

private const val EMPTY_STRING = ""
private const val DEFAULT_INT = -1
