package com.lyncan.opus.ui.components.attendance

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.min
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.lyncan.opus.entities.SubjectEntity
import com.lyncan.opus.entities.TimeTableEntity
import com.lyncan.opus.viewmodels.Components.HeroViewModel

@Composable
fun Hero(){
    val viewModel = hiltViewModel<HeroViewModel>()
    val currentLectures = remember { mutableStateOf<List<TimeTableEntity>>(emptyList()) }
    val currentLecture = remember { mutableIntStateOf(0) }
    LaunchedEffect(Unit) {
        currentLectures.value = viewModel.getCurrentLecture()
    }
    val subject = remember { mutableStateOf<SubjectEntity?>(null) }
    LaunchedEffect(currentLectures.value, currentLecture.intValue) {
        try {
            if(currentLectures.value.isEmpty()) {
                subject.value = null
                return@LaunchedEffect
            }
            subject.value = viewModel.getSubjectNameById(currentLectures.value[currentLecture.intValue].subjectid)
        }catch (e: Exception) {
            subject.value = null
        }
    }
    Column(modifier = Modifier
        .clickable {
            if (currentLectures.value.size > 1) {
                currentLecture.intValue++

                if (currentLecture.intValue == currentLectures.value.size) {
                    currentLecture.intValue = 0
                }
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
                Text(subject.value?.name ?: "No Lecture", color = Color.White.copy(.7f), fontSize = 30.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis)
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
                if (currentLectures.value.size != 0){
                    currentLectures.value.get(currentLecture.intValue).startTime                    
                } else {
                    "No Lecture"
                }

            } - ${
                if (currentLectures.value.size != 0){
                    currentLectures.value.get(currentLecture.intValue).endTime
                } else {
                    "No Lecture"
                }
            }", color = Color.White.copy(alpha = .7f))
        }
    }
}