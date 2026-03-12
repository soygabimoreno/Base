package soy.gabimoreno.presentation.screen

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.lifecycle.viewmodel.compose.viewModel
import soy.gabimoreno.presentation.screen.audiocourses.detail.AudioCourseDetailViewModel
import soy.gabimoreno.presentation.screen.audiocourses.list.AudioCoursesViewModel
import soy.gabimoreno.presentation.screen.auth.AuthViewModel
import soy.gabimoreno.presentation.screen.detail.DetailViewModel
import soy.gabimoreno.presentation.screen.home.HomeViewModel
import soy.gabimoreno.presentation.screen.player.PlayerViewModel
import soy.gabimoreno.presentation.screen.premium.PremiumViewModel
import soy.gabimoreno.presentation.screen.profile.ProfileViewModel
import soy.gabimoreno.presentation.screen.senior.SeniorViewModel
import soy.gabimoreno.presentation.screen.webview.WebViewViewModel

object ViewModelProvider {
    val homeViewModel: HomeViewModel
        @Composable
        get() = localHomeViewModel.current

    val seniorViewModel: SeniorViewModel
        @Composable
        get() = localSeniorViewModel.current

    val detailViewModel: DetailViewModel
        @Composable
        get() = localDetailViewModel.current

    val playerViewModel: PlayerViewModel
        @Composable
        get() = localPlayerViewModel.current

    val webViewViewModel: WebViewViewModel
        @Composable
        get() = localWebViewViewModel.current

    val premiumViewModel: PremiumViewModel
        @Composable
        get() = localPremiumViewModel.current

    val audioCoursesViewModel: AudioCoursesViewModel
        @Composable
        get() = localAudioCoursesViewModel.current

    val audioCourseDetailViewModel: AudioCourseDetailViewModel
        @Composable
        get() = localAudioCourseDetailViewModel.current

    val profileViewModel: ProfileViewModel
        @Composable
        get() = localProfileViewModel.current

    val authViewModel: AuthViewModel
        @Composable
        get() = localAuthViewModel.current
}

@Composable
fun ProvideMultiViewModel(content: @Composable () -> Unit) {
    val authViewModel: AuthViewModel = viewModel()
    val homeViewModel: HomeViewModel = viewModel()
    val seniorViewModel: SeniorViewModel = viewModel()
    val detailViewModel: DetailViewModel = viewModel()
    val playerViewModel: PlayerViewModel = viewModel()
    val webViewViewModel: WebViewViewModel = viewModel()
    val premiumViewModel: PremiumViewModel = viewModel()
    val audioCoursesViewModel: AudioCoursesViewModel = viewModel()
    val audioCourseDetailViewModel: AudioCourseDetailViewModel = viewModel()
    val profileViewModel: ProfileViewModel = viewModel()

    CompositionLocalProvider(
        localAuthViewModel provides authViewModel,
        localHomeViewModel provides homeViewModel,
        localSeniorViewModel provides seniorViewModel,
        localWebViewViewModel provides webViewViewModel,
        localPremiumViewModel provides premiumViewModel,
        localAudioCoursesViewModel provides audioCoursesViewModel,
        localAudioCourseDetailViewModel provides audioCourseDetailViewModel,
        localProfileViewModel provides profileViewModel,
    ) {
        CompositionLocalProvider(
            localDetailViewModel provides detailViewModel,
        ) {
            CompositionLocalProvider(
                localPlayerViewModel provides playerViewModel,
            ) {
                content()
            }
        }
    }
}

private val localHomeViewModel =
    staticCompositionLocalOf<HomeViewModel> {
        error("No HomeViewModel provided")
    }

private val localSeniorViewModel =
    staticCompositionLocalOf<SeniorViewModel> {
        error("No SeniorViewModel provided")
    }

private val localDetailViewModel =
    staticCompositionLocalOf<DetailViewModel> {
        error("No DetailViewModel provided")
    }

private val localPlayerViewModel =
    staticCompositionLocalOf<PlayerViewModel> {
        error("No PlayerViewModel provided")
    }

private val localWebViewViewModel =
    staticCompositionLocalOf<WebViewViewModel> {
        error("No WebViewViewModel provided")
    }

private val localPremiumViewModel =
    staticCompositionLocalOf<PremiumViewModel> {
        error("No PremiumViewModel provided")
    }

private val localAudioCoursesViewModel =
    staticCompositionLocalOf<AudioCoursesViewModel> {
        error("No CoursesListViewModel provided")
    }

private val localAudioCourseDetailViewModel =
    staticCompositionLocalOf<AudioCourseDetailViewModel> {
        error("No AudioCoursesDetailViewModel provided")
    }

private val localProfileViewModel =
    staticCompositionLocalOf<ProfileViewModel> {
        error("No ProfileViewModel provided")
    }

private val localAuthViewModel =
    staticCompositionLocalOf<AuthViewModel> {
        error("No AuthViewModel provided")
    }
