package com.lyncan.opus.Domain.UseCases.AttendanceDetailsUseCases

import com.lyncan.opus.DataLayer.Repositories.AttendanceRepository
import javax.inject.Inject

class SetCustomAttendance @Inject constructor(
    private val attendanceRepo: AttendanceRepository
) {
    operator fun invoke() {

    }
}