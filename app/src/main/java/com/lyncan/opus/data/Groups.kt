package com.lyncan.opus.data

import kotlinx.serialization.Serializable

@Serializable
data class Groups(
    val group_id: Int? = null,
    val group_name: String,
    val group_banner: String,
    val college: String? = null,
    val group_members: List<String> = emptyList(),
    val Table: List<List<String>>? = null,
    val admin: String,
    val description: String? = null,
    val invite_code: String
)