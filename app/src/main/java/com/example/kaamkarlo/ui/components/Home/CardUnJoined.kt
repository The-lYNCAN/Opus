package com.example.kaamkarlo.uI.components.Home

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PersonAddAlt
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.kaamkarlo.uI.navigation.Route
import kotlinx.coroutines.flow.MutableStateFlow

@Composable
fun CardUnJoined(title: String, description: String, icon: ImageVector, navigator: NavController, joinOrCreate: Boolean) {
    Row(modifier = Modifier.fillMaxWidth().height(130.dp).padding(vertical = 10.dp, horizontal = 20.dp).border(1.dp, color = Color.LightGray, shape = androidx.compose.foundation.shape.RoundedCornerShape(10.dp)).clickable{
        navigator.navigate(Route.JoinOrCreate.createRoute(joinOrCreate))
    }) {
        Box(
            modifier = Modifier.height(100.dp),
            contentAlignment = androidx.compose.ui.Alignment.Center
        ){
            Icon(icon,
                "Join Group",
                modifier = Modifier.size(60.dp).padding(horizontal = 15.dp))
        }
        Column(modifier = Modifier.fillMaxHeight(),
            verticalArrangement = Arrangement.Center,) {
            Text(title, fontWeight = FontWeight.Bold)
            Text(description)
        }
    }
}