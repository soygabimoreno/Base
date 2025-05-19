@file:OptIn(ExperimentalCoroutinesApi::class)

package soy.gabimoreno.presentation.screen.auth

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestDispatcher
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
import soy.gabimoreno.domain.usecase.IsBearerTokenValid
import soy.gabimoreno.domain.usecase.LoginUseCase
import soy.gabimoreno.domain.usecase.LoginValidationUseCase
import soy.gabimoreno.domain.usecase.ResetJwtAuthTokenUseCase
import soy.gabimoreno.domain.usecase.SaveCredentialsInDataStoreUseCase
import soy.gabimoreno.domain.usecase.SetShouldIReloadAudioCoursesUseCase


class AuthViewModelTest {

    private val tracker: Tracker = relaxedMockk()
    private val loginValidationUseCase: LoginValidationUseCase = relaxedMockk()
    private val saveCredentialsInDataStoreUseCase: SaveCredentialsInDataStoreUseCase =
        relaxedMockk()
    private val memberSession: MemberSession = relaxedMockk()
    private val loginUseCase: LoginUseCase = relaxedMockk()
    private val isBearerTokenValid: IsBearerTokenValid = relaxedMockk()
    private val resetJwtAuthTokenUseCase = relaxedMockk<ResetJwtAuthTokenUseCase>()
    private val testDispatcher: TestDispatcher = StandardTestDispatcher()
    private val setShouldIReloadAudioCoursesUseCase =
        relaxedMockk<SetShouldIReloadAudioCoursesUseCase>()
    private lateinit var viewModel: AuthViewModel

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        viewModel = AuthViewModel(
            tracker = tracker,
            loginValidationUseCase = loginValidationUseCase,
            saveCredentialsInDataStoreUseCase = saveCredentialsInDataStoreUseCase,
            memberSession = memberSession,
            loginUseCase = loginUseCase,
            isBearerTokenValid = isBearerTokenValid,
            setShouldIReloadAudioCoursesUseCase = setShouldIReloadAudioCoursesUseCase,
            resetJwtAuthTokenUseCase = resetJwtAuthTokenUseCase,
            dispatcher = testDispatcher
        )
    }

    @After
    fun tearDownDispatcher() {
        Dispatchers.resetMain()
    }

    @Test
    fun `GIVEN  WHEN onViewScreen THEN track event `() {
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
