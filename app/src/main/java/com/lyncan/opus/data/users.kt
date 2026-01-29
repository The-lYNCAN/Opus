package com.lyncan.opus.data

import kotlinx.serialization.Serializable

@Serializable
data class user(
    val user_id: String,
    val avatar_url: String,
    val token_identifier: String,
    val email: String,
    val full_name: String,
    var group_id: Int?
)
