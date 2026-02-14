package com.lyncan.opus.viewmodels

import android.util.Log
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
        Log.d("TimeTableManagementFormViewModel", "Adding TimeTableEntry: $timeEntry")
        viewModelScope.launch {
            val id = subMan.createTimeTableEntry(TimeTableEntry(
                subjectid = timeEntry.subjectid,
                day = timeEntry.day,
                startTime = timeEntry.startTime,
                endTime = timeEntry.endTime,
                type = timeEntry.type,
                room = timeEntry.room
            ))
            Log.d("Inserted value in addtimetable:",id.toString())
            if(id?.id != null){
                val entry = timeEntry.copy(id = id.id)
                timeRepo.insert(entry)
            }
        }
    }
}