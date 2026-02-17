package com.lyncan.opus.Presentation.ui.components.SubjectManagement

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun Header(){
    var total = 0
    Column(modifier = Modifier.fillMaxWidth().drawBehind {
        val strokeWidth = 2.dp.toPx()

        // Top border
        drawLine(
            color = Color.LightGray,
            start = Offset(0f, 0f),
            end = Offset(size.width, 0f),
            strokeWidth = strokeWidth
        )

        // Bottom border
        drawLine(
            color = Color.LightGray,
            start = Offset(0f, size.height),
            end = Offset(size.width, size.height),
            strokeWidth = strokeWidth
        )
    }.padding(20.dp)
    ) {
        Text("Subjects", style = MaterialTheme.typography.headlineMedium)
        Text("Add all your semester subjects ($total addec)", color = Color.Gray)
    }

}
