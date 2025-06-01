package soy.gabimoreno.data.local.playlist.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class PlaylistDbModel(
    @PrimaryKey(autoGenerate = true)
    val id: Int? = null,
    val title: String,
    val description: String,
    val position: Int,
)
