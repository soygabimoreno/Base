package soy.gabimoreno.data.network.mapper

import soy.gabimoreno.data.network.model.Category

fun List<Int>.toSubcategory(): Category? {
    if (size != 2) return null
    val subcategoryId = filterNot { it == Category.PREMIUM.id }[0]
    return findCategoryById(subcategoryId)
}

private fun findCategoryById(id: Int): Category? {
    enumValues<Category>().forEach { category ->
        if (category.id == id) {
            return category
        }
    }
    return null
}
