package com.example.kaamkarlo.uI.components.Assignment

import android.util.Log
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.kaamkarlo.data.Uploads
import com.example.kaamkarlo.data.user
import com.example.kaamkarlo.uI.navigation.Route
import com.example.kaamkarlo.viewmodels.DetailsViewModel
import kotlin.let

@Composable
fun UserList(navController: NavController, uploads: List<Uploads>?, viewModel: DetailsViewModel) {
    Log.d("UserList", "Rendering UserList with uploads: $uploads")
    val uploadList = remember { mutableStateOf<List<Triple<user, String, Int>>>(emptyList()) }
    LaunchedEffect("") {
        val tempList = mutableListOf<Triple<user,String, Int>>()
        uploads?.forEach {
            viewModel.loadUser(it.user_id)?.let { user ->
                tempList.add(Triple(user, it.created_at.toString(), third=it.upload_id!!))
                Log.d("UserList", "Fetched user: $user for upload: $it")
            }
        }
        uploadList.value = tempList
    }
    Column(modifier = Modifier
        .fillMaxWidth()
        .padding(40.dp)
        .border(1.dp, color = Color.LightGray, shape = RoundedCornerShape(10.dp)))
    {




        // Team Members Section
        Text(
            text = "Team Members",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.padding(20.dp)
        )
        uploadList.value.forEach {
            Log.d("UserList", "Rendering TeamMemberCard for user: ${it.first.full_name}")

            TeamMemberCard(
                initials = it.first.full_name.split(" ")[0].take(1) + it.first.full_name.split(" ").getOrNull(1)?.take(1).orEmpty(),
                name = it.first.full_name,
                navController = navController,
                date = it.second,
                statusColor = Color.Green,
                uploadId = it.third,
                clickAction = {
                    viewModel.subMan.id = it.third
                    viewModel.subMan.whichOne = true
                    navController.navigate(Route.ViewAssignment.route)
                }
            )
        }
        // Team Member Cards

    }
}

@Composable
fun RowScope.SubmissionStatusItem(label: String, count: String, color: Color) {
    Card(
        modifier = Modifier
            .weight(1f)
            .padding(horizontal = 4.dp),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = Color.LightGray.copy(alpha = 0.2f))
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = count,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = color
            )
            Text(
                text = label,
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}

@Composable
fun TeamMemberCard(
    initials: String,
    name: String,
    date: String?,
    statusColor: Color,
    navController: NavController,
    uploadId: Int,
    clickAction: () -> Unit = { }
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp)
            .border(1.dp, color = Color.LightGray, shape = RoundedCornerShape(8.dp)),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        onClick = clickAction
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Initials Circle
            Card(
                modifier = Modifier
                    .size(40.dp),
                shape = CircleShape,
                colors = CardDefaults.cardColors(containerColor = Color.LightGray.copy(alpha = 0.3f))
            ) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(
                        text = initials,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }
            Spacer(modifier = Modifier.width(12.dp))
            // Name and Details
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = name,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )

                if (date != null) {
                    Text(
                        text = date,
                        fontSize = 12.sp,
                        color = Color.Gray
                    )
                }
            }
            // Status

        }
    }
}