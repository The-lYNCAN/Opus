package com.lyncan.opus.DataLayer.Repositories

import com.lyncan.opus.Domain.Repository.SubjectManagement
import javax.inject.Inject

class SubjectManagementImpl @Inject constructor(
    private val subjectManagement: SubjectRepository
): SubjectManagement {
    override suspend fun getSubjectById(subjectId: Int) = subjectManagement.getSubjectById(subjectId)
}