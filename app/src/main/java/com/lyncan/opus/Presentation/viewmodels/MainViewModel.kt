package com.lyncan.opus.Presentation.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.lyncan.opus.DataLayer.Repositories.SubjectManagement
import com.lyncan.opus.DataLayer.Repositories.SupabaseRepository
import com.lyncan.opus.Domain.Repository.UserStateInter
import com.lyncan.opus.Presentation.ui.navigation.Route
import com.lyncan.opus.data.user
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    val repo: SupabaseRepository, val userState: UserStateInter, val subMan: SubjectManagement
) : ViewModel() {

    private val _isLoggedIn = MutableStateFlow<Boolean>(false)
    val isLoggedIn = _isLoggedIn
    private val _assignment = MutableStateFlow<Int>(0)
    val assignment = _assignment
    private val _loaded = MutableStateFlow(true)
    val loaded = _loaded
    private val _userDetails = MutableStateFlow<user?>(null)
    val user = _userDetails
    private val _group = MutableStateFlow<Int?>(null)
    val group = _group
    init {
        performNetworkOperationsForUserData()
    }

    fun setLogin(loggedIn: Boolean){
        _isLoggedIn.value = loggedIn
    }

    private fun performNetworkOperationsForUserData(){
        viewModelScope.launch {
            loaded.value = false
            user.value = repo.getCurrentUserDetails()
//            Log.d("User Details at mvm", "User details fetched: ${user.value}")
//            userState.updateUser(user.value?: user())
            loaded.value = true
        }
    }


    fun createGroup(groupName: String, description: String, navController: NavController){
        viewModelScope.launch {
            val groupId = repo.createGroup(groupName, description)
            val newUser = userState.getCurrentUser()
            newUser?.group_id = groupId
            userState.updateUser(newUser!!)
            user.value = userState.getCurrentUser()
            Log.d("Group Creation", "New group created with ID: $groupId and user updated: ${user.value}")

        }.invokeOnCompletion {
            navigateToHome(navController)
        }
    }
    fun joinGroup(inviteCode: String, navController: NavController){
        viewModelScope.launch {
            val groupId = repo.joinGroup(inviteCode)
            val currentUser = userState.getCurrentUser()
            currentUser?.group_id = groupId
            userState.updateUser(currentUser!!)
            user.value = userState.getCurrentUser()
            subMan.clearAllData()
//            subMan.Retrieve()
        }.invokeOnCompletion {
            navController.navigate(Route.Home.route)
        }
    }



    fun navigateToHome(navController: NavController){
        Log.d("Navigation", "Navigating to Home Screen with the information ${user.value}")
        navController.navigate(Route.Home.route)
    }

}