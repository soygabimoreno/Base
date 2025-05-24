package soy.gabimoreno.domain.usecase

import android.content.Context
import io.mockk.coJustRun
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.unmockkStatic
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import soy.gabimoreno.core.testing.coVerifyOnce
import soy.gabimoreno.framework.datastore.InAppReviewPrefs
import soy.gabimoreno.framework.datastore.setDataStoreShouldIShowInAppReview

class SetShouldIShowInAppReviewUseCaseTest {

    private val context: Context = mockk()
    private lateinit var useCase: SetShouldIShowInAppReviewUseCase

    @Before
    fun setUp() {
        mockkStatic("soy.gabimoreno.framework.datastore.DataStoreShouldIShowInAppReviewKt")
        useCase = SetShouldIShowInAppReviewUseCase(context)
    }

    @After
    fun tearDown() {
        unmockkStatic("soy.gabimoreno.framework.datastore.DataStoreShouldIShowInAppReviewKt")
    }

    @Test
    fun `GIVEN counter value WHEN invoke THEN setDataStoreShouldIShowInAppReview is called`() =
        runTest {
            val counterValue = 3
            coJustRun { context.setDataStoreShouldIShowInAppReview(any()) }

            useCase(counterValue)

            coVerifyOnce {
                context.setDataStoreShouldIShowInAppReview(counterValue)
            }
        }

    @Test
    fun `GIVEN USER_DO_NOT_SHOW_AGAIN value WHEN invoke THEN setDataStoreShouldIShowInAppReview is called`() =
        runTest {
            val counterValue = InAppReviewPrefs.USER_DO_NOT_SHOW_AGAIN
            coJustRun { context.setDataStoreShouldIShowInAppReview(any()) }

            useCase(counterValue)

            coVerifyOnce {
                context.setDataStoreShouldIShowInAppReview(-1)
            }
        }
}
