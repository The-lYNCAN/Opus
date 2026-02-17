package com.lyncan.opus.Domain.Repository

import com.lyncan.opus.data.user
import kotlinx.coroutines.flow.Flow

interface UserStateInter {
    val currentUser: Flow<user?>
    fun getCurrentUser(): user?

    fun updateUser(user: user)
}