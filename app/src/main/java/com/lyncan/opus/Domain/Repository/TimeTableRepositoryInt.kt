package com.lyncan.opus.Domain.Repository

import com.lyncan.opus.data.TimeTableEntry

interface TimeTableRepositoryInt {
    suspend fun createTimeTableEntry(entry: TimeTableEntry)
    suspend fun deleteTimeTableEntry(timeTableEntry: Int)
}