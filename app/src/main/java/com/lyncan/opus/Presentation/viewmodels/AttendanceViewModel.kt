package com.lyncan.opus.Presentation.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lyncan.opus.Presentation.viewmodels.Components.toLocalTime
import com.lyncan.opus.DataLayer.Repositories.AttendanceRepository
import com.lyncan.opus.DataLayer.Repositories.SubjectManagement
import com.lyncan.opus.DataLayer.Repositories.SubjectRepository
import com.lyncan.opus.DataLayer.Repositories.TimeTableRepository
import com.lyncan.opus.data.AttendanceUiModel
import com.lyncan.opus.DataLayer.local.entities.AttendanceEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
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
    fun getTT() = timetableRepo.getAllTimeTableEntries()
    val dayMap = mapOf(
        DayOfWeek.MONDAY to "MON",
        DayOfWeek.TUESDAY to "TUE",
        DayOfWeek.WEDNESDAY to "WED",
        DayOfWeek.THURSDAY to "THU",
        DayOfWeek.FRIDAY to "FRI",
        DayOfWeek.SATURDAY to "SAT",
        DayOfWeek.SUNDAY to "SUN"
    )

    init{
        val today = LocalDate.now(ZoneId.systemDefault())
        val todayAccordingToDB = dayMap.getValue(today.dayOfWeek)
        Log.d("AttendanceViewModel", "Today is: $todayAccordingToDB, ${LocalDate.now(ZoneId.systemDefault())}")
        viewModelScope.launch {
            subMan.Retrieve()
            subMan.retrieveTimeTable()
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
                    if (attendanceTime != null && attendanceTime.toLocalTime() < LocalTime.now()){
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

            attendanceRepository.getTodaysAttendance(today.toString()).collect {
                val uiItems = it
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
    }
    fun attendedFunc(id: Int){
        viewModelScope.launch {
            val today = LocalDate.now(ZoneId.systemDefault())

            attendanceRepository.markPresent(id)
            attendanceRepository.getTodaysAttendance(today.toString()).collect {
                if (it.firstOrNull{it.isPresent == null} == null){
                    mark.value = true
                }
            }

        }
    }

    fun bunkedFunc(id: Int){

        viewModelScope.launch {
            val today = LocalDate.now(ZoneId.systemDefault())

            attendanceRepository.markAbsent(id)
            if(attendanceRepository.getTodaysAttendance(today.toString()).first().firstOrNull{it.isPresent == null} == null){
                mark.value = true
            }
        }
    }

}