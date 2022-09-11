package soy.gabimoreno.data.network.mapper.category

import soy.gabimoreno.data.network.model.Category

fun List<Int>.toCategory(): Category? {
    if (size != 1) return null
    val categoryId = this[0]
    val category = findCategoryById(categoryId)
    return if (category == Category.PREMIUM) {
        category
    } else {
        null
    }
}
