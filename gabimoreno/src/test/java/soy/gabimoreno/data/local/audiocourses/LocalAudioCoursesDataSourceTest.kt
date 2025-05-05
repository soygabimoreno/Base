@file:OptIn(ExperimentalCoroutinesApi::class)

package soy.gabimoreno.data.local.audiocourses

import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.amshove.kluent.shouldBe
import org.amshove.kluent.shouldBeEqualTo
import org.amshove.kluent.shouldHaveSize
import org.junit.After
import org.junit.Before
import org.junit.Test
import soy.gabimoreno.core.testing.coVerifyOnce
import soy.gabimoreno.core.testing.relaxedMockk
import soy.gabimoreno.data.local.GabiMorenoDatabase
import soy.gabimoreno.data.local.audiocourses.dao.AudioCourseDbModelDao
import soy.gabimoreno.data.local.audiocourses.dao.AudioCourseItemDbModelDao
import soy.gabimoreno.data.local.audiocourses.dao.AudioCourseTransactionDao
import soy.gabimoreno.fake.buildAudioCourseWithItems
import soy.gabimoreno.fake.buildAudioCourses
import soy.gabimoreno.fake.buildAudioCoursesDbModel

class LocalAudioCoursesDataSourceTest {

    private val audioCourseDbModelDao: AudioCourseDbModelDao = relaxedMockk<AudioCourseDbModelDao>()
    private val audioCourseItemDbModelDao: AudioCourseItemDbModelDao =
        relaxedMockk<AudioCourseItemDbModelDao>()
    private val audioCourseTransactionDao: AudioCourseTransactionDao =
        relaxedMockk<AudioCourseTransactionDao>()
    private val gabiMorenoDatabase: GabiMorenoDatabase = mockk<GabiMorenoDatabase> {
        every { audioCourseDbModelDao() } returns audioCourseDbModelDao
        every { audioCourseItemDbModelDao() } returns audioCourseItemDbModelDao
        every { audioCourseTransactionDao() } returns audioCourseTransactionDao
    }
    private lateinit var dataSource: LocalAudioCoursesDataSource

    @OptIn(ExperimentalCoroutinesApi::class)
    @Before
    fun setUp() {
        Dispatchers.setMain(StandardTestDispatcher())
        dataSource = LocalAudioCoursesDataSource(
            gabiMorenoDatabase,
            Dispatchers.Main
        )
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `GIVEN count is 0 WHEN isEmpty THEN true`() = runTest {
        coEvery { audioCourseDbModelDao.count() } returns 0

        val result = dataSource.isEmpty()

        result shouldBe true
        coVerifyOnce { audioCourseDbModelDao.count() }
    }


    @Test
    fun `GIVEN count is more than 0 WHEN isEmpty THEN false`() = runTest {
        coEvery { audioCourseDbModelDao.count() } returns 5

        val result = dataSource.isEmpty()

        result shouldBe false
        coVerifyOnce { audioCourseDbModelDao.count() }
    }

    @Test
    fun `GIVEN audioCourses WHEN saveAudioCourses THEN call transactionDao`() = runTest {
        val audioCourses = buildAudioCourses()

        dataSource.saveAudioCourses(audioCourses)

        coVerifyOnce { audioCourseTransactionDao.upsertAudioCoursesWithItems(audioCourses) }
    }

    @Test
    fun `GIVEN audioCourses in DB WHEN getAudioCourses THEN return mapped list`() = runTest {
        val audioCourses = buildAudioCoursesDbModel()
        coEvery { audioCourseDbModelDao.getAudioCourseDbModels() } returns audioCourses

        val result = dataSource.getAudioCourses()

        result shouldHaveSize audioCourses.size
        result.first().id shouldBeEqualTo audioCourses.first().id
        coVerifyOnce { audioCourseDbModelDao.getAudioCourseDbModels() }
    }

    @Test
    fun `GIVEN matching course in DB WHEN getAudioCourseById THEN return mapped course`() =
        runTest {
            val audioCoursesWithItems = buildAudioCourseWithItems()
            coEvery {
                audioCourseTransactionDao.getAudioCoursesWithItems(audioCoursesWithItems.course.id)
            } returns audioCoursesWithItems

            val result = dataSource.getAudioCourseById(audioCoursesWithItems.course.id)

            result?.id shouldBeEqualTo audioCoursesWithItems.course.id
            coVerifyOnce {
                audioCourseTransactionDao.getAudioCoursesWithItems(audioCoursesWithItems.course.id)
            }
        }

    @Test
    fun `GIVEN no course in DB WHEN getAudioCourseById THEN return null`() = runTest {
        coEvery { audioCourseTransactionDao.getAudioCoursesWithItems("1-1") } returns null

        val result = dataSource.getAudioCourseById("1-1")

        result shouldBe null
        coVerifyOnce { audioCourseTransactionDao.getAudioCoursesWithItems("1-1") }
    }

    @Test
    fun `WHEN reset THEN call deleteAllAudioCourseDbModels`() = runTest {
        dataSource.reset()

        coVerifyOnce { audioCourseDbModelDao.deleteAllAudioCourseDbModels() }
    }
}
