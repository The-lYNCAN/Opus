package com.lyncan.opus.ui.components.TimeTable

import android.app.TimePickerDialog
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableIntState
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.util.Calendar


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimeTableManagementForm(addSubject: MutableState<Boolean>) {

    val options = listOf("Lecture", "Lab")
    val selectedType = remember { mutableIntStateOf(0) }
    val selectedGroup = remember { mutableIntStateOf(0) }
    val subjects = listOf("Mathematics", "Physics", "Data Structures", "OS")
    var expanded by remember { mutableStateOf(false) }
    var selectedSubject by remember { mutableStateOf("") }
    var startTime by remember { mutableStateOf("03:00 PM") }
    var endTime by remember { mutableStateOf("04:00 PM") }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .border(2.dp, Color(0xFF4F39F6), RoundedCornerShape(16.dp))
            .background(Color.White, RoundedCornerShape(16.dp))
            .padding(16.dp)
    ) {

        // Select Subject
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = !expanded },
            modifier = Modifier.fillMaxWidth()
        ) {

            OutlinedTextField(
                value = selectedSubject,
                onValueChange = {},
                readOnly = true,
                modifier = Modifier
                    .menuAnchor() // 🔑 REQUIRED
                    .fillMaxWidth(),
                placeholder = { Text("Select Subject *") },
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
                },
                shape = RoundedCornerShape(12.dp),
                colors = TextFieldDefaults.colors(
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    unfocusedContainerColor = Color(0xFFF3F4F6)
                )
            )

            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                subjects.forEach { subject ->
                    DropdownMenuItem(
                        text = { Text(subject) },
                        onClick = {
                            selectedSubject = subject
                            expanded = false
                        }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Start / End Time
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            TimeBox("Start Time", startTime) { startTime = it }
            TimeBox("End Time", endTime) { endTime = it }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Lecture / Lab
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            options.forEachIndexed { index, text ->
                SegmentedButtonStyled(
                    text = text,
                    selected = selectedType.intValue == index,
                    onClick = { selectedType.intValue = index }
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Room
        OutlinedTextField(
            value = "",
            onValueChange = {},
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            placeholder = { Text("Room (e.g., A101)") },
            colors = TextFieldDefaults.colors(
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                unfocusedContainerColor = Color(0xFFF3F4F6)
            )
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Groups
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            listOf("All", "G1", "G2").forEachIndexed { index, text ->
                SegmentedButtonStyled(
                    text = text,
                    selected = selectedGroup.intValue == index,
                    onClick = { selectedGroup.intValue = index }
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Bottom Buttons
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            Button(
                onClick = { addSubject.value = false },
                modifier = Modifier
                    .weight(1f)
                    .height(48.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4F39F6))
            ) {
                Text("Add Lecture", color = Color.White)
            }

            Button(
                onClick = { addSubject.value = false },
                modifier = Modifier
                    .weight(1f)
                    .height(48.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE5E7EB))
            ) {
                Text("Cancel", color = Color.Black)
            }
        }
    }
}

@Composable
fun RowScope.TimeBox(label: String,
                     time: String,
                     onTimeSelected: (String) -> Unit) {
    val context = LocalContext.current
    val calendar = Calendar.getInstance()

    Box(
        modifier = Modifier
            .weight(1f)
            .background(Color(0xFFF3F4F6), RoundedCornerShape(12.dp))
            .clickable {
                TimePickerDialog(
                    context,
                    { _, hour, minute ->
                        val formatted = String.format(
                            "%02d:%02d %s",
                            if (hour % 12 == 0) 12 else hour % 12,
                            minute,
                            if (hour >= 12) "PM" else "AM"
                        )
                        onTimeSelected(formatted)
                    },
                    calendar.get(Calendar.HOUR_OF_DAY),
                    calendar.get(Calendar.MINUTE),
                    false
                ).show()
            }
            .padding(12.dp)
    ) {
        Column {
            Text(label, fontSize = 12.sp, color = Color.Gray)
            Spacer(modifier = Modifier.height(4.dp))
            Text(time, fontWeight = FontWeight.Medium)
        }
    }
}

@Composable
fun RowScope.SegmentedButtonStyled(
    text: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .weight(1f)
            .height(44.dp),
        shape = RoundedCornerShape(12.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = if (selected) Color(0xFF4F39F6) else Color(0xFFF3F4F6)
        )
    ) {
        Text(
            text = text,
            color = if (selected) Color.White else Color.Black
        )
    }
}

@Composable
fun RowScope.SegmentedButtons(
    string: String,
    selected: Boolean,
    selectedIndex: MutableIntState,
    index: Int
) {
    Button(
        onClick = { selectedIndex.intValue = index },
        modifier = Modifier
            .weight(1f)
            .padding(horizontal = 4.dp), // spacing BETWEEN buttons
        shape = RoundedCornerShape(10.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = if (selected) Color(0xFF4F39F6) else Color.LightGray.copy(alpha = .3f)
        ),
        contentPadding = PaddingValues( // 🔑 key fix
            horizontal = 8.dp,
            vertical = 12.dp
        )
    ) {
        Text(
            text = string,
            maxLines = 1,
            softWrap = false,
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.titleMedium,
            color = if (selected) Color.White else Color.Black,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Preview
@Composable
fun ManagementPreview(){
    TimeTableManagementForm(addSubject = remember { mutableStateOf(true) })
}
