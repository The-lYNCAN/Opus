package com.lyncan.opus.viewmodels

import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.lyncan.opus.Repositories.SubjectManagement
import com.lyncan.opus.Repositories.SupabaseRepository
import com.lyncan.opus.data.Subject
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.jan.supabase.storage.UploadStatus
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UploadAssignmentViewModel @Inject constructor(
    val subMan: SubjectManagement,
    val repo: SupabaseRepository
): ViewModel() {
    private val _loading = MutableStateFlow(false)
    val loading = _loading

    fun getSubjects(): MutableSet<Subject>{
        val subjects = subMan.subjectList.keys
        return subjects
    }
    fun uploadAssignment(subjectId: Int, subName: String, assignment: Uri, dueDate: String, navController: NavController) {
        viewModelScope.launch {
            val progress = repo.uploadAssignment(subjectId, subName, assignment, dueDate.split("T")[0])
            loading.value = true
            progress.collect {
                when (it) {
                    is UploadStatus.Progress -> {
                        val percent = (it.totalBytesSend.toFloat()/it.contentLength.toFloat()) * 100
                        Log.d("UploadAssignmentViewModel", "Upload progress: $percent%")
                    }

                    is UploadStatus.Success -> {
                        Log.d("UploadAssignmentViewModel", "Upload completed successfully.")
                        navController.popBackStack()
                        loading.value = false
                    }
                }
            }
        }
    }
}