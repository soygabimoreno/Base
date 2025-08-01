@file:OptIn(ExperimentalCoroutinesApi::class, ExperimentalCoroutinesApi::class)

package soy.gabimoreno.presentation.screen.playlist.list

import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.amshove.kluent.shouldBe
import org.amshove.kluent.shouldBeEqualTo
import org.amshove.kluent.shouldContain
import org.amshove.kluent.shouldContainSame
import org.amshove.kluent.shouldHaveSize
import org.amshove.kluent.shouldNotContain
import org.junit.After
import org.junit.Before
import org.junit.Test
import soy.gabimoreno.core.testing.coVerifyNever
import soy.gabimoreno.core.testing.coVerifyOnce
import soy.gabimoreno.core.testing.relaxedMockk
import soy.gabimoreno.core.testing.verifyOnce
import soy.gabimoreno.data.tracker.Tracker
import soy.gabimoreno.data.tracker.main.PlaylistTrackerEvent
import soy.gabimoreno.domain.usecase.DeletePlaylistByIdUseCase
import soy.gabimoreno.domain.usecase.GetAllPlaylistUseCase
import soy.gabimoreno.domain.usecase.InsertPlaylistUseCase
import soy.gabimoreno.domain.usecase.UpsertPlaylistsUseCase
import soy.gabimoreno.ext.left
import soy.gabimoreno.ext.right
import soy.gabimoreno.fake.buildPlaylist

class PlaylistViewModelTest {
    private val getAllPlaylistUseCase = mockk<GetAllPlaylistUseCase>()
    private val insertPlaylistUseCase = mockk<InsertPlaylistUseCase>()
    private val upsertPlaylistsUseCase = mockk<UpsertPlaylistsUseCase>()
    private val deletePlaylistByIdUseCase = mockk<DeletePlaylistByIdUseCase>()
    private val tracker: Tracker = relaxedMockk()
    private val testDispatcher = StandardTestDispatcher()
    private lateinit var viewModel: PlaylistViewModel

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        coEvery { getAllPlaylistUseCase() } returns right(emptyList())
        viewModel =
            PlaylistViewModel(
                getAllPlaylistUseCase = getAllPlaylistUseCase,
                insertPlaylistUseCase = insertPlaylistUseCase,
                upsertPlaylistsUseCase = upsertPlaylistsUseCase,
                deletePlaylistByIdUseCase = deletePlaylistByIdUseCase,
                tracker = tracker,
                dispatcher = testDispatcher,
            )
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `GIVEN initialization WHEN state is collected THEN initial state is correct and tracker is called`() =
        runTest {
            val expectedState = PlaylistState()
            val job =
                launch {
                    viewModel.state.collect { }
                }
            val initialState = viewModel.state.first()
            advanceUntilIdle()

            initialState shouldBeEqualTo expectedState
            verifyOnce {
                tracker.trackEvent(PlaylistTrackerEvent.ViewScreen)
            }
            job.cancel()
        }

    @Test
    fun `GIVEN init WHEN onStart THEN state isLoading true and then playlists loaded and isLoading false`() =
        runTest {
            val playlists = listOf(buildPlaylist())
            coEvery { getAllPlaylistUseCase() } returns right(playlists)

            val states = mutableListOf<PlaylistState>()
            val job =
                launch(UnconfinedTestDispatcher()) {
                    viewModel.state.toList(states)
                }
            advanceUntilIdle()

            states[0].isLoading shouldBe true
            states.last().apply {
                isLoading shouldBe false
                this.playlists shouldContainSame playlists
            }
            job.cancel()
        }

    @Test
    fun `GIVEN failed playlists loading WHEN onStart THEN error emitted and loading false`() =
        runTest {
            val error = Throwable("Network error")
            coEvery { getAllPlaylistUseCase() } returns left(error)

            val states = mutableListOf<PlaylistState>()
            val events = mutableListOf<PlaylistEvent>()
            val stateJob = launch { viewModel.state.toList(states) }
            val eventJob = launch { viewModel.events.toList(events) }
            advanceUntilIdle()

            events shouldHaveSize 1
            events.first() shouldBeEqualTo PlaylistEvent.Error(error)
            states.last().isLoading shouldBe false
            stateJob.cancel()
            eventJob.cancel()
        }

    @Test
    fun `GIVEN OnAddNewPlaylistClicked WHEN onAction THEN dialog shown`() =
        runTest {
            val states = mutableListOf<PlaylistState>()
            val stateJob = launch { viewModel.state.toList(states) }

            viewModel.onAction(PlaylistAction.OnAddNewPlaylistClicked)
            advanceUntilIdle()

            states.last().shouldIShowAddPlaylistDialog shouldBe true
            stateJob.cancel()
        }

    @Test
    fun `GIVEN OnAddPlaylistDismissDialog WHEN onAction THEN dialog reset`() =
        runTest {
            val emptyString = ""
            viewModel.onAction(PlaylistAction.OnAddPlaylistDismissDialog)

            val states = mutableListOf<PlaylistState>()
            val stateJob = launch { viewModel.state.toList(states) }
            advanceUntilIdle()

            states.last().apply {
                shouldIShowAddPlaylistDialog shouldBe false
                dialogTitle shouldBe emptyString
                dialogDescription shouldBe emptyString
                dialogTitleError shouldBe false
            }
            stateJob.cancel()
        }

    @Test
    fun `GIVEN OnDialogTitleChange with value WHEN onAction THEN title updated and error false`() =
        runTest {
            val title = "New Title"

            viewModel.onAction(PlaylistAction.OnDialogTitleChange(title))
            val states = mutableListOf<PlaylistState>()
            val stateJob = launch { viewModel.state.toList(states) }
            advanceUntilIdle()

            states.last().apply {
                dialogTitle shouldBe title
                dialogTitleError shouldBe false
            }
            stateJob.cancel()
        }

    @Test
    fun `GIVEN empty title WHEN OnDialogTitleChange THEN error true`() =
        runTest {
            val emptyTitle = ""
            viewModel.onAction(PlaylistAction.OnDialogTitleChange(emptyTitle))

            val states = mutableListOf<PlaylistState>()
            val stateJob = launch { viewModel.state.toList(states) }
            advanceUntilIdle()

            states.last().dialogTitleError shouldBe true
            stateJob.cancel()
        }

    @Test
    fun `GIVEN valid input WHEN OnAddPlaylistConfirmDialog THEN insert and update state`() =
        runTest {
            val emptyString = ""
            val playlist = buildPlaylist()
            coEvery {
                insertPlaylistUseCase(name = playlist.title, description = playlist.description)
            } returns right(playlist)

            val states = mutableListOf<PlaylistState>()
            val stateJob = launch { viewModel.state.toList(states) }
            viewModel.onAction(PlaylistAction.OnDialogTitleChange(playlist.title))
            viewModel.onAction(PlaylistAction.OnDialogDescriptionChange(playlist.description))
            advanceUntilIdle()
            viewModel.onAction(PlaylistAction.OnAddPlaylistConfirmDialog)
            advanceUntilIdle()

            states.last().apply {
                isLoading shouldBe false
                dialogTitle shouldBe emptyString
                dialogDescription shouldBe emptyString
                playlists shouldContain playlist
                dialogTitleError shouldBe false
                shouldIShowAddPlaylistDialog shouldBe false
            }
            coVerifyOnce {
                insertPlaylistUseCase(name = playlist.title, description = playlist.description)
            }
            stateJob.cancel()
        }

    @Test
    fun `GIVEN empty title WHEN OnAddPlaylistConfirmDialog THEN error shown and dialog remains open`() =
        runTest {
            val states = mutableListOf<PlaylistState>()
            val stateJob = launch { viewModel.state.toList(states) }

            viewModel.onAction(PlaylistAction.OnDialogTitleChange(""))
            advanceUntilIdle()
            viewModel.onAction(PlaylistAction.OnAddPlaylistConfirmDialog)
            advanceUntilIdle()

            states.last().apply {
                dialogTitleError shouldBe true
                shouldIShowAddPlaylistDialog shouldBe false
            }
            coVerifyNever { insertPlaylistUseCase(any(), any()) }
            stateJob.cancel()
        }

    @Test
    fun `GIVEN insert failure WHEN OnAddPlaylistConfirmDialog THEN error emitted and loading false`() =
        runTest {
            val error = Throwable("Database error")
            val title = "Valid Title"
            val description = "Valid Description"

            coEvery {
                insertPlaylistUseCase(name = title, description = description)
            } returns left(error)

            val states = mutableListOf<PlaylistState>()
            val events = mutableListOf<PlaylistEvent>()
            val stateJob = launch { viewModel.state.toList(states) }
            val eventJob = launch { viewModel.events.toList(events) }

            viewModel.onAction(PlaylistAction.OnDialogTitleChange(title))
            viewModel.onAction(PlaylistAction.OnDialogDescriptionChange(description))
            advanceUntilIdle()
            viewModel.onAction(PlaylistAction.OnAddPlaylistConfirmDialog)
            advanceUntilIdle()

            events shouldHaveSize 1
            events.first() shouldBeEqualTo PlaylistEvent.Error(error)
            states.last().isLoading shouldBe false

            eventJob.cancel()
            stateJob.cancel()
        }

    @Test
    fun `GIVEN OnItemDragFinish WHEN onAction THEN playlists updated`() =
        runTest {
            val reorderedPlaylists = listOf(buildPlaylist())
            coEvery {
                upsertPlaylistsUseCase(reorderedPlaylists)
            } returns right(Unit)
            val states = mutableListOf<PlaylistState>()
            val stateJob = launch { viewModel.state.toList(states) }
            advanceUntilIdle()

            viewModel.onAction(PlaylistAction.OnItemDragFinish(reorderedPlaylists))
            advanceUntilIdle()

            coVerifyOnce {
                upsertPlaylistsUseCase(reorderedPlaylists)
            }
            stateJob.cancel()
        }

    @Test
    fun `GIVEN playlistId WHEN OnRemovePlaylistClicked THEN confirm dialog is shown`() =
        runTest {
            val playlistId = 1
            val states = mutableListOf<PlaylistState>()
            val job = launch { viewModel.state.toList(states) }

            viewModel.onAction(PlaylistAction.OnRemovePlaylistClicked(playlistId))
            advanceUntilIdle()

            states.last().apply {
                selectedPlaylistId shouldBeEqualTo playlistId
                shouldIShowConfirmDialog shouldBe true
            }
            job.cancel()
        }

    @Test
    fun `GIVEN selectedPlaylistId WHEN OnConfirmDeleteDialog THEN delete and update state`() =
        runTest {
            val playlistToDelete = buildPlaylist(id = 10)
            coEvery { deletePlaylistByIdUseCase(playlistToDelete.id) } returns right(Unit)
            val states = mutableListOf<PlaylistState>()
            val job = launch { viewModel.state.toList(states) }

            viewModel.onAction(PlaylistAction.OnRemovePlaylistClicked(playlistToDelete.id))
            advanceUntilIdle()
            viewModel.onAction(PlaylistAction.OnConfirmDeleteDialog)
            advanceUntilIdle()

            states.last().apply {
                selectedPlaylistId shouldBe null
                shouldIShowConfirmDialog shouldBe false
                playlists shouldNotContain playlistToDelete
            }
            coVerifyOnce {
                deletePlaylistByIdUseCase(playlistToDelete.id)
            }
            job.cancel()
        }

    @Test
    fun `GIVEN delete error WHEN OnConfirmDeleteDialog THEN error emitted`() =
        runTest {
            val playlistId = 42
            val error = Throwable("Delete failed")
            coEvery { deletePlaylistByIdUseCase(playlistId) } returns left(error)
            val events = mutableListOf<PlaylistEvent>()
            val states = mutableListOf<PlaylistState>()
            val eventJob = launch { viewModel.events.toList(events) }
            val stateJob = launch { viewModel.state.toList(states) }

            viewModel.onAction(PlaylistAction.OnRemovePlaylistClicked(playlistId))
            advanceUntilIdle()
            viewModel.onAction(PlaylistAction.OnConfirmDeleteDialog)
            advanceUntilIdle()

            events.first() shouldBeEqualTo PlaylistEvent.Error(error)
            coVerifyOnce {
                deletePlaylistByIdUseCase(playlistId)
            }
            stateJob.cancel()
            eventJob.cancel()
        }
}
