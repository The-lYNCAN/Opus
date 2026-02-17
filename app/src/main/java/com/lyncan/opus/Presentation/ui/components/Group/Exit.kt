package com.lyncan.opus.Presentation.ui.components.Group

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.ExitToApp
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun Exit(showConfirmationDialog: MutableState<Boolean>, isGroupOwner: MutableState<Boolean>) {
    Box(
        modifier = Modifier
    )
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
            .height(40.dp)
            .clickable(
                onClick = {
                    showConfirmationDialog.value = true
                    //                            viewModel.deleteGroup()
                }
            )
            .background(Color.Red)
            .border(1.dp, Color.Red, shape = RoundedCornerShape(15.dp)),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically) {
        if (isGroupOwner.value) {
            Icon(
                imageVector = Icons.Outlined.Delete,
                contentDescription = "Delete Group",

                )
            Spacer(modifier = Modifier.width(10.dp))
            Text("Delete Group", modifier = Modifier, color = Color.White)
        } else {
            Icon(
                imageVector = Icons.Outlined.ExitToApp,
                contentDescription = "Exit Group",

                )
            Spacer(modifier = Modifier.width(10.dp))
            Text("Exit Group", modifier = Modifier, color = Color.White)
        }
    }
    Box(
        modifier = Modifier
    )
}