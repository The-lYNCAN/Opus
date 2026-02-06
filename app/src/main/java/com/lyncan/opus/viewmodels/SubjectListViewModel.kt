package com.lyncan.opus.viewmodels

import android.util.Log
import androidx.compose.runtime.MutableIntState
import androidx.compose.runtime.MutableState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lyncan.opus.Repositories.SubjectManagement
import com.lyncan.opus.Repositories.SubjectRepository
import com.lyncan.opus.Repositories.UserState
import com.lyncan.opus.data.user
import com.lyncan.opus.entities.SubjectEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.WhileSubscribed
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SubjectListViewModel @Inject constructor(
    private val subRepo: SubjectRepository,
    private val userState: UserState,
    private var subMan: SubjectManagement
): ViewModel() {
    private var _subject = getAllSubjects().stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        emptyList()
    )
    val subjects = _subject
    private val _group_id = MutableStateFlow(userState.getUser().group_id)
    val group_id = _group_id
    fun getAllSubjects() = subRepo.getAllSubjects()
    init {
        viewModelScope.launch {
            val subs = getAllSubjects()
            subs.collect {
                Log.d("From the SubjectListVM", "Subject: $it")
            }
//            Log.d("From the SubjectListVM", "Initialized with group_id: ${getAllSubjects().toList()}")
        }
    }

    suspend fun updateSubject(subjectId: Int, newName: String, newCode: String?,
                              type: Int = 0) {
        subMan.updateSubject(subjectId, newName, newCode, userState.getUser().group_id ?: -1, type=type)
        subRepo.update(SubjectEntity(id = subjectId, name = newName, code =  newCode, type=type))
    }

    suspend fun updateValues(
        index: Int,
        subjectName: MutableState<String>,
        subjectCode: MutableState<String>,
        selectedIndex: MutableIntState
    ) {
        val subject = subRepo.getSubjectById(index)
        Log.d("SubjectListViewModel", "Updating values for subject ID $index: $subject")
        subjectName.value = subject?.name ?: ""
        subjectCode.value = subject?.code ?: ""
        selectedIndex.intValue = subject?.type ?: 0
    }

}