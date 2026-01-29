package com.lyncan.opus.viewmodels

import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.credentials.CredentialManager
import androidx.credentials.GetCredentialRequest
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.lyncan.opus.MyApplication
import com.lyncan.opus.UserState
import com.lyncan.opus.data.user
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.auth.providers.Google
import io.github.jan.supabase.auth.providers.builtin.IDToken
import io.github.jan.supabase.postgrest.postgrest
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class LoginViewModel @Inject constructor(val userState: UserState): ViewModel() {
    private val userId: String? = null
    private val _user = MutableStateFlow<user?>(null)
    val user = _user


    init {
        if(MyApplication.Companion.supabase.auth.currentUserOrNull() == null){
            _user.value = null
        }else{
            val currentUser = MyApplication.Companion.supabase.auth.currentUserOrNull()
            viewModelScope.launch{
                userState.setUser(getUserData(currentUser!!.id))
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
                MyApplication.supabase.auth.signInWith(IDToken) {
                    this.idToken = idToken
                    provider = Google
                }

                val user = MyApplication.supabase.auth.currentUserOrNull()
                Log.d("LoginViewModel", "Supabase login successful: $user")
                if (user != null) {
                    userState.setUser(getUserData(user.id))
                    onSuccess()
                }
            } catch (e: Exception) {
                Log.e("LoginViewModel", "Supabase login failed", e)
            }
        }
    }

    private suspend fun getUserData(userId: String): user {
        return MyApplication.supabase.postgrest
            .from("users")
            .select {
                filter { eq("user_id", userId) }
            }
            .decodeSingle()
    }
}