package com.lyncan.opus.uI.screens

import android.Manifest
import android.graphics.Bitmap
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Matrix
import android.graphics.BitmapFactory
import android.graphics.pdf.PdfDocument
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CameraAlt
import androidx.compose.material.icons.outlined.Upload
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.navigation.NavController
import coil3.compose.rememberAsyncImagePainter
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import androidx.core.graphics.scale
import androidx.hilt.navigation.compose.hiltViewModel
import com.lyncan.opus.viewmodels.MainViewModel
import com.lyncan.opus.viewmodels.SubmissionViewModel
import kotlinx.coroutines.launch
import androidx.exifinterface.media.ExifInterface
import com.lyncan.opus.data.Assignment
import com.lyncan.opus.data.Uploads
import java.io.IOException


@Composable
fun Submit(navController: NavController, sharedViewModel: MainViewModel, assignmentId: Int) {
    val gridState = rememberLazyGridState()
    val viewModel = hiltViewModel<SubmissionViewModel>()
    viewModel.setAssignment(assignmentId)
    val context = LocalContext.current
    val photos = remember { mutableStateListOf<Uri>() }
    val maxPhotos = 50
    var showPermissionDeniedMessage by remember { mutableStateOf(false) }
    val photoUri = remember { mutableStateOf<Uri?>(null) }
    val scope = rememberCoroutineScope()
    val takePictureLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.TakePicture()
    ) { success ->
        if (success && photoUri.value != null) {
            if (photos.size < maxPhotos) {
                photos.add(photoUri.value!!)
            } else {
                // Optionally show a message that max photos reached
            }
        }
    }
    val assignment = viewModel.assignment.collectAsState()
    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            // Create a new file for the photo
            photoUri.value = createImageFileUri(context)
            takePictureLauncher.launch(photoUri.value!!)
        } else {
            showPermissionDeniedMessage = true
        }
    }
    Column {

        Spacer(modifier = Modifier.height(20.dp))
        Text("Document Scanner", fontSize = 24.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(horizontal = 20.dp))
        Text(assignment.value?.assignment_name?: "N/A", Modifier.padding(horizontal = 20.dp))
        Box(modifier = Modifier
            .fillMaxWidth()
            .padding(20.dp)
            .border(1.dp, Color.LightGray, shape = RoundedCornerShape(10.dp))){
            Column {
                Spacer(modifier = Modifier.height(10.dp))
                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(horizontal = 10.dp)) {
                    Icon(Icons.Outlined.CameraAlt, contentDescription = "Camera Icon", modifier = Modifier.padding(10.dp))
                    Text("Capture Pages")
                }
                Spacer(modifier = Modifier.height(20.dp))
                Row(modifier = Modifier.padding(10.dp)) {
                    Box(
                        modifier = Modifier
                            .clip(shape = RoundedCornerShape(10.dp))
                            .weight(1f)
                            .height(95.dp)
                            .border(1.dp, Color.LightGray, shape = RoundedCornerShape(10.dp))
                            .background(Color.Black)
                            .clickable {
                                if (photos.size >= maxPhotos) {
                                    showPermissionDeniedMessage =
                                        true // Reuse for max photos message
//                                    return@Button
                                }
                                if (ContextCompat.checkSelfPermission(
                                        context,
                                        Manifest.permission.CAMERA
                                    ) == PackageManager.PERMISSION_GRANTED
                                ) {
                                    Log.d("Submit", "Camera permission already granted")
                                    photoUri.value = createImageFileUri(context)
                                    takePictureLauncher.launch(photoUri.value!!)
                                } else {
                                    Log.d("Submit", "Requesting camera permission")
                                    permissionLauncher.launch(Manifest.permission.CAMERA)

                                }

                            },
                        contentAlignment = Alignment.Center
                    ){
                        Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
                            Icon(Icons.Outlined.CameraAlt, contentDescription = "Camera Icon", tint = Color.White)
                            Text("Take Photo", color = Color.White)
                        }
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Box(modifier = Modifier
                        .weight(1f)
                        .height(95.dp)
                        .border(1.dp, Color.LightGray, shape = RoundedCornerShape(10.dp)), contentAlignment = Alignment.Center){
                        Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
                            Icon(Icons.Outlined.Upload, contentDescription = "Upload Icon")
                            Text("Upload Image")
                        }
                    }
                }
                Spacer(modifier = Modifier.height(20.dp))
                Text("Pages Captured: ${photos.size}")
            }
        }
        if (photos.isNotEmpty()) {
            LazyVerticalGrid(
                columns = GridCells.Fixed(3), // Defines a grid with 3 fixed columns
                modifier = Modifier
                    .fillMaxWidth(),
                contentPadding = PaddingValues(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(count = photos.size) { item ->
                    GridItem(photos[item])
                }
            }
        }
        Button(
            onClick = {
                scope.launch {
                    uploadPhotos(context, photos, viewModel, assignment)
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            enabled = photos.isNotEmpty(),
            shape = RoundedCornerShape(5.dp)
        ) {
            Text("Upload Assignment")
        }
    }
}

@Composable
fun GridItem(item: Uri) {
//    Card(
//        shape = RoundedCornerShape(8.dp),
//        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
//        modifier = Modifier.size(120.dp) // Set a fixed size for each item
//    ) {
//        // You would load a real image here. For this example, we use a placeholder.
//        Text(text = "Item $item", modifier = Modifier.padding(16.dp))
//    }
    Image(
        painter = rememberAsyncImagePainter(item),
        contentDescription = "Captured photo",
        modifier = Modifier
            .size(100.dp)
            .padding(4.dp)
    )
}
private fun createImageFileUri(context: Context): Uri {
    val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(Date())
    val storageDir = context.cacheDir
    val imageFile = File.createTempFile(
        "JPEG_${timeStamp}_",
        ".jpg",
        storageDir
    )
    return FileProvider.getUriForFile(
        context,
        "com.lyncan.opus.fileprovider", // Must match AndroidManifest.xml
        imageFile,
    )
}


fun createPdfFromImages(context: Context, imageUris: List<Uri>): File? {
    if (imageUris.isEmpty()) {
        Toast.makeText(context, "No images to create a PDF from.", Toast.LENGTH_SHORT).show()
        return null
    }

    val pdfDocument = PdfDocument()

    // A4 page size in points (1/72 of an inch)
    val pageHeight = 842
    val pageWidth = 595

    try {
        imageUris.forEachIndexed { index, uri ->
            context.contentResolver.openInputStream(uri)?.use { inputStream ->
                val originalBitmap = BitmapFactory.decodeStream(inputStream)

                // --- Start: Fix for Rotation ---
                // Get the orientation of the image from its EXIF data
                val exif = context.contentResolver.openInputStream(uri)?.use { exifStream ->
                    ExifInterface(exifStream)
                }
                val orientation = exif?.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL) ?: ExifInterface.ORIENTATION_NORMAL

                val matrix = Matrix()
                when (orientation) {
                    ExifInterface.ORIENTATION_ROTATE_90 -> matrix.postRotate(90f)
                    ExifInterface.ORIENTATION_ROTATE_180 -> matrix.postRotate(180f)
                    ExifInterface.ORIENTATION_ROTATE_270 -> matrix.postRotate(270f)
                }

                val rotatedBitmap = Bitmap.createBitmap(
                    originalBitmap, 0, 0, originalBitmap.width, originalBitmap.height, matrix, true,
                )
                // --- End: Fix for Rotation ---

                // --- Start: Improved Scaling Logic ---
                val scaleFactor = Math.min(
                    pageWidth.toFloat() / rotatedBitmap.width,
                    pageHeight.toFloat() / rotatedBitmap.height
                )
                val scaledBitmap = rotatedBitmap.scale(
                    (rotatedBitmap.width * scaleFactor).toInt(), (rotatedBitmap.height * scaleFactor).toInt()
                )
                // --- End: Improved Scaling Logic ---

                // Create a new page
                val pageInfo = PdfDocument.PageInfo.Builder(pageWidth, pageHeight, index + 1).create()
                val page = pdfDocument.startPage(pageInfo)
                val canvas = page.canvas

                // Center the image on the page
                val leftMargin = (pageWidth - scaledBitmap.width) / 2f
                val topMargin = (pageHeight - scaledBitmap.height) / 2f

                canvas.drawBitmap(scaledBitmap, leftMargin, topMargin, null)
                pdfDocument.finishPage(page)

                // Recycle bitmaps to free up memory immediately
                // Only recycle rotatedBitmap if it's a different object from originalBitmap
                if (originalBitmap != rotatedBitmap) {
                    rotatedBitmap.recycle()
                }
                originalBitmap.recycle()
                scaledBitmap.recycle()
            }
        }

        val outputDir = File(context.filesDir, "pdfs")
        if (!outputDir.exists()) {
            outputDir.mkdirs()
        }
        val outputFile = File(outputDir, "generated_doc_${System.currentTimeMillis()}.pdf")

        pdfDocument.writeTo(FileOutputStream(outputFile))
        Toast.makeText(context, "PDF saved to app's private folder", Toast.LENGTH_LONG).show()
        return outputFile

    } catch (e: IOException) {
        e.printStackTrace()
        Toast.makeText(context, "Error creating PDF: ${e.message}", Toast.LENGTH_LONG).show()
        return null
    } finally {
        pdfDocument.close()
    }
}


private suspend fun uploadPhotos(
    context: Context,
    photos: List<Uri>,
    viewModel: SubmissionViewModel,
    assignment: State<Assignment?>, ) {
    // Placeholder function to simulate photo upload
    // In a real app, implement actual upload logic here
    val pdfFile = createPdfFromImages(context, photos)
    val progress = viewModel.handleUpload(pdfFile!!.name, pdfFile)
    var key: String? = ""
    progress.collect {
        Log.d("Upload", "Upload progress: $it")
        if(it is io.github.jan.supabase.storage.UploadStatus.Success) {
            key = it.response.key
        }
    }
    Log.d("Submit", progress.toString())
    val uploadObj = Uploads(
        assignment_id = assignment.value?.assignment_id?: -1,
        user_id = viewModel.currentUser.value.user_id,
        views = 0,
        upload_url = viewModel.getLink(key?:""),
        mark_as_solution = false,
    )
    viewModel.makeEntry(uploadObj)


    Log.d("Submit", "Uploading ${photos.size} photos")
}