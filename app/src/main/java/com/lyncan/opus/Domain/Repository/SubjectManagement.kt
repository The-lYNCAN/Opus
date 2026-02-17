package com.lyncan.opus.Domain.Repository

import com.lyncan.opus.DataLayer.local.entities.SubjectEntity

interface SubjectManagement {
    suspend fun getSubjectById(subjectId: Int): SubjectEntity?
}