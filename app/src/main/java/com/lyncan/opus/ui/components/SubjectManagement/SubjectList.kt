package com.lyncan.opus.ui.components.SubjectManagement

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.sharp.MenuBook
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import com.lyncan.opus.data.Assignment
import com.lyncan.opus.data.Subject

@Composable
fun ColumnScope.SubjectList(subjects: State<MutableMap<Subject, List<Assignment>>>, deltFunc: (Int) ->Unit, addSubject: MutableState<Boolean>, addSubjectFunc: (String, String) ->Unit) {
    val density = LocalDensity.current

    val imeHeightDp = with(density) {
        WindowInsets.ime.getBottom(this).toDp()
    }
    Log.d("KeyboardHeight", "IME Height: $imeHeightDp")
    Column(modifier = Modifier.fillMaxWidth().weight(1f).navigationBarsPadding()
    ) {
        if(subjects.value.size > 0 || addSubject.value){
            LazyColumn() {
                items(subjects.value.size, itemContent = { index ->
                    val subject = subjects.value.keys.toList()[index]
                    val assignments = subjects.value[subject]!!
                    SubjectCard(
                        subject.Subject_name,
                        assignments.size,
                        subject = subject,
                        deltFunc = {
                            deltFunc(subject.subject_id!!)
                        })
                } )
                item {
                    if (addSubject.value){
                        SubjectForm(addSubject, addSubjectFunc)
                    }
                }
            }
            Spacer(Modifier.height(imeHeightDp))
//            subjects.value.forEach { (subject, assignments) ->
//                SubjectCard(
//                    subject.Subject_name,
//                    assignments.size,
//                    subject = subject,
//                    deltFunc = {
//                        deltFunc(subject.subject_id!!)
//                    })
//            }
        }else{
            Column(modifier = Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(8.dp), horizontalAlignment = Alignment.CenterHorizontally, ) {
                Spacer(modifier = Modifier.weight(1f))
                Box(modifier = Modifier.background(Color(0xFFE0E7FF), shape = CircleShape).padding(15.dp)){
                    Icon(Icons.Sharp.MenuBook, contentDescription = "", Modifier.size(35.dp), tint = Color(0xFF4F39F6))
                }
                Text("No subjects yet", style = MaterialTheme.typography.headlineSmall)
                Text("Start by adding your subjects", color = Color.Gray)
                Spacer(modifier = Modifier.weight(1f))
            }
        }

    }
}