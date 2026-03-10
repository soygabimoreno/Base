package soy.gabimoreno.domain.usecase

import android.content.Context
import soy.gabimoreno.framework.datastore.setDataStoreLastSurveyId
import javax.inject.Inject

class SetLastSurveyIdUseCase
    @Inject
    constructor(
        private val context: Context,
    ) {
        suspend operator fun invoke(surveyId: Int) = context.setDataStoreLastSurveyId(surveyId)
    }
