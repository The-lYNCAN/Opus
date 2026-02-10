package com.lyncan.opus.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.lyncan.opus.ui.components.attendance.Hero
import com.lyncan.opus.ui.components.attendance.SubjectList
import com.lyncan.opus.viewmodels.AttendanceViewModel

@Composable
fun AttendanceScreen(navController: NavController) {
    val viewModel = hiltViewModel<AttendanceViewModel>()
    val marked = viewModel.mark.collectAsState()
    if(marked.value){
        AttendanceUI(navController, viewModel)
    }else{
        MarkAttendanceScreen(viewModel)
    }
}

@Composable
fun AttendanceUI(navController: NavController, viewModel: AttendanceViewModel) {
    Column(modifier = Modifier.padding(horizontal = 20.dp).safeDrawingPadding().background(Color.White)) {
        Hero()
        SubjectList(navController, viewModel)
    }
}

//@Preview
//@Composable
//fun AttendancePreview() {
//    val viewModel = hiltViewModel<AttendanceViewModel>()
//    val dumNav = NavController(LocalContext.current)
//    AttendanceUI(dumNav, viewModel)
//}