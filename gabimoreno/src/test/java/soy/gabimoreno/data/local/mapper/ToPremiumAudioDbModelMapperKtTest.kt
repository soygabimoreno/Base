package soy.gabimoreno.data.local.mapper

import org.amshove.kluent.shouldBe
import org.amshove.kluent.shouldBeEqualTo
import org.junit.Test
import soy.gabimoreno.fake.buildPremiumAudio

class ToPremiumAudioDbModelMapperKtTest {

    @Test
    fun `GIVEN a PremiumAudio WHEN toPremiumAudioDbModel THEN get the expected PremiumAudioDbModel`() {
        val premiumAudio = buildPremiumAudio()
        with(premiumAudio) {
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

            val result = premiumAudio.toPremiumAudioDbModel()
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
        }
    }
}
