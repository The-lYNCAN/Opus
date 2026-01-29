package com.lyncan.opus.uI.components.Assignment

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CameraAlt
import androidx.compose.material.icons.outlined.MenuBook
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.lyncan.opus.uI.navigation.Route
import com.lyncan.opus.viewmodels.DetailsViewModel
import kotlinx.coroutines.flow.MutableStateFlow

@Composable
fun Submission(navController: NavController, canSubmit: MutableStateFlow<Boolean>, assignmentId: Int, viewModel: DetailsViewModel){
    val enabled = canSubmit.collectAsState()
    val enabledColor = if (enabled.value) Color.Black else Color.Gray
    Column {
        Row(modifier = Modifier.padding(horizontal = 30.dp)) {

            Box(modifier = Modifier.clip(RoundedCornerShape(10.dp)).border(
                1.dp,
                Color.LightGray,
                shape = RoundedCornerShape(10.dp)
            ).clickable(
                enabled = enabled.value
            ){
                navController.navigate(Route.Submit.createRoute(assignmentId))

            }.weight(1f).background(enabledColor).padding(horizontal = 16.dp, vertical = 8.dp)) {
                Row(modifier = Modifier){
                    Icon(Icons.Outlined.CameraAlt, contentDescription = "Camera Icon", tint = Color.White)
                    Spacer(modifier = Modifier.width(2.dp))
                    Text("Scan & Submit", color = Color.White, modifier = Modifier)
                }
            }
            Spacer(modifier = Modifier.width(6.dp))
            Box(modifier = Modifier.clickable{
                    viewModel.seeProblemSet()
                    navController.navigate(Route.ViewAssignment.route)
                }.weight(1f).border(
                1.dp,
                Color.LightGray,
                shape = RoundedCornerShape(10.dp)).padding(horizontal = 16.dp, vertical = 8.dp)
            ){
                Row(){
                    Icon(Icons.Outlined.MenuBook, contentDescription = "Camera Icon")
                    Spacer(modifier = Modifier.weight(1f))
                    Text("Problem Set")
                    Spacer(modifier = Modifier.weight(1f))
                }
            }
        }
    }
}