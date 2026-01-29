package com.lyncan.opus.uI.components.Assignment

import android.R
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.lyncan.opus.data.Assignment

import com.lyncan.opus.uI.navigation.Route

@Composable
fun AssignmentCard(navController: NavController, subject: String, assignments: Assignment, status: String) {
    Column(
        modifier = Modifier.padding(top = 30.dp).fillMaxWidth(.85f).height(145.dp).border(
            shape = RoundedCornerShape(10.dp), width = 1.dp, color = Color.LightGray
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Left section with title and details
            Column(
                modifier = Modifier.weight(1f)
            ) {
                // Title
                Text(
                    text = assignments.assignment_name,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.height(4.dp))
                // Subtitle
                Text(
                    text = subject,
                    fontSize = 14.sp,
                    color = Color.Gray
                )
                Spacer(modifier = Modifier.height(8.dp))
                // Due Date with Icon
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_menu_my_calendar), // Replace with your calendar icon
                        contentDescription = "Due Date",
                        modifier = Modifier.size(16.dp),
                        tint = Color.Gray
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = assignments.due_date?: "N/A",
                        fontSize = 14.sp,
                        color = Color.Gray
                    )
                }
            }

            // Right section with status and action
            Column(
                horizontalAlignment = Alignment.End
            ) {
                // Status
                Button(
                    onClick = { /* Handle status click */ },
                    modifier = Modifier
                        .padding(bottom = 8.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.LightGray.copy(alpha = 0.3f),
                        contentColor = MaterialTheme.colorScheme.onSurface
                    ),
                    shape = RoundedCornerShape(16.dp),
                    contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp)
                ) {
                    Text(status)
                }
                // View Details Button
                TextButton(
                    onClick = { navController.navigate(Route.AssignmentDetail.createRoute(
                        assignments.assignment_id!!,
                        subName = subject
                    )) },
                    colors = ButtonDefaults.textButtonColors(
                        contentColor = MaterialTheme.colorScheme.primary
                    )
                ) {
                    Text("View Details")
                }
            }
        }
    }
}