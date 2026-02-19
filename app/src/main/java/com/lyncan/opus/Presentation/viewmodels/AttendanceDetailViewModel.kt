package com.lyncan.opus.Presentation.viewmodels

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lyncan.opus.Domain.UseCases.AttendanceDetailsUseCases.AttendanceDetailsEvents
import com.lyncan.opus.Domain.UseCases.AttendanceDetailsUseCases.EditAttendanceUseCase
import com.lyncan.opus.Domain.UseCases.AttendanceDetailsUseCases.GetDetailsUseCase
import com.lyncan.opus.Domain.UseCases.AttendanceDetailsUseCases.SetCustomAttendanceUseCase
import com.lyncan.opus.Presentation.States.AttendanceDetailState
import com.lyncan.opus.Presentation.States.AttendanceUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AttendanceDetailViewModel @Inject constructor(
    private val getDetailUseCase: GetDetailsUseCase,
    private val editAttendanceUseCase: EditAttendanceUseCase,
    savedStateHandle: SavedStateHandle,
    private val setCustomAttendanceUseCase: SetCustomAttendanceUseCase
): ViewModel() {
    val subId = savedStateHandle.get<Int>("Subject_Id_Key")
    private val _state = MutableStateFlow<AttendanceDetailState?>(null)
    val state = _state

    private val _uiState = MutableStateFlow(AttendanceUiState())
    val uiState = _uiState.asStateFlow()


    init {
        viewModelScope.launch {
            getDetailUseCase(subId?:0).collect {
                when(it){
                    is com.lyncan.opus.Common.Resourse.Success -> {
                        _state.value = it.data
                    }
                    is com.lyncan.opus.Common.Resourse.Error -> {
                        // Handle error case if needed
                    }
                    is com.lyncan.opus.Common.Resourse.Loading -> {
                        // Handle loading state if needed
                    }
                }
            }
        }
    }

    fun onEvent(event: AttendanceDetailsEvents) {
        when (event) {
            is AttendanceDetailsEvents.EditButton -> {
                if (event.attId != null && event.isPresent != null) {
                    viewModelScope.launch {
                        editAttendanceUseCase(event.attId, event.isPresent)
                    }
                }
            }
            is AttendanceDetailsEvents.EditButtonClicked -> {
                _uiState.update { it.copy(showDialog = true) }
            }

            is AttendanceDetailsEvents.setAttendance -> TODO()


            is AttendanceDetailsEvents.EditButtonConfirmed -> {
                viewModelScope.launch {
                    setCustomAttendanceUseCase(event.total, event.present, subId ?: 0)
                    _uiState.update { it.copy(showDialog = false) }
                }


            }
            is AttendanceDetailsEvents.EditButtonDismissed -> {
                _uiState.update { it.copy(showDialog = false) }
            }
        }
    }
}