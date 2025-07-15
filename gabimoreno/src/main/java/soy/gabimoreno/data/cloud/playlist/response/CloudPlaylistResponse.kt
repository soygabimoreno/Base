package soy.gabimoreno.data.cloud.playlist.response

data class CloudPlaylistResponse(
    val playlistId: String = EMPTY_STRING,
    val categoryId: Int = DEFAULT_INT,
    val description: String = EMPTY_STRING,
    val position: Int = DEFAULT_INT,
    val title: String = EMPTY_STRING
)

private const val EMPTY_STRING = ""
private const val DEFAULT_INT = -1
