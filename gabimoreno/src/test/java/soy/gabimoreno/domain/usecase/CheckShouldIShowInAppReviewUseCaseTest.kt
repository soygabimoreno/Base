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
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.amshove.kluent.shouldBeEqualTo
import org.junit.After
import org.junit.Before
import org.junit.Test
import soy.gabimoreno.core.testing.coVerifyOnce
import soy.gabimoreno.framework.datastore.InAppReviewPrefs
import soy.gabimoreno.framework.datastore.dataStore
import soy.gabimoreno.framework.datastore.setDataStoreShouldIShowInAppReview

class CheckShouldIShowInAppReviewUseCaseTest {

    private val context: Context = mockk()
    private val dataStore: DataStore<Preferences> = mockk()
    private val preferences: Preferences = mockk()
    private lateinit var useCase: CheckShouldIShowInAppReviewUseCase
    private val key = intPreferencesKey(SHOULD_I_SHOW_IN_APP_REVIEW)

    @Before
    fun setUp() {
        mockkStatic("soy.gabimoreno.framework.datastore.DataStoreKt")
        mockkStatic("soy.gabimoreno.framework.datastore.DataStoreShouldIShowInAppReviewKt")
        every { context.dataStore } returns dataStore
        every { dataStore.data } returns flowOf(preferences)
        coJustRun { context.setDataStoreShouldIShowInAppReview(any()) }
        useCase = CheckShouldIShowInAppReviewUseCase(context)
    }

    @After
    fun tearDown() {
        unmockkStatic("soy.gabimoreno.framework.datastore.DataStoreKt")
        unmockkStatic("soy.gabimoreno.framework.datastore.DataStoreShouldIShowInAppReviewKt")
    }

    @Test
    fun `GIVEN counter is USER_DO_NOT_SHOW_AGAIN WHEN invoke THEN returns false`() = runTest {
        every { preferences[key] } returns InAppReviewPrefs.USER_DO_NOT_SHOW_AGAIN

        val result = useCase()

        result shouldBeEqualTo false
    }

    @Test
    fun `GIVEN counter is REPEAT_SHOW_IN_APP_REVIEW WHEN invoke THEN returns true and resets counter`() =
        runTest {
            val counterValue = 3
            every { preferences[key] } returns counterValue

            val result = useCase()

            result shouldBeEqualTo true
            coVerifyOnce {
                context.setDataStoreShouldIShowInAppReview(0)
            }
        }

    @Test
    fun `GIVEN counter is 1 WHEN invoke THEN returns true and increments counter`() = runTest {
        val counterValue = 1
        val incrementedCounterValue = 2
        every { preferences[key] } returns counterValue

        val result = useCase()

        result shouldBeEqualTo true
        coVerifyOnce {
            context.setDataStoreShouldIShowInAppReview(incrementedCounterValue)
        }
    }

    @Test
    fun `GIVEN no preference stored WHEN invoke THEN uses default value and returns true`() =
        runTest {
            val expectedValue = 3
            every { preferences[key] } returns null

            val result = useCase()

            result shouldBeEqualTo true
            coVerifyOnce {
                context.setDataStoreShouldIShowInAppReview(expectedValue)
            }
        }
}

private const val SHOULD_I_SHOW_IN_APP_REVIEW = "SHOULD_I_SHOW_IN_APP_REVIEW"

