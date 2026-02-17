package com.lyncan.opus.Presentation.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lyncan.opus.Domain.UseCases.AuthenticationUseCase.GetCurrentUserUseCase
import com.lyncan.opus.Domain.UseCases.AuthenticationUseCase.UserLoggedInUseCase
import com.lyncan.opus.Domain.UseCases.AuthenticationUseCase.UserNotLoggedInUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainActViewModel @Inject constructor(
    private val userLoggedInUseCase: UserLoggedInUseCase,
    private val userNotLoggedInUseCase: UserNotLoggedInUseCase,
    private val getCurrentUserUseCase: GetCurrentUserUseCase
): ViewModel(){
    private val _loaded = MutableStateFlow(true)
    val loaded = _loaded.asStateFlow()
    init {
        viewModelScope.launch {
            val loggedIn = getCurrentUserUseCase()
            Log.d("MainActViewModel", "User logged in: $loggedIn")
            _loaded.value = false
        }
    }
}