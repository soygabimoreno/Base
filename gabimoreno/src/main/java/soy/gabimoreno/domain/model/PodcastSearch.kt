package soy.gabimoreno.domain.model

data class PodcastSearch(
    val count: Long,
    val total: Long,
    val results: List<Episode>
)
