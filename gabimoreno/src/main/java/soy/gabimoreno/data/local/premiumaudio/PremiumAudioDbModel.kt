package soy.gabimoreno.data.local.premiumaudio

import androidx.room.Entity
import androidx.room.PrimaryKey
import soy.gabimoreno.data.remote.model.Category
import soy.gabimoreno.domain.model.audio.Saga

@Entity
data class PremiumAudioDbModel(
    @PrimaryKey
    val id: String,
    val title: String,
    val description: String,
    val saga: Saga,
    val url: String,
    val audioUrl: String,
    val imageUrl: String,
    val thumbnailUrl: String,
    val pubDateMillis: Long,
    val audioLengthInSeconds: Int,
    val category: Category,
    val excerpt: String,
)
