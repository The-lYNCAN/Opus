package com.lyncan.opus.uI.screens


import android.app.Activity
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.navigation.NavController
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.common.api.ApiException
import com.lyncan.opus.MyApplication
import com.lyncan.opus.uI.navigation.Route
import com.lyncan.opus.viewmodels.LoginViewModel
import com.lyncan.opus.viewmodels.MainViewModel
import io.github.jan.supabase.auth.auth


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
    val viewModel: LoginViewModel = hiltViewModel()
    val context = LocalContext.current

    // ✅ Activity Result Launcher
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
            try {
                val account = task.getResult(ApiException::class.java)
                val idToken = account.idToken

                if (idToken != null) {
                    viewModel.signInWithGoogleIdToken(
                        idToken = idToken,
                        onSuccess = {
                            onLogin()
                        }
                    )
                }
            } catch (e: ApiException) {
                Log.e("LoginScreen", "Google sign-in failed", e)
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text("Login", fontSize = 30.sp)
        Spacer(modifier = Modifier.height(56.dp))

        Button(
            onClick = {
                viewModel.launchGoogleSignIn(context, launcher)
            },
            modifier = Modifier
                .width(300.dp)
                .height(75.dp),
            shape = RoundedCornerShape(14.dp)
        ) {
            Text("Login with Google", fontSize = 24.sp)
        }
    }
}