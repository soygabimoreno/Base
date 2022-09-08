package soy.gabimoreno.domain.model.content

import soy.gabimoreno.data.network.model.Category

data class Post(
    val id: Long,
    val title: String,
    val content: String,
    val audioUrl: String?,
    val author: String,
    val subcategoryTitle: String?,
    val url: String,
    val pubDateMillis: Long,
    val category: Category?,
) {
    fun getPremiumAudioId() = id.toString()
}
