//package com.lyncan.opus.entities
//
//import androidx.room.Entity
//import androidx.room.ForeignKey
//import androidx.room.Index
//import androidx.room.PrimaryKey
//
//@Entity(
//    tableName = "attendance",
//    foreignKeys = [
//        ForeignKey(entity = SubjectEntity::class,
//            parentColumns = arrayOf("id"),
//            childColumns = arrayOf("subjectId"),
//            onDelete = ForeignKey.CASCADE
//            )
//    ],
//    indices = [Index("subjectId")]
//)
//data class AttendanceEntity(
//    @PrimaryKey(autoGenerate = true)
//    val id: Int = 0,
//    val subjectId: Int,
//    val date: String,
//    val time: String,
//    val isPresent: Boolean
//)
