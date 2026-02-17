package com.lyncan.opus.Presentation.States

import com.lyncan.opus.Presentation.data.AttendanceDetailItem

data class AttendanceDetailState(
    val List: List<AttendanceDetailItem> = emptyList(),
    val percentage: Double,
    val present: Int,
    val absent: Int,
    val subjectName: String,
    val subjectCode: String?
    )
