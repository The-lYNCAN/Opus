package com.lyncan.opus.viewmodels

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lyncan.opus.Repositories.SubjectRepository
import com.lyncan.opus.Repositories.TimeTableRepository
import com.lyncan.opus.data.Subject
import com.lyncan.opus.entities.SubjectEntity
import com.lyncan.opus.entities.TimeTableEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TimeTableViewModel @Inject constructor(
    private val timeRepo: TimeTableRepository,
    private val subjectRepo: SubjectRepository
) : ViewModel() {
    fun getTimeTableEntries() = timeRepo.getAllTimeTableEntries()

    fun getAllSubjects() = subjectRepo.getAllSubjects()

    fun deleteTimeTableEntry(entry: TimeTableEntity) {
        viewModelScope.launch {
            val entry = timeRepo.getTimeTableById(entry.id)
            if (entry != null) {
                timeRepo.delete(entry)
            }
        }
    }
}