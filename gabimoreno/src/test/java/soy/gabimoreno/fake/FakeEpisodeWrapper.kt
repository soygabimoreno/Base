package soy.gabimoreno.fake

import soy.gabimoreno.domain.model.podcast.EpisodesWrapper

fun buildEpisodesWrapper() = EpisodesWrapper(
    count = 1L,
    total = 1L,
    episodes = buildEpisodes()
)
