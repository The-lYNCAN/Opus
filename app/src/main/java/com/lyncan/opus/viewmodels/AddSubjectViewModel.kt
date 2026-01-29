package com.lyncan.opus.viewmodels

import androidx.lifecycle.ViewModel
import com.lyncan.opus.SubjectManagement
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

@HiltViewModel
class AddSubjectViewModel @Inject constructor(val subMan: SubjectManagement): ViewModel() {
    private val _loading = MutableStateFlow(false)
    val loading = _loading
    init {

    }

}