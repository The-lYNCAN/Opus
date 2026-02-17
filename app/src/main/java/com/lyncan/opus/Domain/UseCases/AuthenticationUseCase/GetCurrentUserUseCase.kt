package com.lyncan.opus.Domain.UseCases.AuthenticationUseCase

import com.lyncan.opus.Domain.Repository.Authentication
import com.lyncan.opus.Domain.Repository.UserStateInter
import javax.inject.Inject

class GetCurrentUserUseCase @Inject constructor(
    private val userState: UserStateInter,
    private val auth: Authentication
) {
    suspend operator fun invoke(): Boolean{
        if(auth.userLoggedIn()){
            userState.updateUser(auth.getCurrentUser()!!)
            return true
        }else{
            return false
        }
    }
}