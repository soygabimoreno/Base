package soy.gabimoreno.presentation.screen.survey

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
import soy.gabimoreno.domain.usecase.GetActiveSurveyUseCase
import soy.gabimoreno.domain.usecase.SetLastSurveyIdUseCase
import javax.inject.Inject

@HiltViewModel
class SurveyDialogViewModel
    @Inject
    constructor(
        private val getActiveSurveyUseCase: GetActiveSurveyUseCase,
        private val setLastSurveyIdUseCase: SetLastSurveyIdUseCase,
        @param:IO private val dispatcher: CoroutineDispatcher,
    ) : ViewModel() {
        var state by mutableStateOf(SurveyDialogState())
            private set

        private val eventChannel = MutableSharedFlow<SurveyDialogEvent>()
        val events = eventChannel.asSharedFlow()

        init {
            viewModelScope.launch(dispatcher) {
                getActiveSurveyUseCase()
                    .onRight { survey ->
                        state = state.copy(survey = survey)
                    }
            }
        }

        fun onAction(action: SurveyDialogAction) {
            when (action) {
                SurveyDialogAction.OnConfirmDialog -> {
                    viewModelScope.launch(dispatcher) {
                        val survey = state.survey
                        if (survey != null) {
                            eventChannel.emit(SurveyDialogEvent.LaunchSurvey(survey.url))
                            state = state.copy(survey = null)
                            setLastSurveyIdUseCase(survey.id)
                        }
                    }
                }

                SurveyDialogAction.OnDismissDialog -> {
                    state = state.copy(survey = null)
                }
            }
        }
    }
