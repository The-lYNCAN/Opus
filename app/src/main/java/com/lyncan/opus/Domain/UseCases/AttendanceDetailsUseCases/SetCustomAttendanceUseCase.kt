package com.lyncan.opus.Domain.UseCases.AttendanceDetailsUseCases

import com.lyncan.opus.DataLayer.Repositories.AttendanceRepository
import com.lyncan.opus.DataLayer.Repositories.TimeTableRepository
import com.lyncan.opus.DataLayer.local.entities.AttendanceEntity
import kotlinx.coroutines.flow.first
import java.time.LocalDate
import java.time.ZoneId
import javax.inject.Inject

class SetCustomAttendanceUseCase @Inject constructor(
    private val attendanceRepo: AttendanceRepository,
    private val timeTableRepo: TimeTableRepository
) {
    suspend operator fun invoke(total: Int, present: Int, subId: Int) {
        val firstTimeTable = timeTableRepo.getAllTimeTableEntries().first().first{it.subjectid == subId}
        val attendancesToDelete = attendanceRepo.getAttendanceBySubject(subId).first()
        attendancesToDelete.forEach{
            attendanceRepo.delete(it)
        }
        for (i in 0..total-1){
            attendanceRepo.insert(AttendanceEntity(
                0, subId, isPresent = if (i >= present)false else true,
                date = LocalDate.now(ZoneId.systemDefault()).toString(),
                time = "",
                timeTableId = firstTimeTable.id,
                custom = true
            ))
        }
    }
}