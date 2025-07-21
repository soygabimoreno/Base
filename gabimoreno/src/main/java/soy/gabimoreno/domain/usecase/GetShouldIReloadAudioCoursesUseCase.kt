package soy.gabimoreno.domain.usecase

import android.content.Context
import kotlinx.coroutines.flow.Flow
import soy.gabimoreno.framework.datastore.dataStoreShouldIReloadAudiosCourses
import javax.inject.Inject

class GetShouldIReloadAudioCoursesUseCase
    @Inject
    constructor(
        private val context: Context,
    ) {
        operator fun invoke(): Flow<Boolean> = context.dataStoreShouldIReloadAudiosCourses()
    }
