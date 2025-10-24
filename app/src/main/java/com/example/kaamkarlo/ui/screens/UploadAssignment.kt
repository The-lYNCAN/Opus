package com.example.kaamkarlo.ui.screens

import android.content.Context
import android.net.Uri
import android.provider.OpenableColumns
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Circle
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.outlined.Upload
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerState
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Popup
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.kaamkarlo.viewmodels.UploadAssignmentViewModel
import kotlinx.datetime.Instant
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


@Composable
fun UploadAssignment(navController: NavController){
    val viewModel: UploadAssignmentViewModel = hiltViewModel()
    val loading = viewModel.loading.collectAsState()
    if(!loading.value){

        Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
            Text("Upload Assignment", fontWeight = FontWeight.Bold, fontSize = 24.sp
                , modifier = Modifier.padding(20.dp))
            Disclaimer()
            UploadSection(viewModel, navController)
            UploadGuidelines()
        }
    }else{
        Column(modifier= Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {

            CircularProgressIndicator(
                modifier = Modifier.size(48.dp)
            )
            Text("Uploadng Assignment", modifier = Modifier.padding(top = 8.dp))
        }
    }
}


@Composable
fun Disclaimer(){
    Row(modifier = Modifier
        .padding(20.dp)
        .border(1.dp, color = Color.LightGray, shape = RoundedCornerShape(15.dp))) {
        Box(modifier = Modifier
            .padding(20.dp)
            .clip(CircleShape)
            .background(Color.LightGray)
            .size(40.dp), contentAlignment = Alignment.Center)
        { Icon(Icons.Outlined.Upload, contentDescription = "Subject Management"
            , Modifier.size(32.dp)) }
        Column(modifier = Modifier.padding(top = 20.dp, bottom = 20.dp, start = 0.dp, end = 10.dp)) {
            Text("Upload Assignment PDF", fontWeight = FontWeight.Bold, fontSize = 18.sp)
            Text("Upload your assignment PDF to share with your group. Choose the appropriate subject " +
                    "and set a due date.", fontSize = 15.sp)
        }
    }
}

fun getFileName(context: Context, uri: Uri?): String? {
    // 1. Check if the URI is a content URI (not null)
    if (uri?.scheme != "content") {
        return uri?.lastPathSegment // Fallback for file:// or other schemes
    }

    // 2. Query the ContentResolver
    val cursor = context.contentResolver.query(uri, null, null, null, null)

    cursor?.use {
        // Move to the first row of the cursor
        if (it.moveToFirst()) {
            // Get the index of the DISPLAY_NAME column
            val nameIndex = it.getColumnIndex(OpenableColumns.DISPLAY_NAME)

            // Return the value from that column
            return if (nameIndex >= 0) it.getString(nameIndex) else null
        }
    }
    return null
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UploadSection(viewModel: UploadAssignmentViewModel, navController: NavController){
    val textModifier = Modifier
    val textFieldColors = TextFieldDefaults.colors(
        focusedIndicatorColor = Color.Transparent,
        unfocusedIndicatorColor = Color.Transparent,
        disabledIndicatorColor = Color.Transparent, // For disabled state if needed
        errorIndicatorColor = Color.Transparent, // For error state if needed
        focusedContainerColor = Color(0xFFF0F0F0), // Background color when focused
        unfocusedContainerColor = Color(0xFFE0E0E0),

        )
    val datePickerState = rememberDatePickerState()

    var expanded = remember { mutableStateOf(false) }
    val subjectId = remember { mutableStateOf<Int?>(null) }
    val subName = remember { mutableStateOf("") }
    val items = viewModel.getSubjects()
    var selectedItem = remember { mutableStateOf("Select an option") }
    val fileName = remember { mutableStateOf("No file chosen") }
    val file = remember { mutableStateOf<Uri?>(null) }
    val context = LocalContext.current
    val pdfPickerLauncher = rememberLauncherForActivityResult(ActivityResultContracts.OpenDocument()){
        file.value = it
        fileName.value = getFileName(context = context, it)?: "No file chosen"
    }
    Column(modifier = Modifier
        .padding(20.dp)
        .border(1.dp, color = Color.LightGray, shape = RoundedCornerShape(15.dp))) {
        Column(modifier = Modifier.padding(20.dp)) {

            Text("Subject", fontWeight = FontWeight.Bold, fontSize = 15.sp, textAlign = TextAlign.Center, )
            Box(

            ){
                Text(
                    text = selectedItem.value,

                    modifier = Modifier
                        .clip(RoundedCornerShape(10.dp))
                        .background(Color.LightGray)
                        .fillMaxWidth()
                        .padding(10.dp)
                        .clickable {
                            expanded.value = true
                        },

                )
                DropdownMenu(
                    expanded = expanded.value,
                    onDismissRequest = { expanded.value = false }, // Close menu when clicking outside
                    modifier = Modifier.background(Color.White)
                ) {
                    items.forEach { item ->
                        DropdownMenuItem(
                            text = { Text(item.Subject_name) },
                            onClick = {
                                selectedItem.value = item.Subject_name // Update selected item
                                subjectId.value = item.subject_id
                                expanded.value = false // Close menu
                            }
                        )
                    }
                }
            }
            Text("Assignment Title", fontWeight = FontWeight.Bold, fontSize = 15.sp)
            TextField(
                value = subName.value,
                onValueChange = {
                    if(it.length <= 100){
                        subName.value = it

                    }
                                },
                placeholder = {
                    Text(
                        text = "Enter text here",
                        style = androidx.compose.ui.text.TextStyle(
                            fontSize = 14.sp, // Adjust font size to fit smaller TextField
                            color = Color.Gray // Placeholder text color
                        )
                    )
                },
                textStyle = androidx.compose.ui.text.TextStyle(
                    fontSize = 14.sp,
                    color = Color.Black
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 10.dp)
                    .height(55.dp),
                shape = RoundedCornerShape(10.dp),
                colors = TextFieldDefaults.colors(
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent, // For disabled state if needed
                    errorIndicatorColor = Color.Transparent, // For error state if needed
                    focusedContainerColor = Color(0xFFF0F0F0), // Background color when focused
                    unfocusedContainerColor = Color(0xFFE0E0E0),

                    )
            )
            Text("Give your assignment a descriptive title (${subName.value.length}/100) characters")
            Text("Due Date")
            DatePickerDocked(datePickerState)
//            TextField(value = "", onValueChange = {}, modifier = Modifier.fillMaxWidth())
            Text("Assignment PDF")
            Box(modifier = Modifier
                .fillMaxWidth()
                .height(65.dp)
                .padding(vertical = 10.dp)
                .clip(RoundedCornerShape(10.dp)).clickable{
                    pdfPickerLauncher.launch(arrayOf("application/pdf"))
                },
                contentAlignment = Alignment.CenterStart,){
                Text(fileName.value, modifier = Modifier.padding(start = 10.dp))
            }

            Row {
                Box(modifier = Modifier
                    .width(120.dp)
                    .height(50.dp)
                    .clip(RoundedCornerShape(15.dp))
                    .background(Color.Black).clickable{
                        navController.popBackStack()
                    },
                    contentAlignment = Alignment.Center){
                    Text("Cancel", color = Color.White, fontSize = 18.sp)
                }
                Spacer(modifier = Modifier.width(20.dp))
                Box(modifier = Modifier
                    .width(220.dp)
                    .height(50.dp)
                    .clip(RoundedCornerShape(15.dp))
                    .background(Color(0xFF62C370)).clickable{
                        if(subjectId.value != null && file.value != null && subName.value.isNotEmpty()) {
                            viewModel.uploadAssignment(
                                subjectId.value!!,
                                subName.value,
                                file.value!!,
                                dueDate = convertToReadable(datePickerState.selectedDateMillis),
                                navController
                            )

                        }
                    },
                    contentAlignment = Alignment.Center){
                    Text("Upload Assignment", fontSize = 18.sp)
                }
            }
        }
    }
}

@Composable
fun GuideLinesList() {
    val items = listOf("Use clear, descriptive names for easy identification", "Subjects are shared with all group members", "Only group owners can create and delete subjects", "Deleting a subject removes all it's assignments")
    Box(modifier = Modifier
        .padding(20.dp)
        .border(1.dp, color = Color.LightGray, shape = RoundedCornerShape(15.dp))){
        Column(modifier = Modifier
            .fillMaxWidth()
            .padding(20.dp)) {
            Text("Upload Guidelines", fontWeight = FontWeight.Bold, fontSize = 18.sp)
            Spacer(modifier = Modifier.height(30.dp))
            items.forEach { item ->
                Row(verticalAlignment = Alignment.CenterVertically, ) {
                    Icon(
                        imageVector = Icons.Default.Circle,
                        contentDescription = null,
                        modifier = Modifier
                            .padding(end = 8.dp)
                            .size(8.dp),
                        tint = Color.Black
                    )
                    Text(
                        text = item,
                        fontSize = 16.sp,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                }
            }
        }
    }}

@Composable
fun UploadGuidelines(){
    Column {

        GuideLinesList()
    }
}

fun convertToReadable(instant: Long?): String {
    if(instant != null){
        return java.time.Instant.ofEpochMilli(instant).toString()
    }else{
        return ""
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePickerDocked(datePickerState: DatePickerState) {
    var showDatePicker = remember { mutableStateOf(false) }
    val selectedDate = datePickerState.selectedDateMillis?.let {
        convertMillisToDate(it)
    } ?: ""
    LaunchedEffect(datePickerState.selectedDateMillis) {
        val date = convertToReadable(datePickerState.selectedDateMillis)
        Log.d("DatePickerDocked", "Selected date millis: ${date}")
        showDatePicker.value = false

    }

    Box(
        modifier = Modifier.clickable{
            Log.d("DatePickerDocked", "TextField clicked")
            showDatePicker.value = !showDatePicker.value
        }
    ) {
        OutlinedTextField(
            value = selectedDate,
            onValueChange = { },
            interactionSource = remember { MutableInteractionSource() }
                .also { interactionSource ->
                    LaunchedEffect(interactionSource) {
                        interactionSource.interactions.collect {
                            if (it is PressInteraction.Release) {
                                showDatePicker.value = !showDatePicker.value
                                Log.d("DatePickerDocked", "TextField clicked")


                            }
                        }
                    }
                },
            label = { Text("Due Date") },
            readOnly = true,
            trailingIcon = {
                IconButton(onClick = { showDatePicker.value = !showDatePicker.value }) {
                    Icon(
                        imageVector = Icons.Default.DateRange,
                        contentDescription = "Select date"
                    )
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(64.dp)
        )

        if (showDatePicker.value) {
            Popup(
                onDismissRequest = { showDatePicker.value = false },
                alignment = Alignment.TopStart
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth(0.8f)
                        .offset(y = 64.dp)
                        .shadow(elevation = 4.dp)
                        .background(MaterialTheme.colorScheme.surface)
                        .padding(16.dp)
                ) {
                    DatePicker(
                        state = datePickerState,
                        showModeToggle = false
                    )
                }
            }
        }
    }
}

fun convertMillisToDate(millis: Long): String {
    val formatter = SimpleDateFormat("MM/dd/yyyy", Locale.getDefault())
    return formatter.format(Date(millis))
}