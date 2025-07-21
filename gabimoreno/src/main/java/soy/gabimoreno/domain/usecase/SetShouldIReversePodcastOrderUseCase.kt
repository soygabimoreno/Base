package soy.gabimoreno.domain.usecase

import android.content.Context
import soy.gabimoreno.framework.datastore.setDataStoreShouldIReversePodcastOrder
import javax.inject.Inject

class SetShouldIReversePodcastOrderUseCase
    @Inject
    constructor(
        private val context: Context,
    ) {
        suspend operator fun invoke(reversePodcastOrder: Boolean) =
            context.setDataStoreShouldIReversePodcastOrder(reversePodcastOrder)
    }
