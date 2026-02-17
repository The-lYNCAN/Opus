package com.lyncan.opus.data

import com.lyncan.opus.DataLayer.local.entities.TimeTableEntity
import kotlinx.serialization.Serializable

@Serializable
data class TimeTableEntry(
    val id: Int? = 0,
    val subjectid: Int,
    val day: String,
    val startTime: String,
    val endTime: String,
    val type: Int,
    val room: String? = "Home",
    val group: Int = 0
){
    fun toEntity(): TimeTableEntity {
        return TimeTableEntity(
            id = id!!,
            subjectid = subjectid,
            day = day,
            startTime = startTime,
            endTime = endTime,
            type = type,
            room = room,
            group = group
        )
    }
}
