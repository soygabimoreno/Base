package soy.gabimoreno.data.remote.repository

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coJustRun
import io.mockk.just
import io.mockk.mockk
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.amshove.kluent.shouldBe
import org.amshove.kluent.shouldBeEqualTo
import org.amshove.kluent.shouldBeInstanceOf
import org.junit.Before
import org.junit.Test
import soy.gabimoreno.core.testing.coVerifyNever
import soy.gabimoreno.core.testing.coVerifyOnce
import soy.gabimoreno.core.testing.relaxedMockk
import soy.gabimoreno.data.cloud.audiosync.datasource.AudioCoursesCloudDataSource
import soy.gabimoreno.data.local.audiocourse.LocalAudioCoursesDataSource
import soy.gabimoreno.data.local.mapper.toAudioCourseItem
import soy.gabimoreno.data.local.mapper.toPlaylistAudioItem
import soy.gabimoreno.data.remote.datasource.audiocourses.RemoteAudioCoursesDataSource
import soy.gabimoreno.data.remote.model.Category
import soy.gabimoreno.domain.model.content.AudioCourse
import soy.gabimoreno.domain.repository.audiocourses.DefaultAudioCoursesRepository
import soy.gabimoreno.domain.usecase.RefreshPremiumAudiosFromRemoteUseCase
import soy.gabimoreno.ext.right
import soy.gabimoreno.fake.buildAudioCourse
import soy.gabimoreno.fake.buildAudioCourseItem
import soy.gabimoreno.fake.buildAudioCourseItemDbModel
import soy.gabimoreno.fake.buildAudioCourses
import soy.gabimoreno.fake.buildCloudAudioCourseResponses
import soy.gabimoreno.fake.buildCloudAudiocourseResponseList

class DefaultAudioCoursesRepositoryTest {
    private val cloudDataSource: AudioCoursesCloudDataSource = mockk()
    private val localAudioCoursesDataSource = relaxedMockk<LocalAudioCoursesDataSource>()
    private val remoteAudioCoursesDataSource = relaxedMockk<RemoteAudioCoursesDataSource>()
    private val refreshPremiumAudiosFromRemoteUseCase =
        mockk<RefreshPremiumAudiosFromRemoteUseCase>()
    private lateinit var repository: DefaultAudioCoursesRepository

    @Before
    fun setUp() {
        repository =
            DefaultAudioCoursesRepository(
                cloudDataSource,
                localAudioCoursesDataSource,
                remoteAudioCoursesDataSource,
                refreshPremiumAudiosFromRemoteUseCase,
            )
    }

    @Test
    fun `GIVEN refresh is true WHEN getCourses THEN fetch from remote and save locally`() =
        runTest {
            val categories = listOf(Category.AUDIOCOURSES)
            val audioCourses = listOf(relaxedMockk<AudioCourse>())
            val cloudAudioCourses = buildCloudAudiocourseResponseList()

            coEvery { cloudDataSource.getAudioCoursesItems(EMAIL) } returns cloudAudioCourses
            coEvery { refreshPremiumAudiosFromRemoteUseCase(any(), any()) } returns false
            coEvery { localAudioCoursesDataSource.isEmpty() } returns true
            coEvery { localAudioCoursesDataSource.getAudioCoursesWithItems() } returns emptyList()
            coEvery { remoteAudioCoursesDataSource.getAudioCourses(categories) } returns audioCourses.right()
            coEvery { localAudioCoursesDataSource.getAudioCourses() } returns audioCourses

            val result = repository.getCourses(categories, EMAIL)

            result shouldBeEqualTo audioCourses.right()
            coVerifyOnce {
                remoteAudioCoursesDataSource.getAudioCourses(categories)
                localAudioCoursesDataSource.saveAudioCourses(any())
            }
        }

    @Test
    fun `GIVEN local has ARE listened items WHEN getCourses THEN remote items are listened`() =
        runTest {
            val categories = listOf(Category.AUDIOCOURSES)
            val remoteCourses = buildAudioCourses(hasBeenListened = false)
            val cloudAudioCourses =
                buildCloudAudioCourseResponses(remoteCourses, hasBeenListened = true)
            coEvery { refreshPremiumAudiosFromRemoteUseCase(any(), any()) } returns true
            coEvery { localAudioCoursesDataSource.isEmpty() } returns false
            coEvery { remoteAudioCoursesDataSource.getAudioCourses(categories) } returns remoteCourses.right()
            coEvery { cloudDataSource.getAudioCoursesItems(EMAIL) } returns cloudAudioCourses
            coEvery { localAudioCoursesDataSource.saveAudioCourses(any()) } just Runs

            repository.getCourses(categories = categories, email = EMAIL, forceRefresh = false)

            val expectedMerged =
                remoteCourses.map { course ->
                    course.copy(
                        audios =
                            course.audios.map {
                                it.copy(
                                    hasBeenListened = true,
                                    markedAsFavorite = false,
                                )
                            },
                    )
                }
            coVerifyOnce {
                cloudDataSource.getAudioCoursesItems(EMAIL)
                localAudioCoursesDataSource.saveAudioCourses(expectedMerged)
            }
        }

    @Test
    fun `GIVEN local has NOT listened items WHEN getCourses THEN remote items remain unlistened`() =
        runTest {
            val categories = listOf(Category.AUDIOCOURSES)
            val remoteCourses = buildAudioCourses(hasBeenListened = false)
            val cloudAudioCourses = buildCloudAudioCourseResponses(remoteCourses)
            coEvery { refreshPremiumAudiosFromRemoteUseCase(any(), any()) } returns true
            coEvery { localAudioCoursesDataSource.isEmpty() } returns false
            coEvery { remoteAudioCoursesDataSource.getAudioCourses(categories) } returns remoteCourses.right()
            coEvery { cloudDataSource.getAudioCoursesItems(EMAIL) } returns cloudAudioCourses
            coEvery { localAudioCoursesDataSource.saveAudioCourses(any()) } just Runs

            val result = repository.getCourses(categories, EMAIL)

            val expectedHasBeenListened =
                result.getOrNull()!!.flatMap { it.audios }.none { it.hasBeenListened }
            expectedHasBeenListened shouldBeEqualTo true

            coVerifyOnce {
                localAudioCoursesDataSource.saveAudioCourses(
                    match {
                        it
                            .flatMap { audioCourse -> audioCourse.audios }
                            .none { audioCourseItem -> audioCourseItem.hasBeenListened }
                    },
                )
            }
        }

    @Test
    fun `GIVEN local is empty WHEN getCourses THEN fetch from remote`() =
        runTest {
            val categories = listOf(Category.AUDIOCOURSES)
            val remoteCourses = buildAudioCourses()
            val cloudAudioCourses = buildCloudAudioCourseResponses(remoteCourses)
            coEvery { refreshPremiumAudiosFromRemoteUseCase(any(), any()) } returns false
            coEvery { localAudioCoursesDataSource.isEmpty() } returns true
            coEvery { localAudioCoursesDataSource.getAudioCoursesWithItems() } returns emptyList()
            coEvery { remoteAudioCoursesDataSource.getAudioCourses(categories) } returns remoteCourses.right()
            coEvery { cloudDataSource.getAudioCoursesItems(EMAIL) } returns cloudAudioCourses
            coEvery { localAudioCoursesDataSource.getAudioCourses() } returns remoteCourses

            val result = repository.getCourses(categories, EMAIL, forceRefresh = false)

            result shouldBeEqualTo remoteCourses.right()
            coVerifyOnce {
                remoteAudioCoursesDataSource.getAudioCourses(categories)
                localAudioCoursesDataSource.saveAudioCourses(remoteCourses)
            }
        }

    @Test
    fun `GIVEN remote fails WHEN getCourses THEN return error`() =
        runTest {
            val categories = listOf(Category.AUDIOCOURSES)
            val error = Throwable("Network error")
            coEvery { refreshPremiumAudiosFromRemoteUseCase(any(), any()) } returns true
            coEvery { remoteAudioCoursesDataSource.getAudioCourses(categories) } returns error.left()

            val result = repository.getCourses(listOf(Category.AUDIOCOURSES), EMAIL)

            result shouldBeEqualTo error.left()
        }

    @Test
    fun `GIVEN no refresh and local is not empty WHEN getCourses THEN fetch from local only`() =
        runTest {
            val categories = listOf(Category.AUDIOCOURSES)
            val courses = listOf(relaxedMockk<AudioCourse>())
            coEvery { refreshPremiumAudiosFromRemoteUseCase(any(), any()) } returns false
            coEvery { localAudioCoursesDataSource.isEmpty() } returns false
            coEvery { localAudioCoursesDataSource.getAudioCourses() } returns courses

            val result = repository.getCourses(listOf(Category.AUDIOCOURSES), EMAIL)

            result shouldBeEqualTo courses.right()
            coVerifyNever {
                remoteAudioCoursesDataSource.getAudioCourses(categories)
            }
        }

    @Test
    fun `GIVEN valid course id WHEN getCourseById THEN return course`() =
        runTest {
            val course = relaxedMockk<AudioCourse>()
            val id = "id"
            coEvery {
                localAudioCoursesDataSource.getAudioCourseById(id)
            } returns flowOf(course)

            val result = repository.getCourseById(id)

            result.isRight() shouldBe true
            result.getOrNull()!!.first() shouldBe course
        }

    @Test
    fun `GIVEN invalid course id WHEN getCourseById THEN return error`() =
        runTest {
            coEvery {
                localAudioCoursesDataSource.getAudioCourseById("invalid")
            } returns flowOf(null)

            val result = repository.getCourseById("invalid")

            result.isLeft() shouldBe true
            result.mapLeft { it.message shouldBe "AudioCourse not found" }
        }

    @Test
    fun `GIVEN item is listened WHEN markAudioCourseItemAsListened THEN field is updated`() =
        runTest {
            val audioCourse = buildAudioCourse()
            coJustRun {
                cloudDataSource.upsertAudioCourseItemFields(
                    EMAIL,
                    audioCourse.id,
                    mapOf(
                        "id" to audioCourse.id,
                        "hasBeenListened" to true,
                    ),
                )
            }
            repository.markAudioCourseItemAsListened(audioCourse.id, EMAIL, true)

            coVerifyOnce {
                localAudioCoursesDataSource.updateHasBeenListened(audioCourse.id, true)
                cloudDataSource.upsertAudioCourseItemFields(
                    EMAIL,
                    audioCourse.id,
                    mapOf(
                        "id" to audioCourse.id,
                        "hasBeenListened" to true,
                    ),
                )
            }
        }

    @Test
    fun `GIVEN item is unlistened WHEN markAudioCourseItemAsListened THEN field is updated`() =
        runTest {
            val audioCourse = buildAudioCourse()
            coJustRun {
                cloudDataSource.upsertAudioCourseItemFields(
                    EMAIL,
                    audioCourse.id,
                    mapOf(
                        "id" to audioCourse.id,
                        "hasBeenListened" to false,
                    ),
                )
            }
            repository.markAudioCourseItemAsListened(audioCourse.id, EMAIL, false)

            coVerifyOnce {
                localAudioCoursesDataSource.updateHasBeenListened(audioCourse.id, false)
                cloudDataSource.upsertAudioCourseItemFields(
                    EMAIL,
                    audioCourse.id,
                    mapOf(
                        "id" to audioCourse.id,
                        "hasBeenListened" to false,
                    ),
                )
            }
        }

    @Test
    fun `GIVEN repository WHEN markAllAudioCourseItemsAsUnlistened THEN local dataSource is called`() =
        runTest {
            coEvery {
                localAudioCoursesDataSource.markAllAudioCourseItemsAsUnlistened()
            } returns Unit
            coJustRun {
                cloudDataSource.batchUpdateFieldsForAllAudioCoursesItems(
                    EMAIL,
                    mapOf("hasBeenListened" to false),
                )
            }

            repository.markAllAudioCourseItemsAsUnlistened(EMAIL)

            coVerifyOnce {
                localAudioCoursesDataSource.markAllAudioCourseItemsAsUnlistened()
                cloudDataSource.batchUpdateFieldsForAllAudioCoursesItems(
                    EMAIL,
                    mapOf("hasBeenListened" to false),
                )
            }
        }

    @Test
    fun `WHEN reset THEN call reset on local`() =
        runTest {
            coEvery { localAudioCoursesDataSource.reset() } returns Unit

            repository.reset()

            coVerifyOnce { localAudioCoursesDataSource.reset() }
        }

    @Test
    fun `GIVEN audioCourseItemId WHEN getAudioCourseItem THEN return AudioCourseItem`() =
        runTest {
            val audioCourseItem = buildAudioCourseItemDbModel()
            coEvery {
                localAudioCoursesDataSource.getAudioCourseItem(audioCourseItem.id)
            } returns audioCourseItem

            val result = repository.getAudioCourseItem(audioCourseItem.id)

            result.isRight() shouldBe true
            result.getOrNull() shouldBeEqualTo audioCourseItem.toAudioCourseItem()
            coVerifyOnce {
                localAudioCoursesDataSource.getAudioCourseItem(audioCourseItem.id)
            }
        }

    @Test
    fun `GIVEN audioCourseItemId WHEN getAudioCourseItem THEN return null`() =
        runTest {
            val audioCourseItemId = "1"
            coEvery {
                localAudioCoursesDataSource.getAudioCourseItem(audioCourseItemId)
            } returns null

            val result = repository.getAudioCourseItem(audioCourseItemId)

            result.isLeft() shouldBe true
            coVerifyOnce {
                localAudioCoursesDataSource.getAudioCourseItem(audioCourseItemId)
            }
        }

    @Test
    fun `GIVEN existing audioCourseId WHEN getAudioCourseItemById THEN returns PlaylistAudioItem`() =
        runTest {
            val audioCourse = buildAudioCourse()
            val audioCourseItem = buildAudioCourseItem()
            val position = 1
            val playlistAudioItem = audioCourseItem.toPlaylistAudioItem(audioCourse, position)
            coEvery { localAudioCoursesDataSource.getAudioCourseById(audioCourse.id) } returns flowOf(audioCourse)

            val result = repository.getAudioCourseItemById(audioCourseItem.id)

            result shouldBeEqualTo right(playlistAudioItem)
            coVerifyOnce {
                localAudioCoursesDataSource.getAudioCourseById(audioCourse.id)
            }
        }

    @Test
    fun `GIVEN nonExisting audioCourseId WHEN getAudioCourseItemById THEN returns error`() =
        runTest {
            val audioCourseId = "1"
            val audioCourseItemId = "1-1"
            coEvery { localAudioCoursesDataSource.getAudioCourseById(audioCourseId) } returns flowOf(null)

            val result = repository.getAudioCourseItemById(audioCourseItemId)

            result shouldBeInstanceOf Either.Left::class.java
            coVerifyOnce {
                localAudioCoursesDataSource.getAudioCourseById(audioCourseId)
            }
        }

    @Test
    fun `GIVEN repository WHEN getAllFavoriteAudioCoursesItems THEN return AudioCourseItem list with favorites`() =
        runTest {
            val audioCourseItems =
                listOf(
                    buildAudioCourseItemDbModel(markedAsFavorite = true),
                    buildAudioCourseItemDbModel(markedAsFavorite = true),
                )
            coEvery {
                localAudioCoursesDataSource.getAllFavoriteAudioCoursesItems()
            } returns audioCourseItems

            val result = repository.getAllFavoriteAudioCoursesItems()

            result.isRight() shouldBe true
            result.getOrNull() shouldBeEqualTo audioCourseItems.map { it.toAudioCourseItem() }
            coVerifyOnce {
                localAudioCoursesDataSource.getAllFavoriteAudioCoursesItems()
            }
        }

    @Test
    fun `GIVEN repository WHEN updateMarkedAsFavorite THEN local and cloud dataSources are called`() =
        runTest {
            val audioCourseItem = buildAudioCourseItemDbModel()
            coJustRun {
                localAudioCoursesDataSource.updateMarkedAsFavorite(audioCourseItem.id, true)
            }
            coJustRun {
                cloudDataSource.upsertAudioCourseItemFields(
                    EMAIL,
                    audioCourseItem.id,
                    mapOf(
                        "id" to audioCourseItem.id,
                        "markedAsFavorite" to true,
                    ),
                )
            }

            repository.updateMarkedAsFavorite(audioCourseItem.id, EMAIL, true)

            coVerifyOnce {
                localAudioCoursesDataSource.updateMarkedAsFavorite(audioCourseItem.id, true)
                cloudDataSource.upsertAudioCourseItemFields(
                    EMAIL,
                    audioCourseItem.id,
                    mapOf(
                        "id" to audioCourseItem.id,
                        "markedAsFavorite" to true,
                    ),
                )
            }
        }
}

private const val EMAIL = "test@test.com"
