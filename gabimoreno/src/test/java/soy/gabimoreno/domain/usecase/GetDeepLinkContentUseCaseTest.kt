package soy.gabimoreno.domain.usecase

import org.amshove.kluent.shouldBe
import org.amshove.kluent.shouldBeEqualTo
import org.junit.Before
import org.junit.Test
import soy.gabimoreno.domain.model.GABI_MORENO_WEB_BASE_URL

class GetDeepLinkContentUseCaseTest {
    private lateinit var useCase: GetDeepLinkContentUseCase

    @Before
    fun setUp() {
        useCase = GetDeepLinkContentUseCase()
    }

    @Test
    fun `GIVEN parameters with an episodeNumber WHEN invoke THEN get the expected result`() {
        val episodeNumber = 1
        val parameters = listOf(episodeNumber.toString())

        val result = useCase(parameters)

        result.episodeNumber shouldBe episodeNumber
        result.url shouldBe null
    }

    @Test
    fun `GIVEN empty parameters WHEN invoke THEN get the expected result`() {
        val parameters = emptyList<String>()

        val result = useCase(parameters)

        result.episodeNumber shouldBe null
        result.url shouldBe GABI_MORENO_WEB_BASE_URL
    }

    @Test
    fun `GIVEN parameters with a simple url WHEN invoke THEN get the expected result`() {
        val parameters = listOf("plan-impulso-android")

        val result = useCase(parameters)

        result.episodeNumber shouldBe null
        result.url shouldBeEqualTo "$GABI_MORENO_WEB_BASE_URL/${parameters[0]}"
    }

    @Test
    fun `GIVEN parameters with a complex url WHEN invoke THEN get the expected result`() {
        val parameters = listOf("categoria", "podcast")

        val result = useCase(parameters)

        result.episodeNumber shouldBe null
        result.url shouldBeEqualTo "$GABI_MORENO_WEB_BASE_URL/${parameters[0]}/${parameters[1]}"
    }
}
