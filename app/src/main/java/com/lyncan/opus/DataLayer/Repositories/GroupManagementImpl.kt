package com.lyncan.opus.DataLayer.Repositories

import com.lyncan.opus.DataLayer.Repositories.TimeTable.Local
import com.lyncan.opus.DataLayer.Repositories.TimeTable.Remote
import com.lyncan.opus.Domain.Repository.GroupManagement
import com.lyncan.opus.Domain.Repository.TimeTableRepositoryInt
import com.lyncan.opus.data.TimeTableEntry
import javax.inject.Inject

class GroupManagementImpl @Inject constructor(

): GroupManagement {
    override suspend fun createGroup(groupName: String, description: String?): Int? {
        TODO("Not yet implemented")
    }

    override suspend fun joinGroup(inviteCode: String): Int? {
        TODO("Not yet implemented")
    }

    override suspend fun deleteGroup(groupId: Int) {
        TODO("Not yet implemented")
    }

    override suspend fun exitGroup(groupId: Int) {
        TODO("Not yet implemented")
    }
}

class TimeTableRepositoryImpl @Inject constructor(
    private val remote: Remote,
    private val local: Local
): TimeTableRepositoryInt {
    override suspend fun createTimeTableEntry(entry: TimeTableEntry) {
        val entryRemote = remote.createTimeTableEntry(entry)!!.toEntity()
        local.createTimeTableEntry(entryRemote)

    }

    override suspend fun deleteTimeTableEntry(timeTableEntry: Int) {
        remote.deleteTimeTableEntry(timeTableEntry)
    }
}