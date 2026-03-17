package soy.gabimoreno.domain.usecase

import android.content.Context
import kotlinx.coroutines.flow.Flow
import soy.gabimoreno.framework.datastore.dataStoreGetLastSurveyId
import javax.inject.Inject

class GetLastSurveyIdUseCase
    @Inject
    constructor(
        private val context: Context,
    ) {
        operator fun invoke(): Flow<Int> = context.dataStoreGetLastSurveyId()
    }
