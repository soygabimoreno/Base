@file:OptIn(ExperimentalCoroutinesApi::class)

package soy.gabimoreno.presentation.screen.playlist.audio

import io.mockk.coEvery
import io.mockk.coJustRun
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.amshove.kluent.shouldBe
import org.amshove.kluent.shouldHaveSize
import org.junit.After
import org.junit.Before
import org.junit.Test
import soy.gabimoreno.core.testing.coVerifyNever
import soy.gabimoreno.core.testing.coVerifyOnce
import soy.gabimoreno.core.testing.relaxedMockk
import soy.gabimoreno.data.tracker.Tracker
import soy.gabimoreno.data.tracker.domain.TRACKER_KEY_PLAYLIST_AUDIO_ID
import soy.gabimoreno.data.tracker.main.PlaylistAudioTrackerEvent
import soy.gabimoreno.domain.usecase.DeletePlaylistItemByIdUseCase
import soy.gabimoreno.domain.usecase.GetAllPlaylistUseCase
import soy.gabimoreno.domain.usecase.GetAudioCourseItemByIdUseCase
import soy.gabimoreno.domain.usecase.GetPlaylistByPlaylistItemIdUseCase
import soy.gabimoreno.domain.usecase.GetPodcastByIdUseCase
import soy.gabimoreno.domain.usecase.GetPremiumAudioByIdUseCase
import soy.gabimoreno.domain.usecase.SetPlaylistItemsUseCase
import soy.gabimoreno.ext.right
import soy.gabimoreno.fake.buildPlaylist
import soy.gabimoreno.fake.buildPremiumAudio

class PlaylistAudioItemViewModelTest {
    private val deletePlaylistItemUseCase = mockk<DeletePlaylistItemByIdUseCase>()
    private val getAllPlaylistUseCase = mockk<GetAllPlaylistUseCase>()
    private val getAudioCourseItemByIdUseCase = mockk<GetAudioCourseItemByIdUseCase>()
    private val getPlaylistByPlaylistItemIdUseCase = mockk<GetPlaylistByPlaylistItemIdUseCase>()
    private val getPodcastByIdUseCase = mockk<GetPodcastByIdUseCase>()
    private val getPremiumAudioByIdUseCase = mockk<GetPremiumAudioByIdUseCase>()
    private val setPlaylistItemsUseCase = mockk<SetPlaylistItemsUseCase>()
    private val tracker: Tracker = relaxedMockk()
    private val testDispatcher = StandardTestDispatcher()

    private lateinit var viewModel: PlaylistAudioItemViewModel

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        viewModel =
            PlaylistAudioItemViewModel(
                deletePlaylistItemUseCase = deletePlaylistItemUseCase,
                getAllPlaylistUseCase = getAllPlaylistUseCase,
                getAudioCourseItemByIdUseCase = getAudioCourseItemByIdUseCase,
                getPlaylistByPlaylistItemIdUseCase = getPlaylistByPlaylistItemIdUseCase,
                getPodcastByIdUseCase = getPodcastByIdUseCase,
                getPremiumAudioByIdUseCase = getPremiumAudioByIdUseCase,
                setPlaylistItemsUseCase = setPlaylistItemsUseCase,
                tracker = tracker,
                dispatcher = testDispatcher,
            )
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `WHEN onViewScreen THEN track event`() =
        runTest {
            val playlist = buildPlaylist()
            val premiumAudio = buildPremiumAudio()
            val playlistIds = emptyList<Int>()
            coEvery { getPremiumAudioByIdUseCase(premiumAudio.id) } returns right(premiumAudio)
            coEvery { getAllPlaylistUseCase() } returns right(listOf(playlist))
            coEvery {
                getPlaylistByPlaylistItemIdUseCase(premiumAudio.id)
            } returns right(playlistIds)
            viewModel.onViewScreen(premiumAudio.id)
            advanceUntilIdle()

            coVerifyOnce {
                tracker.trackEvent(
                    PlaylistAudioTrackerEvent.ViewScreen(
                        parameters = mapOf(TRACKER_KEY_PLAYLIST_AUDIO_ID to premiumAudio.id),
                    ),
                )
            }
        }

    @Test
    fun `GIVEN playlists loaded WHEN toggle one THEN its selection changes`() =
        runTest {
            val playlist = buildPlaylist()
            val premiumAudio = buildPremiumAudio()
            val playlistIds = emptyList<Int>()
            coEvery { getPremiumAudioByIdUseCase(premiumAudio.id) } returns right(premiumAudio)
            coEvery { getAllPlaylistUseCase() } returns right(listOf(playlist))
            coEvery {
                getPlaylistByPlaylistItemIdUseCase(premiumAudio.id)
            } returns right(playlistIds)

            viewModel.onViewScreen(premiumAudio.id)
            advanceUntilIdle()

            viewModel.onAction(PlaylistAudioItemAction.OnTogglePlaylist(playlistId = 1))

            viewModel.state.playlists
                .first()
                .isSelected shouldBe true
        }

    @Test
    fun `GIVEN selected playlist WHEN save THEN it calls setPlaylistItems and emits success`() =
        runTest {
            val playlist = buildPlaylist()
            val premiumAudio = buildPremiumAudio()
            val playlistIds = emptyList<Int>()
            coEvery { getPremiumAudioByIdUseCase(premiumAudio.id) } returns right(premiumAudio)
            coEvery { getAllPlaylistUseCase() } returns right(listOf(playlist))
            coEvery {
                getPlaylistByPlaylistItemIdUseCase(premiumAudio.id)
            } returns right(playlistIds)
            coJustRun { setPlaylistItemsUseCase(premiumAudio.id, listOf(playlist.id)) }
            coJustRun { deletePlaylistItemUseCase(any(), any()) }
            val events = mutableListOf<PlaylistAudioItemEvent>()
            val eventJob = launch { viewModel.events.toList(events) }

            viewModel.onViewScreen(premiumAudio.id)
            advanceUntilIdle()
            viewModel.onAction(PlaylistAudioItemAction.OnTogglePlaylist(playlistId = 1))
            viewModel.onAction(PlaylistAudioItemAction.OnSaveClicked)
            advanceUntilIdle()

            events shouldHaveSize 1
            events.first() shouldBe PlaylistAudioItemEvent.Success
            coVerifyOnce {
                setPlaylistItemsUseCase(premiumAudio.id, listOf(playlist.id))
            }
            coVerifyNever {
                deletePlaylistItemUseCase(any(), any())
            }
            eventJob.cancel()
        }

    @Test
    fun `GIVEN initially selected playlist WHEN deselect and save THEN it calls delete`() =
        runTest {
            val playlist = buildPlaylist()
            val premiumAudio = buildPremiumAudio()
            val playlistIds = listOf(playlist.id)
            coEvery { getPremiumAudioByIdUseCase(premiumAudio.id) } returns right(premiumAudio)
            coEvery { getAllPlaylistUseCase() } returns right(listOf(playlist))
            coEvery {
                getPlaylistByPlaylistItemIdUseCase(premiumAudio.id)
            } returns right(playlistIds)
            coJustRun { setPlaylistItemsUseCase(any(), any()) }
            coJustRun { deletePlaylistItemUseCase(premiumAudio.id, playlist.id) }
            val events = mutableListOf<PlaylistAudioItemEvent>()
            val eventJob = launch { viewModel.events.toList(events) }

            viewModel.onViewScreen(premiumAudio.id)
            advanceUntilIdle()
            viewModel.onAction(PlaylistAudioItemAction.OnTogglePlaylist(playlistId = 1))
            viewModel.onAction(PlaylistAudioItemAction.OnSaveClicked)
            advanceUntilIdle()

            events shouldHaveSize 1
            events.first() shouldBe PlaylistAudioItemEvent.Success
            coVerifyOnce {
                deletePlaylistItemUseCase(premiumAudio.id, playlist.id)
            }
            coVerifyNever {
                setPlaylistItemsUseCase(any(), any())
            }
            eventJob.cancel()
        }
}
