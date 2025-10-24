package com.example.kaamkarlo.viewmodels

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.lifecycle.ViewModel
import com.example.kaamkarlo.MyApplication
import com.example.kaamkarlo.SupabaseRepository
import com.example.kaamkarlo.UserState
import com.example.kaamkarlo.data.Assignment
import com.example.kaamkarlo.data.Groups
import com.example.kaamkarlo.data.Subject
import com.example.kaamkarlo.data.user
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.jan.supabase.postgrest.postgrest
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class groupModel @Inject constructor(
    private val repo: SupabaseRepository,
    private val appScope: CoroutineScope,
    private val userState: UserState
): ViewModel() {
    private val _user = MutableStateFlow<user?>(null)
    val user = _user
    private val _group = MutableStateFlow<Groups?>(null)
    val group = _group
    private val _subject = MutableStateFlow<List<Subject>>( emptyList())
    val subjects = _subject
    private val _assignments = MutableStateFlow<MutableMap<Int, List<Assignment>>>( mutableMapOf<Int, List<Assignment>>())
    val assignmentsObj = _assignments

    private val _members = MutableStateFlow<List<user>>(emptyList())
    val members = _members
    init {
        appScope.launch {
            _user.value = userState.getUser()
            Log.d("This is user", "${_user.value}")
            val result = repo.database().from("Group").select {
                filter {
                    eq("group_id", user.value!!.group_id?: 0)
                }
            }.decodeSingleOrNull<Groups>()
            _group.value = result
            if(_group.value != null){

                _subject.value = repo.getSubjects(group.value?.group_id!!)
                for (i in 0 .. _subject.value.size -1){
    //                repo.getAssignments(_subject.value[i].subject_id)
                    _assignments.value[_subject.value[i].subject_id!!] = repo.getAssignments(_subject.value[i].subject_id!!)
                }
                Log.d("Assignment Details", "${_assignments.value}")
                _members.value = repo.database().from("users").select {
                    filter {
                        eq("group_id", user.value!!.group_id?: 0)
                    }
                }.decodeList<user>()
            }
        }
    }
    fun deleteGroup(){
        appScope.launch {
            repo.deleteGroup()
            var userStateCurrent = userState.getUser()
            userStateCurrent.group_id = null
            userState.setUser(userStateCurrent)
            user.value = userStateCurrent
            group.value = null
        }
    }

    fun exitGroup(){
        appScope.launch {
            repo.exitGroup()
            var userStateCurrent = userState.getUser()
            userStateCurrent.group_id = null
            userState.setUser(userStateCurrent)
            user.value = userStateCurrent
            group.value = null
        }
    }


}