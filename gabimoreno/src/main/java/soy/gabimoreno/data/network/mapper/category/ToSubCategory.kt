package soy.gabimoreno.data.network.mapper.category

import soy.gabimoreno.data.network.model.Category

fun List<Int>.toSubcategory(): Category? {
    val premiumCategories = Category.values().toList().map { it.id }
    val intersectedCategories = intersect(premiumCategories.toSet()).toList()
    if (intersectedCategories.size != 2) return intersectedCategories.toCategory()
    val subcategoryId = intersectedCategories.filterNot { it == Category.PREMIUM.id }[0]
    return findCategoryById(subcategoryId)
}
