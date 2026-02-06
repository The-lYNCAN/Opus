package com.lyncan.opus.viewmodels

import androidx.lifecycle.ViewModel
import com.lyncan.opus.Repositories.SubjectManagement
import com.lyncan.opus.Repositories.SupabaseRepository
import com.lyncan.opus.Repositories.UserState
import com.lyncan.opus.data.Assignment
import com.lyncan.opus.data.Uploads
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import java.time.Instant
import java.time.format.DateTimeFormatter
import javax.inject.Inject


@HiltViewModel
class AssignmentViewModel @Inject constructor(
    private val repo: SupabaseRepository,
    private val subjectManagement: SubjectManagement,
    private val SubMan: SubjectManagement,
    private val userState: UserState
): ViewModel() {
    private val _assignments = MutableStateFlow(subjectManagement.getAllData())
    val assignments = _assignments
    private val _assignmentList = MutableStateFlow<List<Uploads>>(emptyList())
    val assignmentList = _assignmentList
    init {

    }
    fun doneOrNot(assignmentId: Int): Boolean {

        SubMan.getAllData().first.values.forEach {
            it.forEach {
                if(it.assignment_id == assignmentId){
//                    if(uploads.value.)
                        SubMan.getAllData().second.get(it)!!.forEach {
                        if(it.user_id == userState.getUser().user_id){
                            return true
                        }
                    }
                }
            }
        }
        return false
    }

    fun getAssignmentStatus(assignmentId: Int): String {
        SubMan.getAllData().first.values.forEach {
            it.forEach { assignment ->
                if(assignment.assignment_id == assignmentId){
                    // Check if done by the current user
                    val uploads = SubMan.getAllData().second[assignment] ?: emptyList()
                    val isDone = uploads.any { upload -> upload.user_id == userState.getUser().user_id }
                    if (isDone) {
                            return "Done"
                    }

                    // If not done, check if it's overdue
                    if (assignment.isOverdue()) {
                        return "Overdue"
                    }
                }
            }
        }
        return "Pending"
    }
}

private fun Assignment.isOverdue(): Boolean {
    val now = Instant.now()
//    val deadline = java.time.Instant.parse(this.due_date)
    val deadline = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSXXX").parse(this.due_date+"T10:30:00.000+05:00") //2025-10-28


    return now.isAfter(Instant.from(deadline))

}

