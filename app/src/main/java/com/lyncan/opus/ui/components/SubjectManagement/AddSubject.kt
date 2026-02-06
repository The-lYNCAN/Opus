package com.lyncan.opus.ui.components.SubjectManagement

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.sharp.Add
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableIntState
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun ColumnScope.AddSubject(
    addSubject: MutableState<Boolean>,
    update: MutableState<Boolean>,
    subjectCode: MutableState<String>,
    subjectName: MutableState<String>,
    selectedIndex: MutableIntState
){
    Box(modifier = Modifier
        .drawBehind {
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
        }
        .fillMaxWidth()
        .padding(16.dp).wrapContentSize()
    ){
        Button(modifier = Modifier.fillMaxWidth().heightIn(min = 70.dp).background(Color(0xFF4F39F6), shape = RoundedCornerShape(10.dp)),
            onClick = {
                subjectName.value = ""
                subjectCode.value = ""
                selectedIndex.value = 0
                update.value = false
                addSubject.value = true
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF4F39F6)
            ),
            shape = RoundedCornerShape(10.dp)
            ) {
            Row {
                Icon(Icons.Sharp.Add, contentDescription = "", Modifier.size(30.dp), tint = Color.White)
                Spacer(modifier = Modifier.width(10.dp))
                Text("Add Subject", maxLines = 1, style = MaterialTheme.typography.titleLarge)
            }
        }
    }
}