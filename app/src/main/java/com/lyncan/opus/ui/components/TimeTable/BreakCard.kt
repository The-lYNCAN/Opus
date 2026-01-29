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

@Composable
fun BreakCard() {
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
                "Break (90 min)",
                color = Color(0xFFFF6F00),
                fontWeight = FontWeight.Bold
            )
            Text(
                "12:30 PM - 2:00 PM",
                color = Color(0xFFFF6F00),
                fontSize = 12.sp
            )
        }
    }
}