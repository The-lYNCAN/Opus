package com.lyncan.opus.Domain.UseCases.AttendanceDetailsUseCases

sealed class AttendanceDetailsEvents () {
    data class EditButton(val attId: Int? = null, val isPresent: Boolean? = null): AttendanceDetailsEvents()
    data class setAttendance(val total: Int, val present: Int): AttendanceDetailsEvents()
    object EditButtonClicked: AttendanceDetailsEvents()
    data class EditButtonConfirmed(val total: Int, val present: Int): AttendanceDetailsEvents()

    object EditButtonDismissed: AttendanceDetailsEvents()


}