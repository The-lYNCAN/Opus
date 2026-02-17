package com.lyncan.opus.Modules

import android.content.Context
import com.lyncan.opus.DataLayer.Repositories.SubjectManagement
import com.lyncan.opus.DataLayer.Repositories.SubjectRepository
import com.lyncan.opus.DataLayer.Repositories.SupabaseRepository
import com.lyncan.opus.DataLayer.Repositories.TimeTableRepository
import com.lyncan.opus.Domain.Repository.UserStateInter
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
        userState: UserStateInter,
        subRepo: SubjectRepository,
        @ApplicationContext context: Context,
        subdb: SubjectRepository,
        ttRepo: TimeTableRepository
    ): SubjectManagement {
        return SubjectManagement(
            repo = repository,
            userState = userState,
            subRepo = subRepo,
            context,
            subdb,
            ttRepo
        )
    }



}