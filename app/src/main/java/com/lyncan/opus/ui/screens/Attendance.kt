package com.lyncan.opus.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.lyncan.opus.ui.components.attendance.Hero
import com.lyncan.opus.ui.components.attendance.SubjectList

@Composable
fun AttendanceScreen(navController: NavController) {
    AttendanceUI(navController)
}

@Composable
fun AttendanceUI(navController: NavController) {
    Column(modifier = Modifier.padding(horizontal = 20.dp).safeDrawingPadding().background(Color.White)) {
        Hero()
        SubjectList(navController)
    }
}

@Preview
@Composable
fun AttendancePreview() {
    val dumNav = NavController(LocalContext.current)
    AttendanceUI(dumNav)
}