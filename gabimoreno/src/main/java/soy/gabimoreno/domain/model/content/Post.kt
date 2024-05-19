package soy.gabimoreno.domain.model.content

import soy.gabimoreno.data.remote.model.Category

data class Post(
    val id: Long,
    val title: String,
    val excerpt: String,
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
