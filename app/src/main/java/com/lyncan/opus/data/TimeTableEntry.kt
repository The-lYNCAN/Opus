package com.lyncan.opus.data

data class TimeTableEntry(
    val id: Int? = 0,
    val subjectid: Int,
    val day: String,
    val startTime: String,
    val endTime: String,
    val type: Int = 0,
    val room: String? = "Home",
    val group: Int = 0
)
