package com.lyncan.opus.viewmodels

import androidx.lifecycle.ViewModel
import com.lyncan.opus.Repositories.AttendanceRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AttendanceViewModel @Inject constructor(
    private val attendanceRepository: AttendanceRepository
): ViewModel() {

}