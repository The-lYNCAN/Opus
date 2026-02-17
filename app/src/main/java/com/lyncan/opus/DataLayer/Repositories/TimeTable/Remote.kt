package com.lyncan.opus.DataLayer.Repositories.TimeTable

import com.lyncan.opus.DataLayer.Repositories.SupabaseRepository
import com.lyncan.opus.Domain.Repository.UserStateInter
import com.lyncan.opus.data.Groups
import com.lyncan.opus.data.TimeTableEntry
import kotlinx.datetime.Clock
import javax.inject.Inject

class Remote @Inject constructor(
    private val repo: SupabaseRepository,
    private val userState: UserStateInter
): RemoteInter {
    override suspend fun createTimeTableEntry(entry: TimeTableEntry): TimeTableEntry? {
        val group_id = userState.getCurrentUser()?.group_id
        val ttId = repo.database().from("timetableentries").insert(
            TimeTableEntry(
                group = group_id!!,
                startTime = entry.startTime,
                endTime = entry.endTime,
                room = entry.room,
                subjectid = entry.subjectid,
                day = entry.day,
                type = entry.type,
            )
        ){
            select()
        }.decodeSingleOrNull<TimeTableEntry>()
        updateTimeForTimeTableInGroup(groupId = group_id)
        return ttId
    }

    override suspend fun deleteTimeTableEntry(timeTableEntry: Int) {
        val group_id = userState.getCurrentUser()?.group_id
        repo.database().from("timetableentries").delete {
            select()
            filter {
                eq("id", timeTableEntry)
            }
        }
        updateTimeForTimeTableInGroup(groupId = group_id?:-1)
    }

    override suspend fun updateTimeForTimeTableInGroup(groupId: Int) {
        repo.database().from("Group").update({
            Groups::time_table_at setTo Clock.System.now().toEpochMilliseconds().toString()
        }){
            filter {
                eq("group_id", groupId)
            }
        }
    }

}