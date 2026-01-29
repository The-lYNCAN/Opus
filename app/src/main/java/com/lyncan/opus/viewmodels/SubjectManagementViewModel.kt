package com.lyncan.opus.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lyncan.opus.SubjectManagement
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SubjectManagementViewModel @Inject constructor(val subjectManage: SubjectManagement): ViewModel(){
    var subMan = subjectManage
    private val subjectList = MutableStateFlow(subMan.subjectList)
    val subjects = subjectList
    private val _loading = MutableStateFlow(false)
    val loading = _loading
    init{
//        viewModelScope.launch {
//            subjectManage.Retrieve()
//        }
    }

    fun deltFunc(subjectId: Int){

        viewModelScope.launch {
            loading.value = true
            subMan.deleteSubject(subjectId)
            loading.value = false

        }
    }
    fun resetSubState(){
        subMan = subjectManage
        subjectList.value = subjectManage.subjectList
        Log.d("SubjectManagementVM", subMan.subjectList.toString())
    }
    fun createSubject(subjectName: String, subjectCode: String){
        viewModelScope.launch {
            loading.value = true
            subMan.createSubject(subjectName)
            loading.value = false
        }
    }
}