package soy.gabimoreno.domain.usecase

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.intPreferencesKey
import io.mockk.coJustRun
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.unmockkStatic
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.amshove.kluent.shouldBeEqualTo
import org.junit.After
import org.junit.Before
import org.junit.Test
import soy.gabimoreno.framework.datastore.dataStore
import soy.gabimoreno.framework.datastore.setDataStoreLastSurveyId

class GetLastSurveyIdUseCaseTest {
    private val context: Context = mockk()
    private val dataStore: DataStore<Preferences> = mockk()
    private val preferences: Preferences = mockk()

    private lateinit var useCase: GetLastSurveyIdUseCase
    private val key = intPreferencesKey(LAST_SURVEY_ID)

    @Before
    fun setUp() {
        mockkStatic("soy.gabimoreno.framework.datastore.DataStoreKt")
        mockkStatic("soy.gabimoreno.framework.datastore.DataStoreSurveyKt")
        every { context.dataStore } returns dataStore
        every { dataStore.data } returns flowOf(preferences)
        coJustRun { context.setDataStoreLastSurveyId(any()) }
        useCase = GetLastSurveyIdUseCase(context)
    }

    @After
    fun tearDown() {
        unmockkStatic("soy.gabimoreno.framework.datastore.DataStoreKt")
        unmockkStatic("soy.gabimoreno.framework.datastore.DataStoreSurveyKt")
    }

    @Test
    fun `GIVEN no preference stored WHEN invoke THEN uses default value`() =
        runTest {
            every { preferences[key] } returns DEFAULT_FLAG_SURVEY_ID

            val result = useCase()

            result.first() shouldBeEqualTo DEFAULT_FLAG_SURVEY_ID
        }

    @Test
    fun `GIVEN preference stored WHEN invoke THEN returns value`() =
        runTest {
            val surveyId = 1
            every { preferences[key] } returns surveyId

            val result = useCase()

            result.first() shouldBeEqualTo surveyId
        }
}

private const val DEFAULT_FLAG_SURVEY_ID = 0
private const val LAST_SURVEY_ID = "LAST_SURVEY_ID"
