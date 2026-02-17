package com.lyncan.opus.data

import com.lyncan.opus.DataLayer.local.entities.AttendanceEntity
import com.lyncan.opus.DataLayer.local.entities.SubjectEntity

data class AttendanceUiModel(
    val attendance: AttendanceEntity,
    val subject: SubjectEntity
)