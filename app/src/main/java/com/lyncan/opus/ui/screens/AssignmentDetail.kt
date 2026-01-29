package com.lyncan.opus.uI.screens

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.lyncan.opus.uI.components.Assignment.DetailsCard
import com.lyncan.opus.uI.components.Assignment.Submission
import com.lyncan.opus.uI.components.Assignment.UserList
import com.lyncan.opus.viewmodels.DetailsViewModel
import com.lyncan.opus.viewmodels.MainViewModel


@Composable
fun AssignmentDetail(navController: NavController, sharedViewModel: MainViewModel, assignmentId: Int?, subName: String?) {
    val viewModel = hiltViewModel<DetailsViewModel>()
    viewModel.loadAssignment(assignmentId!!)
    val assignment = viewModel.assignment.collectAsState()
    val uploads = viewModel.uploads.collectAsState()
    Log.d("AssignmentDetail", "Navigated to AssignmentDetail with assignmentId: $assignmentId")
    Column(modifier = Modifier.verticalScroll(rememberScrollState())
        , horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center, ) {
        Text("Assignment Detail", modifier = Modifier, fontWeight = FontWeight.Bold, fontSize = 26.sp)
        DetailsCard(assignment.value, subName)
        Submission(navController, viewModel.canSubmit, assignmentId, viewModel)
        UserList(navController, uploads.value, viewModel)
    }
}

