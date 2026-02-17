package com.lyncan.opus.Presentation.ui.components.attendanceDetail

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
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
import com.lyncan.opus.Presentation.States.TopSection.TopSectionState

@Composable
fun TopSection(topSectionState: TopSectionState, onEdit: () -> Unit) {
    val dialog = remember { mutableStateOf(false) }
    Column(
        modifier = Modifier.padding(16.dp)
    ) {


        Spacer(modifier = Modifier.height(16.dp))

        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .background(Color(0xFFF44386), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text(if (topSectionState.subjectName.isNotEmpty())topSectionState.subjectName[0].toString() else "U", color = Color.White, fontWeight = FontWeight.Bold)
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column {
                Text(topSectionState.subjectName, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                Text(topSectionState.subjectCode?: "", color = Color.Gray)
            }
            Spacer(modifier = Modifier.weight(1f))
            IconButton(onClick = {
                onEdit()
            }) {
                Icon(Icons.Default.Edit, contentDescription = null)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            StatCard(topSectionState.percentage.toString(), "Attendance", Color(0xFF4C6FFF))
            StatCard(topSectionState.present.toString(), "Present", Color(0xFF00C853))
            StatCard(topSectionState.absent.toString(), "Absent", Color(0xFFFF5252))
        }
    }
}

