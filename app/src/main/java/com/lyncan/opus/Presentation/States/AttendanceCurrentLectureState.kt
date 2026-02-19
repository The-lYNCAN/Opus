package com.lyncan.opus.Presentation.States

data class AttendanceCurrentLectureStateItem(val startTime: String, val endTime: String, val subjectName: String?)

data class AttendanceCurrentLectureState(
    val isLoading: Boolean = false,
    val currentLectures: List<AttendanceCurrentLectureStateItem?> = emptyList()
)