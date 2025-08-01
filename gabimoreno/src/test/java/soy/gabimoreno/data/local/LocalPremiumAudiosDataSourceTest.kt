package soy.gabimoreno.data.local

import androidx.paging.PagingSource
import io.mockk.coEvery
import io.mockk.coJustRun
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.amshove.kluent.shouldBe
import org.amshove.kluent.shouldBeEqualTo
import org.junit.Before
import org.junit.Test
import soy.gabimoreno.core.testing.coVerifyOnce
import soy.gabimoreno.core.testing.verifyOnce
import soy.gabimoreno.data.local.mapper.toPremiumAudioDbModel
import soy.gabimoreno.data.local.premiumaudio.LocalPremiumAudiosDataSource
import soy.gabimoreno.data.local.premiumaudio.PremiumAudioDbModel
import soy.gabimoreno.data.local.premiumaudio.PremiumAudioDbModelDao
import soy.gabimoreno.fake.buildPremiumAudioDbModel
import soy.gabimoreno.fake.buildPremiumAudios

@ExperimentalCoroutinesApi
class LocalPremiumAudiosDataSourceTest {
    private val premiumAudioDbModelDao: PremiumAudioDbModelDao = mockk()
    private val gabiMorenoDatabase: ApplicationDatabase = mockk()
    private val testDispatcher: TestDispatcher = UnconfinedTestDispatcher()
    private lateinit var datasource: LocalPremiumAudiosDataSource

    @Before
    fun setUp() {
        every { gabiMorenoDatabase.premiumAudioDbModelDao() } returns premiumAudioDbModelDao
        datasource =
            LocalPremiumAudiosDataSource(
                gabiMorenoDatabase,
                testDispatcher,
            )
    }

    @Test
    fun `GIVEN database empty WHEN isEmpty THEN return true`() =
        runTest {
            every { premiumAudioDbModelDao.count() } returns EMPTY_DATABASE_COUNT

            val result = datasource.isEmpty()

            result shouldBe true
        }

    @Test
    fun `GIVEN database filled WHEN isEmpty THEN return false`() =
        runTest {
            every { premiumAudioDbModelDao.count() } returns FILLED_DATABASE_COUNT

            val result = datasource.isEmpty()

            result shouldBe false
        }

    @Test
    fun `WHEN savePremiumAudios THEN they are inserted into the database`() =
        runTest {
            val premiumAudios = buildPremiumAudios()
            val premiumAudioDbModels = premiumAudios.map { it.toPremiumAudioDbModel() }
            every { premiumAudioDbModelDao.upsertPremiumAudioDbModels(premiumAudioDbModels) } returns Unit

            datasource.savePremiumAudios(premiumAudios)

            verifyOnce {
                premiumAudioDbModelDao.upsertPremiumAudioDbModels(premiumAudioDbModels)
            }
        }

    @Test
    fun `WHEN getPremiumAudios THEN they are get from the database`() =
        runTest {
            val premiumAudios = buildPremiumAudios()
            val premiumAudioDbModels = premiumAudios.map { it.toPremiumAudioDbModel() }
            every { premiumAudioDbModelDao.getPremiumAudioDbModels() } returns premiumAudioDbModels

            val result = datasource.getPremiumAudios()

            result shouldBeEqualTo premiumAudios
        }

    @Test
    fun `WHEN getPremiumAudiosPagingSource THEN paging source is get from the database`() =
        runTest {
            val pagingSource: PagingSource<Int, PremiumAudioDbModel> = mockk()
            every { premiumAudioDbModelDao.getPremiumAudioDbModelsPagingSource() } returns pagingSource

            val result = datasource.getPremiumAudiosPagingSource()

            result shouldBeEqualTo pagingSource
            verifyOnce {
                premiumAudioDbModelDao.getPremiumAudioDbModelsPagingSource()
            }
        }

    @Test
    fun `WHEN getPremiumAudioById THEN they are get from the database`() =
        runTest {
            val premiumAudio = buildPremiumAudios().first()
            val premiumAudioDbModel = premiumAudio.toPremiumAudioDbModel()
            every { premiumAudioDbModelDao.getPremiumAudioDbModelById(premiumAudio.id) } returns premiumAudioDbModel

            val result = datasource.getPremiumAudioById(premiumAudio.id)

            result shouldBeEqualTo premiumAudio
        }

    @Test
    fun `GIVEN datasource WHEN markAllPremiumAudiosAsUnlistened THEN dao is called`() =
        runTest {
            every {
                premiumAudioDbModelDao.markAllPremiumAudiosAsUnlistened()
            } returns Unit

            datasource.markAllPremiumAudiosAsUnlistened()

            verifyOnce {
                premiumAudioDbModelDao.markAllPremiumAudiosAsUnlistened()
            }
        }

    @Test
    fun `GIVEN datasource WHEN getAllFavoritePremiumAudios THEN get all favorite premium audios`() =
        runTest {
            val premiumAudios =
                listOf(
                    buildPremiumAudioDbModel(markedAsFavorite = true),
                    buildPremiumAudioDbModel(id = "2", markedAsFavorite = true),
                )

            coEvery { datasource.getAllFavoritePremiumAudios() } returns premiumAudios

            val result = datasource.getAllFavoritePremiumAudios()

            result shouldBeEqualTo premiumAudios
            coVerifyOnce {
                premiumAudioDbModelDao.getAllFavoriteAudioPremiumAudioDbModels()
            }
        }

    @Test
    fun `GIVEN premiumAudioId WHEN updateMarkedAsFavorite THEN mark premium audio as favorite`() =
        runTest {
            val premiumAudioId = "1"
            val markedAsFavorite = true
            coJustRun {
                premiumAudioDbModelDao.updateMarkedAsFavorite(premiumAudioId, markedAsFavorite)
            }

            datasource.updateMarkedAsFavorite(premiumAudioId, markedAsFavorite)
            coVerifyOnce {
                premiumAudioDbModelDao.updateMarkedAsFavorite(premiumAudioId, markedAsFavorite)
            }
        }

    @Test
    fun `WHEN reset THEN the database is empty`() =
        runTest {
            // TODO: Maybe should I use the `gabimoreno-db-test` for this purpose?
        }
}

private const val EMPTY_DATABASE_COUNT = 0
private const val FILLED_DATABASE_COUNT = 1234
