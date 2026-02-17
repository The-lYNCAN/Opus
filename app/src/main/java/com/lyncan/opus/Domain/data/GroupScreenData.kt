package com.lyncan.opus.Domain.data

import com.lyncan.opus.Presentation.States.GroupState
import com.lyncan.opus.data.user

data class GroupScreenData(
    val groupName: String,
    val groupCode: String,
    val groupMembers: List<user>,
    val totalSubjects: Int,
    val timeTableSetted: Boolean,
    val admin: String
){
    fun toState(): GroupState {
        return GroupState(
            groupName = groupName,
            groupCode = groupCode,
            groupMembers = groupMembers,
            totalSubjects = totalSubjects,
            timeTableSetted = timeTableSetted,
            admin = admin
        )
    }
}
