package com.lyncan.opus.Domain.UseCases.AttendanceUseCase

import com.lyncan.opus.DataLayer.Repositories.AttendanceRepository
import com.lyncan.opus.DataLayer.Repositories.TimeTableRepository
import com.lyncan.opus.DataLayer.local.entities.AttendanceEntity
import kotlinx.coroutines.flow.first
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.ZoneId
import javax.inject.Inject

class MarkAttendanceUseCase @Inject constructor(
    private val attendanceRepository: AttendanceRepository,
    val timeTableRepository: TimeTableRepository
) {
    suspend operator fun invoke() {
        val dayMap = mapOf( DayOfWeek.MONDAY to "MON", DayOfWeek.TUESDAY to "TUE", DayOfWeek.WEDNESDAY to "WED", DayOfWeek.THURSDAY to "THU", DayOfWeek.FRIDAY to "FRI", DayOfWeek.SATURDAY to "SAT", DayOfWeek.SUNDAY to "SUN" )
        val today = LocalDate.now(ZoneId.systemDefault())
        val todayAccordingToDB = dayMap.getValue(today.dayOfWeek)

        syncAttendanceForDay(todayAccordingToDB, today.toString())
    }
    suspend fun syncAttendanceForDay(
        day: String,
        date: String
    ) {
        val timeTables =
            timeTableRepository.getTimeTableByDay(day)

        val attendanceList =
            attendanceRepository.getAttendanceByDate(date).first()

        val validTimeTableIds =
            timeTables.map { it.id }.toSet()

        /* ----------------------------------
           1️⃣ Remove orphaned attendance
        ----------------------------------- */

        attendanceList
            .filter { it.timeTableId == null }
            .forEach {
                attendanceRepository.delete(it)
            }

        /* ----------------------------------
           2️⃣ Remove extra attendance
        ----------------------------------- */

        attendanceList
            .filter {
                it.timeTableId != null &&
                        it.timeTableId !in validTimeTableIds
            }
            .forEach {
                attendanceRepository.delete(it)
            }

        /* ----------------------------------
           3️⃣ Insert missing attendance
        ----------------------------------- */

        val attendanceTimeTableIds =
            attendanceList.mapNotNull { it.timeTableId }.toSet()

        val missingIds =
            validTimeTableIds - attendanceTimeTableIds

        timeTables
            .filter { it.id in missingIds }
            .forEach { timetable ->

                attendanceRepository.insert(
                    AttendanceEntity(
                        subjectId = timetable.subjectid,
                        date = date,
                        isPresent = null,
                        timeTableId = timetable.id,
                        time = "${timetable.startTime} - ${timetable.endTime}"
                    )
                )
            }
    }
}