package soy.gabimoreno.domain.usecase

import android.content.Context
import soy.gabimoreno.framework.datastore.setDataStoreLastAudioListenedId
import javax.inject.Inject

class SetLastAudioListenedIdUseCase
    @Inject
    constructor(
        private val context: Context,
    ) {
        suspend operator fun invoke(audioId: String) =
            context.setDataStoreLastAudioListenedId(audioId)
    }
