package com.lyncan.opus.Presentation.ui.components.Attendance

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Book
import androidx.compose.material.icons.outlined.ShowChart
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.lyncan.opus.Presentation.States.SubjectCardsState
import com.lyncan.opus.Presentation.ui.navigation.Route
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.Locale


fun String.toLocalTime(): LocalTime {
    val formatter = DateTimeFormatter.ofPattern("hh:mm a", Locale.ENGLISH)
    return LocalTime.parse(this.trim(), formatter)
}



@Composable
fun SubjectCard(
    navController: NavController,
    subjectItemState: SubjectCardsState
) {
    val greenCol = Color(0xFF00b747)
    val orangeCol = Color(0xFFdd9800)


//    val allAttendances = attendanceList.filter { it.subjectId == subject.id && tt.value.firstOrNull()?.endTime?.toLocalTime()!! < LocalTime.now()} // Filter attendances for the subject and only include those that have already occurred
//
//
//    val presentAttendance = allAttendances.count { it.isPresent == true }
//    val performanceBool = if(allAttendances.size != 0)(presentAttendance.toFloat()/allAttendances.size) >= 0.75f else true
    val sufficient = if(subjectItemState.percentage > 75) greenCol else orangeCol
    val status = if (subjectItemState.percentage > 75) "Good" else "Low"
    val numberOfRequiredClasses = 3*subjectItemState.total - 4*subjectItemState.present
//    val totalClasses = allAttendances.size
    Column(modifier = Modifier
        .fillMaxWidth()
        .clip(RoundedCornerShape(20.dp))
        .clickable {
            navController.navigate(Route.AttendanceDetail.createRoute(subjectItemState.subjectId))
        }
        .border(shape = RoundedCornerShape(20.dp), width = 2.dp, color = Color.LightGray)
        .heightIn(min = 145.dp)
        .padding(25.dp)
    ) {
        Row {
            Box(modifier = Modifier
                .wrapContentWidth()
                .background(sufficient, shape = RoundedCornerShape(10.dp))
                .padding(10.dp), contentAlignment = Alignment.Center){
                Icon(
                    imageVector = Icons.Outlined.Book,
                    modifier = Modifier.size(40.dp),
                    contentDescription = "Current Lecture",
                    tint = Color.White.copy(alpha = .7f),
                )
            }
            Spacer(modifier = Modifier.width(10.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(subjectItemState.subjectName, color = Color.Black, style = MaterialTheme.typography.titleLarge, modifier = Modifier, overflow = TextOverflow.Ellipsis, maxLines = 1)
                Text("${subjectItemState.present} of ${subjectItemState.total} classes", color = Color.Gray)
            }
//            Spacer(modifier = Modifier.weight(.5f))
            Column(modifier = Modifier) {
                Text("${subjectItemState.percentage}%", color = sufficient, style = MaterialTheme.typography.titleLarge, maxLines = 1, modifier = Modifier)
                Row {
                    Box(modifier = Modifier.wrapContentSize()){
                        Icon(
                            imageVector = Icons.Outlined.ShowChart,
                            modifier = Modifier.size(20.dp),
                            contentDescription = "Current Lecture",
                            tint = sufficient,
                        )
                    }
                    Text(status, color = Color.Gray, maxLines = 1)
                }
            }
        }

        if(subjectItemState.percentage > 75)Spacer(modifier = Modifier
            .weight(1f)
            .heightIn(min = 20.dp))else Spacer(modifier = Modifier.height(20.dp))
        LinearProgressIndicator(
            modifier = Modifier
                .fillMaxWidth()
                .height(15.dp),
            progress = if(subjectItemState.total != 0)(subjectItemState.present.toFloat()/subjectItemState.total) else 1f,
            color = sufficient
        )
        Spacer(modifier = Modifier.weight(1f))
        if (subjectItemState.percentage > 75){
        }else{
            Warning(numberOfRequiredClasses)
        }
    }
}

@Composable
fun Warning(numberOfRequiredClasses: Int){
    val orangeCol = Color(0xFFdd9800)
    val background = Color(0xFFfff7ed)
    Spacer(modifier = Modifier.height(10.dp))
    Box(modifier = Modifier
        .border(
            shape = RoundedCornerShape(20.dp),
            width = 1.dp,
            color = orangeCol.copy(alpha = .6f)
        )
        .background(background, shape = RoundedCornerShape(20.dp))
        .padding(20.dp), contentAlignment = Alignment.Center){
        Text("⚠\uFE0F You need to attend $numberOfRequiredClasses more classes to reach 75%", color = Color.Red, style = MaterialTheme.typography.bodyMedium)
    }
}