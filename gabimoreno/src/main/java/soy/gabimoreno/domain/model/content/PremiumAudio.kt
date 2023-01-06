package soy.gabimoreno.domain.model.content

import soy.gabimoreno.data.remote.model.Category
import soy.gabimoreno.domain.model.audio.Audio
import soy.gabimoreno.domain.model.audio.Saga

data class PremiumAudio(
    override val id: String,
    override val title: String,
    override val description: String,
    override val saga: Saga,
    override val url: String,
    override val audioUrl: String,
    override val imageUrl: String,
    override val thumbnailUrl: String,
    override val pubDateMillis: Long,
    override val audioLengthInSeconds: Int,
    val category: Category,
) : Audio
