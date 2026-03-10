@file:OptIn(ExperimentalCoroutinesApi::class)

package soy.gabimoreno.presentation.screen.playlist.detail

import io.mockk.coEvery
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
import org.amshove.kluent.shouldBeEqualTo
import org.amshove.kluent.shouldContain
import org.junit.After
import org.junit.Before
import org.junit.Test
import soy.gabimoreno.core.testing.coVerifyOnce
import soy.gabimoreno.core.testing.relaxedMockk
import soy.gabimoreno.data.tracker.Tracker
import soy.gabimoreno.data.tracker.domain.TRACKER_KEY_PLAYLIST_ID
import soy.gabimoreno.data.tracker.main.PlaylistDetailTrackerEvent
import soy.gabimoreno.domain.usecase.DeletePlaylistItemByIdUseCase
import soy.gabimoreno.domain.usecase.GetPlaylistByIdUseCase
import soy.gabimoreno.domain.usecase.UpdatePlaylistItemsUseCase
import soy.gabimoreno.domain.usecase.UpsertPlaylistsUseCase
import soy.gabimoreno.ext.left
import soy.gabimoreno.ext.right
import soy.gabimoreno.fake.buildPlaylist

class PlaylistDetailScreenKtTest {
    private val getPlaylistByIdUseCase = mockk<GetPlaylistByIdUseCase>()
    private val updatePlaylistItemsUseCase = mockk<UpdatePlaylistItemsUseCase>()
    private val deletePlaylistItemByIdUseCase = mockk<DeletePlaylistItemByIdUseCase>()
    private val upsertPlaylistsUseCase = mockk<UpsertPlaylistsUseCase>()
    private val tracker: Tracker = relaxedMockk()
    private val testDispatcher = StandardTestDispatcher()

    private lateinit var viewModel: PlaylistDetailViewModel

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        viewModel =
            PlaylistDetailViewModel(
                getPlaylistByIdUseCase = getPlaylistByIdUseCase,
                updatePlaylistItemsUseCase = updatePlaylistItemsUseCase,
                deletePlaylistItemByIdUseCase = deletePlaylistItemByIdUseCase,
                upsertPlaylistsUseCase = upsertPlaylistsUseCase,
                tracker = tracker,
                dispatcher = testDispatcher,
            )
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `GIVEN playlist WHEN onScreenView THEN state is updated with playlist and tracker is called`() =
        runTest {
            val playlist = buildPlaylist()
            coEvery {
                getPlaylistByIdUseCase(playlist.id)
            } returns right(playlist)

            viewModel.onScreenView(playlist.id)
            advanceUntilIdle()

            viewModel.state.playlist shouldBe playlist
            viewModel.state.isLoading shouldBe false
            coVerifyOnce {
                getPlaylistByIdUseCase(playlist.id)
                tracker.trackEvent(
                    PlaylistDetailTrackerEvent.ViewScreen(
                        parameters = mapOf(TRACKER_KEY_PLAYLIST_ID to playlist.id),
                    ),
                )
            }
        }

    @Test
    fun `GIVEN error WHEN onScreenView THEN event is emitted and loading false`() =
        runTest {
            val error = Throwable("error")
            coEvery { getPlaylistByIdUseCase(any()) } returns left(error)
            val events = mutableListOf<PlaylistDetailEvent>()
            val job = launch { viewModel.events.toList(events) }

            viewModel.onScreenView(1)
            advanceUntilIdle()

            events shouldContain PlaylistDetailEvent.Error(error)
            viewModel.state.isLoading shouldBe false
            job.cancel()
        }

    @Test
    fun `GIVEN OnPlayClicked WHEN audio items exist THEN audio is set and event is emitted`() =
        runTest {
            val playlist = buildPlaylist()
            viewModel.onScreenView(playlist.id)
            coEvery { getPlaylistByIdUseCase(playlist.id) } returns right(playlist)
            advanceUntilIdle()
            val events = mutableListOf<PlaylistDetailEvent>()
            val job = launch { viewModel.events.toList(events) }

            viewModel.onAction(PlaylistDetailAction.OnPlayClicked)
            advanceUntilIdle()

            viewModel.state.audio shouldBeEqualTo playlist.items.first()
            events shouldContain PlaylistDetailEvent.PlayAudio
            job.cancel()
        }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `GIVEN reordered items WHEN OnAudioItemsReordered THEN update use case is called`() =
        runTest {
            val playlist = buildPlaylist()
            viewModel.onScreenView(playlist.id)
            val reordered = playlist.items.reversed()
            coEvery { getPlaylistByIdUseCase(playlist.id) } returns right(playlist)
            coEvery {
                updatePlaylistItemsUseCase(playlistId = playlist.id, playlistItems = reordered)
            } returns right(Unit)
            advanceUntilIdle()

            viewModel.onAction(PlaylistDetailAction.OnAudioItemsReordered(reordered))
            advanceUntilIdle()

            viewModel.state.playlistAudioItems shouldBe reordered
            coVerifyOnce {
                updatePlaylistItemsUseCase(playlistId = playlist.id, playlistItems = reordered)
            }
        }

    @Test
    fun `GIVEN valid item WHEN OnConfirmDialog THEN item is removed and state updated`() =
        runTest {
            val playlist = buildPlaylist()
            viewModel.onScreenView(playlist.id)
            val id = playlist.items.first().id
            coEvery { getPlaylistByIdUseCase(playlist.id) } returns right(playlist)
            coEvery {
                deletePlaylistItemByIdUseCase(audioItemId = id, playlistId = playlist.id)
            } returns right(Unit)
            advanceUntilIdle()

            viewModel.onAction(PlaylistDetailAction.OnRemovePlaylistAudioItem(id))
            viewModel.onAction(PlaylistDetailAction.OnConfirmDialog)
            advanceUntilIdle()

            viewModel.state.playlistAudioItems.none { it.id == id } shouldBe true
            viewModel.state.shouldIShowDialog shouldBe false
            coVerifyOnce {
                deletePlaylistItemByIdUseCase(audioItemId = id, playlistId = playlist.id)
            }
        }

    @Test
    fun `GIVEN empty title WHEN OnEditPlaylistConfirmDialog THEN error is set true`() =
        runTest {
            viewModel.onAction(PlaylistDetailAction.OnDialogTitleChange(""))

            viewModel.onAction(PlaylistDetailAction.OnEditPlaylistConfirmDialog)
            advanceUntilIdle()

            viewModel.state.dialogTitleError shouldBe true
        }

    @Test
    fun `GIVEN valid data WHEN OnEditPlaylistConfirmDialog THEN playlist is updated`() =
        runTest {
            val newTitle = "Updated Title"
            val newDesc = "Updated Description"
            val playlist = buildPlaylist()
            viewModel.onScreenView(playlist.id)
            coEvery { getPlaylistByIdUseCase(playlist.id) } returns right(playlist)
            coEvery {
                upsertPlaylistsUseCase(listOf(playlist.copy(title = newTitle, description = newDesc)))
            } returns right(Unit)
            advanceUntilIdle()

            viewModel.onAction(PlaylistDetailAction.OnDialogTitleChange(newTitle))
            viewModel.onAction(PlaylistDetailAction.OnDialogDescriptionChange(newDesc))
            viewModel.onAction(PlaylistDetailAction.OnEditPlaylistConfirmDialog)
            advanceUntilIdle()

            viewModel.state.playlist!!.title shouldBeEqualTo newTitle
            viewModel.state.playlist!!.description shouldBeEqualTo newDesc
            viewModel.state.shouldIShowDialog shouldBe false
            coVerifyOnce {
                upsertPlaylistsUseCase(listOf(playlist.copy(title = newTitle, description = newDesc)))
            }
        }
}
