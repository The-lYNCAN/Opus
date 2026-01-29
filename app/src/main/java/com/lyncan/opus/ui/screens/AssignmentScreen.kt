package com.lyncan.opus.uI.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MultiChoiceSegmentedButtonRow
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.text.isDigitsOnly
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.lyncan.opus.data.Assignment
import com.lyncan.opus.uI.components.Assignment.AssignmentCard
import com.lyncan.opus.viewmodels.AssignmentViewModel
import com.lyncan.opus.viewmodels.MainViewModel

@Composable
fun AssignmentScreen(navController: NavController, sharedViewModel: MainViewModel) {

    val viewModel = hiltViewModel<AssignmentViewModel>()
    // 1. Get the raw assignment data from the ViewModel
    val assignmentState = viewModel.assignments.collectAsState()

    // 2. Keep the state for the selected filter option
    val selectedOption = remember { mutableStateOf("All") }

    // 3. Derive the filtered list reactively.
    // This code runs automatically on any recomposition caused by a state change.
    val filteredAssignments = remember(assignmentState.value, selectedOption.value) {
        val allAssignments = mutableListOf<Triple<String, Assignment, String>>()
        // First, flatten the complex data structure into a simple list of pairs (Subject, Assignment)
        assignmentState.value.first.forEach { (subject, assignmentsInSubject) ->
            assignmentsInSubject.forEach { assign ->
                val status = viewModel.getAssignmentStatus(assign.assignment_id!!)
                allAssignments.add(Triple(subject.Subject_name, assign, status))
            }
        }

        // Now, filter the flattened list based on the selected option
        val filteredList = when (selectedOption.value) {
            "All" -> allAssignments
            "Pending" -> allAssignments.filter { it.third == "Pending" }
            "Done" -> allAssignments.filter { it.third == "Done" }
            "Overdue" -> allAssignments.filter { it.third == "Overdue" }
            else -> allAssignments // Default to showing all
        }
        // Sort the filtered list by due date, handling non-date strings
        filteredList.sortedBy {
            it.second.due_date.takeIf { date -> date!!.isDigitsOnly() }?.toLong() ?: Long.MAX_VALUE
        }
    }


    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.verticalScroll(rememberScrollState())
    ) {
        Text("Assignments", fontSize = 30.sp, modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center)
        Text("Manage your coursework and deadlines", modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center)
        // Pass the state holder to the SegmentedControl
        SegmentedControl(selectedOption)

        // 4. Iterate over the derived 'filteredAssignments' list to display the UI
        filteredAssignments.forEach { (subject, assignment, status) ->
            AssignmentCard(
                navController = navController,
                subject = subject,
                assignments = assignment,
                status = status
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SegmentedControl(selectedOption: MutableState<String>) {

    val options = listOf("All", "Pending", "Done", "Overdue")
    MultiChoiceSegmentedButtonRow(modifier = Modifier
        .fillMaxWidth(0.8f)
        .padding(top = 18.dp)) {
        options.forEach { option->
            SegmentedButton(
                checked = (option == selectedOption.value),
                onCheckedChange = { selectedOption.value = option },
                shape = SegmentedButtonDefaults.itemShape(
                    index = options.indexOf(option),
                    count = options.size
                ),
            ) {
                Text(option)
            }
        }
    }
}