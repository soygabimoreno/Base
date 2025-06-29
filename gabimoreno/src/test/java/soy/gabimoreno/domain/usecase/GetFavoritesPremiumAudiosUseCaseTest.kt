package soy.gabimoreno.domain.usecase

import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.amshove.kluent.shouldBe
import org.amshove.kluent.shouldBeEqualTo
import org.junit.Before
import org.junit.Test
import soy.gabimoreno.domain.repository.premiumaudios.PremiumAudiosRepository
import soy.gabimoreno.ext.left
import soy.gabimoreno.ext.right
import soy.gabimoreno.fake.buildPremiumAudio

class GetFavoritesPremiumAudiosUseCaseTest {

    private val repository = mockk<PremiumAudiosRepository>()
    private lateinit var useCase: GetFavoritesPremiumAudiosUseCase

    @Before
    fun setUp() {
        useCase = GetFavoritesPremiumAudiosUseCase(repository)
    }

    @Test
    fun `GIVEN repository returns Right WHEN invoked THEN returns list of PremiumAudio`() =
        runTest {
            val audios = listOf(buildPremiumAudio(), buildPremiumAudio())
            coEvery { repository.getAllFavoritePremiumAudios() } returns right(audios)

            val result = useCase()

            result.isRight() shouldBe true
            result.getOrNull() shouldBe audios
        }

    @Test
    fun `GIVEN repository returns Left WHEN invoked THEN returns error`() = runTest {
        val error = Throwable("Server error")
        coEvery { repository.getAllFavoritePremiumAudios() } returns left(error)

        val result = useCase()

        result shouldBeEqualTo left(error)
    }
}
