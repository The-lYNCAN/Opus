package com.lyncan.opus.uI.screens

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.lyncan.opus.uI.components.PrintService.PrintJobItem
import com.lyncan.opus.viewmodels.MainViewModel
import java.io.File
import java.io.FileOutputStream

@Suppress("unused")
@Composable
fun PrintScreen(navController: NavController, sharedViewModel: MainViewModel) {
    val context = LocalContext.current
    var selectedFileUri by remember { mutableStateOf<Uri?>(null) }

    // Launcher for file picker
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ){uri ->
        selectedFileUri = uri as Uri?
        uri?.let{handleFileSelection(context, it)}

    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp).verticalScroll(
                rememberScrollState()
            )
    ) {
        Text("Print Service", modifier = Modifier, fontWeight = FontWeight.Bold, fontSize = 24.sp)
        Text("Upload documents and manage print jobs")
//        Text("Don't worry we will find you")
        // Header with Balance and Completed Jobs
        Spacer(modifier = Modifier.height(25.dp))
        Box(
            modifier = Modifier.fillMaxWidth().height(160.dp)
                .border(width = 1.dp, color = Color.LightGray, shape = RoundedCornerShape(15.dp)),
            contentAlignment = Alignment.Center
        ){
            Column {

                Text("1", fontWeight = FontWeight.Bold, fontSize = 20.sp, modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center)
                Text("Completed", modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center)
            }
        }

        // Upload New Document Section
//        Card(
//            modifier = Modifier
//                .fillMaxWidth()
//                .padding(top = 16.dp)
//        ) {
//            Column(
//                modifier = Modifier.padding(16.dp),
//                horizontalAlignment = Alignment.CenterHorizontally
//            ) {
//                Icon(Icons.Default.DateRange, contentDescription = "Upload")
//                Text("Upload New Document")
//                Box(
//                    modifier = Modifier
//                        .size(200.dp)
//                        .border(1.dp, Color.Gray)
//                ) {
//                    Text("Drag and drop your PDF here,\nor click to browse")
//                    Button(onClick = { {
//                        Log.d("Upload BUtton was clicked", "clicked !")
//                        launcher.launch("application/pdf") } }) {
//                        Text("Choose File")
//                    }
//                }
//            }
//        }
        Spacer(modifier = Modifier.height(30.dp))
        Box(
            modifier = Modifier.fillMaxWidth()
                .border(width = 1.dp, color = Color.LightGray, shape = RoundedCornerShape(15.dp)),
            contentAlignment = Alignment.Center
        ){
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(Icons.Default.DateRange, contentDescription = "Upload")
                Text("Upload New Document")
                Button(onClick = {
                    Log.d("Upload BUtton was clicked", "clicked !")
                    launcher.launch("application/pdf")  }) {
                    Text("Choose File")
                }
                Text("Pricing: ")
                Text("B&W: 2Rs")
                Text("Color: 11Rs")

            }
        }

        // Pricing Section
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp)
        ) {

        }

        // Print Jobs Section
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                TabRow(selectedTabIndex = 0) {
                    Tab(text = { Text("All") }, selected = true, onClick = {})
                    Tab(text = { Text("Ready") }, selected = false, onClick = {})
                    Tab(text = { Text("Printing") }, selected = false, onClick = {})
                    Tab(text = { Text("Done") }, selected = false, onClick = {})
                }
                PrintJobItem(
                    title = "Assignment_Math_Chapter7.pdf",
                    pages = 15,
                    copies = 1,
                    color = false,
                    status = "Completed",
                    uploaded = "Feb 20, 09:30 AM",
                    cost = "$",
                    pickedUp = true
                )
                PrintJobItem(
                    title = "Physics_Lab_Report.pdf",
                    pages = 8,
                    copies = 2,
                    color = true,
                    status = "Ready for Pickup",
                    uploaded = "Feb 20, 02:15 PM",
                    cost = "$3.20",
                    pickedUp = false
                )
                PrintJobItem(
                    title = "Literature_Essay_Draft.pdf",
                    pages = 6,
                    copies = 1,
                    color = false,
                    status = "Printing",
                    uploaded = "Feb 20, 04:45 PM",
                    cost = "$0.60",
                    progress = 60
                )
            }
        }
    }
}
fun handleFileSelection(context: Context, uri: Uri) {
    // Example: Copy file to app's internal storage and perform operations
    val inputStream = context.contentResolver.openInputStream(uri)
    val file = File(context.filesDir, "selected_file.pdf")
    inputStream?.use { input ->
        FileOutputStream(file).use { output ->
            input.copyTo(output)
        }
    }
    // Add your file processing logic here, e.g., read PDF, upload, etc.
    // For example: Log the file path or process the PDF content
}