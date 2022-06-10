package soy.gabimoreno.data.network.model

import soy.gabimoreno.domain.model.PodcastSearch

// TODO: Change Dto to ApiModel
data class PodcastSearchDto(
    val count: Long,
    val total: Long,
    val results: List<EpisodeDto>
) {

    fun asDomainModel() = PodcastSearch(
        count,
        total,
        results.map { it.asDomainModel() }
    )
}
