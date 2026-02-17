package com.lyncan.opus.Presentation.ui.components.SubjectManagement

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


@Composable
fun Stats(totalSub: Int, totalAss: Int){
    Column (modifier = Modifier
        .padding(horizontal = 20.dp, vertical = 15.dp)
        .border(1.dp, color = Color.LightGray, shape = RoundedCornerShape(15.dp))
        .fillMaxWidth()){
        Text("Subject Statistics", modifier = Modifier.padding(start = 20.dp, top = 20.dp), fontWeight = FontWeight.Bold, fontSize = 20.sp)
        Row(modifier = Modifier.padding(top = 30.dp, bottom = 20.dp)) {
            Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
                Text(totalSub.toString(), fontSize = 30.sp, fontWeight = FontWeight.Bold)
                Text("Total Subjects")
            }
            Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
                Text(totalAss.toString(), fontSize = 30.sp, fontWeight = FontWeight.Bold)
                Text("Total Assignments")
            }

        }
    }
}