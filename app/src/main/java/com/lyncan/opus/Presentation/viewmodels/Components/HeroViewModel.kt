package com.lyncan.opus.Presentation.viewmodels.Components

import androidx.lifecycle.ViewModel
import com.lyncan.opus.DataLayer.Repositories.SubjectRepository
import com.lyncan.opus.DataLayer.Repositories.TimeTableRepository
import com.lyncan.opus.DataLayer.local.entities.SubjectEntity
import com.lyncan.opus.DataLayer.local.entities.TimeTableEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import java.time.DayOfWeek
import java.time.LocalDate
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
        val today = LocalDate.now(ZoneId.systemDefault()).dayOfWeek
        val dayMap = mapOf(
            DayOfWeek.MONDAY to "MON",
            DayOfWeek.TUESDAY to "TUE",
            DayOfWeek.WEDNESDAY to "WED",
            DayOfWeek.THURSDAY to "THU",
            DayOfWeek.FRIDAY to "FRI",
            DayOfWeek.SATURDAY to "SAT",
            DayOfWeek.SUNDAY to "SUN"
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