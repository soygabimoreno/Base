package soy.gabimoreno.data.network.mapper.category

import soy.gabimoreno.data.network.model.Category

fun List<Int>.toSubcategory(): Category? {
    if (size != 2) return toCategory()
    val subcategoryId = filterNot { it == Category.PREMIUM.id }[0]
    return findCategoryById(subcategoryId)
}
