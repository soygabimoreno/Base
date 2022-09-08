package soy.gabimoreno.data.network.mapper

import soy.gabimoreno.data.network.model.Category

fun List<Int>.toSubcategoryTitleResId(): String? {
    if (size != 2) return null
    val subcategoryId = filterNot { it == Category.PREMIUM.id }[0]
    return findCategoryTitleById(subcategoryId)
}

private fun findCategoryTitleById(id: Int): String? {
    enumValues<Category>().forEach { category ->
        if (category.id == id) {
            return category.title
        }
    }
    return null
}
