package com.lyncan.opus

import android.util.Log
import com.lyncan.opus.data.user

class UserState constructor() {

    init {
        println("UserState initialized")
    }
    private var userObj: user = user("", "", "", "", "", null)
    fun getUser(): user {
        return userObj
    }
    fun setUser(user: user?) {
        Log.d("UserState", "Setting user: $user")
        userObj = if(user != null) user else user("", "", "", "", "", null)
    }

}