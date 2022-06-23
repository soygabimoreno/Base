package soy.gabimoreno.presentation.navigation

object Destination {
    const val home = "home"
    const val detail = "$DETAIL/{id}"

    fun detail(episodeId: String): String = "$DETAIL/$episodeId"
}

private const val DETAIL = "detail"
