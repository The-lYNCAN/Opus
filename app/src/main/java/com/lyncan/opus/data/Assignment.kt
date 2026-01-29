package com.lyncan.opus.data

import kotlinx.serialization.Serializable

@Serializable
data class Assignment(
    val assignment_id: Int? = null,
//    @Contextual val createdAt: Timestamp,
    val subject_id: Int,
    val assignment_name: String,
    val assignment_url: String,
    val due_date: String? = null,
    val assignment_pic_url: String? = null,
)
