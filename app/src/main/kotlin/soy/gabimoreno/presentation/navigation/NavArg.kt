package soy.gabimoreno.presentation.navigation

import androidx.navigation.NavType

enum class NavArg(
    val key: String,
    val navType: NavType<*>,
) {
    EpisodeId("episodeId", NavType.StringType),
    EncodedUrl("encodedUrl", NavType.StringType),
    PremiumAudioId("premiumAudioId", NavType.StringType),
    AudioCourseId("audioCourseId", NavType.StringType),
    PlaylistId("playlistId", NavType.StringType),
    AudioItemId("audioItemId", NavType.StringType),
}
