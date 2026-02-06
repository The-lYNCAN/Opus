package com.lyncan.opus.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "timetable",
    foreignKeys = [
        ForeignKey(entity = SubjectEntity::class,
            parentColumns = arrayOf("id"),
            childColumns = arrayOf("subjectid"),
            onDelete = ForeignKey.CASCADE)
    ],
    indices = [androidx.room.Index("subjectid")]
)
data class TimeTableEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
    val code: String? = null,
    val subjectid: Int,
    val day: String,
    val startTime: String,
    val endTime: String
)
