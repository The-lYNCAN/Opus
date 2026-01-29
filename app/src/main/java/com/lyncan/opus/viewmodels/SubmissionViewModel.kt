package com.lyncan.opus.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lyncan.opus.SubjectManagement
import com.lyncan.opus.Modules.SupabaseRepository
import com.lyncan.opus.UserState
import com.lyncan.opus.data.Assignment
import com.lyncan.opus.data.Uploads
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.jan.supabase.storage.UploadStatus
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

@HiltViewModel
class SubmissionViewModel @Inject constructor(val repo: SupabaseRepository,
                                              val subRep: SubjectManagement,
                                              val user: UserState
): ViewModel() {

    private val _assignment = MutableStateFlow<Assignment?>(null)
    val assignment = _assignment
    private val _user = MutableStateFlow(user.getUser())
    val currentUser = _user

    fun setAssignment(assignmentId: Int) {
        subRep.getAllData().first.values.forEach {
            it.forEach {
                if(it.assignment_id == assignmentId){
                    _assignment.value = it
                }

            }
        }
    }
    fun getLink(key: String): String{
        return repo.getPublicKey(key)
    }
    fun handleUpload(filePath: String, assignmentPdf: File): Flow<UploadStatus> {
        return repo.uploadPdf(assignmentPdf, filePath)
    }

    fun makeEntry(uploadDetails: Uploads){
        viewModelScope.launch {
            repo.upload(uploadDetails = uploadDetails)
        }
    }

}