package soy.gabimoreno.presentation.screen.premium

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test
import soy.gabimoreno.core.testing.relaxedMockk
import soy.gabimoreno.core.testing.verifyOnce
import soy.gabimoreno.data.tracker.Tracker
import soy.gabimoreno.data.tracker.main.PremiumTrackerEvent
import soy.gabimoreno.domain.session.MemberSession
import soy.gabimoreno.domain.usecase.GetPremiumAudioByIdUseCase
import soy.gabimoreno.domain.usecase.GetPremiumAudiosManagedUseCase
import soy.gabimoreno.domain.usecase.IsBearerTokenValid
import soy.gabimoreno.domain.usecase.LoginUseCase
import soy.gabimoreno.domain.usecase.LoginValidationUseCase
import soy.gabimoreno.domain.usecase.MarkPremiumAudioAsListenedUseCase
import soy.gabimoreno.domain.usecase.SaveCredentialsInDataStoreUseCase

@ExperimentalCoroutinesApi
class PremiumViewModelTest {

    private val tracker: Tracker = relaxedMockk()
    private val loginValidationUseCase: LoginValidationUseCase = relaxedMockk()
    private val saveCredentialsInDataStoreUseCase: SaveCredentialsInDataStoreUseCase =
        relaxedMockk()
    private val memberSession: MemberSession = relaxedMockk()
    private val loginUseCase: LoginUseCase = relaxedMockk()
    private val getPremiumAudiosMediatorUseCase: GetPremiumAudiosManagedUseCase = relaxedMockk()
    private val getPremiumAudioByIdUseCase: GetPremiumAudioByIdUseCase = relaxedMockk()
    private val isBearerTokenValid: IsBearerTokenValid = relaxedMockk()
    private val testDispatcher: TestDispatcher = UnconfinedTestDispatcher()
    private val markPremiumAudioAsListenedUseCase =
        relaxedMockk<MarkPremiumAudioAsListenedUseCase>()
    private lateinit var viewModel: PremiumViewModel

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        viewModel = PremiumViewModel(
            tracker = tracker,
            loginValidationUseCase = loginValidationUseCase,
            saveCredentialsInDataStoreUseCase = saveCredentialsInDataStoreUseCase,
            memberSession = memberSession,
            loginUseCase = loginUseCase,
            getPremiumAudiosMediatorUseCase = getPremiumAudiosMediatorUseCase,
            getPremiumAudioByIdUseCase = getPremiumAudioByIdUseCase,
            isBearerTokenValid = isBearerTokenValid,
            markPremiumAudioAsListenedUseCase = markPremiumAudioAsListenedUseCase,
            dispatcher = testDispatcher
        )
    }

    @After
    fun tearDownDispatcher() {
        Dispatchers.resetMain()
    }

    @Test
    fun `GIVEN  WHEN onViewScreen THEN track event and show login`() {
        val email = "email@example.com"
        val password = "1234"

        viewModel.onViewScreen(email, password)

        verifyOnce {
            tracker.trackEvent(PremiumTrackerEvent.ViewScreen)
            // TODO: Test this navigation
//            runTest(testDispatcher) {
//                val viewEvent = viewModel.viewEventFlow.first()
//                viewEvent shouldBe PremiumViewModel.ViewEvent.ShowLogin
//            }
        }
    }
}
