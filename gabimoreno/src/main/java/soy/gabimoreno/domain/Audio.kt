package soy.gabimoreno.domain

import java.io.Serializable

data class Audio(
    val id: Long,
    val artist: String,
    val filename: String,
    val name: String,
    val description: String,
    val thumbnailUrl: String,
    val audioUrl: String,
) : Serializable
