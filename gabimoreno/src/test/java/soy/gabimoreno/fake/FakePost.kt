package soy.gabimoreno.fake

import soy.gabimoreno.domain.model.content.Post

fun buildPosts() = listOf(
    buildPost().copy(id = 1L),
    buildPost().copy(id = 2L),
    buildPost().copy(id = 3L)
)

fun buildPost() = Post(
    id = 1234L,
    title = "title",
    content = "content",
    audioUrl = "audioUrl"
)
