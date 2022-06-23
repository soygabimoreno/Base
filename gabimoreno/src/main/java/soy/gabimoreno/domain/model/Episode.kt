package soy.gabimoreno.domain.model

data class Episode(
    val id: String,
    val url: String,
    val audioUrl: String,
    val imageUrl: String,
    val podcast: Podcast,
    val thumbnailUrl: String,
    val pubDateMillis: Long,
    val title: String,
    val audioLengthSeconds: Int,
    val description: String
)
