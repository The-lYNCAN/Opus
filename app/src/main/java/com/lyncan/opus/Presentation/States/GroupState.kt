package com.lyncan.opus.Presentation.States

import com.lyncan.opus.data.user

data class GroupState(val groupName: String,
                      val groupCode: String,
                      val groupMembers: List<user>,
                      val totalSubjects: Int,
                      val timeTableSetted: Boolean,
                        val admin: String
)
