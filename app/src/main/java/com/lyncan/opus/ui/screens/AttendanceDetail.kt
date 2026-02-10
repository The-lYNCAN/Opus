package com.lyncan.opus.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.lyncan.opus.data.AttendanceItem
import com.lyncan.opus.ui.components.attendanceDetail.AttendanceHistoryCard
import com.lyncan.opus.ui.components.attendanceDetail.TopSection

@Composable
fun AttendanceDetailScreen(subjectId: Int?) {
    val history = listOf(
        AttendanceItem("Jan 26, 2026", "11:30 AM - 12:30 PM", true),
        AttendanceItem("Jan 23, 2026", "10:15 AM - 11:15 AM", true),
        AttendanceItem("Jan 22, 2026", "9:00 AM - 10:00 AM", true),
        AttendanceItem("Jan 19, 2026", "11:30 AM - 12:30 PM", true),
        AttendanceItem("Jan 16, 2026", "10:15 AM - 11:15 AM", true),
        AttendanceItem("Jan 15, 2026", "9:00 AM - 10:00 AM", true),
        AttendanceItem("Jan 12, 2026", "11:30 AM - 12:30 PM", true),
        AttendanceItem("Jan 9, 2026", "10:15 AM - 11:15 AM", false),
        AttendanceItem("Jan 8, 2026", "9:00 AM - 10:00 AM", false),
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFFF4F6))
            .safeDrawingPadding()
    ) {

        TopSection()

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White, RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp))
                .padding(16.dp)
        ) {

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Attendance History", fontWeight = FontWeight.Bold)
                Text("${history.size} entries", color = Color.Gray)
            }

            Spacer(modifier = Modifier.height(12.dp))

            LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                items(history) { item ->
                    AttendanceHistoryCard(item)
                }
            }
        }
    }
}

@Preview
@Composable
fun AttendanceDetailScreenPreview() {
    AttendanceDetailScreen(subjectId = 1)
}