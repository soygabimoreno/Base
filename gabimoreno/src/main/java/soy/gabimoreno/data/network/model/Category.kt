package soy.gabimoreno.data.network.model

enum class Category(
    val id: Int,
    val title: String,
) {

    PREMIUM(262, "Premium"),
    PREMIUM_ALGORITHMS(267, "Algoritmos"),
    PREMIUM_AUDIO_COURSES(263, "AudioCursos"),
    PREMIUM_AUDIO_TIPS(264, "AudioTips"),
    PREMIUM_BIOGRAPHIES(265, "Biografías"),
    PREMIUM_TECH_TALKS(268, "Charlas Técnicas"),
    PREMIUM_INTERVIEWS(269, "Entrevistas"),
    PREMIUM_NEWS(266, "Noticias")
}

@Throws
fun List<Category>.toQueryValue(): String {
    val ids = map { it.id }
    return ids.joinToString(",")
}
