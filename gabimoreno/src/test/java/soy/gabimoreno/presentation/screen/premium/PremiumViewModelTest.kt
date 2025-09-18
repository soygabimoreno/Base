package soy.gabimoreno.presentation.screen.premium

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.amshove.kluent.shouldBeEqualTo
import org.junit.After
import org.junit.Before
import org.junit.Test
import soy.gabimoreno.core.testing.relaxedMockk
import soy.gabimoreno.domain.usecase.GetPremiumAudioByIdUseCase
import soy.gabimoreno.domain.usecase.GetPremiumAudiosManagedUseCase
import soy.gabimoreno.domain.usecase.MarkPremiumAudioAsListenedUseCase
import soy.gabimoreno.domain.usecase.RefreshBearerTokenUseCase
import soy.gabimoreno.domain.usecase.UpdateAudioItemFavoriteStateUseCase

@ExperimentalCoroutinesApi
class PremiumViewModelTest {
    private val getPremiumAudiosMediatorUseCase = relaxedMockk<GetPremiumAudiosManagedUseCase>()
    private val getPremiumAudioByIdUseCase = relaxedMockk<GetPremiumAudioByIdUseCase>()
    private val markPremiumAudioAsListenedUseCase =
        relaxedMockk<MarkPremiumAudioAsListenedUseCase>()
    private val refreshBearerTokenUseCase = relaxedMockk<RefreshBearerTokenUseCase>()
    private val updateAudioItemFavoriteStateUseCase =
        relaxedMockk<UpdateAudioItemFavoriteStateUseCase>()
    private val testDispatcher = UnconfinedTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `GIVEN shouldIAccessPremium true WHEN onViewScreen THEN state is updated with access granted`() =
        runTest {
            val viewModel =
                PremiumViewModel(
                    getPremiumAudiosMediatorUseCase,
                    getPremiumAudioByIdUseCase,
                    markPremiumAudioAsListenedUseCase,
                    refreshBearerTokenUseCase,
                    updateAudioItemFavoriteStateUseCase,
                    testDispatcher,
                )
            val states = mutableListOf<PremiumState>()
            val stateJob = launch { viewModel.state.toList(states) }
            viewModel.onViewScreen(true)
            advanceUntilIdle()

            states.last().shouldIAccessPremium shouldBeEqualTo true
            stateJob.cancel()
        }

    @Test
    fun `GIVEN shouldIAccessPremium false WHEN onViewScreen THEN state is updated with access denied`() =
        runTest {
            val viewModel =
                PremiumViewModel(
                    getPremiumAudiosMediatorUseCase,
                    getPremiumAudioByIdUseCase,
                    markPremiumAudioAsListenedUseCase,
                    refreshBearerTokenUseCase,
                    updateAudioItemFavoriteStateUseCase,
                    testDispatcher,
                )
            val states = mutableListOf<PremiumState>()
            val stateJob = launch { viewModel.state.toList(states) }
            viewModel.onViewScreen(false)
            advanceUntilIdle()

            states.last().shouldIAccessPremium shouldBeEqualTo false
            states.last().isLoading shouldBeEqualTo false
            stateJob.cancel()
        }
}
