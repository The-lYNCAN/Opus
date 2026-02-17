package com.lyncan.opus.Domain.data

data class TimeTable(
    val id: Int? = 0,
    val subjectid: Int,
    val day: String,
    val startTime: String,
    val endTime: String,
    val type: Int,
    val room: String? = "Home",
    val group: Int = 0
)
