package com.lyncan.opus.Modules

import com.lyncan.opus.SubjectManagement
import com.lyncan.opus.Modules.SupabaseRepository
import com.lyncan.opus.UserState
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object AppModule{

    @Provides
    @Singleton
    fun provideSubjectManagement(
        repository: SupabaseRepository,
        userState: UserState
    ): SubjectManagement {
        return SubjectManagement(
            repo = repository,
            userState = userState
        )
    }



}