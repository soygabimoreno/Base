@file:OptIn(ExperimentalMaterialApi::class)

package soy.gabimoreno.presentation.screen.courses.list

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import soy.gabimoreno.R
import soy.gabimoreno.framework.toast
import soy.gabimoreno.presentation.screen.ViewModelProvider
import soy.gabimoreno.presentation.screen.courses.list.view.ItemListCourse
import soy.gabimoreno.presentation.theme.Spacing


@Composable
fun AudioCoursesListScreenRoot(
    onItemClicked: (courseId: String) -> Unit,
) {
    val context = LocalContext.current
    val coursesListViewModel = ViewModelProvider.audioCoursesListViewModel
    LaunchedEffect(Unit) {
        coursesListViewModel.events.collect { event ->
            when (event) {
                is AudioCoursesListEvent.Error -> {
                    context.toast(context.getString(R.string.unexpected_error))
                }

                is AudioCoursesListEvent.ShowDetail -> {
                    onItemClicked(event.audioCourseId)
                }

                AudioCoursesListEvent.ShowTokenExpiredError -> {
                    context.toast(context.getString(R.string.premium_error_token_expired))
                }
            }
        }
    }

    CoursesListScreenScreen(
        state = coursesListViewModel.state,
        onAction = { action ->
            when (action) {
                is AudioCoursesListAction.OnItemClicked -> onItemClicked(action.courseId)
                else -> Unit
            }
            coursesListViewModel.onAction(action)
        }
    )
}

@Composable
private fun CoursesListScreenScreen(
    state: AudioCoursesListState,
    onAction: (AudioCoursesListAction) -> Unit,
) {
    val pullRefreshState = rememberPullRefreshState(
        state.isRefreshing,
        { onAction(AudioCoursesListAction.OnRefreshContent) })

    Column(
        modifier = Modifier
            .fillMaxSize()
            .windowInsetsPadding(WindowInsets.safeDrawing.only(WindowInsetsSides.Top))
    ) {
        Text(
            text = stringResource(id = R.string.nav_item_courses).uppercase(),
            style = MaterialTheme.typography.h5,
            modifier = Modifier
                .padding(top = Spacing.s16, start = Spacing.s16)
        )
        Spacer(
            modifier = Modifier
                .padding(top = Spacing.s8)
        )
        Box(
            Modifier
                .fillMaxSize()
                .pullRefresh(pullRefreshState)
        ) {
            if (state.isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier
                        .align(Alignment.Center)
                )
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = Spacing.s8),
                    verticalArrangement = Arrangement.spacedBy(Spacing.s16),
                ) {
                    items(
                        count = state.audiocourses.size,
                        key = { state.audiocourses[it].id }
                    ) { index ->
                        ItemListCourse(
                            audioCourse = state.audiocourses[index],
                            onItemClick = { onAction(AudioCoursesListAction.OnItemClicked(state.audiocourses[index].id)) }
                        )
                        if (state.audiocourses.size == index + 1)
                            Spacer(modifier = Modifier.padding(bottom = Spacing.s16))
                    }
                }
            }

            PullRefreshIndicator(
                refreshing = state.isRefreshing,
                state = pullRefreshState,
                modifier = Modifier.align(Alignment.TopCenter)
            )
        }
    }
}
