package com.lyncan.opus.Domain.UseCases.GroupScreenUseCase

import com.lyncan.opus.Common.Resourse
import com.lyncan.opus.DataLayer.Repositories.GroupScreenImpl
import com.lyncan.opus.Domain.Repository.UserStateInter
import com.lyncan.opus.Domain.data.GroupScreenData
import javax.inject.Inject

class UserGroupJoinedUseCase @Inject constructor(
    private val userStateInter: UserStateInter,
    private val groupScreen: GroupScreenImpl
) {
    suspend operator fun invoke(): Resourse<GroupScreenData>{
        val groupId = userStateInter.getCurrentUser()?.group_id?.toInt()
        if (groupId != null) {
            val groupDetails = groupScreen.getGroupDetails(groupId)
            val groupCode = groupDetails.invite_code
            val groupname = groupDetails.group_name
            val groupMember = groupScreen.getGroupMembers(groupId)
            val totalSubjects = groupScreen.getTotalSubjects(groupId)
            val timeTableSet = groupScreen.timeTableSetted(groupId)
            return Resourse.Success<GroupScreenData>(GroupScreenData(
                groupName = groupname,
                groupCode = groupCode,
                groupMembers = groupMember,
                totalSubjects = totalSubjects,
                timeTableSetted = timeTableSet,
                admin = groupDetails.admin
            ))
            // You can return or use the groupDetails as needed
        } else {
            // Handle the case where the user ID is null (e.g., user not logged in)
            return Resourse.Error("User not logged in")
        }
    }

}