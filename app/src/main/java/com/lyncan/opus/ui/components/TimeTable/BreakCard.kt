package com.lyncan.opus.ui.components.TimeTable

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lyncan.opus.entities.TimeTableEntity
import java.time.Duration
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.Locale


fun String.toLocalTime(): LocalTime {
    val formatter = DateTimeFormatter.ofPattern("hh:mm a", Locale.ENGLISH)
    return LocalTime.parse(this.trim(), formatter)
}
@Composable
fun BreakCard(entity: TimeTableEntity) {
    val time = "${entity.startTime} - ${entity.endTime}"
    val start = entity.startTime.toLocalTime()
    val end = entity.endTime.toLocalTime()
    val gap = Duration.between(start, end).toMinutes()
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp)
            .border(
                BorderStroke(
                    2.dp,
                    Brush.linearGradient(listOf(Color(0xFFFF9800), Color(0xFFFF9800)))
                ),
                shape = RoundedCornerShape(16.dp)
            )
            .background(Color(0xFFFFF3E0), RoundedCornerShape(16.dp))
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                "Break ($gap min)",
                color = Color(0xFFFF6F00),
                fontWeight = FontWeight.Bold
            )
            Text(
                time,
                color = Color(0xFFFF6F00),
                fontSize = 12.sp
            )
        }
    }
}