package soy.gabimoreno.domain.model.content

enum class Author(
    val id: Int,
    val displayName: String,
) {
    GABI_MORENO(DEFAULT_AUTHOR_ID, DEFAULT_AUTHOR_DISPLAY_NAME),
}

fun findAuthorDisplayNameById(id: Int): String? {
    enumValues<Author>().forEach { author ->
        if (author.id == id) {
            return author.displayName
        }
    }
    return null
}

private const val DEFAULT_AUTHOR_DISPLAY_NAME = "Gabi Moreno"
private const val DEFAULT_AUTHOR_ID = 1001
