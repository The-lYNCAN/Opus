package com.lyncan.opus.Domain.UseCases.AttendanceDetailsUseCases

import com.lyncan.opus.DataLayer.Repositories.AttendanceDetails
import javax.inject.Inject

class EditAttendanceUseCase @Inject constructor(
    val attendanceDetails: AttendanceDetails
) {
    suspend operator fun invoke(attId: Int, isPresent: Boolean) {
        attendanceDetails.editAttendance(attId, isPresent)
    }
}