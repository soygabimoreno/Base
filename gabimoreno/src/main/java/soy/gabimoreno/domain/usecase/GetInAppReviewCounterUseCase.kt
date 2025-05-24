package soy.gabimoreno.domain.usecase

import android.content.Context
import kotlinx.coroutines.flow.Flow
import soy.gabimoreno.framework.datastore.dataStoreInAppReviewCounter
import javax.inject.Inject

class GetInAppReviewCounterUseCase @Inject constructor(
    private val context: Context,
) {
    operator fun invoke(): Flow<Int> {
        return context.dataStoreInAppReviewCounter
    }
}
