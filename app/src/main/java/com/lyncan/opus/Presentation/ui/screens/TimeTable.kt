package com.lyncan.opus.Presentation.ui.screens

import android.util.Log
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.lyncan.opus.DataLayer.local.entities.TimeTableEntity
import com.lyncan.opus.Presentation.ui.components.TimeTable.BreakCard
import com.lyncan.opus.Presentation.ui.components.TimeTable.DayChip
import com.lyncan.opus.Presentation.ui.components.TimeTable.LectureCard
import com.lyncan.opus.Presentation.ui.components.TimeTable.TimeTableManagementForm
import com.lyncan.opus.Presentation.viewmodels.TimeTableViewModel
import java.time.LocalTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale


fun String.toLocalTime(): LocalTime {
    val formatter = DateTimeFormatter.ofPattern("hh:mm a", Locale.ENGLISH)
    return LocalTime.parse(this.trim(), formatter)
}
@Composable
fun TimetableScreen() {
    val viewModel = hiltViewModel<TimeTableViewModel>()
    val today = java.time.LocalDate.now(ZoneId.systemDefault()).dayOfWeek
    val dayMap = mapOf(
        java.time.DayOfWeek.MONDAY to "MON",
        java.time.DayOfWeek.TUESDAY to "TUE",
        java.time.DayOfWeek.WEDNESDAY to "WED",
        java.time.DayOfWeek.THURSDAY to "THU",
        java.time.DayOfWeek.FRIDAY to "FRI",
        java.time.DayOfWeek.SATURDAY to "SAT",
        java.time.DayOfWeek.SUNDAY to "SUN"
    )
    val days = listOf("MON", "TUE", "WED", "THU", "FRI")
    val selectedDay = remember { mutableStateOf(dayMap.getValue(today)) }
    val manage = remember { mutableStateOf(false) }
    val timeTable = viewModel.getTimeTableEntries().collectAsState(emptyList())
    val subjects = viewModel.getAllSubjects().collectAsState(emptyList())
    Log.d("TimetableScreen", "TimeTable entries: ${timeTable.value.size}, Results: ${timeTable.value.groupBy { it.day }}")
    val totalInADay = timeTable.value.groupBy { it.day }
    var todaysLectureInOrder = if (timeTable.value.groupBy { it.day }.contains(selectedDay.value)){
        timeTable.value.groupBy { it.day }.get(selectedDay.value)?.sortedBy { it.startTime.toLocalTime() }

    } else {
        emptyList()
    }
    todaysLectureInOrder = todaysLectureInOrder?.toMutableList()
    if (timeTable.value.groupBy { it.day }.contains(selectedDay.value)){
        for (i in 0..if(todaysLectureInOrder != null) todaysLectureInOrder.size-1 else 0){
            val item = todaysLectureInOrder!![i]
            val isLast = i == (todaysLectureInOrder.size - 1)
            if(!isLast){
                val nextItem = todaysLectureInOrder[i + 1]
                val currentEndTime = item.endTime.toLocalTime()
                val nextStartTime = nextItem.startTime.toLocalTime()
                val gap = java.time.Duration.between(currentEndTime, nextStartTime).toMinutes()
                if (gap.toInt() == 0 || gap.toInt() < 0){
                    continue
                }else{
                    todaysLectureInOrder.add(i + 1, TimeTableEntity(
                        id = -1, // Dummy ID for break
                        subjectid = -1, // No subject for break
                        day = selectedDay.value,
                        startTime = item.endTime,
                        endTime = nextItem.startTime
                    ))
                }
            }
        }
    }
    Log.d("TimetableScreen", "Today's lectures: ${todaysLectureInOrder?.size}, Results: ${todaysLectureInOrder}")

    Box(modifier = Modifier
        .fillMaxSize()
        .padding(top = 20.dp)) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            // Header
            Text(
                text = "Weekly Timetable",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "Build your schedule day by day",
                fontSize = 14.sp,
                color = Color.Gray
            )
            Spacer(modifier = Modifier.height(16.dp))
            // Day Tabs
            Row(
                modifier = Modifier.horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                days.forEach { day ->
//                    Log.d("TimetableScreen", "Rendering DayChip for $day, selected: ${.keys.contains(day)}, total: ${timeTable.value.count { it.day == day }}")
                    DayChip(
                        day = day,
                        selected = day == selectedDay.value,
                        onClick = { selectedDay.value = day },
                        total = if(totalInADay.keys.contains(day)) totalInADay.getValue(day).size else 0
                    )
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            // Cards
            LazyColumn(modifier = Modifier.weight(1f)) {
                todaysLectureInOrder?.forEach {
                    item {
                        if (it.subjectid != -1){
                            LectureCard(
                                color = Color(0xFF4C6FFF),
                                title = subjects.value.find { subject -> subject.id == it.subjectid }?.name ?: "Unknown Subject",
                                time = "${it.startTime} - ${it.endTime}",
                                onDelete = {
                                    // Handle delete action, e.g., call viewModel to delete the entry
                                    viewModel.deleteTimeTableEntry(it)
                                }
                            )
                        }else{
                            BreakCard(it)
                        }
                    }
                }
                item {
                    if(manage.value){
                        TimeTableManagementForm(manage, selectedDay)
                    }
                }
            }
            Button(
                onClick = {
                    manage.value = true
                },
                modifier = Modifier
//                    .align(Alignment.BottomCenter)
                    .padding(16.dp)
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4C3CFF))
            ) {
                Icon(Icons.Default.Add, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Add Lecture to ${selectedDay.value}")
            }
        }
    }
}



@Preview
@Composable
fun TimetableScreenPreview() {
    TimetableScreen()
}