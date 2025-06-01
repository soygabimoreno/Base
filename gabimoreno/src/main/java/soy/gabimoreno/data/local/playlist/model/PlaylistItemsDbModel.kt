package soy.gabimoreno.data.local.playlist.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "PlaylistItemsDbModel",
    foreignKeys = [
        ForeignKey(
            entity = PlaylistDbModel::class,
            parentColumns = ["id"],
            childColumns = ["playlistId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("playlistId")]
)
data class PlaylistItemsDbModel(
    @PrimaryKey
    val id: String,
    val playlistId: Int,
    val position: Int,
)
