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
import soy.gabimoreno.framework.datastore.setDataStoreLastSurveyId

class SetLastSurveyIdUseCaseTest {
    private val context: Context = mockk()
    private val dataStore: DataStore<Preferences> = mockk()
    private val preferences: Preferences = mockk()

    private lateinit var useCase: SetLastSurveyIdUseCase

    @Before
    fun setUp() {
        mockkStatic("soy.gabimoreno.framework.datastore.DataStoreKt")
        mockkStatic("soy.gabimoreno.framework.datastore.DataStoreSurveyKt")
        every { context.dataStore } returns dataStore
        every { dataStore.data } returns flowOf(preferences)
        coJustRun { context.setDataStoreLastSurveyId(any()) }
        useCase = SetLastSurveyIdUseCase(context)
    }

    @After
    fun tearDown() {
        unmockkStatic("soy.gabimoreno.framework.datastore.DataStoreKt")
        unmockkStatic("soy.gabimoreno.framework.datastore.DataStoreSurveyKt")
    }

    @Test
    fun `GIVEN surveyId WHEN invoke THEN set preference`() =
        runTest {
            val surveyId = 1

            useCase(surveyId)

            coVerifyOnce {
                context.setDataStoreLastSurveyId(surveyId)
            }
        }
}
