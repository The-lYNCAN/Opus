package com.lyncan.opus.Presentation.ui.components.Attendance

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Book
import androidx.compose.material.icons.outlined.Group
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.lyncan.opus.Presentation.viewmodels.Components.HeroViewModel

@Composable
fun Hero(){
    val viewModel = hiltViewModel<HeroViewModel>()
    val currentLectures = viewModel.state.collectAsState().value?.currentLectures
    val currentLecture = remember { mutableIntStateOf(0) }

    Column(modifier = Modifier
        .clickable {
            currentLecture.intValue++
            if(currentLectures?.size == currentLecture.intValue){
                currentLecture.intValue = 0
            }
        }
        .fillMaxWidth()
        .fillMaxHeight(0.23f)
        .heightIn(min = 145.dp)
        .background(
            brush = Brush.linearGradient(
                colors = listOf(
                    Color(0xFF0066FF),
                    Color(0xFFC953ED)
                )
            ), shape = RoundedCornerShape(10.dp)
        )
        .padding(30.dp)
    ) {
        Row {
            Box(modifier = Modifier
                .border(shape = CircleShape, width = 1.dp, color = Color.White.copy(.3f))
                .background(color = Color.White.copy(.3f), shape = CircleShape)
                .padding(10.dp),
            ){

                Icon(
                    imageVector = Icons.Outlined.Book,
                    modifier = Modifier.size(40.dp),
                    contentDescription = "Current Lecture",
                    tint = Color.White.copy(alpha = .7f),
                )
            }
            Column(modifier = Modifier.padding(start = 10.dp)) {
                Text("Current Lecture", color = Color.White.copy(.7f),)
                if(currentLectures?.isNotEmpty()?: false){
                    Text(
                        currentLectures.get(currentLecture.intValue)?.subjectName ?: "No Lecture", color = Color.White.copy(.7f), fontSize = 30.sp,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis)
                }else{
                    Text(
                        "No Lecture", color = Color.White.copy(.7f), fontSize = 30.sp,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis)
                }
            }
        }
        Row (modifier = Modifier.padding(top = 14.dp )){
            Icon(
                imageVector = Icons.Outlined.Group,
                modifier = Modifier.size(24.dp),
                contentDescription = "Copy Invite Link",
                tint = Color.White.copy(.7f)
            )
            Text("${
                if (currentLectures?.isNotEmpty()?: false){
                    currentLectures[currentLecture.intValue]?.startTime                    
                } else {
                    "No Lecture"
                }

            } - ${
                if (currentLectures?.isNotEmpty()?: false){
                    currentLectures.get(currentLecture.intValue)?.endTime
                } else {
                    "No Lecture"
                }
            }", color = Color.White.copy(alpha = .7f))
        }
    }
}