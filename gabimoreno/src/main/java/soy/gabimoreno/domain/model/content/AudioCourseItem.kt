package soy.gabimoreno.domain.model.content

data class AudioCourseItem(
    val id: String,
    val title: String,
    val url: String,
    val hasBeenListened: Boolean = false
)
