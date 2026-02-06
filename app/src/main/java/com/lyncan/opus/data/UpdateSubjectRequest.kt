package com.lyncan.opus.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UpdateSubjectRequest(
    @SerialName("Subject_name")
    val subjectName: String,

    @SerialName("subject_code")
    val subjectCode: String?,

    @SerialName("type")
    val type: Int
)