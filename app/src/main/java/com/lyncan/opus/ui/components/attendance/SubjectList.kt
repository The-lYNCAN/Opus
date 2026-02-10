package com.lyncan.opus.ui.components.attendance

import android.util.Log
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.lyncan.opus.entities.AttendanceEntity
import com.lyncan.opus.entities.SubjectEntity
import com.lyncan.opus.viewmodels.AttendanceViewModel
import kotlin.collections.emptyList

@Composable
fun SubjectList(navController: NavController, viewModel: AttendanceViewModel) {
    val attendance = viewModel.getAllAttendance().collectAsState(emptyList())
    val allAttendance = remember { mutableStateOf<Map<SubjectEntity, List<AttendanceEntity>>?>(null) }
    LaunchedEffect(attendance.value) {
        val everything = attendance.value.groupBy { it.subjectId }
        allAttendance.value = everything.mapKeys { entry ->
            val subjectId = entry.key
            viewModel.getSubjectById(subjectId) ?: SubjectEntity(subjectId, "Unknown Subject", "N/A", 0)
        }
    }
    Log.d("SubjectListAttendance", "Attendance grouped by subject: $attendance")
    LazyColumn(modifier = Modifier.padding(top = 10.dp)) {
        item{

            Text("Attendance", style = MaterialTheme.typography.displayMedium)
            Text("Track your class participation", style = MaterialTheme.typography.titleMedium, color = Color.Black.copy(alpha = .8f))
            Spacer(modifier = Modifier.height(16.dp))
        }
        allAttendance.value?.forEach { subject, attendanceList ->
//                SubjectCard(subject, attendanceList, navController)
            item{
                SubjectCard(navController, subject, attendanceList)
            }
        }


    }
}