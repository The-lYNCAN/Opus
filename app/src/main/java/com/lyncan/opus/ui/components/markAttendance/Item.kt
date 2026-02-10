package com.lyncan.opus.ui.components.markAttendance

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lyncan.opus.data.AttendanceUiModel
import com.lyncan.opus.ui.components.AttendanceDetail.StatusChip

@Composable
fun Card(item: AttendanceUiModel) {
    val bgColor = if (item.attendance.isPresent != null) Color(0xFFEFFFF4) else Color(0xFFFFEEEE)
    val borderColor = if (item.attendance.isPresent != null) Color(0xFF9AFFB3) else Color(0xFFFFB3B3)
    val statusColor = if (item.attendance.isPresent != null) Color(0xFF00C853) else Color(0xFFFF1744)

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(bgColor, RoundedCornerShape(16.dp))
            .border(1.dp, borderColor, RoundedCornerShape(16.dp))
            .padding(12.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {

            Icon(Icons.Default.DateRange, contentDescription = null)

            Spacer(modifier = Modifier.width(8.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(item.subject.name, fontWeight = FontWeight.Bold)
                Text(item.attendance.date, fontSize = 12.sp, color = Color.Gray)
            }

            StatusChip(item.attendance.isPresent ?: false)

            Spacer(modifier = Modifier.width(8.dp))

            IconButton(onClick = { }) {
                Icon(Icons.Default.Edit, contentDescription = null)
            }
        }
    }
}