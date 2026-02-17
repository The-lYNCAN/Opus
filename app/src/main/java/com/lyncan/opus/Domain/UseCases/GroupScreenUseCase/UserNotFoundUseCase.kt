package com.lyncan.opus.Domain.UseCases.GroupScreenUseCase

import android.util.Log
import com.lyncan.opus.UserState
import javax.inject.Inject

class UserNotFoundUseCase @Inject constructor(
    private val userState: UserState
) {
    suspend operator fun invoke(): String {
        userState.currentUser.collect {

            Log.d("UserNotFoundUseCase", it.toString())

        }
    }
}