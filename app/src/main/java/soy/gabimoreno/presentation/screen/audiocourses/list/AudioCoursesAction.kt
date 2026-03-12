package soy.gabimoreno.presentation.screen.audiocourses.list

sealed interface AudioCoursesAction {
    data class OnItemClicked(
        val courseId: String,
    ) : AudioCoursesAction

    data object OnRefreshContent : AudioCoursesAction
    data object OnPlaylistClicked : AudioCoursesAction
}
