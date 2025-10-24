package com.example.kaamkarlo


import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.runtime.collectAsState
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.kaamkarlo.uI.navigation.AppNavigation
import com.example.kaamkarlo.ui.theme.KaamKarloTheme
import com.example.kaamkarlo.viewmodels.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
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

