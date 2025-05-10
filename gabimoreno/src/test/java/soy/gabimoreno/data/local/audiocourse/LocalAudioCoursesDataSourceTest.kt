@file:OptIn(ExperimentalCoroutinesApi::class)

package soy.gabimoreno.data.local.audiocourse

import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
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
import soy.gabimoreno.data.local.audiocourse.dao.AudioCourseDbModelDao
import soy.gabimoreno.data.local.audiocourse.dao.AudioCourseItemDbModelDao
import soy.gabimoreno.data.local.audiocourse.dao.AudioCourseTransactionDao
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
        coVerifyOnce {
            audioCourseDbModelDao.count()
        }
    }


    @Test
    fun `GIVEN count is more than 0 WHEN isEmpty THEN false`() = runTest {
        coEvery { audioCourseDbModelDao.count() } returns 5

        val result = dataSource.isEmpty()

        result shouldBe false
        coVerifyOnce {
            audioCourseDbModelDao.count()
        }
    }

    @Test
    fun `GIVEN audioCourses WHEN saveAudioCourses THEN call transactionDao`() = runTest {
        val audioCourses = buildAudioCourses()

        dataSource.saveAudioCourses(audioCourses)

        coVerifyOnce {
            audioCourseTransactionDao.upsertAudioCoursesWithItems(audioCourses)
        }
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
            val audioCourseWithItems = buildAudioCourseWithItems()
            coEvery {
                audioCourseTransactionDao.getAudioCourseWithItems(audioCourseWithItems.course.id)
            } returns flowOf(audioCourseWithItems)

            val result = dataSource.getAudioCourseById(audioCourseWithItems.course.id).first()

            result?.id shouldBeEqualTo audioCourseWithItems.course.id
            coVerifyOnce {
                audioCourseTransactionDao.getAudioCourseWithItems(audioCourseWithItems.course.id)
            }
        }

    @Test
    fun `GIVEN no course in DB WHEN getAudioCourseById THEN return null`() = runTest {
        val id = "1-1"
        coEvery { audioCourseTransactionDao.getAudioCourseWithItems(id) } returns flowOf(null)

        val result = dataSource.getAudioCourseById(id).first()

        result shouldBe null
        coVerifyOnce { audioCourseTransactionDao.getAudioCourseWithItems(id) }
    }

    @Test
    fun `WHEN reset THEN call deleteAllAudioCourseDbModels`() = runTest {
        dataSource.reset()

        coVerifyOnce {
            audioCourseDbModelDao.deleteAllAudioCourseDbModels()
        }
    }
}
