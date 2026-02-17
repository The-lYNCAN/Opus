package com.lyncan.opus.Presentation.ui.screens

import android.graphics.Bitmap
import android.widget.ImageView
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Group
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateMap
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.window.Dialog
import androidx.core.graphics.createBitmap
import androidx.core.graphics.set
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.google.zxing.BarcodeFormat
import com.google.zxing.qrcode.QRCodeWriter
import com.lyncan.opus.Presentation.States.GroupState
import com.lyncan.opus.Presentation.ui.components.ConfirmationDialog
import com.lyncan.opus.Presentation.ui.components.Group.Exit
import com.lyncan.opus.Presentation.ui.components.Group.Hero
import com.lyncan.opus.Presentation.ui.components.Group.Members
import com.lyncan.opus.Presentation.ui.components.Group.OwnerAction
import com.lyncan.opus.Presentation.viewmodels.MainViewModel
import com.lyncan.opus.Presentation.viewmodels.groupModel
import com.lyncan.opus.data.Groups
import com.lyncan.opus.data.Subject
import com.lyncan.opus.data.user
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@Composable
fun GroupScreen(navController: NavController, sharedViewModel: MainViewModel) {
    val viewModel = hiltViewModel<groupModel>()
    val subjectList = viewModel.subjects.collectAsState()
    val expandedMap = remember { mutableStateMapOf<Int, Boolean>() }
    val group_details = viewModel.group.collectAsState()
    val showQR = remember { mutableStateOf(false) }
    val user = viewModel.user.collectAsState()
    val members = viewModel.members.collectAsState()
    val showConfirmationDialog = remember { mutableStateOf(false) }
    val isGroupOwner = remember {
        mutableStateOf(user.value?.user_id == group_details.value?.admin)
    }
    val state = viewModel.state.collectAsState()

    if(group_details.value != null){
        if(showQR.value){
//        QrCodeSenderScreen(group_details.value.invite_code)
            MinimalDialog(inviteCode = group_details.value!!.invite_code, onDismissRequest = { showQR.value = false })
        }else{
            if(!showConfirmationDialog.value){
                GroupScreenUI(group_details, subjectList, expandedMap, members, isGroupOwner, navController, showQR, showConfirmationDialog, state)
            }else{
                if(isGroupOwner.value){
                    ConfirmationDialog(
                        title = "Delete Group",
                        text = "Are you sure you want to delete this group? This action cannot be undone.",
                        onConfirm = {
                            viewModel.deleteGroup()
                        },
                        onDismiss = {
                            showConfirmationDialog.value = false
                        }
                    )
                }else{
                    ConfirmationDialog(
                        title = "Exit Group",
                        text = "Are you sure you want to exit this group? This action cannot be undone.",
                        onConfirm = {
                            viewModel.exitGroup()
                        },
                        onDismiss = {
                            showConfirmationDialog.value = false
                        }
                    )
                }
            }
        }
    }else{
        Column(modifier=Modifier.verticalScroll(rememberScrollState())) {
            //Here comes an image
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ){
                Box(
                    modifier = Modifier
                        .fillMaxSize(.25f)
                        .border(1.dp, color = Color.LightGray, shape = CircleShape)
                        .aspectRatio(1f),
                    contentAlignment = Alignment.Center,
                    ){
                    Icon(
                        Icons.Filled.Group,
                        contentDescription = "Group Icon",
                        modifier = Modifier.fillMaxSize(.7f)
                    )
                }
            }
            Text("Welcome to Opus!", modifier = Modifier.fillMaxWidth(),
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,)
            Text("Get started by joining or creating a study group to collaborate on assignments.", modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 40.dp, vertical = 15.dp),
                textAlign = TextAlign.Center,)

        }
    }
}



fun encodeToQrBitmap(data: String, size: Int = 512): Bitmap {
    val writer = QRCodeWriter()
    val bitMatrix = writer.encode(data, BarcodeFormat.QR_CODE, size, size)

    val width = bitMatrix.width
    val height = bitMatrix.height
    val bitmap = createBitmap(width, height, Bitmap.Config.RGB_565)

    // Convert the bitMatrix to a Bitmap
    for (x in 0 until width) {
        for (y in 0 until height) {
            bitmap[x, y] = (if (bitMatrix.get(x, y)) Color.Black else Color.White).hashCode()
        }
    }
    return bitmap
}

@Composable
fun QrCodeSenderScreen(dataToShare: String) {
    // State to hold the generated Bitmap
    var qrBitmap by remember { mutableStateOf<Bitmap?>(null) }

    // LaunchedEffect to run the encoding off the main thread once
    LaunchedEffect(dataToShare) {
        withContext(Dispatchers.Default) {
            qrBitmap = encodeToQrBitmap(dataToShare)
        }
    }

    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxSize()) {


        qrBitmap?.let { bitmap ->
            // Use AndroidView to display the Bitmap in an ImageView
            AndroidView(
                factory = { context ->
                    ImageView(context).apply {
                        scaleType = ImageView.ScaleType.FIT_CENTER
                    }
                },
                update = { imageView ->
                    imageView.setImageBitmap(bitmap)
                },
                modifier = Modifier.size(200.dp)
            )
        } ?: CircularProgressIndicator()
    }
}

@Composable
fun MinimalDialog(onDismissRequest: () -> Unit, inviteCode: String) {
    Dialog(onDismissRequest = { onDismissRequest() }) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .padding(16.dp),
            shape = RoundedCornerShape(16.dp),
        ) {
            QrCodeSenderScreen(inviteCode)
        }
    }
}

@Composable
fun GroupScreenUI(
    group_details: State<Groups?>,
    subjectList: State<List<Subject>>,
    expandedMap: SnapshotStateMap<Int, Boolean>,
    members: State<List<user>>,
    isGroupOwner: MutableState<Boolean>,
    navController: NavController,
    showQR: MutableState<Boolean>,
    showConfirmationDialog: MutableState<Boolean>,
    state: State<GroupState?>
) {
    Column(modifier = Modifier.verticalScroll(rememberScrollState())) {

        Hero(showQR, state.value?.groupName?: "")
        Spacer(Modifier.height(20.dp))
        OwnerAction(navController, state.value?.totalSubjects?: 0, state.value?.timeTableSetted?: false)
        Spacer(Modifier.height(20.dp))

        Members(state.value?.groupMembers?: emptyList(), admin = state.value?.admin?: "")
        Exit(showConfirmationDialog, isGroupOwner)
    }
}