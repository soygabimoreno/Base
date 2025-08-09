package soy.gabimoreno.domain.util

enum class AudioItemType {
    AUDIO_COURSE,
    PREMIUM_AUDIO,
    PODCAST,
}

fun audioItemTypeDetector(id: String): AudioItemType =
    when {
        Regex("^\\d+-\\d+$").matches(id) -> AudioItemType.AUDIO_COURSE
        Regex("^\\d+$").matches(id) -> AudioItemType.PREMIUM_AUDIO
        Regex("^[0-9a-fA-F\\-]{36}$").matches(id) -> AudioItemType.PODCAST
        else -> throw IllegalArgumentException("Unknown audio item format: $id")
    }
