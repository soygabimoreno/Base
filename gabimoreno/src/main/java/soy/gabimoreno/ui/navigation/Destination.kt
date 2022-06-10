package soy.gabimoreno.ui.navigation

object Destination {
    // TODO: Remove
//    const val welcome = "welcome"
    const val home = "home"
    const val podcast = "podcast/{id}"

    fun podcast(id: String): String = "podcast/$id"
}
