package com.lyncan.opus

import android.util.Log
import com.lyncan.opus.Domain.Repository.UserStateInter
import com.lyncan.opus.data.user
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserState @Inject constructor() : UserStateInter {
    private val _currentUser = MutableStateFlow<user?>(null)
    override val currentUser = _currentUser

    init {
        Log.d("UserStateInter", "UserStateInter initialized with hash: ${this.hashCode()}")
        Exception("Created here").printStackTrace()

    }

    override fun getCurrentUser(): user? {
        Log.d("UserStateInter", "returning user: ${_currentUser.value}")
        return currentUser.value // Placeholder for actual implementation
    }

    override fun updateUser(user: user) {
        Log.d("UserStateInter", "Updating user: $user")
        _currentUser.value = user
    }
}