package soy.gabimoreno.data.network.mapper

import soy.gabimoreno.data.network.model.PostApiModel
import soy.gabimoreno.domain.model.content.Post

fun List<PostApiModel>.toDomain(): List<Post> = map { it.toDomain() }

fun PostApiModel.toDomain() = Post(
    id = id
)
