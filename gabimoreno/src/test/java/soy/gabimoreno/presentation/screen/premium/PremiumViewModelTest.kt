package soy.gabimoreno.presentation.screen.premium

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.amshove.kluent.shouldBeEqualTo
import org.junit.After
import org.junit.Before
import org.junit.Ignore
import org.junit.Test
import soy.gabimoreno.core.testing.relaxedMockk
import soy.gabimoreno.domain.usecase.GetPremiumAudioByIdUseCase
import soy.gabimoreno.domain.usecase.GetPremiumAudiosManagedUseCase
import soy.gabimoreno.domain.usecase.MarkPremiumAudioAsListenedUseCase

@ExperimentalCoroutinesApi
class PremiumViewModelTest {

    private val getPremiumAudiosMediatorUseCase = relaxedMockk<GetPremiumAudiosManagedUseCase>()
    private val getPremiumAudioByIdUseCase = relaxedMockk<GetPremiumAudioByIdUseCase>()
    private val markPremiumAudioAsListenedUseCase =
        relaxedMockk<MarkPremiumAudioAsListenedUseCase>()
    private val testDispatcher = UnconfinedTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Ignore
    @Test
    fun `GIVEN shouldIAccessPremium true WHEN onViewScreen THEN state is updated with access granted`() =
        runTest {
            val viewModel = PremiumViewModel(
                getPremiumAudiosMediatorUseCase,
                getPremiumAudioByIdUseCase,
                markPremiumAudioAsListenedUseCase,
                testDispatcher
            )

            viewModel.onViewScreen(true)
            advanceUntilIdle()

            viewModel.state.shouldIAccessPremium shouldBeEqualTo true
        }

    @Ignore
    @Test
    fun `GIVEN shouldIAccessPremium false WHEN onViewScreen THEN state is updated with access denied`() =
        runTest {
            val viewModel = PremiumViewModel(
                getPremiumAudiosMediatorUseCase,
                getPremiumAudioByIdUseCase,
                markPremiumAudioAsListenedUseCase,
                testDispatcher
            )

            viewModel.onViewScreen(false)
            advanceUntilIdle()

            viewModel.state.shouldIAccessPremium shouldBeEqualTo false
            viewModel.state.isLoading shouldBeEqualTo false
        }
}
