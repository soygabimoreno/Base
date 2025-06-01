@file:OptIn(ExperimentalCoroutinesApi::class)

package soy.gabimoreno.data.local.playlist

import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.amshove.kluent.shouldBe
import org.amshove.kluent.shouldBeEqualTo
import org.junit.After
import org.junit.Before
import org.junit.Test
import soy.gabimoreno.core.testing.relaxedMockk
import soy.gabimoreno.data.local.GabiMorenoDatabase
import soy.gabimoreno.data.local.audiocourse.LocalAudioCoursesDataSource
import soy.gabimoreno.data.local.audiocourse.dao.AudioCourseDbModelDao
import soy.gabimoreno.data.local.audiocourse.dao.AudioCourseItemDbModelDao
import soy.gabimoreno.data.local.audiocourse.dao.AudioCourseTransactionDao
import soy.gabimoreno.data.local.mapper.toPlaylistMapper
import soy.gabimoreno.data.local.mapper.toPremiumAudioDbModel
import soy.gabimoreno.data.local.playlist.dao.PlaylistDbModelDao
import soy.gabimoreno.data.local.playlist.dao.PlaylistItemDbModelDao
import soy.gabimoreno.data.local.playlist.dao.PlaylistTransactionDao
import soy.gabimoreno.data.local.playlist.model.PlaylistDbModel
import soy.gabimoreno.data.local.playlist.model.PlaylistItemsDbModel
import soy.gabimoreno.data.local.playlist.model.PlaylistWithItems
import soy.gabimoreno.data.local.premiumaudio.LocalPremiumAudiosDataSource
import soy.gabimoreno.data.local.premiumaudio.PremiumAudioDbModelDao
import soy.gabimoreno.fake.buildPlaylist

class LocalPlaylistRepositoryTest {

    private val premiumAudioDbModelDao: PremiumAudioDbModelDao =
        relaxedMockk<PremiumAudioDbModelDao>()
    private val audioCourseDbModelDao: AudioCourseDbModelDao = relaxedMockk<AudioCourseDbModelDao>()
    private val audioCourseItemDbModelDao: AudioCourseItemDbModelDao =
        relaxedMockk<AudioCourseItemDbModelDao>()
    private val audioCourseTransactionDao: AudioCourseTransactionDao =
        relaxedMockk<AudioCourseTransactionDao>()
    private val playlistDbModelDao: PlaylistDbModelDao = relaxedMockk<PlaylistDbModelDao>()
    private val playlistItemDbModelDao: PlaylistItemDbModelDao =
        relaxedMockk<PlaylistItemDbModelDao>()
    private val playlistTransactionDao: PlaylistTransactionDao =
        relaxedMockk<PlaylistTransactionDao>()
    private val gabiMorenoDatabase: GabiMorenoDatabase = createMockedDatabase()

    private lateinit var premiumAudiosDataSource: LocalPremiumAudiosDataSource
    private lateinit var audioCoursesDataSource: LocalAudioCoursesDataSource
    private lateinit var playlistDataSource: LocalPlaylistDataSource
    private val dispatcher: TestDispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(dispatcher)
        premiumAudiosDataSource = LocalPremiumAudiosDataSource(gabiMorenoDatabase, dispatcher)
        audioCoursesDataSource = LocalAudioCoursesDataSource(gabiMorenoDatabase, dispatcher)
        playlistDataSource = LocalPlaylistDataSource(gabiMorenoDatabase, dispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `GIVEN valid data WHEN getAllPlaylists THEN mapped playlists are returned`() = runTest {
        val playlist = buildPlaylist()
        val playlistDbModel = PlaylistDbModel(
            id = playlist.id,
            title = playlist.title,
            description = playlist.description,
            position = playlist.position
        )
        val itemsDbModel = playlist.items.map {
            PlaylistItemsDbModel(
                id = it.id,
                playlistId = playlist.id,
                position = it.position
            )
        }
        val playlistWithItems = PlaylistWithItems(playlistDbModel, itemsDbModel)
        val premiumAudiosDbModel = playlist.items.map { it.toPremiumAudioDbModel() }

        coEvery {
            playlistTransactionDao.getPlaylistsWithItems()
        } returns listOf(playlistWithItems)
        coEvery {
            premiumAudioDbModelDao.getPremiumAudiosByIds(any())
        } returns premiumAudiosDbModel

        val result = playlistDataSource.getAllPlaylists()

        val expected = playlistDbModel.toPlaylistMapper(playlist.items)
        result shouldBeEqualTo listOf(expected)
    }

    @Test
    fun `GIVEN valid playlist WHEN getPlaylistById THEN mapped playlist is emitted`() = runTest {
        val playlist = buildPlaylist()
        val playlistDbModel = PlaylistDbModel(
            playlist.id,
            playlist.title,
            playlist.description,
            playlist.position,
        )
        val itemsDbModel = playlist.items.map {
            PlaylistItemsDbModel(
                it.id,
                playlist.id,
                it.position,
            )
        }
        val playlistWithItems = PlaylistWithItems(playlistDbModel, itemsDbModel)
        val premiumAudiosDbModel = playlist.items.map { premiumAudio ->
            premiumAudio.toPremiumAudioDbModel()
        }
        every {
            playlistTransactionDao.getPlaylistWithItemsById(playlist.id)
        } returns flowOf(playlistWithItems)
        coEvery {
            premiumAudioDbModelDao.getPremiumAudiosByIds(any())
        } returns premiumAudiosDbModel
        coEvery {
            audioCourseDbModelDao.getAudioCoursesByIds(any())
        } returns emptyList()
        coEvery {
            audioCourseItemDbModelDao.getAudioCourseItemsByIds(any())
        } returns emptyList()

        val result = playlistDataSource.getPlaylistById(playlist.id).first()

        val expected = playlistDbModel.toPlaylistMapper(playlist.items)
        result shouldBeEqualTo expected
    }

    @Test
    fun `GIVEN null result WHEN getPlaylistById THEN null is emitted`() = runTest {
        val playlistId = 1
        every {
            playlistTransactionDao.getPlaylistWithItemsById(playlistId)
        } returns flowOf(null)

        val result = playlistDataSource.getPlaylistById(playlistId).first()

        result shouldBe null
    }

    private fun createMockedDatabase(): GabiMorenoDatabase = mockk {
        every { premiumAudioDbModelDao() } returns premiumAudioDbModelDao
        every { audioCourseDbModelDao() } returns audioCourseDbModelDao
        every { audioCourseItemDbModelDao() } returns audioCourseItemDbModelDao
        every { audioCourseTransactionDao() } returns audioCourseTransactionDao
        every { playlistDbModelDao() } returns playlistDbModelDao
        every { playlistItemDbModelDao() } returns playlistItemDbModelDao
        every { playlistTransactionDao() } returns playlistTransactionDao
    }
}
