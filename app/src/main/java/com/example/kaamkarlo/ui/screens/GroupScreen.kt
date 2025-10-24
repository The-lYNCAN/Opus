package com.example.kaamkarlo.uI.screens

import android.graphics.Bitmap
import android.util.Log
import android.view.RoundedCorner
import android.widget.ImageView
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.MenuBook
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.PersonAddAlt
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.ContentCopy
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.ExitToApp
import androidx.compose.material.icons.outlined.Group
import androidx.compose.material.icons.outlined.MenuBook
import androidx.compose.material.icons.outlined.QrCode
import androidx.compose.material.icons.outlined.Share
import androidx.compose.material.icons.outlined.Subject
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.ModifierLocalBeyondBoundsLayout
import androidx.compose.ui.layout.VerticalAlignmentLine
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil3.compose.AsyncImage
import coil3.request.colorSpace
import com.example.kaamkarlo.uI.navigation.Route
import com.example.kaamkarlo.viewmodels.MainViewModel
import com.example.kaamkarlo.viewmodels.groupModel
import com.google.zxing.BarcodeFormat
import com.google.zxing.qrcode.QRCodeWriter
import androidx.core.graphics.set
import androidx.core.graphics.createBitmap
import com.example.kaamkarlo.uI.components.Home.CardUnJoined
import com.example.kaamkarlo.uI.components.Home.UnJoinedInfoCard
import com.example.kaamkarlo.ui.components.ConfirmationDialog
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@Composable
fun GroupScreen(navController: NavController, sharedViewModel: MainViewModel) {
    val viewModel = hiltViewModel<groupModel>()
    val subjectList = viewModel.subjects.collectAsState()
    val assignmentsObj = viewModel.assignmentsObj.collectAsState()
    val expandedMap = remember { mutableStateMapOf<Int, Boolean>() }
    val group_details = viewModel.group.collectAsState()
    val showQR = remember { mutableStateOf(false) }
    val user = viewModel.user.collectAsState()
    val members = viewModel.members.collectAsState()
    val showConfirmationDialog = remember { mutableStateOf(false) }
    val isGroupOwner = remember {
        mutableStateOf(user.value?.user_id == group_details.value?.admin)
    }


    if(group_details.value != null){
        if(showQR.value){
//        QrCodeSenderScreen(group_details.value.invite_code)
            MinimalDialog(inviteCode = group_details.value!!.invite_code, onDismissRequest = { showQR.value = false })
        }else{
            if(!showConfirmationDialog.value){

                Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
                    Text(text = group_details.value!!.group_name, fontWeight = FontWeight.Bold, fontSize = 24.sp, modifier = Modifier.padding(20.dp))
                    Column(modifier = Modifier
                        .padding(16.dp)
                        .border(1.dp, color = Color.LightGray, shape = RoundedCornerShape(10.dp))
                        .fillMaxWidth()) {
                        Row(modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 20.dp, bottom = 20.dp), horizontalArrangement = Arrangement.SpaceBetween) {
                            Row(modifier = Modifier.padding(start = 25.dp)) {
                                Icon(
                                    imageVector = Icons.Outlined.Group,
                                    modifier = Modifier.size(24.dp),
                                    contentDescription = "Copy Invite Link"
                                )
                                Text("1 Members")
                            }
                            Row(modifier = Modifier.padding(end = 25.dp)) {
                                Icon(
                                    imageVector = Icons.Outlined.QrCode,
                                    modifier = Modifier.size(24.dp),
                                    contentDescription = "Copy Invite Link"
                                )
                                Text("Invite Link")
                                Icon(
                                    imageVector = Icons.Outlined.ContentCopy,
                                    modifier = Modifier
                                        .size(24.dp)
                                        .clickable(
                                            onClick = {
                                                Log.d("Copy Invite Link", "Copy Invite Link Clicked.")
                                            }
                                        ),
                                    contentDescription = "Copy Invite Link",

                                    )
                            }
                        }
                        //Two Buttons for Show QR Code and Copy Invite Link
                        Row(modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp), horizontalArrangement = Arrangement.SpaceEvenly) {

                            Box(
                                modifier = Modifier
                                    .clickable {
                                        showQR.value = true
                                    }
                                    .weight(1f)
                                    .border(
                                        1.dp,
                                        color = Color.LightGray,
                                        shape = RoundedCornerShape(10.dp)
                                    )
                                    .padding(10.dp)

                            ){
                                Row(horizontalArrangement = Arrangement.SpaceEvenly) {
                                    Icon(
                                        imageVector = Icons.Outlined.QrCode,
                                        contentDescription = "",
                                        modifier = Modifier.size(24.dp)
                                    )
                                    Text("Show QR Code")
                                }

                            }
                            Spacer(modifier = Modifier.width(15.dp))
                            Box(
                                modifier = Modifier
                                    .clickable(
                                        onClick = {
                                            //here is the share link button
                                            //here is the share link button
                                            //you can implement the share functionality here
                                            //here is the share functionality here
                                            Log.d("Share Link", "Share Link Clicked.")
                                        }
                                    )
                                    .weight(1f)
                                    .border(
                                        1.dp,
                                        color = Color.LightGray,
                                        shape = RoundedCornerShape(10.dp)
                                    )
                                    .padding(10.dp)

                            ){
                                Row(horizontalArrangement = Arrangement.SpaceEvenly) {
                                    Icon(
                                        imageVector = Icons.Outlined.Share,
                                        contentDescription = "",
                                        modifier = Modifier.size(24.dp)
                                    )
                                    Text("Share Link")
                                }

                            }
                        }
                        //Information that anyone can invite anyone using the qr code or invite link
                        Box(
                            modifier = Modifier
                                .padding(bottom = 20.dp, start = 20.dp, end = 20.dp)
                                .border(1.dp, color = Color.LightGray, shape = RoundedCornerShape(10.dp))
                                .padding(10.dp)
                        ){

                            Text("Any group member can invite others using the invite code or by sharing the QR code above.")
                        }
                    }
                    Spacer(Modifier.height(20.dp))
                    Column(modifier = Modifier
                        .padding(16.dp)
                        .border(
                            1.dp,
                            color = Color.LightGray,
                            shape = RoundedCornerShape(10.dp)
                        )
                        .fillMaxWidth()) {
                        Text(
                            "Owner Actions",
                            fontWeight = FontWeight.Bold,
                            fontSize = 20.sp,
                            modifier = Modifier.padding(start = 15.dp, top = 20.dp)
                        )
                        Spacer(Modifier.height(30.dp))
                        Row(
                            modifier = Modifier
                                .height(80.dp)
                                .padding(16.dp)
                                .border(
                                    1.dp,
                                    color = Color.LightGray,
                                    shape = RoundedCornerShape(10.dp)
                                )
                                .fillMaxWidth()
                                .clickable {
                                    navController.navigate(Route.UploadAssignment.route)
                                },
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ){
                            Icon(Icons.Outlined.Add, contentDescription = "Add Members")
                            Spacer(Modifier.width(20.dp))
                            Text("Upload Assignment")
                        }
                        Row(modifier = Modifier
                            .height(80.dp)
                            .padding(16.dp)
                            .border(1.dp, color = Color.LightGray, shape = RoundedCornerShape(10.dp))
                            .fillMaxWidth()
                            .clickable {
                                navController.navigate(Route.SubjectManagement.route)
                            }, verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Center){
                            Icon(Icons.Outlined.Subject, contentDescription = "Add Members")
                            Spacer(Modifier.width(20.dp))
                            Text("Manage Subjects")
                        }
                        Spacer(modifier = Modifier.height(20.dp))
                        Text("As the group owner, you can upload assignments, manage subjects, and ban members.", modifier = Modifier
                            .padding(20.dp))
                    }
                    Spacer(Modifier.height(20.dp))

                    Column(modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp)
                        .border(1.dp, color = Color.LightGray, shape = RoundedCornerShape(10.dp))) {
                        Text("Members", fontWeight = FontWeight.Bold, fontSize = 24.sp, modifier = Modifier.padding(start = 15.dp, top = 20.dp))
                        Spacer(Modifier.height(20.dp))
                        Column(modifier = Modifier.height(300.dp).verticalScroll(rememberScrollState())) {
                            if(members.value.isEmpty()){
                                Text("Retrieving Members", modifier = Modifier.padding(20.dp))
                            }else{

                                for (i in 0..members.value.size -1){

                                    Row(modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(start = 20.dp, end = 10.dp, top = 10.dp, bottom = 10.dp), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            Card(
                                                modifier = Modifier
                                                    .size(40.dp),
                                                shape = CircleShape,
                                                colors = CardDefaults.cardColors(containerColor = Color.LightGray.copy(alpha = 0.3f))
                                            ) {
                                                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                                                    val name = ""
                                                    Text(
                                                        text = members.value[i].full_name.split(" ")[0][0].toString() + if(members.value[i].full_name.split(" ").size > 1) members.value[i].full_name.split(" ")[1][0].toString() else "",
                                                        fontSize = 16.sp,
                                                        fontWeight = FontWeight.Bold,
                                                        color = MaterialTheme.colorScheme.onSurface
                                                    )
                                                }
                                            }
                                            Spacer(modifier = Modifier.width(10.dp))
                                            Column {
                                                Text(members.value[i].full_name, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                                                Text(members.value[i].email, fontSize = 12.sp)
                                            }
                                            Spacer(modifier = Modifier.weight(1f))
                                            Text(modifier = Modifier
                                                .clip(RoundedCornerShape(10.dp)) // Clip to rounded shape
                                                .background(if(group_details.value!!.admin == members.value[i].user_id) Color(0xFF62C370) else Color(red = 236, blue = 242, green = 238)) // Apply background after clipping
                                                .border(
                                                    1.dp,
                                                    Color.LightGray,
                                                    shape = RoundedCornerShape(10.dp)
                                                ) // Border with same shape
                                                .padding(horizontal = 10.dp)
                                                .padding(horizontal = 10.dp), text = if(group_details.value!!.admin == members.value[i].user_id) "admin" else "member", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                                        }
                                    }
                                }
                            }


                        }
                    }
                    Box(
                        modifier = Modifier
                    )
                    Row(modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp)
                        .height(40.dp)
                        .clickable(
                            onClick = {
                            showConfirmationDialog.value = true
    //                            viewModel.deleteGroup()
                            }
                        )
                        .background(Color.Red)
                        .border(1.dp, Color.Red, shape = RoundedCornerShape(15.dp)), horizontalArrangement = Arrangement.Center, verticalAlignment = Alignment.CenterVertically) {
                        if(isGroupOwner.value){
                            Icon(
                                imageVector = Icons.Outlined.Delete,
                                contentDescription = "Delete Group",

                                )
                            Spacer(modifier = Modifier.width(10.dp))
                            Text("Delete Group", modifier = Modifier, color = Color.White)
                        }else{
                            Icon(
                                imageVector = Icons.Outlined.ExitToApp,
                                contentDescription = "Exit Group",

                                )
                            Spacer(modifier = Modifier.width(10.dp))
                            Text("Exit Group", modifier = Modifier, color = Color.White)
                        }
                    }
                    Box(
                        modifier = Modifier
                    )

                }
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
            Column {
                CardUnJoined(title = "Create a Study Group", description = "Start your own group and invite classmates", icon = Icons.Filled.PersonAddAlt, navController, joinOrCreate = true)
                CardUnJoined(title = "Join a Study Group", description = "Enter a group code or scan QR code to join", icon = Icons.Filled.Group, navController, joinOrCreate = false)
                Spacer(modifier = Modifier.height(30.dp))
                UnJoinedInfoCard(color = Color.LightGray, title = "How Study Groups Work", description = "Join a study group to share assignments, collaborate on projects, and track progress together. Each user can only be in one group at a time.", icon = Icons.Outlined.MenuBook)
                UnJoinedInfoCard(color = Color.Green, title = "Easy Group Joining", description = "Ask your classmates for a group invite code or scan their QR code to instantly join their study group.", icon = Icons.Outlined.QrCode)
            }
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