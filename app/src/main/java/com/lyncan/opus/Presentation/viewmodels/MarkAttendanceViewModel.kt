package com.lyncan.opus.Presentation.viewmodels

import androidx.lifecycle.ViewModel
import com.lyncan.opus.DataLayer.Repositories.AttendanceRepository
import com.lyncan.opus.DataLayer.Repositories.SubjectRepository
import com.lyncan.opus.DataLayer.Repositories.TimeTableRepository
import com.lyncan.opus.DataLayer.local.entities.AttendanceEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import java.time.DayOfWeek
import javax.inject.Inject


@HiltViewModel
class MarkAttendanceViewModel @Inject constructor(
    private val attendanceRepository: AttendanceRepository,
    private val timetableRepo: TimeTableRepository,
    val subjectRepo: SubjectRepository
): ViewModel() {

    val mList = mutableListOf<AttendanceEntity>()
    val dayMap = mapOf(
        DayOfWeek.MONDAY to "MON",
        DayOfWeek.TUESDAY to "TUE",
        DayOfWeek.WEDNESDAY to "WED",
        DayOfWeek.THURSDAY to "THU",
        DayOfWeek.FRIDAY to "FRI",
        DayOfWeek.SATURDAY to "SAT",
        DayOfWeek.SUNDAY to "SUN"
    )
    init {

    }

    suspend fun getSubjectById(id: Int) = subjectRepo.getSubjectById(id)


}

//if (attendanceTime != null && attendanceTime.toLocalTime() < java.time.LocalTime.now()){
