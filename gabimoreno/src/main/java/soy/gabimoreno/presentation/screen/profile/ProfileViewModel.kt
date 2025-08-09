package soy.gabimoreno.presentation.screen.profile

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import soy.gabimoreno.di.IO
import soy.gabimoreno.domain.usecase.SetAllAudiocoursesAsUnlistenedUseCase
import soy.gabimoreno.domain.usecase.SetAllPodcastAsUnlistenedUseCase
import soy.gabimoreno.domain.usecase.SetAllPremiumAudiosAsUnlistenedUseCase
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel
    @Inject
    constructor(
        private val setAllAudiocoursesAsUnlistenedUseCase: SetAllAudiocoursesAsUnlistenedUseCase,
        private val setAllPodcastAsUnlistenedUseCase: SetAllPodcastAsUnlistenedUseCase,
        private val setAllPremiumAudiosAsUnlistenedUseCase: SetAllPremiumAudiosAsUnlistenedUseCase,
        @param:IO private val dispatcher: CoroutineDispatcher,
    ) : ViewModel() {
        var state by mutableStateOf(ProfileState())
            private set

        private val eventChannel = MutableSharedFlow<ProfileEvent>()
        val events = eventChannel.asSharedFlow()

        fun onAction(action: ProfileAction) {
            when (action) {
                is ProfileAction.OnEmailChanged -> {
                    state = state.copy(email = action.email)
                }

                ProfileAction.OnResetAudioCoursesClicked -> {
                    state =
                        state.copy(
                            showResetDialog = true,
                            selectedTypeDialog = TypeDialog.AUDIOCOURSES,
                        )
                }

                ProfileAction.OnResetPremiumAudioClicked -> {
                    state =
                        state.copy(
                            showResetDialog = true,
                            selectedTypeDialog = TypeDialog.PREMIUM,
                        )
                }

                ProfileAction.OnResetPodcastClicked -> {
                    state =
                        state.copy(
                            showResetDialog = true,
                            selectedTypeDialog = TypeDialog.PODCAST,
                        )
                }

                is ProfileAction.OnConfirmDialog -> {
                    when (state.selectedTypeDialog) {
                        TypeDialog.AUDIOCOURSES -> {
                            viewModelScope.launch(dispatcher) {
                                setAllAudiocoursesAsUnlistenedUseCase()
                                eventChannel.emit(
                                    ProfileEvent.ResetSuccess(TypeDialog.AUDIOCOURSES),
                                )
                            }
                        }
                        TypeDialog.PODCAST -> {
                            viewModelScope.launch(dispatcher) {
                                setAllPodcastAsUnlistenedUseCase()
                                eventChannel.emit(ProfileEvent.ResetSuccess(TypeDialog.PODCAST))
                            }
                        }
                        TypeDialog.PREMIUM -> {
                            viewModelScope.launch(dispatcher) {
                                setAllPremiumAudiosAsUnlistenedUseCase()
                                eventChannel.emit(ProfileEvent.ResetSuccess(TypeDialog.PREMIUM))
                            }
                        }

                        null -> Unit
                    }
                    state =
                        state.copy(
                            showResetDialog = false,
                            selectedTypeDialog = null,
                        )
                }

                ProfileAction.OnDismissDialog -> {
                    state =
                        state.copy(
                            showResetDialog = false,
                            selectedTypeDialog = null,
                        )
                }

                else -> Unit
            }
        }
    }
