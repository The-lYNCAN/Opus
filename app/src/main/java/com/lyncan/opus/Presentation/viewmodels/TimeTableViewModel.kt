package com.lyncan.opus.Presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lyncan.opus.DataLayer.Repositories.SubjectManagement
import com.lyncan.opus.DataLayer.Repositories.SubjectRepository
import com.lyncan.opus.DataLayer.Repositories.TimeTableRepository
import com.lyncan.opus.DataLayer.local.entities.TimeTableEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TimeTableViewModel @Inject constructor(
    private val timeRepo: TimeTableRepository,
    private val subjectRepo: SubjectRepository,
    private val subMan: SubjectManagement
) : ViewModel() {
    fun getTimeTableEntries() = timeRepo.getAllTimeTableEntries()

    fun getAllSubjects() = subjectRepo.getAllSubjects()

    fun deleteTimeTableEntry(entry: TimeTableEntity) {
        viewModelScope.launch {
            val entryR = timeRepo.getTimeTableById(entry.id)
            if (entryR != null) {
                timeRepo.delete(entryR.id)
                subMan.deleteTimeTableEntry(entryR.id)
            }
        }
    }
}