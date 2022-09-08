package soy.gabimoreno.data.network.model

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.ui.graphics.vector.ImageVector

enum class Category(
    val id: Int,
    val title: String,
    val icon: ImageVector,
) {
    PREMIUM(262, "Premium", Icons.Default.WorkspacePremium),
    PREMIUM_ALGORITHMS(267, "Algoritmos", Icons.Default.Calculate),
    PREMIUM_ANDROID_STORIES(270, "Historias androides", Icons.Default.AutoStories),
    PREMIUM_AUDIO_COURSES(263, "AudioCursos", Icons.Default.MenuBook),
    PREMIUM_AUDIO_TIPS(264, "AudioTips", Icons.Default.TipsAndUpdates),
    PREMIUM_BIOGRAPHIES(265, "Biografías", Icons.Default.HistoryEdu),
    PREMIUM_INTERVIEWS(269, "Entrevistas", Icons.Default.Group),
    PREMIUM_NEWS(266, "Noticias", Icons.Default.Feed),
    PREMIUM_TECH_TALKS(268, "Charlas Técnicas", Icons.Default.Code)
}

@Throws
fun List<Category>.toQueryValue(): String {
    val ids = map { it.id }
    return ids.joinToString(",")
}
