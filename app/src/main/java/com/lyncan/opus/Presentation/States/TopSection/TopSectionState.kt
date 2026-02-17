package com.lyncan.opus.Presentation.States.TopSection

data class TopSectionState(
    val percentage: Double,
    val present: Int,
    val absent: Int,
    val subjectName: String,
    val subjectCode: String?
)
