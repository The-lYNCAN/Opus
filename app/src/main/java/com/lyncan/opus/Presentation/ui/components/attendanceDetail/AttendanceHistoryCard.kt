package com.lyncan.opus.Presentation.ui.components.attendanceDetail

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
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lyncan.opus.Presentation.data.AttendanceDetailItem

@Composable
fun AttendanceHistoryCard(item: AttendanceDetailItem, onEdit: (Int, Boolean) -> Unit) {

    val bgColor = if (item.status != null && item.status) Color(0xFFEFFFF4) else Color(0xFFFFEEEE)
    val borderColor = if (item.status != null && item.status) Color(0xFF9AFFB3) else Color(0xFFFFB3B3)
    val statusColor = if (item.status != null && item.status) Color(0xFF00C853) else Color(0xFFFF1744)

    val editing = remember { mutableStateOf(false) }

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
                Text(item.date, fontWeight = FontWeight.Bold)
                Text(item.time, fontSize = 12.sp, color = Color.Gray)
            }
            if (item.status != null){
                StatusChip(item.status)
            }

            Spacer(modifier = Modifier.width(8.dp))

            if(editing.value){

                Row {
                    IconButton(onClick = {
                        onEdit(item.id, true)
                        editing.value = false
                    }) {
                        Icon(Icons.Default.Check, contentDescription = null)
                    }
                    IconButton(onClick = {
                        onEdit(item.id, false)
                        editing.value = false
                    }) {
                        Icon(Icons.Default.Close, contentDescription = null)
                    }
                }

            }else{

                IconButton(onClick = {
                    editing.value = true
                }) {
                    Icon(Icons.Default.Edit, contentDescription = null)
                }
            }

        }
    }
}