package soy.gabimoreno.data.remote.survey

import arrow.core.Either
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.toObjects
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.unmockkAll
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.test.runTest
import org.amshove.kluent.shouldBeEqualTo
import org.amshove.kluent.shouldBeInstanceOf
import org.junit.After
import org.junit.Before
import org.junit.Test
import soy.gabimoreno.data.remote.survey.mapper.toDomain
import soy.gabimoreno.data.remote.survey.model.SurveyApiModel
import soy.gabimoreno.ext.left
import soy.gabimoreno.ext.right
import soy.gabimoreno.fake.buildSurveyApiModel

class DefaultSurveyRepositoryTest {
    private val firestore: FirebaseFirestore = mockk()
    private val collection: CollectionReference = mockk()
    private val task: Task<QuerySnapshot> = mockk()
    private val querySnapshot: QuerySnapshot = mockk()

    private lateinit var database: DefaultSurveyRepository

    @Before
    fun setUp() {
        mockkStatic("kotlinx.coroutines.tasks.TasksKt")
        every { firestore.collection(any()) } returns collection
        database = DefaultSurveyRepository(firestore)
    }

    @After
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun `GIVEN Firestore returns a document WHEN getLastedSurvey THEN should return Survey mapped correctly`() =
        runTest {
            val apiModel = buildSurveyApiModel()
            val expectedSurvey = apiModel.toDomain()
            every {
                collection.orderBy(ORDER_BY_FIELD, Query.Direction.DESCENDING)
            } returns collection
            every { collection.limit(DOCUMENTS_LIMIT) } returns collection
            coEvery { collection.get() } returns task
            coEvery { task.await() } returns querySnapshot
            every { querySnapshot.toObjects<SurveyApiModel>() } returns listOf(apiModel)

            val result = database.getLastedSurvey()

            result shouldBeEqualTo right(expectedSurvey)
        }

    @Test
    fun `GIVEN Firestore throws WHEN getLastedSurvey THEN should return Left with exception`() =
        runTest {
            val exception = RuntimeException("Firestore failed")
            every {
                collection.orderBy(ORDER_BY_FIELD, Query.Direction.DESCENDING)
            } returns collection
            every { collection.limit(DOCUMENTS_LIMIT) } returns collection
            coEvery { collection.get() } returns task
            coEvery { task.await() } throws exception

            val result = database.getLastedSurvey()

            result shouldBeEqualTo left(exception)
        }

    @Test
    fun `GIVEN Firestore returns empty list WHEN getLastedSurvey THEN should return Left with NoSuchElementException`() =
        runTest {
            every {
                collection.orderBy(ORDER_BY_FIELD, Query.Direction.DESCENDING)
            } returns collection
            every { collection.limit(DOCUMENTS_LIMIT) } returns collection
            coEvery { collection.get() } returns task
            coEvery { task.await() } returns querySnapshot
            every { querySnapshot.toObjects<SurveyApiModel>() } returns emptyList()

            val result = database.getLastedSurvey()

            result shouldBeInstanceOf Either.Left::class
            (result as Either.Left).value shouldBeInstanceOf NoSuchElementException::class
        }
}

private const val DOCUMENTS_LIMIT = 1L
private const val ORDER_BY_FIELD = "id"
