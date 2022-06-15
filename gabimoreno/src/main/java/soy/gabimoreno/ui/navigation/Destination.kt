package soy.gabimoreno.ui.navigation

object Destination {
    const val home = "home"
    const val podcast = "podcast/{id}"

    fun podcast(id: String): String = "podcast/$id"
}
