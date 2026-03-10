package soy.gabimoreno.domain.usecase

import android.content.Context
import kotlinx.coroutines.flow.first
import soy.gabimoreno.framework.datastore.dataStoreGetLastAudioListenedId
import javax.inject.Inject

class GetLastAudioListenedIdUseCase
    @Inject
    constructor(
        private val context: Context,
    ) {
        suspend operator fun invoke(): String = context.dataStoreGetLastAudioListenedId().first()
    }
