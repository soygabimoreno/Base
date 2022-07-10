package soy.gabimoreno.data.tracker

import org.amshove.kluent.shouldBe
import org.junit.Test
import soy.gabimoreno.data.tracker.domain.EPISODE_ID
import soy.gabimoreno.data.tracker.domain.EPISODE_TITLE
import soy.gabimoreno.fake.buildEpisode

class TrackerMapperKtTest {

    @Test
    fun `GIVEN an episode WHEN toMap() THEN get the expected map`() {
        val episode = buildEpisode()
        val episodeId = episode.id
        val episodeTitle = episode.title

        val result = episode.toMap()

        result[EPISODE_ID] shouldBe episodeId
        result[EPISODE_TITLE] shouldBe episodeTitle
    }
}
