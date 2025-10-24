package com.example.kaamkarlo.ui.screens

import android.R.attr.end
import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.DocumentScanner
import androidx.compose.material.icons.outlined.MenuBook
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ModifierInfo
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.kaamkarlo.data.Subject
import com.example.kaamkarlo.uI.navigation.Route
import com.example.kaamkarlo.viewmodels.MainViewModel
import com.example.kaamkarlo.viewmodels.SubjectManagementViewModel

@Composable
fun SubjectManagement(navController: NavController, mainViewModel: MainViewModel) {
    val viewModel: SubjectManagementViewModel = hiltViewModel()
    val totalAss= remember { mutableStateOf(0) }
    val loading = viewModel.loading.collectAsState()
    val subjects = viewModel.subjects.collectAsState()
    viewModel.subMan.subjectList.forEach { subject, assignment ->
        totalAss.value += assignment.size
    }
    val lifecycleOwner = LocalLifecycleOwner.current
    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                Log.d("SubjectManagement", "Screen resumed, refreshing subjects.")
                viewModel.resetSubState() // Assuming this function refreshes your data

                Log.d("SubjectManagement", "Subjects after refresh: ${subjects.value}")
            }
        }

        // Add the observer to the lifecycle
        lifecycleOwner.lifecycle.addObserver(observer)

        // The onDispose block is called when the composable leaves the screen
        onDispose {
            Log.d("SubjectManagement", "Disposing observer.")
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }
    LaunchedEffect(subjects.value) {
        Log.d("SubjectManagement", "Subjects updated: ${subjects.value}")
    }

    if(!loading.value) {
        Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
            Row(modifier = Modifier.padding(20.dp)) {
                Icon(Icons.Outlined.MenuBook, contentDescription = "Subject Management")
                Text(
                    "Manage Subjects",
                    modifier = Modifier.width(110.dp),
                    fontWeight = FontWeight.Bold,
                    fontSize = 24.sp
                )
                Spacer(modifier = Modifier.weight(1f))
                Box(
                    modifier = Modifier
                        .height(40.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color.Black)
                        .clickable {
                            navController.navigate(Route.AddSubject.route)
                        }, contentAlignment = Alignment.Center
                ) {
                    Row(
                        modifier = Modifier.padding(8.dp),
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            Icons.Outlined.Add,
                            contentDescription = "Add subject",
                            tint = Color.White
                        )
                        Spacer(modifier = Modifier.width(20.dp))
                        Text("Add Subject", color = Color.White)
                    }
                }
            }
            ManagementInfo()
            Column {

                subjects.value.forEach { (subject, assignments) ->
                    SubjectCard(
                        subject.Subject_name,
                        assignments.size,
                        subject = subject,
                        deltFunc = {

                            viewModel.deltFunc(subject.subject_id!!)
                        })
                }

            }
            Stats(viewModel.subMan.subjectList.size, totalAss.value)
        }
    }else{
        Column(modifier= Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {

            CircularProgressIndicator(
                modifier = Modifier.size(48.dp)
            )
            Text("Deleting Subject", modifier = Modifier.padding(top = 8.dp))
        }
    }
}

@Composable
fun ManagementInfo(){
    Row(modifier = Modifier
        .padding(20.dp)
        .border(1.dp, color = Color.LightGray, shape = RoundedCornerShape(15.dp))) {
        Box(modifier = Modifier
            .padding(20.dp)
            .clip(CircleShape)
            .background(Color.LightGray)
            .size(40.dp), contentAlignment = Alignment.Center)
        { Icon(Icons.Outlined.MenuBook, contentDescription = "Subject Management", Modifier.size(32.dp)) }
        Column(modifier = Modifier.padding(top = 20.dp, bottom = 20.dp, start = 0.dp, end = 10.dp)) {
            Text("Subject Management", fontWeight = FontWeight.Bold, fontSize = 18.sp)
            Text("As the group owner, you can create and delete subjects. Deleting a subject will remove all associated assignments.", fontSize = 15.sp)
        }
    }
}

@Composable
fun SubjectCard(subjectName: String, noOfAssignments: Int, subject: Subject, deltFunc: ()->Unit = {}){
    Row(modifier = Modifier
        .padding(horizontal = 20.dp, vertical = 5.dp)
        .border(1.dp, color = Color.LightGray, shape = RoundedCornerShape(15.dp))) {
        Box(modifier = Modifier
            .padding(20.dp)
            .clip(RoundedCornerShape(10.dp))
            .background(Color.LightGray)
            .size(40.dp), contentAlignment = Alignment.Center)
        { Icon(Icons.Outlined.MenuBook, contentDescription = "Subject Management", Modifier.size(32.dp)) }
        Column(modifier = Modifier.padding(top = 20.dp, bottom = 20.dp, start = 0.dp, end = 10.dp)) {
            Row {
                Text(subjectName, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                Spacer(modifier = Modifier.weight(1f))
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(10.dp))
                        .background(Color.LightGray)
                        .size(24.dp), contentAlignment = Alignment.Center
                ){
                    Text(noOfAssignments.toString())
                }
                Icon(
                    Icons.Outlined.Delete,
                    tint = Color.Red,
                    contentDescription = "Delete Subject",
                    modifier = Modifier
                        .padding(start = 10.dp)
                        .size(24.dp)
                        .clickable {

                            deltFunc()
                        },

                    )
            }
            Row (modifier = Modifier.padding(vertical = 10.dp)){

                Icon(Icons.Outlined.DocumentScanner, contentDescription = "")
                Text(noOfAssignments.toString())
                Text("Assignments")

            }

        }
    }
}

@Composable
fun Stats(totalSub: Int, totalAss: Int){
    Column (modifier = Modifier
        .padding(horizontal = 20.dp, vertical = 15.dp)
        .border(1.dp, color = Color.LightGray, shape = RoundedCornerShape(15.dp))
        .fillMaxWidth()){
        Text("Subject Statistics", modifier = Modifier.padding(start = 20.dp, top = 20.dp), fontWeight = FontWeight.Bold, fontSize = 20.sp)
        Row(modifier = Modifier.padding(top = 30.dp, bottom = 20.dp)) {
            Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
                Text(totalSub.toString(), fontSize = 30.sp, fontWeight = FontWeight.Bold)
                Text("Total Subjects")
            }
            Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
                Text(totalAss.toString(), fontSize = 30.sp, fontWeight = FontWeight.Bold)
                Text("Total Assignments")
            }

        }
    }
}