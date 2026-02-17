package com.lyncan.opus.Domain.UseCases.SubjectManaementUseCase

import com.lyncan.opus.Domain.Repository.SubjectManagement
import javax.inject.Inject

class GroupCreationUseCase @Inject constructor(
    private val subjectManagement: SubjectManagement
) {
    operator fun invoke(){

    }
}