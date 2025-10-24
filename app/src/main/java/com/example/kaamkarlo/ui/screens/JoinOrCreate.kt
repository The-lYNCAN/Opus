package com.example.kaamkarlo.ui.screens

import androidx.annotation.OptIn
import androidx.camera.core.CameraSelector
import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.QrCode
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MultiChoiceSegmentedButtonRow
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.window.Dialog
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.navigation.NavController
import com.example.kaamkarlo.uI.screens.QrCodeSenderScreen
import com.example.kaamkarlo.viewmodels.MainViewModel
import com.google.mlkit.vision.barcode.BarcodeScannerOptions
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.common.InputImage
import java.util.concurrent.Executors


@Composable
fun JoinOrCreate(navController: NavController, sharedViewModel: MainViewModel, joinOrCreate: Boolean){
    var groupName by remember { mutableStateOf("") }
    var groupDescription by remember { mutableStateOf("") }
    val join = remember { mutableStateOf(joinOrCreate) }
    var selectedOption by remember{ mutableStateOf<String>("All") }
    var inviteCode by remember { mutableStateOf("") }


    Column {

        Text("Create or Join Group", fontSize = 24.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(horizontal = 24.dp))
        SegmentedControl(selectedOption, onCheckChange = { option ->
            selectedOption = option
            join.value = option != "Join Group"
        })
        if(join.value){
            Box(modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp, vertical = 20.dp).border(1.dp, color = Color.LightGray, shape = RoundedCornerShape(10.dp))){
                Column(verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.padding(top = 30.dp)) {

                    Box(
                        modifier = Modifier.size(80.dp).clip(CircleShape).border(1.dp, color = Color(red = 243, blue = 243, green=245), shape = CircleShape).background(Color(red = 243, blue = 243, green=245)), contentAlignment = Alignment.Center
                    ){
                        Icon(Icons.Filled.Add, contentDescription = "Create a new group", Modifier.size(58.dp))
                    }
                    Spacer(modifier = Modifier.height(15.dp))
                    Text("Create New Group", fontWeight = FontWeight.Bold, fontSize = 24.sp)
                    Text("Start a new study group and invite others to collaborate", textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth().padding(top = 5.dp, bottom = 15.dp))
                    TextFields(
                        groupName,
                        onValueChange = {groupName = it},
                        label = "Group Name",
                        placeholder = "e.g., Computer Science 101"
                    )
                    TextFields(
                        groupDescription,
                        onValueChange = {groupDescription = it},
                        label = "Description",
                        placeholder = "Describe what this is for"
                    )
                    Button(
                        onClick = {
                            val groupID = sharedViewModel.createGroup(groupName, groupDescription, navController)
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.Black,
                            contentColor = Color.White
                        )

                    ) {
                        Text("Create Group", color = Color.White)
                    }
                }
            }
        }else{
            Box(modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp, vertical = 20.dp).border(1.dp, color = Color.LightGray, shape = RoundedCornerShape(10.dp))){
                Column(verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.padding(top = 30.dp)) {

                    Box(
                        modifier = Modifier.size(80.dp).clip(CircleShape).border(1.dp, color = Color(red = 243, blue = 243, green=245), shape = CircleShape).background(Color(red = 243, blue = 243, green=245)), contentAlignment = Alignment.Center
                    ){
                        Icon(Icons.Filled.Add, contentDescription = "Create a new group", Modifier.size(58.dp))
                    }
                    Spacer(modifier = Modifier.height(15.dp))
                    Text("Join Existing Group", fontWeight = FontWeight.Bold, fontSize = 24.sp)
                    Text("Enter a group code or scan QR code to join an existing study group", textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth().padding(top = 5.dp, bottom = 15.dp, start = 20.dp, end = 20.dp))
                    TextFieldForJoin(
                        value = inviteCode,
                        onValueChange = {inviteCode = it},
                        label = "Invite Code",
                        placeholder = "Enter 8-character code"
                    )
                    Button(
                        onClick = { sharedViewModel.joinGroup(inviteCode, navController) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.Black,
                            contentColor = Color.White
                        )

                    ) {
                        Text("Join Group", color = Color.White)
                    }
                }
            }
        }
    }



}

@Composable
fun SegmentedControl(selectedOption: String, onCheckChange: (String) -> Unit = {}){
    MultiChoiceSegmentedButtonRow(modifier = Modifier.fillMaxWidth(1f).padding(horizontal = 30.dp, vertical = 15.dp)) {
        val options = listOf("Create Group", "Join Group")
        options.forEach { option->
            SegmentedButton(
                checked = (option == selectedOption),
                onCheckedChange = { onCheckChange(option) },
                shape = SegmentedButtonDefaults.itemShape(
                    index = options.indexOf(option),
                    count = options.size
                ),
            ) {
                Text(option)
            }
        }
    }
}

@Composable
fun TextFields(value: String, onValueChange: (String) -> Unit, label: String, placeholder: String){
    Column(modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp)) {
        Text(label, fontWeight = FontWeight.Bold)
        TextField(
            value = value,
            onValueChange = onValueChange,
            placeholder = { Text(placeholder) },
            modifier = Modifier
                 // Rounded border
                .fillMaxWidth(), // Optional: make TextField full width
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color(red = 243, blue = 243, green=245), // Background when focused
                unfocusedContainerColor = Color(red = 243, blue = 243, green=245), // Background when not focused
                focusedIndicatorColor = Color.Transparent, // Hide default underline when focused
                unfocusedIndicatorColor = Color.Transparent // Hide default underline when not focused
            ),
            shape = RoundedCornerShape(12.dp)
        )
    }
}

@Composable
fun TextFieldForJoin(value: String, onValueChange: (String) -> Unit, label: String, placeholder: String){
    var isScannerVisible = remember { mutableStateOf(false) }

    // State to hold the result of the scan
    var scannedQrCodeValue by remember { mutableStateOf("No QR code scanned yet") }
    Column(modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp)) {
        Text(label, fontWeight = FontWeight.Bold)
        Row(
            modifier = Modifier.fillMaxWidth().height(intrinsicSize = IntrinsicSize.Min),
            verticalAlignment = Alignment.CenterVertically,
            // Align items vertically
        ) {
            TextField(
                value = value,
                onValueChange = onValueChange,
                placeholder = { Text(placeholder) },
                modifier = Modifier
                    .weight(1f), // TextField takes available width
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color(red = 243, blue = 243, green=245),
                    unfocusedContainerColor = Color(red = 243, blue = 243, green=245),
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                ),
                shape = RoundedCornerShape(12.dp)
            )
            Spacer(modifier = Modifier.width(8.dp)) // Add some space
            Box(
                modifier = Modifier
                    .fillMaxHeight() // This will make the Box try to fill the height of the Row
                    .aspectRatio(1f) // Optional: if you want the Box to be a square
                    .border(1.dp, color = Color.LightGray, shape = RoundedCornerShape(12.dp)) // Match TextField's corner
                    .padding(8.dp)
                    .clickable{
                        isScannerVisible.value = true

                    }, // Add padding inside the border
                contentAlignment = Alignment.Center
            ){
                Icon(Icons.Filled.QrCode, contentDescription = "Scan Qr Code")
            }
        }
        if (isScannerVisible.value) {
            // Place the scanner Composable
            MinimalCameraDialog(
                onDismissRequest = { isScannerVisible.value = false },
                onValueChange = onValueChange,
                isScannerVisible = isScannerVisible
            )
        }
    }
}

@Composable
fun MinimalCameraDialog(onDismissRequest: () -> Unit, onValueChange: (String) -> Unit, isScannerVisible: MutableState<Boolean>) {
    Dialog(onDismissRequest = { onDismissRequest() }) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .padding(16.dp),
            shape = RoundedCornerShape(16.dp),
        ) {
            QRCodeScannerView(
                onQrCodeDetected = { result ->
                    // 3. Handle the detection callback
                    onValueChange(result)
                    isScannerVisible.value = false // Hide the scanner after a successful scan
                    // You would typically navigate or process the result here
                }
            )
        }
    }
}
@Composable
fun QRCodeScannerView(onQrCodeDetected: (String) -> Unit) {
    val localContext = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val cameraExecutor = remember { Executors.newSingleThreadExecutor() }

    DisposableEffect(lifecycleOwner) {
        onDispose {
            // Shutdown the executor when the composable leaves the screen
            cameraExecutor.shutdown()
        }
    }

    AndroidView(
        factory = { context ->
            val previewView = PreviewView(context).apply {
                // Configure PreviewView properties
                scaleType = PreviewView.ScaleType.FILL_CENTER
            }

            // Get a CameraProvider
            val cameraProviderFuture = ProcessCameraProvider.getInstance(context)

            cameraProviderFuture.addListener({
                val cameraProvider = cameraProviderFuture.get()
                val preview = Preview.Builder().build().also {
                    it.setSurfaceProvider(previewView.surfaceProvider)
                }

                val imageAnalyzer = ImageAnalysis.Builder()
                    .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                    .build()
                    .also {
                        // Set your custom QR Code Analyzer
                        it.setAnalyzer(cameraExecutor, BarcodeAnalyzer(onQrCodeDetected))
                    }

                // Select back camera
                val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

                try {
                    cameraProvider.unbindAll()
                    cameraProvider.bindToLifecycle(
                        lifecycleOwner,
                        cameraSelector,
                        preview,
                        imageAnalyzer
                    )
                } catch (e: Exception) {
                    // Handle error
                }
            }, ContextCompat.getMainExecutor(context))

            previewView
        },
        modifier = Modifier.fillMaxWidth()
    )
}
class BarcodeAnalyzer(private val onBarcodeDetected: (String) -> Unit) : ImageAnalysis.Analyzer {

    private val options = BarcodeScannerOptions.Builder()
        .setBarcodeFormats(Barcode.FORMAT_QR_CODE) // Only scan QR codes
        .build()

    private val scanner = BarcodeScanning.getClient(options)

    @OptIn(ExperimentalGetImage::class)
    override fun analyze(imageProxy: ImageProxy) {
        // Convert the image to a format ML Kit can use
        val mediaImage = imageProxy.image ?: return
        val image = InputImage.fromMediaImage(mediaImage, imageProxy.imageInfo.rotationDegrees)

        // Process the image
        scanner.process(image)
            .addOnSuccessListener { barcodes ->
                if (barcodes.isNotEmpty()) {
                    // 2. Data detected! Pass it back and close the scanner
                    barcodes.first().rawValue?.let { rawValue ->
                        onBarcodeDetected(rawValue)
                    }
                }
            }
            .addOnCompleteListener {
                // Close the image to free up the buffer
                imageProxy.close()
            }
    }
}
