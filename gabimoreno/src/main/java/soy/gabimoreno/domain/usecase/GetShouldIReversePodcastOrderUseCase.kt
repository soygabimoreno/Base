package soy.gabimoreno.domain.usecase

import android.content.Context
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import soy.gabimoreno.framework.datastore.dataStoreShouldIReversePodcastOrder
import javax.inject.Inject

class GetShouldIReversePodcastOrderUseCase @Inject constructor(
    private val context: Context,
) {
    suspend operator fun invoke(): Boolean {
        return context.dataStoreShouldIReversePodcastOrder().first()
    }
}
