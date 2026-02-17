package com.lyncan.opus.DataLayer.Repositories

import com.lyncan.opus.Domain.Repository.GroupScreen
import com.lyncan.opus.data.Groups
import com.lyncan.opus.data.Subject
import com.lyncan.opus.data.TimeTableEntry
import com.lyncan.opus.data.user
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.postgrest.postgrest
import javax.inject.Inject

class GroupScreenImpl @Inject constructor(
    supabase: SupabaseClient,
): GroupScreen {
    val db = supabase.postgrest
    override suspend fun getGroupDetails(groupId: Int): Groups {
        return db.from("Group").select {
            filter {
                eq("group_id", groupId)
            }
        }.decodeSingle<Groups>()
    }

    override suspend fun getGroupMembers(groupId: Int): List<user> {
        val members = db.from("users").select {
            filter {
                eq("group_id", groupId)
            }
        }.decodeList<user>()
        return members
    }

    override suspend fun getTotalSubjects(groupId: Int): Int {
        return db.from("sujet").select {
            filter {
                eq("group_id", groupId)
            }
        }.decodeList<Subject>().size
    }

    override suspend fun timeTableSetted(groupId: Int): Boolean {
        return db.from("timetableentries").select {
            filter {
                eq("group", groupId)
            }
        }.decodeList<TimeTableEntry>().isNotEmpty()
    }
}