package com.example.kaamkarlo.data

import kotlinx.serialization.Serializable
import java.util.UUID
import java.util.stream.IntStream

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