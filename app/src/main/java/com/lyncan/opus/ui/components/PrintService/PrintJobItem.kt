package com.lyncan.opus.uI.components.PrintService

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp


@Composable
fun PrintJobItem(
    title: String,
    pages: Int,
    copies: Int,
    color: Boolean,
    status: String,
    uploaded: String,
    cost: String,
    pickedUp: Boolean = false,
    progress: Int? = null
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(Icons.Default.Send, contentDescription = title)
        Column(modifier = Modifier.weight(1f).padding(start = 8.dp)) {
            Text(title)
            Text("$pages pages $copies copies ${if (color) "Color" else "B&w"}")
            Text("Uploaded $uploaded")
            Text(status, color = when (status) {
                "Completed" -> Color.Green
                "Ready for Pickup" -> Color.Yellow
                "Printing" -> Color.Blue
                else -> Color.Red
            })
            if (progress != null) {
                LinearProgressIndicator(progress = progress / 100f)
            }
        }
        Text(cost, modifier = Modifier.padding(start = 8.dp))
    }
}