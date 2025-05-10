package soy.gabimoreno.domain.model.audio

interface Audio {
    val id: String
    val title: String
    val description: String
    val saga: Saga
    val url: String
    val audioUrl: String
    val imageUrl: String
    val thumbnailUrl: String
    val pubDateMillis: Long
    val audioLengthInSeconds: Int
    val hasBeenListened: Boolean
}
