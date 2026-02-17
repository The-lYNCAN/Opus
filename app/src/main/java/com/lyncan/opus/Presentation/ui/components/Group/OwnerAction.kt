package com.lyncan.opus.Presentation.ui.components.Group

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.ChevronRight
import androidx.compose.material.icons.outlined.Subject
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.lyncan.opus.Presentation.ui.navigation.Route

@Composable
fun OwnerAction(navController: NavController, totalSubjects: Int, timeTableSetted: Boolean){
    Column(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Text(
            "Management",
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp,
            modifier = Modifier.padding(start = 15.dp, top = 20.dp)
        )
        Spacer(Modifier.height(30.dp))
        OwnerActionCard(navController, Route.TimeTable.route, Icons.Outlined.Add, color = Color(0xFF97DFFC), "Manage Time Table", if (timeTableSetted) "Time Table Set" else "No Time Table Set")//
        OwnerActionCard(navController, Route.SubjectManagement.route, Icons.Outlined.Subject, color = Color(0xFFA6EBC9), title = "Manage Subjects", "$totalSubjects Subjects added")
        Spacer(modifier = Modifier.height(20.dp))

    }
}

@Composable
fun OwnerActionCard(navController: NavController, route: String, icon: ImageVector, color: Color, title: String, description: String = " "){
    Row(
        modifier = Modifier
            .padding(16.dp)
            .border(
                1.dp,
                color = Color.LightGray,
                shape = RoundedCornerShape(10.dp)
            )
            .fillMaxWidth()
            .clickable {
                navController.navigate(route)
            }
            .padding(20.dp)
        ,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Box(modifier = Modifier.background(color, shape = RoundedCornerShape(10.dp)).padding(8.dp).wrapContentSize()) {
            Icon(icon, contentDescription = "Add Members", Modifier.size(34.dp))
        }
        Spacer(Modifier.width(20.dp))
        Column {
            Text(title, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.W500)
            Text(description, color = Color.Gray)
        }
        Spacer(modifier = Modifier.weight(1f))
        Icon(Icons.Outlined.ChevronRight, contentDescription = "Add Members", Modifier.size(34.dp).padding(end = 10.dp))
    }
}