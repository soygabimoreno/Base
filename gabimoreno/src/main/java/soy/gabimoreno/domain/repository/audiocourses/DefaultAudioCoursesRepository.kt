package soy.gabimoreno.domain.repository.audiocourses

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import soy.gabimoreno.data.local.audiocourse.LocalAudioCoursesDataSource
import soy.gabimoreno.data.remote.datasource.audiocourses.RemoteAudioCoursesDataSource
import soy.gabimoreno.data.remote.model.Category
import soy.gabimoreno.domain.model.content.AudioCourse
import soy.gabimoreno.domain.repository.premiumaudios.TWELVE_HOURS_IN_MILLIS
import soy.gabimoreno.domain.usecase.RefreshPremiumAudiosFromRemoteUseCase
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DefaultAudioCoursesRepository @Inject constructor(
    private val localAudioCoursesDataSource: LocalAudioCoursesDataSource,
    private val remoteAudioCoursesDataSource: RemoteAudioCoursesDataSource,
    private val refreshPremiumAudiosFromRemoteUseCase: RefreshPremiumAudiosFromRemoteUseCase,
) : AudioCoursesRepository {
    override suspend fun getCourses(
        categories: List<Category>
    ): Either<Throwable, List<AudioCourse>> {
        if (refreshPremiumAudiosFromRemoteUseCase(
                currentTimeInMillis = System.currentTimeMillis(),
                timeToRefreshInMillis = TWELVE_HOURS_IN_MILLIS
            ) || localAudioCoursesDataSource.isEmpty()
        ) {
            remoteAudioCoursesDataSource.getAudioCourses(categories)
                .fold(
                    ifLeft = {
                        return it.left()
                    },
                    ifRight = { audioCourses ->
                        localAudioCoursesDataSource.saveAudioCourses(audioCourses)
                    }
                )
        }
        return localAudioCoursesDataSource.getAudioCourses().right()
    }

    override suspend fun getCourseById(idCourse: String): Either<Throwable, AudioCourse> {
        return localAudioCoursesDataSource.getAudioCourseById(idCourse)?.right()
            ?: Throwable("AudioCourse not found").left()
    }

    override suspend fun reset() {
        localAudioCoursesDataSource.reset()
    }
}
