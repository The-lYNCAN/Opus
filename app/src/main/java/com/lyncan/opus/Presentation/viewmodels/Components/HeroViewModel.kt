package com.lyncan.opus.Presentation.viewmodels.Components

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lyncan.opus.Domain.UseCases.AttendanceUseCase.RetreiveCurrentLectureUseCase
import com.lyncan.opus.Presentation.States.AttendanceCurrentLectureState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HeroViewModel @Inject constructor(
    private val RetrieveCurrentLecturesUseCase: RetreiveCurrentLectureUseCase
): ViewModel() {
    private val _state = MutableStateFlow<AttendanceCurrentLectureState?>(
        AttendanceCurrentLectureState(false, emptyList()))
    val state = _state.asStateFlow()
    init {
        viewModelScope.launch {
            _state.value = RetrieveCurrentLecturesUseCase()
        }
    }

}

