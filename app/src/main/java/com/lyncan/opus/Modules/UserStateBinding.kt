package com.lyncan.opus.Modules

import com.lyncan.opus.DataLayer.Repositories.AttendanceDetails
import com.lyncan.opus.DataLayer.Repositories.SubjectManagementImpl
import com.lyncan.opus.Domain.Repository.AttendanceDetailsInter
import com.lyncan.opus.Domain.Repository.SubjectManagement
import com.lyncan.opus.Domain.Repository.UserStateInter
import com.lyncan.opus.UserState
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class UserStateBinding {

    @Binds
    @Singleton
    abstract fun bindUserState(
        impl: UserState
    ): UserStateInter

    @Binds
    @Singleton
    abstract fun bindAttendanceDetails(
        impl: AttendanceDetails
    ): AttendanceDetailsInter

    @Binds
    @Singleton
    abstract fun bindSubjectManagement(
        impl: SubjectManagementImpl
    ): SubjectManagement


}