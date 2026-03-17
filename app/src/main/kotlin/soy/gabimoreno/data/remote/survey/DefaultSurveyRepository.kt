package soy.gabimoreno.data.remote.survey

import arrow.core.Either
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.toObjects
import kotlinx.coroutines.tasks.await
import soy.gabimoreno.data.remote.ext.runCatchingEither
import soy.gabimoreno.data.remote.survey.mapper.toDomain
import soy.gabimoreno.data.remote.survey.model.SurveyApiModel
import soy.gabimoreno.domain.model.survey.Survey
import soy.gabimoreno.domain.repository.survey.SurveyRepository
import javax.inject.Inject

class DefaultSurveyRepository
    @Inject
    constructor(
        private val firestore: FirebaseFirestore,
    ) : SurveyRepository {
        override suspend fun getLastedSurvey(): Either<Throwable, Survey?> =
            runCatchingEither {
                firestore
                    .collection(COLLECTION_NAME)
                    .orderBy(ORDER_BY_FIELD, Query.Direction.DESCENDING)
                    .limit(DOCUMENTS_LIMIT)
                    .get()
                    .await()
                    .toObjects<SurveyApiModel>()
                    .first()
                    .toDomain()
            }
    }

private const val COLLECTION_NAME = "surveys"
private const val DOCUMENTS_LIMIT = 1L
private const val ORDER_BY_FIELD = "id"
