package soy.gabimoreno.presentation.screen.playlist.view.reorderable

internal fun <T> List<T>.moveItemOnList(
    fromIndex: Int,
    toIndex: Int,
): List<T> =
    toMutableList().apply {
        add(toIndex, removeAt(fromIndex))
    }
