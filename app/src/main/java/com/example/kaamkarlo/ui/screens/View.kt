package com.example.kaamkarlo.uI.screens

import android.net.Uri
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import com.example.kaamkarlo.uI.components.View.PdfViewer
import com.example.kaamkarlo.viewmodels.MainViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream
import java.net.HttpURLConnection
import java.net.URL

@Composable
fun ViewAssignment(navController: NavController, sharedViewModel: MainViewModel) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val pdfUri = remember { mutableStateOf<Uri?>(null) }
    val id = sharedViewModel.getId()
    val link = sharedViewModel.getLink()
    Log.d("ViewAssignment", "Navigated to ViewAssignment with id: $id and link: $link")
    // Download the sample PDF
    DisposableEffect(Unit) {
        val job = coroutineScope.launch(Dispatchers.IO) {
            try {
                val url = URL(link)
                val connection = url.openConnection() as HttpURLConnection
                connection.connect()
                val file = File(context.cacheDir, "sample.pdf")
                FileOutputStream(file).use { output ->
                    connection.inputStream.use { input ->
                        input.copyTo(output)
                    }
                }
                pdfUri.value = Uri.fromFile(file)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        onDispose { job.cancel() }
    }

    // Display PDF or loading state
    pdfUri.value?.let { uri ->
        PdfViewer(
            uri = uri,
            modifier = Modifier.fillMaxSize(),
//            verticalArrangement = Arrangement.spacedBy(8.dp)
        )
    } ?: Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Gray),
        contentAlignment = androidx.compose.ui.Alignment.Center
    ) {
        androidx.compose.material3.Text("Loading PDF...", color = Color.White)
    }
}