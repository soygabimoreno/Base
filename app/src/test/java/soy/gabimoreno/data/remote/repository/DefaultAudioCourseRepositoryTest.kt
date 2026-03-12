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
import soy.gabimoreno.data.cloud.audiosync.datasource.AudioCourseCloudDataSource
import soy.gabimoreno.data.local.audiocourse.LocalAudioCourseDataSource
import soy.gabimoreno.data.local.mapper.toAudioCourseItem
import soy.gabimoreno.data.local.mapper.toPlaylistAudioItem
import soy.gabimoreno.data.remote.datasource.audiocourse.RemoteAudioCourseDataSource
import soy.gabimoreno.data.remote.model.Category
import soy.gabimoreno.domain.model.content.AudioCourse
import soy.gabimoreno.domain.repository.audiocourses.DefaultAudioCourseRepository
import soy.gabimoreno.domain.usecase.RefreshPremiumAudiosFromRemoteUseCase
import soy.gabimoreno.ext.right
import soy.gabimoreno.fake.buildAudioCourse
import soy.gabimoreno.fake.buildAudioCourseItem
import soy.gabimoreno.fake.buildAudioCourseItemDbModel
import soy.gabimoreno.fake.buildAudioCourses
import soy.gabimoreno.fake.buildCloudAudioCourseResponses
import soy.gabimoreno.fake.buildCloudAudiocourseResponseList

class DefaultAudioCourseRepositoryTest {
    private val cloudDataSource: AudioCourseCloudDataSource = mockk()
    private val localAudioCourseDataSource = relaxedMockk<LocalAudioCourseDataSource>()
    private val remoteAudioCoursesDataSource = relaxedMockk<RemoteAudioCourseDataSource>()
    private val refreshPremiumAudiosFromRemoteUseCase =
        mockk<RefreshPremiumAudiosFromRemoteUseCase>()
    private lateinit var repository: DefaultAudioCourseRepository

    @Before
    fun setUp() {
        repository =
            DefaultAudioCourseRepository(
                cloudDataSource,
                localAudioCourseDataSource,
                remoteAudioCoursesDataSource,
                refreshPremiumAudiosFromRemoteUseCase,
            )
    }

    @Test
    fun `GIVEN refresh is true WHEN getAudioCourses THEN fetch from remote and save locally`() =
        runTest {
            val categories = listOf(Category.AUDIOCOURSES)
            val audioCourses = listOf(relaxedMockk<AudioCourse>())
            val cloudAudioCourses = buildCloudAudiocourseResponseList()

            coEvery { cloudDataSource.getAudioCoursesItems(EMAIL) } returns cloudAudioCourses
            coEvery { refreshPremiumAudiosFromRemoteUseCase(any(), any()) } returns false
            coEvery { localAudioCourseDataSource.isEmpty() } returns true
            coEvery { localAudioCourseDataSource.getAudioCoursesWithItems() } returns emptyList()
            coEvery { remoteAudioCoursesDataSource.getAudioCourses(categories) } returns audioCourses.right()
            coEvery { localAudioCourseDataSource.getAudioCourses() } returns audioCourses

            val result = repository.getAudioCourses(categories, EMAIL)

            result shouldBeEqualTo audioCourses.right()
            coVerifyOnce {
                remoteAudioCoursesDataSource.getAudioCourses(categories)
                localAudioCourseDataSource.saveAudioCourses(any())
            }
        }

    @Test
    fun `GIVEN local has ARE listened items WHEN getAudioCourses THEN remote items are listened`() =
        runTest {
            val categories = listOf(Category.AUDIOCOURSES)
            val remoteCourses = buildAudioCourses(hasBeenListened = false)
            val cloudAudioCourses =
                buildCloudAudioCourseResponses(remoteCourses, hasBeenListened = true)
            coEvery { refreshPremiumAudiosFromRemoteUseCase(any(), any()) } returns true
            coEvery { localAudioCourseDataSource.isEmpty() } returns false
            coEvery { remoteAudioCoursesDataSource.getAudioCourses(categories) } returns remoteCourses.right()
            coEvery { cloudDataSource.getAudioCoursesItems(EMAIL) } returns cloudAudioCourses
            coEvery { localAudioCourseDataSource.saveAudioCourses(any()) } just Runs

            repository.getAudioCourses(categories = categories, email = EMAIL, forceRefresh = false)

            val expectedMerged =
                remoteCourses.map { audioCourse ->
                    audioCourse.copy(
                        audios =
                            audioCourse.audios.map {
                                it.copy(
                                    hasBeenListened = true,
                                    markedAsFavorite = false,
                                )
                            },
                    )
                }
            coVerifyOnce {
                cloudDataSource.getAudioCoursesItems(EMAIL)
                localAudioCourseDataSource.saveAudioCourses(expectedMerged)
            }
        }

    @Test
    fun `GIVEN local has NOT listened items WHEN getAudioCourses THEN remote items remain unlistened`() =
        runTest {
            val categories = listOf(Category.AUDIOCOURSES)
            val remoteCourses = buildAudioCourses(hasBeenListened = false)
            val cloudAudioCourses = buildCloudAudioCourseResponses(remoteCourses)
            coEvery { refreshPremiumAudiosFromRemoteUseCase(any(), any()) } returns true
            coEvery { localAudioCourseDataSource.isEmpty() } returns false
            coEvery { remoteAudioCoursesDataSource.getAudioCourses(categories) } returns remoteCourses.right()
            coEvery { cloudDataSource.getAudioCoursesItems(EMAIL) } returns cloudAudioCourses
            coEvery { localAudioCourseDataSource.saveAudioCourses(any()) } just Runs

            val result = repository.getAudioCourses(categories, EMAIL)

            val expectedHasBeenListened =
                result.getOrNull()!!.flatMap { it.audios }.none { it.hasBeenListened }
            expectedHasBeenListened shouldBeEqualTo true

            coVerifyOnce {
                localAudioCourseDataSource.saveAudioCourses(
                    match {
                        it
                            .flatMap { audioCourse -> audioCourse.audios }
                            .none { audioCourseItem -> audioCourseItem.hasBeenListened }
                    },
                )
            }
        }

    @Test
    fun `GIVEN local is empty WHEN getAudioCourses THEN fetch from remote`() =
        runTest {
            val categories = listOf(Category.AUDIOCOURSES)
            val remoteCourses = buildAudioCourses()
            val cloudAudioCourses = buildCloudAudioCourseResponses(remoteCourses)
            coEvery { refreshPremiumAudiosFromRemoteUseCase(any(), any()) } returns false
            coEvery { localAudioCourseDataSource.isEmpty() } returns true
            coEvery { localAudioCourseDataSource.getAudioCoursesWithItems() } returns emptyList()
            coEvery { remoteAudioCoursesDataSource.getAudioCourses(categories) } returns remoteCourses.right()
            coEvery { cloudDataSource.getAudioCoursesItems(EMAIL) } returns cloudAudioCourses
            coEvery { localAudioCourseDataSource.getAudioCourses() } returns remoteCourses

            val result = repository.getAudioCourses(categories, EMAIL, forceRefresh = false)

            result shouldBeEqualTo remoteCourses.right()
            coVerifyOnce {
                remoteAudioCoursesDataSource.getAudioCourses(categories)
                localAudioCourseDataSource.saveAudioCourses(remoteCourses)
            }
        }

    @Test
    fun `GIVEN remote fails WHEN getAudioCourses THEN return error`() =
        runTest {
            val categories = listOf(Category.AUDIOCOURSES)
            val error = Throwable("Network error")
            coEvery { refreshPremiumAudiosFromRemoteUseCase(any(), any()) } returns true
            coEvery { remoteAudioCoursesDataSource.getAudioCourses(categories) } returns error.left()

            val result = repository.getAudioCourses(listOf(Category.AUDIOCOURSES), EMAIL)

            result shouldBeEqualTo error.left()
        }

    @Test
    fun `GIVEN no refresh and local is not empty WHEN getAudioCourses THEN fetch from local only`() =
        runTest {
            val categories = listOf(Category.AUDIOCOURSES)
            val courses = listOf(relaxedMockk<AudioCourse>())
            coEvery { refreshPremiumAudiosFromRemoteUseCase(any(), any()) } returns false
            coEvery { localAudioCourseDataSource.isEmpty() } returns false
            coEvery { localAudioCourseDataSource.getAudioCourses() } returns courses

            val result = repository.getAudioCourses(listOf(Category.AUDIOCOURSES), EMAIL)

            result shouldBeEqualTo courses.right()
            coVerifyNever {
                remoteAudioCoursesDataSource.getAudioCourses(categories)
            }
        }

    @Test
    fun `GIVEN valid audioCourse id WHEN getAudioCourseById THEN return audioCourse`() =
        runTest {
            val audioCourse = relaxedMockk<AudioCourse>()
            val id = "id"
            coEvery {
                localAudioCourseDataSource.getAudioCourseById(id)
            } returns flowOf(audioCourse)

            val result = repository.getAudioCourseById(id)

            result.isRight() shouldBe true
            result.getOrNull()!!.first() shouldBe audioCourse
        }

    @Test
    fun `GIVEN invalid audioCourse id WHEN getAudioCourseById THEN return error`() =
        runTest {
            coEvery {
                localAudioCourseDataSource.getAudioCourseById("invalid")
            } returns flowOf(null)

            val result = repository.getAudioCourseById("invalid")

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
                localAudioCourseDataSource.updateHasBeenListened(audioCourse.id, true)
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
                localAudioCourseDataSource.updateHasBeenListened(audioCourse.id, false)
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
                localAudioCourseDataSource.markAllAudioCourseItemsAsUnlistened()
            } returns Unit
            coJustRun {
                cloudDataSource.batchUpdateFieldsForAllAudioCoursesItems(
                    EMAIL,
                    mapOf("hasBeenListened" to false),
                )
            }

            repository.markAllAudioCourseItemsAsUnlistened(EMAIL)

            coVerifyOnce {
                localAudioCourseDataSource.markAllAudioCourseItemsAsUnlistened()
                cloudDataSource.batchUpdateFieldsForAllAudioCoursesItems(
                    EMAIL,
                    mapOf("hasBeenListened" to false),
                )
            }
        }

    @Test
    fun `WHEN reset THEN call reset on local`() =
        runTest {
            coEvery { localAudioCourseDataSource.reset() } returns Unit

            repository.reset()

            coVerifyOnce { localAudioCourseDataSource.reset() }
        }

    @Test
    fun `GIVEN audioCourseItemId WHEN getAudioCourseItem THEN return AudioCourseItem`() =
        runTest {
            val audioCourseItem = buildAudioCourseItemDbModel()
            coEvery {
                localAudioCourseDataSource.getAudioCourseItem(audioCourseItem.id)
            } returns audioCourseItem

            val result = repository.getAudioCourseItem(audioCourseItem.id)

            result.isRight() shouldBe true
            result.getOrNull() shouldBeEqualTo audioCourseItem.toAudioCourseItem()
            coVerifyOnce {
                localAudioCourseDataSource.getAudioCourseItem(audioCourseItem.id)
            }
        }

    @Test
    fun `GIVEN audioCourseItemId WHEN getAudioCourseItem THEN return null`() =
        runTest {
            val audioCourseItemId = "1"
            coEvery {
                localAudioCourseDataSource.getAudioCourseItem(audioCourseItemId)
            } returns null

            val result = repository.getAudioCourseItem(audioCourseItemId)

            result.isLeft() shouldBe true
            coVerifyOnce {
                localAudioCourseDataSource.getAudioCourseItem(audioCourseItemId)
            }
        }

    @Test
    fun `GIVEN existing audioCourseId WHEN getAudioCourseItemById THEN returns PlaylistAudioItem`() =
        runTest {
            val audioCourse = buildAudioCourse()
            val audioCourseItem = buildAudioCourseItem()
            val position = 1
            val playlistAudioItem = audioCourseItem.toPlaylistAudioItem(audioCourse, position)
            coEvery { localAudioCourseDataSource.getAudioCourseById(audioCourse.id) } returns flowOf(audioCourse)

            val result = repository.getAudioCourseItemById(audioCourseItem.id)

            result shouldBeEqualTo right(playlistAudioItem)
            coVerifyOnce {
                localAudioCourseDataSource.getAudioCourseById(audioCourse.id)
            }
        }

    @Test
    fun `GIVEN nonExisting audioCourseId WHEN getAudioCourseItemById THEN returns error`() =
        runTest {
            val audioCourseId = "1"
            val audioCourseItemId = "1-1"
            coEvery { localAudioCourseDataSource.getAudioCourseById(audioCourseId) } returns flowOf(null)

            val result = repository.getAudioCourseItemById(audioCourseItemId)

            result shouldBeInstanceOf Either.Left::class.java
            coVerifyOnce {
                localAudioCourseDataSource.getAudioCourseById(audioCourseId)
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
                localAudioCourseDataSource.getAllFavoriteAudioCoursesItems()
            } returns audioCourseItems

            val result = repository.getAllFavoriteAudioCoursesItems()

            result.isRight() shouldBe true
            result.getOrNull() shouldBeEqualTo audioCourseItems.map { it.toAudioCourseItem() }
            coVerifyOnce {
                localAudioCourseDataSource.getAllFavoriteAudioCoursesItems()
            }
        }

    @Test
    fun `GIVEN repository WHEN updateMarkedAsFavorite THEN local and cloud dataSources are called`() =
        runTest {
            val audioCourseItem = buildAudioCourseItemDbModel()
            coJustRun {
                localAudioCourseDataSource.updateMarkedAsFavorite(audioCourseItem.id, true)
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
                localAudioCourseDataSource.updateMarkedAsFavorite(audioCourseItem.id, true)
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
