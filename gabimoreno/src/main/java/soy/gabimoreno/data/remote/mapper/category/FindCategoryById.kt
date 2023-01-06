package soy.gabimoreno.data.remote.mapper.category

import soy.gabimoreno.data.remote.model.Category

fun findCategoryById(id: Int): Category? {
    enumValues<Category>().forEach { category ->
        if (category.id == id) {
            return category
        }
    }
    return null
}
