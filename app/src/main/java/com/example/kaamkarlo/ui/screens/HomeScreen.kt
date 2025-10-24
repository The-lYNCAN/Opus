package com.example.kaamkarlo.uI.screens

import android.util.Log
import androidx.compose.foundation.border
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.PersonAddAlt
import androidx.compose.material.icons.outlined.MenuBook
import androidx.compose.material.icons.outlined.QrCode
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.runtime.derivedStateOf
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.kaamkarlo.viewmodels.HomeViewModel
import androidx.compose.runtime.getValue
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.core.graphics.component1
import androidx.core.graphics.component2
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.kaamkarlo.R
import com.example.kaamkarlo.uI.components.Home.Card
import com.example.kaamkarlo.uI.components.Home.CardUnJoined
import com.example.kaamkarlo.uI.components.Home.SubCard
import com.example.kaamkarlo.uI.components.Home.UnJoinedInfoCard
import com.example.kaamkarlo.viewmodels.MainViewModel


@Composable
fun HomeScreen(
    navController: NavController,
    mainViewModel: MainViewModel
) {

    val ViewModel: HomeViewModel = hiltViewModel()
    val user = ViewModel.user.collectAsState()
    var showOrNot = user.value.group_id

    val detailsState = ViewModel.details.collectAsState()
    val globalT = remember {mutableIntStateOf(0)}
    val globalC = remember {mutableIntStateOf(0)}
    Log.d("Home Screen checking for Group Screen", "Group ID: ${user.value?: "No User"}")



    LaunchedEffect(detailsState.value) {
        val details = detailsState.value
        var tempTotal = 0
        var tempCompleted = 0
        details.first.forEach { (subject, assignments) ->
            tempTotal += assignments.size
            tempCompleted += assignments.count { assignment ->
                details.second.any { (uploadedAssignment, uploads) ->
                    uploadedAssignment.assignment_id == assignment.assignment_id && uploads.any { it.user_id == user.value?.user_id }
                }
            }
        }
    }


    if(showOrNot!= null){
        Column(modifier = Modifier.fillMaxSize()) {
            Spacer(Modifier.height(20.dp))
            Text("Welcome Back!", fontSize = 30.sp, modifier = Modifier
                .padding(top = 16.dp)
                .fillMaxWidth()
                , textAlign = TextAlign.Center)
            Text("Keep up the great work!", modifier = Modifier.fillMaxWidth()
                , textAlign = TextAlign.Center)
            Spacer(Modifier.height(10.dp))
            Row (modifier = Modifier
                .padding(18.dp)
                .height(120.dp)
                .fillMaxWidth()
                , horizontalArrangement = Arrangement.spacedBy(20.dp))
            {
                Card(img = R.mipmap.snap, num = globalT.intValue, diff = true)
                Card(img = R.mipmap.wait, num = globalT.intValue - globalC.intValue, diff = false)
            }
            Text("Your Subjects", modifier = Modifier
                .padding(start = 18.dp, bottom = 18.dp),
                fontWeight = FontWeight.Bold, fontSize = 20.sp)
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                Log.d("Subject Progress", "${detailsState.value.first.keys.size}")
                items(detailsState.value.first.keys.size){ index ->
                    val entry = detailsState.value.first.keys.elementAt(index)
                    val subjectId = entry.subject_id

                    val assignmentsForSubject = detailsState.value.first[entry] ?: emptyList()
                    val total = assignmentsForSubject.size
                    val completed = assignmentsForSubject.count { assignment ->
                        detailsState.value.second.any { (uploadedAssignment, uploads) ->
                            uploadedAssignment.assignment_id == assignment.assignment_id && uploads.any { it.user_id == user.value?.user_id }
                        }
                    }

                    SubCard(subjectId, if (total > 0) completed.toFloat() / total.toFloat() else 1f,
                        detailsState.value.first.keys.elementAt(index))

                }
            }
            Text("Due Soon", modifier = Modifier
                .padding(start = 18.dp, bottom = 18.dp, top = 20.dp),
                fontWeight = FontWeight.Bold, fontSize = 20.sp)
            Column {
                //Due Soon Cards
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

//first total, second completed
}