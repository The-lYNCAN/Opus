package com.lyncan.opus.Domain.UseCases.AttendanceDetailsUseCases

import android.icu.text.DecimalFormat
import android.util.Log
import com.lyncan.opus.Common.Resourse
import com.lyncan.opus.Domain.Repository.AttendanceDetailsInter
import com.lyncan.opus.Domain.Repository.SubjectManagement
import com.lyncan.opus.Presentation.States.AttendanceDetailState
import com.lyncan.opus.Presentation.data.AttendanceDetailItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetDetailsUseCase @Inject constructor(
    private val attendanceRepository: AttendanceDetailsInter,
    private val subjectManagement: SubjectManagement
) {
    operator fun invoke(subjectId: Int): Flow<Resourse<AttendanceDetailState>> {
        val format = DecimalFormat("0.00")
        return flow {
            attendanceRepository.getDetail(subjectId).collect {
                val totalAbsent = it.count{it.isPresent == false }
                val totalPresent = it.count{it.isPresent == true }
                val percentage = if (totalPresent + totalAbsent > 0) {
                    (totalPresent.toDouble() / (totalPresent + totalAbsent)) * 100
                } else {
                    0.0
                }
                val subjectDetails = subjectManagement.getSubjectById(subjectId)
                val stateToPass = AttendanceDetailState(
                    subjectName = subjectDetails?.name ?: "Unknown Subject",
                    present = totalPresent,
                    absent = totalAbsent,
                    percentage = format.format(percentage).toDouble(),
                    List = it.map { AttendanceDetailItem(it.id, it.isPresent, it.time, it.date, timeTableId = it.timeTableId) },
                    subjectCode = subjectDetails?.code

                )
                Log.d("GetDetailsUseCase", "Emitting state: $stateToPass")
                emit(Resourse.Success(stateToPass))
            }
        }
    }
}