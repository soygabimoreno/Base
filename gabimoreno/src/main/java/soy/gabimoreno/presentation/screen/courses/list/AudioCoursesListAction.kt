package soy.gabimoreno.presentation.screen.courses.list

sealed interface AudioCoursesListAction {
    data class OnItemClicked(val courseId: String) : AudioCoursesListAction
    data object OnRefreshContent : AudioCoursesListAction
}
