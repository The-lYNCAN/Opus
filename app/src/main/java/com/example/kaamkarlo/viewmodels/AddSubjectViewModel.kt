package com.example.kaamkarlo.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kaamkarlo.SubjectManagement
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddSubjectViewModel @Inject constructor(val subMan: SubjectManagement): ViewModel() {
    private val _loading = MutableStateFlow(false)
    val loading = _loading
    init {

    }
    fun createSubject(subjectName: String){
        viewModelScope.launch {
            loading.value = true
            subMan.createSubject(subjectName)
            loading.value = false
        }
    }
}