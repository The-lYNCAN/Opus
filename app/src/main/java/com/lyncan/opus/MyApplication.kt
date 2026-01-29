package com.lyncan.opus

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.Auth
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.postgrest.Postgrest

@HiltAndroidApp
class MyApplication: Application() {
    override fun onCreate() {
        super.onCreate()
        supabase = createSupabaseClient(
            supabaseUrl = "https://tuqcjuywjjtwidvdprel.supabase.co",
            supabaseKey = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6InR1cWNqdXl3amp0d2lkdmRwcmVsIiwicm9sZSI6ImFub24iLCJpYXQiOjE3NDY3NzE5OTEsImV4cCI6MjA2MjM0Nzk5MX0.SonTv7wP4kw_mQsakdfPC3Jg8wPw7Oi6gMxXLA5i2fw"
        ) {
            install(Auth){

                autoLoadFromStorage = true
                autoSaveToStorage = true
                alwaysAutoRefresh = true
            }
            install(Postgrest)
        }

    }


    companion object{
        lateinit var supabase: SupabaseClient
    }
}