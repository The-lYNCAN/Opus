package com.lyncan.opus.Domain.UseCases.AttendanceUseCase

import android.util.Log
import com.lyncan.opus.DataLayer.Repositories.AttendanceRepository
import com.lyncan.opus.DataLayer.Repositories.SubjectRepository
import com.lyncan.opus.Presentation.States.AttendanceRetrievalState
import com.lyncan.opus.Presentation.States.SubjectCardsState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.ZoneId
import javax.inject.Inject

class RetrieveAttendanceUseCase @Inject constructor(
    private val subjectRepository: SubjectRepository,
    private val attendanceRepository: AttendanceRepository
){
    operator fun invoke(): Flow<AttendanceRetrievalState> {
        Log.d("RetrieveAttendanceUseCase", "Starting attendance retrieval")
        val dayMap = mapOf(
            DayOfWeek.MONDAY to "MON",
            DayOfWeek.TUESDAY to "TUE",
            DayOfWeek.WEDNESDAY to "WED",
            DayOfWeek.THURSDAY to "THU",
            DayOfWeek.FRIDAY to "FRI",
            DayOfWeek.SATURDAY to "SAT",
            DayOfWeek.SUNDAY to "SUN"
        )
        val today = LocalDate.now(ZoneId.systemDefault())
        val todayAccordingToDB = dayMap.getValue(today.dayOfWeek)
        val attendances = mutableListOf<SubjectCardsState>()

        return subjectRepository.getAllSubjects()
            .flatMapLatest { subjectList ->

                if (subjectList.isEmpty()) {
                    return@flatMapLatest flowOf(AttendanceRetrievalState(emptyList()))
                }

                val attendanceFlows = subjectList.map { subject ->
                    attendanceRepository.getAttendanceBySubject(subject.id)
                        .map { attendanceList ->

                            var total = 0
                            var present = 0

                            attendanceList.forEach { attendance ->
                                if (attendance.isPresent != null) {
                                    total++
                                    if (attendance.isPresent == true) present++
                                }
                            }

                            val percentage =
                                if (total == 0) 0.0
                                else (present / total.toDouble()) * 100

                            SubjectCardsState(
                                subjectName = subject.name,
                                percentage = percentage,
                                subjectId = subject.id,
                                total = total,
                                present = present
                            )
                        }
                }

                combine(attendanceFlows) { subjectCardsArray ->
                    AttendanceRetrievalState(subjectCardsArray.toList())
                }
            }
    }
}