package com.lyncan.opus.Presentation.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.lyncan.opus.Domain.UseCases.AttendanceDetailsUseCases.AttendanceDetailsEvents
import com.lyncan.opus.Presentation.States.TopSection.TopSectionState
import com.lyncan.opus.Presentation.ui.components.attendanceDetail.AttendanceHistoryCard
import com.lyncan.opus.Presentation.ui.components.attendanceDetail.TopSection
import com.lyncan.opus.Presentation.viewmodels.AttendanceDetailViewModel

@Composable
fun AttendanceDetailScreen() {
    val viewModel = hiltViewModel<AttendanceDetailViewModel>()
    val state = viewModel.state.collectAsState()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFFF4F6))
            .safeDrawingPadding()
    ) {

        TopSection(TopSectionState(percentage = state.value?.percentage ?: 0.0, present = state.value?.present?:0, absent = state.value?.absent?: 0, subjectName = state.value?.subjectName?: "", subjectCode = state.value?.subjectCode),
            onEdit = {
                viewModel.onEvent(AttendanceDetailsEvents.EditButtonClicked)
            }
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White, RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp))
                .padding(16.dp)
        ) {

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Attendance History", fontWeight = FontWeight.Bold)
                Text("${viewModel.state.value?.List?.size?: 0} entries", color = Color.Gray)
            }

            Spacer(modifier = Modifier.height(12.dp))
            if(state.value != null){
                LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    items(state.value!!.List.size) { item ->
                        AttendanceHistoryCard(viewModel.state.collectAsState().value!!.List[item], onEdit = { id, status ->
                            viewModel.onEvent(AttendanceDetailsEvents.EditButton(id, status))
                        })
                    }
                }
            }else{
                Text("Loading attendance history...", color = Color.Gray, modifier = Modifier.padding(20.dp))
            }
        }
        if(uiState.showDialog){
            AddAttendanceDialog(
                onConfirm = {total, present ->
                    viewModel.onEvent(AttendanceDetailsEvents.EditButtonConfirmed(total.toInt(), present.toInt()))

                },
                onDismiss = {
                    viewModel.onEvent(AttendanceDetailsEvents.EditButtonDismissed)
                }
            )
        }
    }
}

@Preview
@Composable
fun AttendanceDetailScreenPreview() {
    AttendanceDetailScreen()
}

@Composable
fun AddAttendanceDialog(
    onConfirm: (String, String) -> Unit,
    onDismiss: () -> Unit
) {
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }

    Dialog(onDismissRequest = onDismiss) {

        Card(
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {

                Text(
                    text = "Add Attendance",
                    style = MaterialTheme.typography.titleLarge
                )

                Spacer(modifier = Modifier.height(16.dp))

                TextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Total") },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number
                    )
                )

                Spacer(modifier = Modifier.height(12.dp))

                TextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("Present") },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number
                    )
                )

                Spacer(modifier = Modifier.height(24.dp))

                Row(
                    horizontalArrangement = Arrangement.End,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    TextButton(onClick = onDismiss) {
                        Text("Cancel")
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    Button(
                        onClick = {
                            onConfirm(title, description)
                        }
                    ) {
                        Text("Save")
                    }
                }
            }
        }
    }
}