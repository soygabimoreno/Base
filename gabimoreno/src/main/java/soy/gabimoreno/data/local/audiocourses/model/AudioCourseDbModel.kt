package soy.gabimoreno.data.local.audiocourses.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import soy.gabimoreno.data.remote.model.Category
import soy.gabimoreno.domain.model.audio.Saga

@Entity
data class AudioCourseDbModel(
    @PrimaryKey
    val id: String,
    val title: String,
    val description: String,
    val excerpt: String,
    val saga: Saga,
    val url: String,
    val videoUrl: String,
    val thumbnailUrl: String,
    val pubDateMillis: Long,
    val audioLengthInSeconds: Int,
    val category: Category,
    val isPurchased: Boolean,
)
