package soy.gabimoreno.domain.model.content

data class Post(
    val id: Long,
    val title: String,
    val content: String,
    val audioUrl: String?,
)
