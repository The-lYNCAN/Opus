package com.lyncan.opus.Domain.Repository

import com.lyncan.opus.data.user

interface Authentication {
    suspend fun getCurrentUser(): user?

    suspend fun userLoggedIn(): Boolean
}