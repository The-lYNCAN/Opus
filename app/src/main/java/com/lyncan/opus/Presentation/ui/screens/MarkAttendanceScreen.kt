package com.lyncan.opus.Presentation.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.lyncan.opus.Presentation.ui.components.markAttendance.Card
import com.lyncan.opus.Presentation.viewmodels.AttendanceViewModel

@Composable
fun MarkAttendanceScreen(viewModel: AttendanceViewModel, marked: State<Boolean>) {
//    val viewModel = hiltViewModel<MarkAttendanceViewModel>()
//    viewModel.retrieve()
    val attendanceItems = viewModel.attendanceItems.collectAsState()

    Column(modifier = Modifier
        .statusBarsPadding()
        .fillMaxSize()) {
        Text("Mark Attendance", modifier = Modifier.padding(16.dp), style = MaterialTheme.typography.headlineMedium)
        attendanceItems.value.forEach {
            Card(it, { viewModel.attendedFunc(it.attendance.id) },
                { viewModel.bunkedFunc(it.attendance.id) })
            // commit some small change to test commit history
        }
    }
}