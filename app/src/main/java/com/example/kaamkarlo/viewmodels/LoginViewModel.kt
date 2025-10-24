package com.example.kaamkarlo.viewmodels

import android.content.ContentValues.TAG
import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kaamkarlo.MyApplication
import com.example.kaamkarlo.UserState
import com.example.kaamkarlo.data.user
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.auth.providers.Google
import io.github.jan.supabase.auth.providers.builtin.IDToken
import io.github.jan.supabase.postgrest.postgrest
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class LoginViewModel @Inject constructor(val userState: UserState): ViewModel() {
    private val userId: String? = null
    private val _user = MutableStateFlow<user?>(null)
    val user = _user


    init {
        if(MyApplication.supabase.auth.currentUserOrNull() == null){
            _user.value = null
        }else{
            val currentUser = MyApplication.supabase.auth.currentUserOrNull()
            viewModelScope.launch{
                userState.setUser(getUserData(currentUser!!.id))
                _user.value = getUserData(currentUser.id)
            }
        }
    }

    fun loginWithGoogle(context: Context, coroutineScope: CoroutineScope, onLogin: () -> Unit) {
        val credentialManager = androidx.credentials.CredentialManager.create(context)
        val googleIdOption: GetGoogleIdOption = GetGoogleIdOption.Builder().setFilterByAuthorizedAccounts(true).setServerClientId("1096106886741-94rvas0odgar1h5sru4hcju9q7u98g4b.apps.googleusercontent.com").setAutoSelectEnabled(true).build()
        val request = androidx.credentials.GetCredentialRequest.Builder().addCredentialOption(googleIdOption).build()
        coroutineScope.launch {
            // Call suspend functions here
//                    launcher.launch(googleSignInClient.signInIntent)
            val result = credentialManager.getCredential(request= request, context=context)
            val credential = result.credential
            val googleIdToken = GoogleIdTokenCredential.createFrom(credential.data)
            Log.i(TAG, googleIdToken.toString())
            Toast.makeText(context, "You are signed in as ${googleIdToken.givenName} ${googleIdToken.familyName}", Toast.LENGTH_LONG).show()
            MyApplication.supabase.auth.signInWith(IDToken) {
                idToken = googleIdToken.idToken
                provider = Google
            }
            if(MyApplication.supabase.auth.currentUserOrNull() != null) {
                userState.setUser(getUserData(MyApplication.supabase.auth.currentUserOrNull()!!.id))
                onLogin()
            }
        }
    }

    suspend fun getUserData(userId: String): user{
        return MyApplication.supabase.postgrest.from("users").select(){
            filter {
                eq("user_id", userId)
            }
        }.decodeSingle<user>()
    }

    fun LoginBtn(){

    }
}