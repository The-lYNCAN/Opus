package com.lyncan.opus.Modules

import android.content.Context
import com.lyncan.opus.MyApplication
import com.lyncan.opus.Repositories.SupabaseRepository
import com.lyncan.opus.Repositories.UserState
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.Auth
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.storage.Storage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AuthModule {

    @Provides
    @Singleton
    fun provideClient(): SupabaseClient{
        return createSupabaseClient(
            supabaseUrl = "https://tuqcjuywjjtwidvdprel.supabase.co",
            supabaseKey = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6InR1cWNqdXl3amp0d2lkdmRwcmVsIiwicm9sZSI6ImFub24iLCJpYXQiOjE3NDY3NzE5OTEsImV4cCI6MjA2MjM0Nzk5MX0.SonTv7wP4kw_mQsakdfPC3Jg8wPw7Oi6gMxXLA5i2fw"
        ) {
            install(Auth)
            install(Postgrest)
            install(Storage)
        }
    }

    @Provides
    @Singleton
    fun provideApplicationScope(): CoroutineScope{
        return CoroutineScope(SupervisorJob() + Dispatchers.IO)
    }

    @Provides
    @Singleton
    fun provideSupabaseRepository(
        supabaseClient: SupabaseClient,
        applicationScope: CoroutineScope,
        @ApplicationContext context: Context,
    ): SupabaseRepository {
        return SupabaseRepository(
            supabaseClient = supabaseClient,
            applicationScope = applicationScope,
            context = context,
        )
    }

    @Provides
    @Singleton
    fun provideContext(application: MyApplication): Context{
        return application.applicationContext
    }

    @Provides
    @Singleton
    fun provideUserState(): UserState {
        return UserState()
    }
}