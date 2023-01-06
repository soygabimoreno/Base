package soy.gabimoreno.data.remote.model

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.ui.graphics.vector.ImageVector

enum class Category(
    val id: Int,
    val title: String,
    val icon: ImageVector,
    val coverUrl: String,
) {
    PREMIUM(262,
            "Premium",
            Icons.Default.WorkspacePremium,
            "${COVER_URL_PREFIX}premium$COVER_URL_SUFFIX"),
    PREMIUM_ALGORITHMS(267,
                       "Algoritmos",
                       Icons.Default.Calculate,
                       "${COVER_URL_PREFIX}algoritmos$COVER_URL_SUFFIX"),
    PREMIUM_ANDROID_STORIES(270,
                            "Historias androides",
                            Icons.Default.AutoStories,
                            "${COVER_URL_PREFIX}historias-androides$COVER_URL_SUFFIX"),
    PREMIUM_AUDIO_COURSES(263,
                          "AudioCursos",
                          Icons.Default.MenuBook,
                          "${COVER_URL_PREFIX}audiocursos$COVER_URL_SUFFIX"),
    PREMIUM_AUDIO_TIPS(264,
                       "AudioTips",
                       Icons.Default.TipsAndUpdates,
                       "${COVER_URL_PREFIX}audiotips$COVER_URL_SUFFIX"),
    PREMIUM_BIOGRAPHIES(265,
                        "Biografías",
                        Icons.Default.HistoryEdu,
                        "${COVER_URL_PREFIX}biografias$COVER_URL_SUFFIX"),
    PREMIUM_INTERVIEWS(269,
                       "Entrevistas",
                       Icons.Default.Group,
                       "${COVER_URL_PREFIX}entrevistas$COVER_URL_SUFFIX"),
    PREMIUM_NEWS(266,
                 "Noticias",
                 Icons.Default.Feed,
                 "${COVER_URL_PREFIX}noticias$COVER_URL_SUFFIX"),
    PREMIUM_TECH_TALKS(268,
                       "Charlas Técnicas",
                       Icons.Default.Code,
                       "${COVER_URL_PREFIX}charlas-tecnicas$COVER_URL_SUFFIX")
}

@Throws
fun List<Category>.toQueryValue(): String {
    val ids = map { it.id }
    return ids.joinToString(",")
}

private const val COVER_URL_PREFIX = "https://gabimoreno.soy/images/premium/cover-"
private const val COVER_URL_SUFFIX = ".png"
