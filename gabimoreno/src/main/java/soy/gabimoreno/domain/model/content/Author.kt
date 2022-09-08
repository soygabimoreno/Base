package soy.gabimoreno.domain.model.content

enum class Author(
    val id: Int,
    val displayName: String,
) {
    GABI_MORENO(1001, "Gabi Moreno"),
}

fun findAuthorDisplayNameById(id: Int): String? {
    enumValues<Author>().forEach { author ->
        if (author.id == id) {
            return author.displayName
        }
    }
    return null
}
