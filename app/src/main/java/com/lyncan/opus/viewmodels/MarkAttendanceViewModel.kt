package com.lyncan.opus.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lyncan.opus.Repositories.AttendanceRepository
import com.lyncan.opus.Repositories.SubjectRepository
import com.lyncan.opus.Repositories.TimeTableRepository
import com.lyncan.opus.data.AttendanceUiModel
import com.lyncan.opus.entities.AttendanceEntity
import com.lyncan.opus.viewmodels.Components.toLocalTime
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale
import javax.inject.Inject


fun String.toLocalTime(): LocalTime {
    val formatter = DateTimeFormatter.ofPattern("hh:mm a", Locale.ENGLISH)
    return LocalTime.parse(this.trim(), formatter)
}

@HiltViewModel
class MarkAttendanceViewModel @Inject constructor(
    private val attendanceRepository: AttendanceRepository,
    private val timetableRepo: TimeTableRepository,
    val subjectRepo: SubjectRepository
): ViewModel() {

    val mList = mutableListOf<AttendanceEntity>()
    val dayMap = mapOf(
        java.time.DayOfWeek.MONDAY to "MON",
        java.time.DayOfWeek.TUESDAY to "TUE",
        java.time.DayOfWeek.WEDNESDAY to "WED",
        java.time.DayOfWeek.THURSDAY to "THU",
        java.time.DayOfWeek.FRIDAY to "FRI",
        java.time.DayOfWeek.SATURDAY to "SAT",
        java.time.DayOfWeek.SUNDAY to "SUN"
    )
    init {

    }

    suspend fun getSubjectById(id: Int) = subjectRepo.getSubjectById(id)


}

//if (attendanceTime != null && attendanceTime.toLocalTime() < java.time.LocalTime.now()){
