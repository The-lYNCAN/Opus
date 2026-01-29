package com.lyncan.opus.uI.components.Home

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun UnJoinedInfoCard(color: Color, title: String, description: String, icon: ImageVector) {

    Row(modifier = Modifier.fillMaxWidth().height(170.dp).padding(vertical = 10.dp, horizontal = 20.dp)
        .background(color.copy(0.2f), shape = androidx.compose.foundation.shape.RoundedCornerShape(10.dp))
        .border(1.dp, color = color, shape = androidx.compose.foundation.shape.RoundedCornerShape(10.dp))) {
        Box(
            modifier = Modifier.height(100.dp),
            contentAlignment = androidx.compose.ui.Alignment.Center
        ){
            Icon(icon,
                "Join Group",
                modifier = Modifier.size(60.dp).padding(horizontal = 15.dp))
        }
        Column(modifier = Modifier.fillMaxHeight(),
            verticalArrangement = Arrangement.Center,) {
            Text(title, fontWeight = FontWeight.Bold)
            Text(description)
        }
    }
}