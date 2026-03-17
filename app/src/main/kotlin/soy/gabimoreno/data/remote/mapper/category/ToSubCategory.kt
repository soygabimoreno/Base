package soy.gabimoreno.data.remote.mapper.category

import soy.gabimoreno.data.remote.model.Category

fun List<Int>.toSubcategory(): Category? {
    val premiumCategories = Category.entries.map { it.id }
    val intersectedCategories = intersect(premiumCategories.toSet()).toList()
    if (intersectedCategories.size != 2) return intersectedCategories.toCategory()
    val subcategoryId = intersectedCategories.filterNot { it == Category.PREMIUM.id }[0]
    return findCategoryById(subcategoryId)
}
