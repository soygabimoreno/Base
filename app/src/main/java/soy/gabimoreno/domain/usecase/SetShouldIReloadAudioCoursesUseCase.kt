package soy.gabimoreno.domain.usecase

import android.content.Context
import soy.gabimoreno.framework.datastore.setDataStoreShouldIReloadAudiosCourses
import javax.inject.Inject

class SetShouldIReloadAudioCoursesUseCase
    @Inject
    constructor(
        private val context: Context,
    ) {
        suspend operator fun invoke(shouldReload: Boolean) =
            context.setDataStoreShouldIReloadAudiosCourses(shouldReload)
    }
