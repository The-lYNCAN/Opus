package com.lyncan.opus.viewmodels

import android.util.Log
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lyncan.opus.Repositories.SubjectManagement
import com.lyncan.opus.Repositories.SubjectRepository
import com.lyncan.opus.Repositories.TimeTableRepository
import com.lyncan.opus.data.TimeTableEntry
import com.lyncan.opus.entities.TimeTableEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TimeTableManagementFormViewModel @Inject constructor(
    private val subjectrepository: SubjectRepository,
    private val timeRepo: TimeTableRepository,
    private val subMan: SubjectManagement
) : ViewModel() {
    fun getSubjects() = subjectrepository.getAllSubjects()

    init {
        viewModelScope.launch {
            getSubjects().collect {
                Log.d("TimeTableManagementFormViewModel", "Subjects fetched: ${it.size}")
            }
        }
    }

    fun addTimeTable(timeEntry: TimeTableEntity) {
        viewModelScope.launch {
            timeRepo.insert(timeEntry)
            subMan.createTimeTableEntry(TimeTableEntry(
                subjectid = timeEntry.subjectid,
                day = timeEntry.day,
                startTime = timeEntry.startTime,
                endTime = timeEntry.endTime,
                type = timeEntry.type,
                room = timeEntry.room
            ))
        }
    }
}