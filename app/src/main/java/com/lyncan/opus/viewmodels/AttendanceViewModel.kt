package com.lyncan.opus.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lyncan.opus.Repositories.AttendanceRepository
import com.lyncan.opus.Repositories.SubjectManagement
import com.lyncan.opus.Repositories.SubjectRepository
import com.lyncan.opus.Repositories.TimeTableRepository
import com.lyncan.opus.data.AttendanceUiModel
import com.lyncan.opus.entities.AttendanceEntity
import com.lyncan.opus.viewmodels.Components.toLocalTime
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import java.time.LocalDate
import java.time.LocalTime
import java.time.ZoneId
import javax.inject.Inject

@HiltViewModel
class AttendanceViewModel @Inject constructor(
    private val attendanceRepository: AttendanceRepository,
    private val subjectRepository: SubjectRepository,
    val timetableRepo : TimeTableRepository,
    val subMan: SubjectManagement
): ViewModel() {

    private val _mark = MutableStateFlow<Boolean>(true)
    val mark = _mark
    private val _attendanceItems = MutableStateFlow<List<AttendanceUiModel>>(emptyList())
    val attendanceItems = _attendanceItems
    suspend fun getSubjectById(id: Int) = subjectRepository.getSubjectById(id)
    fun getAllSubjects() = subjectRepository.getAllSubjects()
    fun getAllAttendance() = attendanceRepository.getALl()
    val dayMap = mapOf(
        java.time.DayOfWeek.MONDAY to "MON",
        java.time.DayOfWeek.TUESDAY to "TUE",
        java.time.DayOfWeek.WEDNESDAY to "WED",
        java.time.DayOfWeek.THURSDAY to "THU",
        java.time.DayOfWeek.FRIDAY to "FRI",
        java.time.DayOfWeek.SATURDAY to "SAT",
        java.time.DayOfWeek.SUNDAY to "SUN"
    )

    init{
        val today = java.time.LocalDate.now(ZoneId.systemDefault())
        val todayAccordingToDB = dayMap.getValue(today.dayOfWeek)
        Log.d("AttendanceViewModel", "Today is: $todayAccordingToDB, ${LocalDate.now(ZoneId.systemDefault())}")
        viewModelScope.launch {
            subMan.Retrieve()
            val timeTableEntries = timetableRepo.getTimeTableByDay(todayAccordingToDB)
            timeTableEntries.forEach {
                val attendanceEnt = AttendanceEntity(subjectId = it.subjectid, date = today.toString(), time = "", isPresent = null, timeTableId = it.id)
                if(!attendanceRepository.attendancePresent(attendanceEnt.date, attendanceEnt.timeTableId)){
                    attendanceRepository.insert(attendanceEnt)
                }
            }
            val attendance = attendanceRepository.getTodaysAttendance(today.toString())
            attendance.collect {
                it.forEach { att ->
                    val attendanceTime = timeTableEntries.firstOrNull{it.id == att.timeTableId}?.endTime
                    if (attendanceTime != null && attendanceTime.toLocalTime() < java.time.LocalTime.now()){
                        if (att.isPresent == null){
                            mark.value = false
                        }
                    }

                }
            }
        }
    }

    fun retrieve(){
        viewModelScope.launch {
            val today = LocalDate.now(ZoneId.systemDefault())
            val todayAccordingToDB = dayMap.getValue(today.dayOfWeek)

            val timetableEntries = timetableRepo.getTimeTableByDay(todayAccordingToDB)
            val attendanceForToday =
                attendanceRepository.getTodaysAttendance(today.toString()).first()

            val uiItems = attendanceForToday
                .filter { att ->
                    val endTime = timetableEntries
                        .firstOrNull { it.id == att.timeTableId }
                        ?.endTime
                        ?.toLocalTime()

                    endTime != null &&
                            endTime < LocalTime.now() &&
                            att.isPresent == null
                }
                .map { att ->
                    val subject = subjectRepository.getSubjectById(att.subjectId)
                    AttendanceUiModel(att, subject!!)
                }

            _attendanceItems.value = uiItems
        }
    }
    fun attendedFunc(id: Int){
        viewModelScope.launch {
            val today = java.time.LocalDate.now(ZoneId.systemDefault())

            attendanceRepository.markPresent(id)
            if(attendanceRepository.getTodaysAttendance(today.toString()).first().firstOrNull{it.isPresent == null} == null){
                mark.value = true
            }

        }
    }

    fun bunkedFunc(id: Int){

        viewModelScope.launch {
            val today = java.time.LocalDate.now(ZoneId.systemDefault())

            attendanceRepository.markAbsent(id)
            if(attendanceRepository.getTodaysAttendance(today.toString()).first().firstOrNull{it.isPresent == null} == null){
                mark.value = true
            }
        }
    }

}