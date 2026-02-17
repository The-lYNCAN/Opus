package com.lyncan.opus.DataLayer.Repositories.TimeTable

import com.lyncan.opus.DataLayer.local.entities.TimeTableEntity

interface LocalInterface {
    suspend fun createTimeTableEntry(entry: TimeTableEntity)
    suspend fun deleteTimeTableEntry(timeTableEntry: Int)
}