package soy.gabimoreno.data.local.mapper

import org.amshove.kluent.shouldBe
import org.amshove.kluent.shouldBeEqualTo
import org.junit.Test
import soy.gabimoreno.fake.buildPremiumAudioDbModel

class ToPremiumAudioMapperKtTest {

    @Test
    fun `GIVEN a PremiumAudioDbModel WHEN toPremiumAudio THEN get the expected PremiumAudio`() {
        val premiumAudioDbModel = buildPremiumAudioDbModel()
        with(premiumAudioDbModel) {
            val id = id
            val title = title
            val description = description
            val saga = saga
            val url = url
            val audioUrl = audioUrl
            val imageUrl = imageUrl
            val thumbnailUrl = thumbnailUrl
            val pubDateMillis = pubDateMillis
            val audioLengthInSeconds = audioLengthInSeconds
            val category = category
            val excerpt = excerpt

            val result = premiumAudioDbModel.toPremiumAudio()
            result.id shouldBe id
            result.title shouldBe title
            result.description shouldBe description
            result.saga shouldBe saga
            result.url shouldBe url
            result.audioUrl shouldBe audioUrl
            result.imageUrl shouldBe imageUrl
            result.thumbnailUrl shouldBe thumbnailUrl
            result.pubDateMillis shouldBe pubDateMillis
            result.audioLengthInSeconds shouldBeEqualTo audioLengthInSeconds
            result.category shouldBeEqualTo category
            result.excerpt shouldBeEqualTo excerpt
        }
    }

    @Test
    fun `GIVEN a PremiumAudioDbModel WHEN toPlaylistAudioItem THEN get the expected PlaylistAudioItem`() {
        val premiumAudioDbModel = buildPremiumAudioDbModel()
        val position = 2
        with(premiumAudioDbModel) {
            val id = id
            val title = title
            val description = description
            val saga = saga
            val url = url
            val audioUrl = audioUrl
            val imageUrl = imageUrl
            val thumbnailUrl = thumbnailUrl
            val pubDateMillis = pubDateMillis
            val audioLengthInSeconds = audioLengthInSeconds
            val category = category
            val excerpt = excerpt

            val result = premiumAudioDbModel.toPlaylistAudioItem(position)
            result.id shouldBe id
            result.title shouldBe title
            result.description shouldBe description
            result.saga shouldBe saga
            result.url shouldBe url
            result.audioUrl shouldBe audioUrl
            result.imageUrl shouldBe imageUrl
            result.thumbnailUrl shouldBe thumbnailUrl
            result.pubDateMillis shouldBe pubDateMillis
            result.audioLengthInSeconds shouldBeEqualTo audioLengthInSeconds
            result.category shouldBeEqualTo category
            result.excerpt shouldBeEqualTo excerpt
            result.position shouldBeEqualTo position
        }
    }
}
