package com.lyncan.opus.ui.components.Attendance

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
import androidx.compose.runtime.State
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.lyncan.opus.entities.AttendanceEntity
import com.lyncan.opus.entities.SubjectEntity
import com.lyncan.opus.entities.TimeTableEntity
import com.lyncan.opus.uI.navigation.Route
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
    subject: SubjectEntity,
    attendanceList: List<AttendanceEntity>,
    tt: State<List<TimeTableEntity>>
) {
    val greenCol = Color(0xFF00b747)
    val orangeCol = Color(0xFFdd9800)

    val allAttendances = attendanceList.filter { it.subjectId == subject.id && tt.value.firstOrNull(){ttEntry -> ttEntry.id == it.timeTableId}?.endTime!!.toLocalTime() < LocalTime.now()} // Filter attendances for the subject and only include those that have already occurred
    val presentAttendance = allAttendances.count { it.isPresent == true }
    val performanceBool = if(allAttendances.size != 0)(presentAttendance.toFloat()/allAttendances.size) >= 0.75f else true
    val sufficient = if(performanceBool) greenCol else orangeCol
    val status = if (performanceBool) "Good" else "Low"
    val numberOfRequiredClasses = 3*allAttendances.size - 4*presentAttendance
    val totalClasses = allAttendances.size
    Column(modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(20.dp)).clickable{
        navController.navigate(Route.AttendanceDetail.createRoute(subject.id) )
    }.border(shape = RoundedCornerShape(20.dp), width = 2.dp, color = Color.LightGray).heightIn(min=145.dp)
        .padding(25.dp)
    ) {
        Row {
            Box(modifier = Modifier.wrapContentWidth().background(sufficient, shape = RoundedCornerShape(10.dp)).padding(10.dp), contentAlignment = Alignment.Center){
                Icon(
                    imageVector = Icons.Outlined.Book,
                    modifier = Modifier.size(40.dp),
                    contentDescription = "Current Lecture",
                    tint = Color.White.copy(alpha = .7f),
                )
            }
            Spacer(modifier = Modifier.width(10.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(subject.name, color = Color.Black, style = MaterialTheme.typography.titleLarge, modifier = Modifier, overflow = TextOverflow.Ellipsis, maxLines = 1)
                Text("$presentAttendance of $totalClasses classes", color = Color.Gray)
            }
//            Spacer(modifier = Modifier.weight(.5f))
            Column(modifier = Modifier) {
                Text("${(if(totalClasses != 0)(presentAttendance.toFloat()/totalClasses) * 100 else 100)}%", color = sufficient, style = MaterialTheme.typography.titleLarge, maxLines = 1, modifier = Modifier)
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

        if(performanceBool)Spacer(modifier = Modifier.weight(1f).heightIn(min=20.dp))else Spacer(modifier = Modifier.height(20.dp))
        LinearProgressIndicator(
            modifier = Modifier.fillMaxWidth().height(15.dp),
            progress = if(totalClasses != 0)(presentAttendance.toFloat()/totalClasses) else 1f,
            color = sufficient
        )
        Spacer(modifier = Modifier.weight(1f))
        if (performanceBool){
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
    Box(modifier = Modifier.border(
        shape = RoundedCornerShape(20.dp),
        width = 1.dp,
        color = orangeCol.copy(alpha = .6f)
    ).background(background, shape = RoundedCornerShape(20.dp)).padding(20.dp), contentAlignment = Alignment.Center){
        Text("⚠\uFE0F You need to attend $numberOfRequiredClasses more classes to reach 75%", color = Color.Red, style = MaterialTheme.typography.bodyMedium)
    }
}