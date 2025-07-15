package soy.gabimoreno.data.cloud.audiosync.mappers

import soy.gabimoreno.data.cloud.playlist.response.CloudPlaylistItemResponse
import soy.gabimoreno.data.local.playlist.model.PlaylistItemsDbModel
import soy.gabimoreno.domain.model.content.PlaylistAudioItem

fun CloudPlaylistItemResponse.toPlaylistItemDbModel() = PlaylistItemsDbModel(
    id = id.toInt(),
    audioItemId = audioItemId,
    playlistId = playlistId.toInt(),
    position = position,
)

fun PlaylistItemsDbModel.toCloudPlaylistItemResponse() = CloudPlaylistItemResponse(
    id = id.toString(),
    audioItemId = audioItemId,
    playlistId = playlistId.toString(),
    position = position,
)

fun PlaylistAudioItem.toCloudPlaylistItemResponse(playlistId: Int) = CloudPlaylistItemResponse(
    id = playlistItemId.toString(),
    audioItemId = id,
    playlistId = playlistId.toString(),
    position = position,
)
