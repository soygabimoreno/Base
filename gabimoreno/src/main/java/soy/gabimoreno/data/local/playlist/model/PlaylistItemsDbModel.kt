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
            childColumns = ["idPlaylist"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("idPlaylist")]
)
data class PlaylistItemsDbModel(
    @PrimaryKey
    val id: String,
    val idPlaylist: Int,
    val position: Int,
)
