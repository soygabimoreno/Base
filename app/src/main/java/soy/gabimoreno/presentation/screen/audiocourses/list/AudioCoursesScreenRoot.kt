@file:OptIn(ExperimentalMaterialApi::class)

package soy.gabimoreno.presentation.screen.audiocourses.list

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LibraryMusic
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import soy.gabimoreno.R
import soy.gabimoreno.framework.toast
import soy.gabimoreno.presentation.screen.ViewModelProvider
import soy.gabimoreno.presentation.screen.audiocourses.list.view.ItemListAudioCourse
import soy.gabimoreno.presentation.theme.Percent
import soy.gabimoreno.presentation.theme.Spacing

@Composable
fun AudioCoursesScreenRoot(
    onItemClick: (courseId: String) -> Unit,
    onPlaylistClick: () -> Unit,
) {
    val context = LocalContext.current
    val unexpectedErrorMessage = stringResource(R.string.unexpected_error)
    val premiumErrorTokenExpiredMessage = stringResource(R.string.premium_error_token_expired)
    val coursesListViewModel = ViewModelProvider.audioCoursesViewModel
    val state by coursesListViewModel.state.collectAsStateWithLifecycle()
    LaunchedEffect(Unit) {
        coursesListViewModel.events.collect { event ->
            when (event) {
                is AudioCoursesEvent.Error -> {
                    context.toast(unexpectedErrorMessage)
                }

                is AudioCoursesEvent.ShowDetail -> {
                    onItemClick(event.audioCourseId)
                }

                AudioCoursesEvent.ShowTokenExpiredError -> {
                    context.toast(premiumErrorTokenExpiredMessage)
                }
            }
        }
    }

    AudioCoursesScreen(
        state = state,
        onAction = { action ->
            when (action) {
                is AudioCoursesAction.OnItemClick -> onItemClick(action.courseId)
                AudioCoursesAction.OnPlaylistClick -> onPlaylistClick()
                else -> Unit
            }
            coursesListViewModel.onAction(action)
        },
    )
}

@Composable
private fun AudioCoursesScreen(
    state: AudioCoursesState,
    onAction: (AudioCoursesAction) -> Unit,
) {
    val pullRefreshState =
        rememberPullRefreshState(
            state.isRefreshing,
            { onAction(AudioCoursesAction.OnRefreshContent) },
        )

    Column(
        modifier =
            Modifier
                .fillMaxSize()
                .windowInsetsPadding(WindowInsets.safeDrawing.only(WindowInsetsSides.Top)),
    ) {
        Row(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(Spacing.s16),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = stringResource(id = R.string.nav_item_audio_courses).uppercase(),
                style = MaterialTheme.typography.h5,
                modifier =
                    Modifier
                        .weight(Percent.EIGHTY),
            )
            Icon(
                imageVector = Icons.Default.LibraryMusic,
                contentDescription = "Playlist",
                modifier =
                    Modifier
                        .clickable {
                            onAction(AudioCoursesAction.OnPlaylistClick)
                        },
            )
        }
        Spacer(
            modifier =
                Modifier
                    .padding(top = Spacing.s8),
        )
        Box(
            Modifier
                .fillMaxSize()
                .pullRefresh(pullRefreshState),
        ) {
            if (state.isLoading) {
                CircularProgressIndicator(
                    modifier =
                        Modifier
                            .align(Alignment.Center),
                )
            } else {
                LazyColumn(
                    modifier =
                        Modifier
                            .fillMaxSize()
                            .padding(horizontal = Spacing.s8),
                    verticalArrangement = Arrangement.spacedBy(Spacing.s16),
                ) {
                    items(
                        count = state.audioCourses.size,
                        key = { state.audioCourses[it].id },
                    ) { index ->
                        ItemListAudioCourse(
                            audioCourse = state.audioCourses[index],
                            onItemClick = {
                                onAction(
                                    AudioCoursesAction.OnItemClick(
                                        state.audioCourses[index].id,
                                    ),
                                )
                            },
                        )
                        if (state.audioCourses.size == index + 1) {
                            Spacer(modifier = Modifier.padding(bottom = Spacing.s16))
                        }
                    }
                }
            }

            PullRefreshIndicator(
                refreshing = state.isRefreshing,
                state = pullRefreshState,
                modifier = Modifier.align(Alignment.TopCenter),
            )
        }
    }
}
