package com.lyncan.opus.DataLayer.Repositories

import com.lyncan.opus.Domain.Repository.Authentication
import com.lyncan.opus.data.user
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.postgrest.postgrest
import javax.inject.Inject

class AuthenticationImpl @Inject constructor(
    private val supabaseClient: SupabaseClient
): Authentication {
    override suspend fun getCurrentUser(): user? {
        val currentUser = supabaseClient.auth.currentUserOrNull()
        if(currentUser == null) {
            return null
        }
        return supabaseClient.postgrest.from("users").select() {
            filter {
                eq("id", currentUser.id)
            }
        }.decodeSingle<user>()


    }

    override suspend fun userLoggedIn(): Boolean {
        return supabaseClient.auth.currentUserOrNull() != null
    }
}