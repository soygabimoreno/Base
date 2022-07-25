package soy.gabimoreno.domain.usecase

import org.amshove.kluent.shouldBeEqualTo
import org.junit.Before
import org.junit.Test
import soy.gabimoreno.core.testing.relaxedMockk
import soy.gabimoreno.data.network.mapper.GABI_MORENO_WEB_BASE_URL
import soy.gabimoreno.data.tracker.Tracker

class EncodeUrlUseCaseKtTest {

    private val tracker: Tracker = relaxedMockk()
    private lateinit var useCase: EncodeUrlUseCase

    @Before
    fun setUp() {
        useCase = EncodeUrlUseCase(
            tracker
        )
    }

    @Test
    fun `GIVEN valid url WHEN encodeUrl THEN get the proper string`() {
        val url = GABI_MORENO_WEB_BASE_URL

        val result = useCase(url)

        result shouldBeEqualTo GABI_MORENO_WEB_BASE_URL_ENCODED
    }
}
