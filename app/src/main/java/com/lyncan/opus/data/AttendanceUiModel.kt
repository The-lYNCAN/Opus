package com.lyncan.opus.data

import com.lyncan.opus.entities.AttendanceEntity
import com.lyncan.opus.entities.SubjectEntity

data class AttendanceUiModel(
    val attendance: AttendanceEntity,
    val subject: SubjectEntity
)