package soy.gabimoreno.data.network.mapper.category

import soy.gabimoreno.data.network.model.Category

fun findCategoryById(id: Int): Category? {
    enumValues<Category>().forEach { category ->
        if (category.id == id) {
            return category
        }
    }
    return null
}
