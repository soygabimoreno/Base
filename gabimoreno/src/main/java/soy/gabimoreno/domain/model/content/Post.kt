package soy.gabimoreno.domain.model.content

data class Post(
    val id: Long,
    val title: String,
    val content: String,
    val audioUrl: String?,
    val author: String,
    val subcategoryTitle: String?,
    val url: String,
    val pubDateMillis: Long,
) {
    fun getPremiumAudioId() = id.toString()
}
