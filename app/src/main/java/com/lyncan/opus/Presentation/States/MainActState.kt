package com.lyncan.opus.Presentation.States

import kotlinx.coroutines.flow.StateFlow

data class MainActState(
    val loaded: StateFlow<Boolean>,

)
