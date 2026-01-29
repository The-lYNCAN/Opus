package com.lyncan.opus.ui.screens

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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Circle
import androidx.compose.material.icons.outlined.MenuBook
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.lyncan.opus.viewmodels.AddSubjectViewModel

@Composable
fun AddSubject(navController: NavController){
    val viewModel: AddSubjectViewModel = hiltViewModel()
//    val createSubject = viewModel::createSubject
    val loading = viewModel.loading.collectAsState()

    if(loading.value){
        Column(modifier= Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {

            CircularProgressIndicator(
                modifier = Modifier.size(48.dp)
            )
            Text("Creating Subject...", modifier = Modifier.padding(top = 8.dp))
        }
    }else{
        Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
            Row(modifier = Modifier.padding(20.dp)) {
                Icon(Icons.Outlined.MenuBook, contentDescription = "Subject Management")
                Text("Create Subjects", fontWeight = FontWeight.Bold, fontSize = 24.sp)

            }
            CreationInfo()
//            SubjectNameInput(createSubject as (String) -> Unit, navController = navController)
            CustomBulletList()

        }
    }
}

@Composable
fun CreationInfo(){
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
            Text("Creating a New Subject", fontWeight = FontWeight.Bold, fontSize = 18.sp)
            Text("Add a new subject to organize your assignments. Once created, you can upload assignments under this subject and share them with your group.", fontSize = 15.sp)
        }
    }
}

@Composable
fun SubjectNameInput(createSub: (String) -> Unit, navController: NavController) {
    val subName = remember { mutableStateOf("") }
    Box(modifier = Modifier
        .padding(20.dp)
        .border(1.dp, color = Color.LightGray, shape = RoundedCornerShape(15.dp))){
        Column(modifier = Modifier.padding(20.dp)) {

            Text("Subject Name", fontWeight = FontWeight.Bold, fontSize = 15.sp)
            TextField(
                value = subName.value,
                onValueChange = {subName.value = it},
                placeholder = {
                    Text(
                        text = "Enter text here",
                        style = TextStyle(
                            fontSize = 14.sp, // Adjust font size to fit smaller TextField
                            color = Color.Gray // Placeholder text color
                        )
                    )                },
                textStyle = TextStyle(
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
            Text("Enter a clear and descriptive name for your subject (0/50) characters", fontSize = 12.sp, color = Color.Gray)
            Row (modifier = Modifier.fillMaxWidth()){
                Box(
                    modifier = Modifier.height(50.dp).weight(1f).clip(RoundedCornerShape(10.dp)).border(1.dp, color = Color.LightGray, shape = RoundedCornerShape(10.dp)), contentAlignment = Alignment.Center
                ){
                    Text("Cancel", color = Color.Black)
                }
                Spacer(modifier = Modifier.width(15.dp))
                Row (modifier = Modifier.weight(1f)
                    .height(50.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(Color.Black)
                    .clickable{
                        createSub(subName.value)
                        navController.popBackStack()
                },
                    horizontalArrangement = Arrangement.Center, verticalAlignment = Alignment.CenterVertically){
                    Icon(Icons.Filled.Add, contentDescription = "Add subject", tint = Color.White)
                    Text("Create Subject", color = Color.White)
                }
            }
        }
    }
}

@Composable
fun CustomBulletList() {
    val items = listOf("Use clear, descriptive names for easy identification", "Subjects are shared with all group members", "Only group owners can create and delete subjects", "Deleting a subject removes all it's assignments")
    Box(modifier = Modifier
        .padding(20.dp)
        .border(1.dp, color = Color.LightGray, shape = RoundedCornerShape(15.dp))){
        Column(modifier = Modifier.fillMaxWidth()
            .padding(20.dp)) {
            Text("Subject Guidelines", fontWeight = FontWeight.Bold, fontSize = 18.sp)
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