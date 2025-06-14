package soy.gabimoreno.data.local.playlist.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import soy.gabimoreno.domain.model.content.PlaylistCategory

@Entity
data class PlaylistDbModel(
    @PrimaryKey(autoGenerate = true)
    val id: Int? = null,
    val categoryId: Int = PlaylistCategory.USER_PLAYLIST.id,
    val description: String,
    val position: Int,
    val title: String,
)
