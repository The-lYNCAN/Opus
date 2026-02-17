package com.lyncan.opus.DataLayer.Repositories.TimeTable

import com.lyncan.opus.DataLayer.Repositories.TimeTableRepository
import com.lyncan.opus.DataLayer.local.entities.TimeTableEntity
import javax.inject.Inject

class Local @Inject constructor(
    private val localTimeRepo: TimeTableRepository
): LocalInterface {
    override suspend fun createTimeTableEntry(entry: TimeTableEntity) {
        localTimeRepo.insert(entry)
    }

    override suspend fun deleteTimeTableEntry(timeTableEntry: Int) {
        localTimeRepo.delete(timeTableEntry)
    }

}