package com.lyncan.opus.Modules

import android.content.Context
import com.lyncan.opus.Repositories.SubjectManagement
import com.lyncan.opus.Repositories.SubjectRepository
import com.lyncan.opus.Repositories.SupabaseRepository
import com.lyncan.opus.Repositories.UserState
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object AppModule{

    @Provides
    @Singleton
    fun provideSubjectManagement(
        repository: SupabaseRepository,
        userState: UserState,
        subRepo: SubjectRepository,
        @ApplicationContext context: Context,
        subdb: SubjectRepository
    ): SubjectManagement {
        return SubjectManagement(
            repo = repository,
            userState = userState,
            subRepo = subRepo,
            context,
            subdb
        )
    }



}