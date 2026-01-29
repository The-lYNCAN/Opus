package com.lyncan.opus.ui.components.attendance

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@Composable
fun SubjectList(navController: NavController) {
    LazyColumn(modifier = Modifier.padding(top = 10.dp)) {
        item{

            Text("Attendance", style = MaterialTheme.typography.displayMedium)
            Text("Track your class participation", style = MaterialTheme.typography.titleMedium, color = Color.Black.copy(alpha = .8f))
            Spacer(modifier = Modifier.height(16.dp))
        }
        items(10){
            SubjectCard(navController)
            Spacer(modifier = Modifier.height(12.dp))
        }

    }
}