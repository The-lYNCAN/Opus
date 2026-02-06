package com.lyncan.opus.viewmodels

import androidx.lifecycle.ViewModel
import com.lyncan.opus.Repositories.SubjectManagement
import com.lyncan.opus.Repositories.SupabaseRepository
import com.lyncan.opus.Repositories.UserState
import com.lyncan.opus.data.Assignment
import com.lyncan.opus.data.Uploads
import com.lyncan.opus.data.user
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

@HiltViewModel
class DetailsViewModel @Inject constructor(private val userState: UserState,
                                           private val repo: SupabaseRepository
                                           , private val SubMan: SubjectManagement
): ViewModel()
{
    val subMan = SubMan
    private val _assignment = MutableStateFlow<Assignment?>(null)
    val assignment = _assignment
    private val _uploads = MutableStateFlow<List<Uploads>?>(emptyList())
    val uploads = _uploads
    private val enableSubmission = MutableStateFlow(true)
    val canSubmit = enableSubmission
    fun loadAssignment(assignmentId: Int) {

        SubMan.getAllData().first.values.forEach {
            it.forEach {
                if(it.assignment_id == assignmentId){
                    assignment.value = it
                    uploads.value = SubMan.getAllData().second.get(it)
//                    if(uploads.value.)
                    uploads.value!!.forEach {
                        if(it.user_id == userState.getUser().user_id){
                            enableSubmission.value = false
                        }
                    }
                }
            }
        }
    }
    suspend fun loadUser(userId: String): user? {
        return repo.getUserDetailsFromUserId(userId)
    }

    fun seeProblemSet(){
        SubMan.changeWhichOne(false)
        SubMan.id = assignment.value?.assignment_id ?: -1
    }

}