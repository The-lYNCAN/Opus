package com.example.kaamkarlo.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kaamkarlo.MyApplication
import com.example.kaamkarlo.SubjectManagement
import com.example.kaamkarlo.SupabaseRepository
import com.example.kaamkarlo.UserState
import com.example.kaamkarlo.data.Assignment
import com.example.kaamkarlo.data.Subject
import com.example.kaamkarlo.data.Uploads
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.jan.supabase.postgrest.postgrest
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(val repo: SupabaseRepository,
                                        val userState: UserState,
                                        val subjectManagement: SubjectManagement): ViewModel()
{


    private val _progressDets = MutableStateFlow<Pair<Int, Int>>(Pair(0,0))
    val progressDets = _progressDets
    private val _user = MutableStateFlow(userState.getUser())
    val user = _user

    private val _Details = MutableStateFlow<Pair<Map<Subject, List<Assignment>>, Map<Assignment, List<Uploads>>>>(Pair(mapOf(
        Subject(
            subject_id = -1,
            group_id = -1,
            Subject_name = "No Subjects Found",
            subjectPic = "",
        ) to listOf(
            Assignment(
                assignment_id = -1,
                subject_id = -1,
                assignment_name = "No Assignments Found",
                due_date = "N/A",
                assignment_url = "N/A",
                assignment_pic_url = null
            )
        )
    ), emptyMap()))
    val details = _Details
    init {
        viewModelScope.launch {
            user.value = userState.getUser()
            Log.d("User Details from HomeViewModel", userState.getUser().toString())
            subjectManagement.Retrieve()
            details.value = Pair(emptyMap(), emptyMap())
            Log.d("HomeViewModel", "Subjects retrieved: ${subjectManagement.getAllSubjects().size}")
            progressDets.value = subjectManagement.retrieveProgressHome()
            details.value = subjectManagement.getAllData()
        }
    }

    suspend fun getAffectations(): List<Assignment> {
        val result = MyApplication.supabase.postgrest["affectation"]
        return try {
            result.select().decodeList<Assignment>()
        } catch (e: Exception) {
            emptyList() // or handle error as needed
        }
    }
}