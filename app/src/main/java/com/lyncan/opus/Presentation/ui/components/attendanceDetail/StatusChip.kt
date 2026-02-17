package com.lyncan.opus.Presentation.ui.components.attendanceDetail

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun StatusChip(isPresent: Boolean) {
    val color = if (isPresent) Color(0xFF00C853) else Color(0xFFFF1744)
    val bg = if (isPresent) Color(0xFFE6F9ED) else Color(0xFFFFEAEA)

    Box(
        modifier = Modifier
            .background(bg, RoundedCornerShape(50))
            .border(1.dp, color, RoundedCornerShape(50))
            .padding(horizontal = 12.dp, vertical = 4.dp)
    ) {
        Text(
            if (isPresent) "PRESENT" else "ABSENT",
            color = color,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold
        )
    }
}