package com.lyncan.opus.Presentation.States

data class SubjectCardsState(val subjectName: String, val percentage: Double, val subjectId: Int, val total: Int, val present: Int)

data class AttendanceRetrievalState(val list: List<SubjectCardsState>)

