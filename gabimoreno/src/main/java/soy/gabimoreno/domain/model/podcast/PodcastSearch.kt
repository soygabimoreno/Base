package soy.gabimoreno.domain.model.podcast

data class PodcastSearch(
    val count: Long,
    val total: Long,
    val results: List<Episode>
)
