package com.lyncan.opus.Presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.lyncan.opus.DataLayer.Repositories.SupabaseRepository
import com.lyncan.opus.Presentation.ui.navigation.AppNavigation
import com.lyncan.opus.Presentation.ui.theme.KaamKarloTheme
import com.lyncan.opus.Presentation.viewmodels.MainActViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @Inject
    lateinit var supabase: SupabaseRepository
    private val viewModel: MainActViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {

        installSplashScreen().apply {
//            screen will load when false is passed
            setKeepOnScreenCondition { viewModel.loaded.value }
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