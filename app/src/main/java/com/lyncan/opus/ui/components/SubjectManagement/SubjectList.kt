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
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.sharp.MenuBook
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableIntState
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.lyncan.opus.data.Assignment
import com.lyncan.opus.data.Subject
import com.lyncan.opus.viewmodels.SubjectListViewModel
import kotlinx.coroutines.launch

@Composable
fun ColumnScope.SubjectList(subjects: State<MutableMap<Subject, List<Assignment>>>,
                            deltFunc: (Int) ->Unit,
                            addSubject: MutableState<Boolean>,
                            addSubjectFunc: (String, String, Int) ->Unit,
                            update: MutableState<Boolean>,
                            subjectName: MutableState<String>,
                            subjectCode: MutableState<String>,
                            selectedIndex: MutableIntState){
    val viewModel = hiltViewModel<SubjectListViewModel>()
    val density = LocalDensity.current
    val imeHeightDp = with(density) {
        WindowInsets.ime.getBottom(this).toDp()
    }
    val scope = rememberCoroutineScope()

    val options = listOf("Lecture", "Lab", "Both")
    val subjectsLocal = viewModel.subjects.collectAsState()
    val listState = rememberLazyListState()
    Log.d("SubjectListComposable", "Subjects: ${subjectsLocal.value}")
    val updateIndex = remember { mutableIntStateOf(-1) }
    LaunchedEffect(updateIndex.intValue) {
        viewModel.updateValues(
            updateIndex.intValue,
            subjectName,
            subjectCode,
            selectedIndex)
    }
    Column(modifier = Modifier.fillMaxWidth().weight(1f).navigationBarsPadding()
    ) {
        if(subjectsLocal.value.size > 0 || addSubject.value){
            LazyColumn(
                state = listState,
                modifier = Modifier.fillMaxWidth()
            ) {

                subjectsLocal.value.forEach {
                    item {
                        SubjectCard(
                            0,
                            subject = Subject(subject_id = it.id, Subject_name = it.name, group_id = viewModel.group_id.value?: -1, type = it.type),
                            deltFunc = {
                                deltFunc(it.id)
                            }, update=update,
                            updateIndex = updateIndex,
                            addSubject
                            )
                    }
                }
                item {
                    if (addSubject.value){
                        SubjectForm(
                            addSubject, addSubjectFunc, listState, itemIndex = subjects.value.size,
                            subjectName = subjectName,
                            subjectCode = subjectCode,
                            options = options,
                            selectedIndex = selectedIndex,
                            update = update,
                            updateFunction = { newName, newCode, type ->
                                scope.launch {
                                    viewModel.updateSubject(updateIndex.intValue, subjectName.value, subjectCode.value, type=type)
                                }
                            }
                        )
                    }
                }
            }

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