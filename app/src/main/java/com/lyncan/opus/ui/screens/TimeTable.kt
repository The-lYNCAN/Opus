package com.lyncan.opus.ui.screens

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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lyncan.opus.ui.components.TimeTable.BreakCard
import com.lyncan.opus.ui.components.TimeTable.DayChip
import com.lyncan.opus.ui.components.TimeTable.LectureCard
import com.lyncan.opus.ui.components.TimeTable.TimeTableManagementForm

@Composable
fun TimetableScreen() {
    val days = listOf("MON", "TUE", "WED", "THU", "FRI")
    var selectedDay by remember { mutableStateOf("MON") }
    val manage = remember { mutableStateOf(false) }

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
                    DayChip(
                        day = day,
                        selected = day == selectedDay,
                        onClick = { selectedDay = day }
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
            // Cards

            LazyColumn(modifier = Modifier.weight(1f)) {
                items(3){
                    LectureCard(
                        color = Color(0xFF4C6FFF),
                        title = "Data Structures",
                        time = "9:00 AM - 10:00 AM"
                    )
                }
                item {
                    BreakCard()
                }
                items(3){
                    LectureCard(
                        color = Color(0xFF4C6FFF),
                        title = "Data Structures",
                        time = "9:00 AM - 10:00 AM"
                    )
                }
                item {
                    if(manage.value){
                        TimeTableManagementForm(manage )
                    }
                }
            }




//            Spacer(modifier = Modifier.height(100.dp))
            // Bottom Button
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
                Text("Add Lecture to $selectedDay")
            }
        }

    }
}

@Preview
@Composable
fun TimetableScreenPreview() {
    TimetableScreen()
}