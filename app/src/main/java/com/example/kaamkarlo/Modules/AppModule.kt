package com.example.kaamkarlo.Modules

import com.example.kaamkarlo.SubjectManagement
import com.example.kaamkarlo.SupabaseRepository
import com.example.kaamkarlo.UserState
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
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