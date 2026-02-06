package com.lyncan.opus.ui.screens

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.navigation.NavController
import com.lyncan.opus.ui.components.SubjectManagement.AddSubject
import com.lyncan.opus.ui.components.SubjectManagement.Header
import com.lyncan.opus.ui.components.SubjectManagement.SubjectList
import com.lyncan.opus.viewmodels.MainViewModel
import com.lyncan.opus.viewmodels.SubjectManagementViewModel

@Composable
fun SubjectManagement(navController: NavController, mainViewModel: MainViewModel) {
    val viewModel: SubjectManagementViewModel = hiltViewModel()
    val totalAss= remember { mutableStateOf(0) }
    val loading = viewModel.loading.collectAsState()
    val subjects = viewModel.subjects.collectAsState()
    val addSubject = remember { mutableStateOf(false) }
    val update = remember { mutableStateOf(false) }
    val subjectName = remember { mutableStateOf("") }
    val subjectCode = remember { mutableStateOf("") }


    val selectedIndex = remember { mutableIntStateOf(0) }

    viewModel.subMan.subjectList.forEach { subject, assignment ->
        totalAss.value += assignment.size
    }
    val lifecycleOwner = LocalLifecycleOwner.current
    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                Log.d("SubjectManagement", "Screen resumed, refreshing subjects.")
                viewModel.resetSubState() // Assuming this function refreshes your data

                Log.d("SubjectManagement", "Subjects after refresh: ${subjects.value}")
            }
        }

        // Add the observer to the lifecycle
        lifecycleOwner.lifecycle.addObserver(observer)

        // The onDispose block is called when the composable leaves the screen
        onDispose {
            Log.d("SubjectManagement", "Disposing observer.")
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }
    LaunchedEffect(subjects.value) {
        Log.d("SubjectManagement", "Subjects updated: ${subjects.value}")
    }

    if(!loading.value) {
        Column(modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()) {
            Header()
            SubjectList(
                subjects, { subId -> viewModel.deltFunc(subId) }, addSubject,

                { subjectName, subjectCode, type ->
                    viewModel.createSubject(
                        subjectName,
                        subjectCode,
                        type
                    )
                }, update = update, subjectName = subjectName, subjectCode = subjectCode,
                selectedIndex = selectedIndex
            )
            if(!addSubject.value){
                AddSubject(addSubject, update,
                    subjectCode, subjectName, selectedIndex)
            }
        }
    }else{
        Column(modifier= Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {

            CircularProgressIndicator(
                modifier = Modifier.size(48.dp)
            )
            Text("Deleting Subject", modifier = Modifier.padding(top = 8.dp))
        }
    }
}
