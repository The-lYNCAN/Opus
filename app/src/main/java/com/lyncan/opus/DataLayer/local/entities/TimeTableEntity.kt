package com.lyncan.opus.DataLayer.local.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.lyncan.opus.data.TimeTableEntry

@Entity(
    tableName = "timetable",
    foreignKeys = [
        ForeignKey(entity = SubjectEntity::class,
            parentColumns = arrayOf("id"),
            childColumns = arrayOf("subjectid"),
            onDelete = ForeignKey.CASCADE)
    ],
    indices = [Index("subjectid")]
)
data class TimeTableEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val subjectid: Int,
    val day: String,
    val startTime: String,
    val endTime: String,
    val type: Int = 0,
    val room: String? = "Home",
    val group: Int = 0,
    val editable: Boolean = true,
    val holiday: Boolean = false
){
    fun toEntry(): TimeTableEntry{
        return TimeTableEntry(
            id = id,
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
