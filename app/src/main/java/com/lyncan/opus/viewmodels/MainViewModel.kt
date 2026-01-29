package com.lyncan.opus.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.lyncan.opus.SubjectManagement
import com.lyncan.opus.Modules.SupabaseRepository
import com.lyncan.opus.UserState
import com.lyncan.opus.data.user
import com.lyncan.opus.uI.navigation.Route
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject
constructor(val repo: SupabaseRepository, val userState: UserState, val subMan: SubjectManagement)
    : ViewModel() {
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

    private fun performNetworkOperationsForUserData(){
        viewModelScope.launch {
            loaded.value = false
            user.value = repo.getCurrentUserDetails()
            userState.setUser(user.value)
            loaded.value = true
            Log.d("MainViewModel", "User details fetched: ${user.value}" )
        }
    }

    fun setAssignment(assignment: Int) {
        _assignment.value = assignment
    }

    fun createGroup(groupName: String, description: String, navController: NavController){
        viewModelScope.launch {
            val groupId = repo.createGroup(groupName, description)
            val newUser = userState.getUser()
            newUser.group_id = groupId
            userState.setUser(newUser)
            user.value = userState.getUser()
            Log.d("Group Creation", "New group created with ID: $groupId and user updated: ${user.value}")

        }.invokeOnCompletion {
            navigateToHome(navController)
        }
    }
    fun joinGroup(inviteCode: String, navController: NavController){
        viewModelScope.launch {
            val groupId = repo.joinGroup(inviteCode)
            val currentUser = userState.getUser()
            currentUser.group_id = groupId
            userState.setUser(currentUser)
            user.value = userState.getUser()
            subMan.clearAllData()
            subMan.Retrieve()
        }.invokeOnCompletion {
            navController.navigate(Route.Home.route)
        }
    }
    fun getId(): Int{
        return subMan.readId()
    }
    fun getLink():String{
        if(subMan.whichOne){
            // here comes assignment
            return subMan.getUpload(subMan.id)

        }else{
            // here comes problem set
            return "https://tuqcjuywjjtwidvdprel.supabase.co/storage/v1/object/public//assignments/"+subMan.getProblemSet(subMan.id)
        }
        return ""
    }

    fun navigateToHome(navController: NavController){
        Log.d("Navigation", "Navigating to Home Screen with the information ${user.value}")
        navController.navigate(Route.Home.route)
    }

}