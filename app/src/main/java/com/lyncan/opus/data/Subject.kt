package com.lyncan.opus.data

import kotlinx.serialization.Serializable

@Serializable
data class Subject(
    val subject_id: Int? = null,
//    val createdAt: Timestamp,
    val subjectPic: String? = null,
    val Subject_name: String,
    val group_id: Int
)
