package com.lyncan.opus.DataLayer.Repositories.TimeTable

import com.lyncan.opus.data.TimeTableEntry

interface RemoteInter {
    suspend fun createTimeTableEntry(entry: TimeTableEntry): TimeTableEntry?
    suspend fun deleteTimeTableEntry(timeTableEntry: Int)
    suspend fun updateTimeForTimeTableInGroup(groupId: Int)
}