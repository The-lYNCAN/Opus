package com.lyncan.opus.Presentation.viewmodels

import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.activity.result.ActivityResultLauncher
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.lyncan.opus.Domain.Repository.UserStateInter
import com.lyncan.opus.data.user
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.auth.providers.Google
import io.github.jan.supabase.auth.providers.builtin.IDToken
import io.github.jan.supabase.postgrest.postgrest
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class LoginViewModel @Inject constructor(
    private val userState: UserStateInter,
    private val supabase: SupabaseClient
): ViewModel() {
    private val userId: String? = null
    private val _user = MutableStateFlow<user?>(null)
    val user = _user


    init {
        if(supabase.auth.currentUserOrNull() == null){
            _user.value = null
        }else{
            val currentUser = supabase.auth.currentUserOrNull()
            viewModelScope.launch{
                userState.updateUser(getUserData(currentUser!!.id))
                _user.value = getUserData(currentUser.id)
            }
        }
    }

    fun launchGoogleSignIn(
        context: Context,
        launcher: ActivityResultLauncher<Intent>
    ) {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(
                "1096106886741-d45fi8v54k0d1p5gjopnj4g5pbvtsc2k.apps.googleusercontent.com"
            )
            .requestEmail()
            .build()

        val googleSignInClient = GoogleSignIn.getClient(context, gso)
        launcher.launch(googleSignInClient.signInIntent)
    }

    fun signInWithGoogleIdToken(
        idToken: String,
        onSuccess: () -> Unit
    ) {
        viewModelScope.launch {
            try {
                supabase.auth.signInWith(IDToken) {
                    this.idToken = idToken
                    provider = Google
                }
                val session = supabase.auth.currentSessionOrNull()

                if (session != null) {
                    userState.updateUser(getUserData(session.user!!.id))
                    Log.d("LoginViewModel", "User data retrieved: ${userState.getCurrentUser()}")
                    onSuccess()
                }

            } catch (e: Exception) {
                Log.e("LoginViewModel", "Supabase login failed", e)
            }
        }
    }

    private suspend fun getUserData(userId: String): user {
        return supabase.postgrest
            .from("users")
            .select {
                filter { eq("user_id", userId) }
            }
            .decodeSingle()
    }
}