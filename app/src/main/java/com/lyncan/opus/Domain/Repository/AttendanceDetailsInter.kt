package com.lyncan.opus.Domain.Repository

import com.lyncan.opus.DataLayer.local.entities.AttendanceEntity
import kotlinx.coroutines.flow.Flow

interface AttendanceDetailsInter {
    suspend fun getDetail(subjectId: Int): Flow<List<AttendanceEntity>>
    suspend fun editAttendance(attendanceId: Int, isPresent: Boolean)
}