package com.lyncan.opus.viewmodels.Components

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lyncan.opus.Repositories.SubjectRepository
import com.lyncan.opus.Repositories.TimeTableRepository
import com.lyncan.opus.entities.SubjectEntity
import com.lyncan.opus.entities.TimeTableEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.forEach
import kotlinx.coroutines.flow.single
import kotlinx.coroutines.launch
import java.time.LocalTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class HeroViewModel @Inject constructor(
    private val timeRepo: TimeTableRepository,
    private val subRepo: SubjectRepository
): ViewModel() {
    suspend fun getCurrentLecture(): List<TimeTableEntity> {
        val today = java.time.LocalDate.now(ZoneId.systemDefault()).dayOfWeek
        val dayMap = mapOf(
            java.time.DayOfWeek.MONDAY to "MON",
            java.time.DayOfWeek.TUESDAY to "TUE",
            java.time.DayOfWeek.WEDNESDAY to "WED",
            java.time.DayOfWeek.THURSDAY to "THU",
            java.time.DayOfWeek.FRIDAY to "FRI",
            java.time.DayOfWeek.SATURDAY to "SAT",
            java.time.DayOfWeek.SUNDAY to "SUN"
        )
        val allCurrentLectures = mutableListOf<TimeTableEntity>()
        val timetable = timeRepo.getTimeTableByDay(dayMap[today] ?: "MON")
        timetable.forEach {
            val start = it.startTime.toLocalTime()
            val end = it.endTime.toLocalTime()
            val now = LocalTime.now()
            if (now.isAfter(start) && now.isBefore(end)){
                allCurrentLectures.add(it)
                // Update your UI with the current lecture information
            }

        }
        return allCurrentLectures
    }

    suspend fun getSubjectNameById(subjectId: Int): SubjectEntity? {
        val subject = subRepo.getSubjectById(subjectId)

        return subject
    }
}

fun String.toLocalTime(): LocalTime {
    val formatter = DateTimeFormatter.ofPattern("hh:mm a", Locale.ENGLISH)
    return LocalTime.parse(this.trim(), formatter)
}