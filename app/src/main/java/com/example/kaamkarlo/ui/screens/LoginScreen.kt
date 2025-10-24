package com.example.kaamkarlo.uI.screens


import android.content.ContentValues.TAG
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.kaamkarlo.MyApplication
import com.example.kaamkarlo.uI.navigation.Route
import com.example.kaamkarlo.viewmodels.LoginViewModel
import com.example.kaamkarlo.viewmodels.MainViewModel
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.auth.providers.Google
import io.github.jan.supabase.auth.providers.builtin.IDToken
import kotlinx.coroutines.launch


fun onLogin(navController: NavController) {
    // This function can be used to handle post-login actions, such as navigating to the home screen or updating UI state.
    // Currently, it is empty and can be implemented as needed.
    navController.navigate(Route.Home.route)
}

@Composable
fun LoginScreen(
    navController: NavController,
    onLogin: () -> Unit,
    sharedViewModel: MainViewModel
) {
    val ViewModel: LoginViewModel = hiltViewModel()
    val coroutineScope = rememberCoroutineScope ()
    val context = LocalContext.current
    LaunchedEffect(sharedViewModel.repo.isAuthenticated()) {
        Log.d("checking login status inside Login Screen", "isAuthenticated: ${sharedViewModel.repo.isAuthenticated()}")
    }
    Column (
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("KAAM KARLO BHEN CHOD!", fontSize = 30.sp)
        Spacer(modifier = Modifier.height(56.dp))
        Button(
            onClick = {
                ViewModel.loginWithGoogle(context, coroutineScope, onLogin={
                    onLogin()
            })
            },
            modifier = Modifier.width(300.dp).height(75.dp),
            shape = RoundedCornerShape(14.dp)
        ) {
            Text("Login with SRM ID", fontSize = 24.sp)
        }
        Text("Welcome ${MyApplication.supabase.auth.currentUserOrNull()}", fontSize = 24.sp)
    }
}
