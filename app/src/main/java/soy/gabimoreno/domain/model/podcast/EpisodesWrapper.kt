package soy.gabimoreno.domain.model.podcast

data class EpisodesWrapper(
    val count: Long,
    val total: Long,
    val episodes: List<Episode>,
)
