package com.lyncan.opus.ui.components.SubjectManagement

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lyncan.opus.data.Subject

@Composable
fun SubjectCard(noOfAssignments: Int, subject: Subject, deltFunc: ()->Unit = {}, update: MutableState<Boolean>,
                updateIndex: MutableState<Int>,
                addSubject: MutableState<Boolean>){
    val shape = RoundedCornerShape(16.dp)

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp),
        shape = shape,
        elevation = CardDefaults.cardElevation(4.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Row(
            modifier = Modifier
                .clip(shape) // 🔑 important
                .height(IntrinsicSize.Min)
        ) {

            // Blue indicator
            Box(
                modifier = Modifier
                    .width(6.dp)
                    .fillMaxHeight()
                    .background(Color(0xFF2979FF))
            )

            Spacer(modifier = Modifier.width(12.dp))

            Text(
                text = subject.Subject_name,
                modifier = Modifier
                    .weight(1f)
                    .align(Alignment.CenterVertically),
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold
            )

            Row(
                modifier = Modifier.padding(end = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                IconButton(onClick = {
                    updateIndex.value = subject.subject_id!!
                    update.value = true
                    addSubject.value = true
                }) {
                    Icon(Icons.Default.Edit, contentDescription = null)
                }
                IconButton(onClick = deltFunc) {
                    Icon(Icons.Default.Close, contentDescription = null)
                }
            }
        }
    }
}

@Preview
@Composable
fun SubjectCardPreview(){
    SubjectCard(noOfAssignments = 5, subject = Subject(
        subject_id = 1,
        subjectPic = "TODO()",
        Subject_name = "TODO()",
        group_id = 0,
    ), update = remember { mutableStateOf(false) },
        updateIndex = remember { mutableStateOf(-1)},
        addSubject = remember { mutableStateOf(false)}
    )
}