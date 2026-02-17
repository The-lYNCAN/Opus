package com.lyncan.opus.DataLayer.Repositories

import com.lyncan.opus.DataLayer.local.entities.AttendanceEntity
import com.lyncan.opus.Domain.Repository.AttendanceDetailsInter
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class AttendanceDetails @Inject constructor(
    private val attendanceRepository: AttendanceRepository
): AttendanceDetailsInter {
    override suspend fun getDetail(subjectId: Int): Flow<List<AttendanceEntity>> {
        return attendanceRepository.getAttendanceBySubject(subjectId)
    }

    override suspend fun editAttendance(attendanceId: Int, isPresent: Boolean) {
        if(isPresent){
            attendanceRepository.markPresent(id = attendanceId)
        }else{
            attendanceRepository.markAbsent(id = attendanceId)
        }
    }
}