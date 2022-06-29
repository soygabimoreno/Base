package soy.gabimoreno.domain.usecase

import io.mockk.every
import io.mockk.mockk
import org.amshove.kluent.shouldBeEqualTo
import org.junit.Before
import org.junit.Test
import soy.gabimoreno.domain.interoperability.BuildConfigBridge

class GetAppVersionNameUseCaseTest {

    private val buildConfigBridge: BuildConfigBridge = mockk()
    private lateinit var useCase: GetAppVersionNameUseCase

    @Before
    fun setUp() {
        useCase = GetAppVersionNameUseCase(
            buildConfigBridge
        )
    }

    @Test
    fun `GIVEN an appVersionName WHEN invoke THEN get the expected text`() {
        val appVersionName = "0.1.0"
        every { buildConfigBridge.appVersionName } returns appVersionName

        val result = useCase()

        result shouldBeEqualTo "v$appVersionName"
    }
}
