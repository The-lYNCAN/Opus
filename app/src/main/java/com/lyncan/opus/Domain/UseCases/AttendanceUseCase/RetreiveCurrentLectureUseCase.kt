package com.lyncan.opus.Domain.UseCases.AttendanceUseCase

import com.lyncan.opus.DataLayer.Repositories.SubjectRepository
import com.lyncan.opus.DataLayer.Repositories.TimeTableRepository
import com.lyncan.opus.DataLayer.local.entities.SubjectEntity
import com.lyncan.opus.DataLayer.local.entities.TimeTableEntity
import com.lyncan.opus.Presentation.States.AttendanceCurrentLectureState
import com.lyncan.opus.Presentation.States.AttendanceCurrentLectureStateItem
import java.time.DayOfWeek
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

class RetreiveCurrentLectureUseCase @Inject constructor(
    private val timeRepo: TimeTableRepository,
    private val subRepo: SubjectRepository
) {
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
        val timetable = timeRepo.getTimeTableByDay(dayMap[today]!!)
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

    operator suspend fun invoke(): AttendanceCurrentLectureState {
        val lectureList = mutableListOf<AttendanceCurrentLectureStateItem>()
        getCurrentLecture().forEach {
            val currentLec = AttendanceCurrentLectureStateItem(it.startTime, it.endTime, getSubjectNameById(it.subjectid)?.name)
            lectureList.add(currentLec)
        }
        return AttendanceCurrentLectureState(true, lectureList)
    }

}