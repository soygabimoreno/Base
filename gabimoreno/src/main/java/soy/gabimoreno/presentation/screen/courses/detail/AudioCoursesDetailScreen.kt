package soy.gabimoreno.presentation.screen.courses.detail

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import soy.gabimoreno.presentation.screen.ViewModelProvider

@Composable
fun AudioCoursesDetailScreenRoot(
    audioCourseId: String,
    onBackPressed: () -> Unit,
) {
    val coursesDetailViewModel = ViewModelProvider.audioCoursesDetailViewModel

    LaunchedEffect(Unit) {
        coursesDetailViewModel.events.collect { event ->
            when (event) {
                is AudioCoursesDetailEvent.Error -> {

                }

                is AudioCoursesDetailEvent.ShowPlayer -> {

                }
            }
        }
    }
}

@Composable
fun AudioCoursesDetailScreen(
    state: AudioCoursesDetailState,
    onAction: (AudioCoursesDetailAction) -> Unit,
) {
    Text("Detail")
}
