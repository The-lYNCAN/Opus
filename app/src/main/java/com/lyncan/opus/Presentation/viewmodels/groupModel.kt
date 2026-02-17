package com.lyncan.opus.Presentation.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lyncan.opus.DataLayer.Repositories.SupabaseRepository
import com.lyncan.opus.Domain.Repository.UserStateInter
import com.lyncan.opus.Domain.UseCases.GroupScreenUseCase.UserGroupJoinedUseCase
import com.lyncan.opus.Presentation.States.GroupState
import com.lyncan.opus.data.Groups
import com.lyncan.opus.data.Subject
import com.lyncan.opus.data.user
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class groupModel @Inject constructor(
    private val repo: SupabaseRepository,
    private val appScope: CoroutineScope,
    private val userState: UserStateInter,
    private val userGroupJoinedUseCase: UserGroupJoinedUseCase
): ViewModel() {
    private val _user = MutableStateFlow<user?>(null)
    val user = _user
    private val _group = MutableStateFlow<Groups?>(null)
    val group = _group
    private val _subject = MutableStateFlow<List<Subject>>( emptyList())
    val subjects = _subject
    private val _state = MutableStateFlow<GroupState?>(null)
    val state = _state.asStateFlow()
    private val _members = MutableStateFlow<List<user>>(emptyList())
    val members = _members
    init {
        Log.d("GroupModel", "Initialized")
        viewModelScope.launch {
            val state = userGroupJoinedUseCase()
            if (state.data != null){
                _state.value = state.data.toState()
            }else{
                _state.value = null
            }
//            userNotFoundUseCase()
//            subMan.Retrieve()
            _user.value = userState.getCurrentUser()
            Log.d("This is user", "${userState.getCurrentUser()}")
            val result = repo.database().from("Group").select {
                filter {
                    eq("group_id", user.value?.group_id?: 0)
                }
            }.decodeSingleOrNull<Groups>()
            _group.value = result


        }
    }
    fun deleteGroup(){
        appScope.launch {
            repo.deleteGroup()
            var userStateCurrent = userState.getCurrentUser()
            userStateCurrent?.group_id = null
            userState.updateUser(userStateCurrent!!)
            user.value = userStateCurrent
            group.value = null
        }
    }

    fun exitGroup(){
        appScope.launch {
            repo.exitGroup()
            var userStateCurrent = userState.getCurrentUser()
            userStateCurrent?.group_id = null
            userState.updateUser(userStateCurrent!!)
            user.value = userStateCurrent
            group.value = null
        }
    }


}