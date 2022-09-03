package soy.gabimoreno.domain.model.podcast

data class Episode(
    val id: String,
    val url: String,
    val audioUrl: String,
    val imageUrl: String,
    val podcast: Podcast,
    val thumbnailUrl: String,
    val pubDateMillis: Long,
    val title: String,
    val audioLengthInSeconds: Int,
    val description: String
)
