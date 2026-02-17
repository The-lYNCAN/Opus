package com.lyncan.opus.Domain.Repository

interface GroupManagement {
    suspend fun createGroup(groupName: String, description: String?= null): Int?
    suspend fun joinGroup(inviteCode: String): Int?
    suspend fun deleteGroup(groupId: Int)
    suspend fun exitGroup(groupId: Int)
}