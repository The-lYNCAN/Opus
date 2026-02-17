package com.lyncan.opus.Presentation.ui.components.Group

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.sharp.Group
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun Hero(showQR: androidx.compose.runtime.MutableState<Boolean>, name: String) {
    Column(
        modifier = Modifier.background(Color(0xFF4B34E9))
            .padding(16.dp).padding(top = 30.dp)
            .fillMaxWidth()
    ) {
        Row {
            Box(modifier = Modifier.background(Color(0xFF5C47E8), shape = CircleShape).padding(10.dp).wrapContentSize()){
                Icon(
                    Icons.Sharp.Group,
                    contentDescription = "Group Icon",
                    tint = Color.White,
                    modifier = Modifier.size(35.dp)
                )

            }
            Column {
                Text(
                    text = name,
                    color = Color.White,
                    modifier = Modifier.padding(start = 16.dp),
                    style = MaterialTheme.typography.headlineMedium
                )
                Text(
                    text = "Admin Panel",
                    color = Color.White,
                    modifier = Modifier.padding(start = 16.dp)
                )
            }
            Spacer(modifier = Modifier.weight(1f))
            Box(modifier = Modifier.background(Color.White, shape = CircleShape).padding(10.dp).wrapContentSize()){
                Icon(
                    Icons.Sharp.Group,
                    contentDescription = "Group Icon",
                    tint = Color.Black
                    ,
                    modifier = Modifier.size(35.dp)
                )

            }
        }
        GroupCode()

    }
}

@Composable
fun GroupCode(){
    Box(modifier = Modifier.fillMaxWidth().padding(10.dp).background(Color(0xFF5C47E8), shape = RoundedCornerShape(10.dp)).border(1.dp, Color.LightGray.copy(alpha = .7f), shape = RoundedCornerShape(10.dp))){
        Column(modifier = Modifier.padding(10.dp)) {
            Text("Group Code", color = Color.White.copy(.5f), modifier = Modifier.padding(bottom = 5.dp))
            Text("LBQVNL", color = Color.White, style = MaterialTheme.typography.headlineMedium)
        }
    }
}