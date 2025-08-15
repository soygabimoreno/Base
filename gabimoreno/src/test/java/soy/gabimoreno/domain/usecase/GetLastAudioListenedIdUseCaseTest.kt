package soy.gabimoreno.domain.usecase

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.stringPreferencesKey
import io.mockk.coJustRun
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.unmockkStatic
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.amshove.kluent.shouldBeEqualTo
import org.junit.After
import org.junit.Before
import org.junit.Test
import soy.gabimoreno.framework.datastore.dataStore
import soy.gabimoreno.framework.datastore.setDataStoreLastAudioListenedId

class GetLastAudioListenedIdUseCaseTest {
    private val context: Context = mockk()
    private val dataStore: DataStore<Preferences> = mockk()
    private val preferences: Preferences = mockk()

    private lateinit var useCase: GetLastAudioListenedIdUseCase
    private val key = stringPreferencesKey(LAST_AUDIO_LISTENED_ID)

    @Before
    fun setUp() {
        mockkStatic("soy.gabimoreno.framework.datastore.DataStoreKt")
        mockkStatic("soy.gabimoreno.framework.datastore.DataStoreLastAudioListenedKt")
        every { context.dataStore } returns dataStore
        every { dataStore.data } returns flowOf(preferences)
        coJustRun { context.setDataStoreLastAudioListenedId(any()) }
        useCase = GetLastAudioListenedIdUseCase(context)
    }

    @After
    fun tearDown() {
        unmockkStatic("soy.gabimoreno.framework.datastore.DataStoreKt")
        unmockkStatic("soy.gabimoreno.framework.datastore.DataStoreLastAudioListenedKt")
    }

    @Test
    fun `GIVEN no preference stored WHEN invoke THEN uses default value`() =
        runTest {
            every { preferences[key] } returns DEFAULT_LAST_AUDIO_LISTENED_ID

            val result = useCase()

            result shouldBeEqualTo DEFAULT_LAST_AUDIO_LISTENED_ID
        }

    @Test
    fun `GIVEN preference stored WHEN invoke THEN returns value`() =
        runTest {
            val audioId = "1"
            every { preferences[key] } returns audioId

            val result = useCase()

            result shouldBeEqualTo audioId
        }
}

private const val DEFAULT_LAST_AUDIO_LISTENED_ID = "0"
private const val LAST_AUDIO_LISTENED_ID = "LAST_AUDIO_LISTENED_ID"
