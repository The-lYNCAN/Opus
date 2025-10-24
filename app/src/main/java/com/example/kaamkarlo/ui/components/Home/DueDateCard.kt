package com.example.kaamkarlo.uI.components.Home

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil3.compose.AsyncImage
//import coil3.compose.AsyncImage
import com.example.kaamkarlo.uI.navigation.Route
import com.example.kaamkarlo.data.Assignment

@Composable
fun DueDateAssCard(navController: NavController, assignment: Assignment) {
    Box(
        modifier = Modifier.height(280.dp).width(250.dp)
    ){
        AsyncImage(model = assignment.assignment_pic_url, contentDescription = "Assignment Image", modifier = Modifier
            .border(0.dp, Color.White, RoundedCornerShape(8.dp))
            .fillMaxSize().clip(RoundedCornerShape(16.dp))
            , contentScale = ContentScale.Crop)
        Column(
            modifier = Modifier
                .border(0.dp, Color.White, RoundedCornerShape(16.dp))
                .height(280.dp)
                .width(250.dp)
                .padding(start = 10.dp)
                .clickable {
                    navController.navigate(route = Route.Group.route)
                },
            verticalArrangement = Arrangement.SpaceBetween,
//            verticalAlignment = Alignment.CenterVertically
        ){
            Text("Engineering Mathematics", modifier = Modifier.padding(10.dp), color = Color.White)
            Text("${assignment.due_date}", modifier = Modifier.padding(10.dp), color = Color.White)

        }
    }

}