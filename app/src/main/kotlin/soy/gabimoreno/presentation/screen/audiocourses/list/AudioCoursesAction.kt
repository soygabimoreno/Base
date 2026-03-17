package soy.gabimoreno.presentation.screen.audiocourses.list

sealed interface AudioCoursesAction {
    data class OnItemClick(
        val courseId: String,
    ) : AudioCoursesAction

    data object OnRefreshContent : AudioCoursesAction
    data object OnPlaylistClick : AudioCoursesAction
}
