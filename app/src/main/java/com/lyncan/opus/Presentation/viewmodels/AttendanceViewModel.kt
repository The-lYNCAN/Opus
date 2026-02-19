package com.lyncan.opus.Presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lyncan.opus.DataLayer.Repositories.AttendanceRepository
import com.lyncan.opus.DataLayer.Repositories.SubjectManagement
import com.lyncan.opus.DataLayer.Repositories.SubjectRepository
import com.lyncan.opus.DataLayer.Repositories.TimeTableRepository
import com.lyncan.opus.DataLayer.local.entities.AttendanceEntity
import com.lyncan.opus.Domain.UseCases.AttendanceUseCase.MarkAttendanceUseCase
import com.lyncan.opus.Domain.UseCases.AttendanceUseCase.RetrieveAttendanceUseCase
import com.lyncan.opus.Domain.UseCases.AttendanceUseCase.toLocalTime
import com.lyncan.opus.Presentation.States.AttendanceRetrievalState
import com.lyncan.opus.data.AttendanceUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.LocalTime
import java.time.ZoneId
import javax.inject.Inject

@HiltViewModel
class AttendanceViewModel @Inject constructor(
    private val attendanceRepository: AttendanceRepository,
    private val subjectRepository: SubjectRepository,
    private val timetableRepo: TimeTableRepository,
    private val subMan: SubjectManagement,
    private val retrieveAttendanceUseCase: RetrieveAttendanceUseCase,
    private val markAttendanceUseCase: MarkAttendanceUseCase
) : ViewModel() {

    private val today = LocalDate.now(ZoneId.systemDefault())
    private val dayMap = mapOf(
        DayOfWeek.MONDAY to "MON",
        DayOfWeek.TUESDAY to "TUE",
        DayOfWeek.WEDNESDAY to "WED",
        DayOfWeek.THURSDAY to "THU",
        DayOfWeek.FRIDAY to "FRI",
        DayOfWeek.SATURDAY to "SAT",
        DayOfWeek.SUNDAY to "SUN"
    )
    private val todayAccordingToDB = dayMap.getValue(today.dayOfWeek)

    /* ---------------------------
       Attendance Summary State
    ---------------------------- */

    val retrievalState: StateFlow<AttendanceRetrievalState> =
        retrieveAttendanceUseCase()
            .stateIn(
                viewModelScope,
                SharingStarted.WhileSubscribed(5000),
                AttendanceRetrievalState(emptyList())
            )

    /* ---------------------------
       Today's Attendance
    ---------------------------- */

    private val attendanceTodayFlow =
        attendanceRepository.getAttendanceByDate(today.toString())

    /* ---------------------------
       Mark Button State
    ---------------------------- */

    val mark: StateFlow<Boolean> =
        attendanceTodayFlow
            .map { list ->
                list.none { it.isPresent == null && !it.custom && it.time.split(" - ")[1].toLocalTime() < LocalTime.now() }
//                list.none { it.isPresent == null && !it.custom }
            }
            .stateIn(
                viewModelScope,
                SharingStarted.WhileSubscribed(5000),
                true
            )

    /* ---------------------------
       Pending Attendance Items
    ---------------------------- */

    val attendanceItems: StateFlow<List<AttendanceUiModel>> =
        attendanceTodayFlow
            .map { attendanceList ->

                val timetableEntries =
                    timetableRepo.getTimeTableByDay(todayAccordingToDB)

                attendanceList
                    .filter { att ->
                        val endTime = timetableEntries
                            .firstOrNull { it.id == att.timeTableId }
                            ?.endTime
                            ?.toLocalTime()

                        endTime != null &&
                                endTime < LocalTime.now() &&
                                att.isPresent == null
                    }
                    .mapNotNull { att ->
                        val subject =
                            subjectRepository.getSubjectById(att.subjectId)
                        subject?.let { AttendanceUiModel(att, it) }
                    }
            }
            .stateIn(
                viewModelScope,
                SharingStarted.WhileSubscribed(5000),
                emptyList()
            )

    /* ---------------------------
       Init Block
    ---------------------------- */

    init {
        viewModelScope.launch {
            subMan.Retrieve()
            subMan.retrieveTimeTable()
//            insertTodayAttendanceIfMissing()
            markAttendanceUseCase()
        }
    }

    private suspend fun insertTodayAttendanceIfMissing() {
        val timeTableEntries =
            timetableRepo.getTimeTableByDay(todayAccordingToDB)

        timeTableEntries.forEach {
            val entity = AttendanceEntity(
                subjectId = it.subjectid,
                date = today.toString(),
                time = "",
                isPresent = null,
                timeTableId = it.id
            )

            if (!attendanceRepository.attendancePresent(
                    entity.date,
                    entity.timeTableId!!
                )
            ) {
                attendanceRepository.insert(entity)
            }
        }
    }

    /* ---------------------------
       Actions
    ---------------------------- */

    fun attendedFunc(id: Int) {
        viewModelScope.launch {
            attendanceRepository.markPresent(id)
        }
    }

    fun bunkedFunc(id: Int) {
        viewModelScope.launch {
            attendanceRepository.markAbsent(id)
        }
    }
}