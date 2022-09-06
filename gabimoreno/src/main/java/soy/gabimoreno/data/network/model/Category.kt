package soy.gabimoreno.data.network.model

enum class Category(val id: Int) {
    PREMIUM(262),
    PREMIUM_ALGORITHMS(267),
    PREMIUM_AUDIO_COURSES(263),
    PREMIUM_AUDIO_TIPS(264),
    PREMIUM_BIOGRAPHIES(265),
    PREMIUM_TECH_TALKS(268),
    PREMIUM_INTERVIEWS(269),
    PREMIUM_NEWS(266)
}

@Throws
fun List<Category>.toQueryValue(): String {
    val ids = map { it.id }
    return ids.joinToString(",")
}
