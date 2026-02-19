package com.lyncan.opus.Presentation.ui.components.Attendance

import android.util.Log
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.lyncan.opus.Presentation.viewmodels.AttendanceViewModel

@Composable
fun SubjectList(navController: NavController, viewModel: AttendanceViewModel) {

    val attendances = viewModel.retrievalState.collectAsState().value
    Log.d("SubjectList", "Retrieved attendances: $attendances")

    LazyColumn(modifier = Modifier.padding(top = 10.dp)) {
        item{

            Text("Attendance", style = MaterialTheme.typography.displayMedium)
            Text("Track your class participation", style = MaterialTheme.typography.titleMedium, color = Color.Black.copy(alpha = .8f))
            Spacer(modifier = Modifier.height(16.dp))
        }
        attendances.list.forEach { it ->
//                SubjectCard(subject, attendanceList, navController)
            item{
                SubjectCard(navController, it)
            }
        }



    }
}