package soy.gabimoreno.domain.usecase

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import io.mockk.coJustRun
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.unmockkStatic
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import soy.gabimoreno.core.testing.coVerifyOnce
import soy.gabimoreno.framework.datastore.dataStore
import soy.gabimoreno.framework.datastore.setDataStoreLastAudioListenedId

class SetLastAudioListenedIdUseCaseTest {
    private val context: Context = mockk()
    private val dataStore: DataStore<Preferences> = mockk()
    private val preferences: Preferences = mockk()

    private lateinit var useCase: SetLastAudioListenedIdUseCase

    @Before
    fun setUp() {
        mockkStatic("soy.gabimoreno.framework.datastore.DataStoreKt")
        mockkStatic("soy.gabimoreno.framework.datastore.DataStoreLastAudioListenedKt")
        every { context.dataStore } returns dataStore
        every { dataStore.data } returns flowOf(preferences)
        coJustRun { context.setDataStoreLastAudioListenedId(any()) }
        useCase = SetLastAudioListenedIdUseCase(context)
    }

    @After
    fun tearDown() {
        unmockkStatic("soy.gabimoreno.framework.datastore.DataStoreKt")
        unmockkStatic("soy.gabimoreno.framework.datastore.DataStoreLastAudioListenedKt")
    }

    @Test
    fun `GIVEN audioId WHEN invoke THEN set preference`() =
        runTest {
            val audioId = "1"

            useCase(audioId)

            coVerifyOnce {
                context.setDataStoreLastAudioListenedId(audioId)
            }
        }
}
