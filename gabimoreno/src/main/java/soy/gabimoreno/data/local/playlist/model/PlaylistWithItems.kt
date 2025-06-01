package soy.gabimoreno.data.local.playlist.model

import androidx.room.Embedded
import androidx.room.Relation

data class PlaylistWithItems(
    @Embedded val playlist: PlaylistDbModel,
    @Relation(
        parentColumn = "id",
        entityColumn = "playlistId"
    )
    val items: List<PlaylistItemsDbModel>
)
