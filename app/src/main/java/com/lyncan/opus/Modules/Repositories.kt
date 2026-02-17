package com.lyncan.opus.Modules

import com.lyncan.opus.DataLayer.Repositories.AuthenticationImpl
import com.lyncan.opus.Domain.Repository.Authentication
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.github.jan.supabase.SupabaseClient
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object Repositories {
    @Provides
    @Singleton
    fun provideAuthentication(
        supabaseClient: SupabaseClient
    ): Authentication{
        return AuthenticationImpl(supabaseClient)
    }

}