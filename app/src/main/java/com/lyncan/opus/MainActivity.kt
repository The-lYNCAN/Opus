package com.lyncan.opus


import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.lyncan.opus.Modules.SupabaseRepository
import com.lyncan.opus.uI.navigation.AppNavigation
import com.lyncan.opus.ui.theme.KaamKarloTheme
import com.lyncan.opus.viewmodels.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @Inject lateinit var supabase: SupabaseRepository
    private val viewModel: MainViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {



        installSplashScreen().apply {
            setKeepOnScreenCondition { !viewModel.loaded.value }
//            setKeepOnScreenCondition { false }
        }
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            KaamKarloTheme {
                AppNavigation()
            }
        }
    }
}

