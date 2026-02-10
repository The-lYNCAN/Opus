package com.lyncan.opus.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.lyncan.opus.ui.components.markAttendance.Card
import com.lyncan.opus.viewmodels.MarkAttendanceViewModel

@Composable
fun MarkAttendanceScreen() {
    val viewModel = hiltViewModel<MarkAttendanceViewModel>()
    val attendanceItems = viewModel.attendanceItems.collectAsState()
    Column(modifier = Modifier
        .statusBarsPadding()
        .fillMaxSize()) {
        Text("Mark Attendance", modifier = Modifier.padding(16.dp), style = MaterialTheme.typography.headlineMedium)
        attendanceItems.value.forEach {
            Card(it)
        }
    }
}