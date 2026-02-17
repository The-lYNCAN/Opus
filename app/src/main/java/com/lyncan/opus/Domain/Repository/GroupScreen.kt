package com.lyncan.opus.Domain.Repository

import com.lyncan.opus.data.Groups
import com.lyncan.opus.data.user

interface GroupScreen {
    suspend fun getGroupDetails(groupId: Int): Groups
    suspend fun getGroupMembers(groupId: Int): List<user>
    suspend fun getTotalSubjects(groupId: Int): Int
    suspend fun timeTableSetted(groupId: Int): Boolean
}