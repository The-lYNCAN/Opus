package com.example.kaamkarlo.uI.navigation

sealed class Route (val route: String) {
    object Home : Route("home")
    object Login : Route("login")
    object Profile : Route("profile")
    object Settings : Route("settings")
    object About : Route("about")
    object Help : Route("help")
    object Group : Route("group")
    object Assignment : Route("assignment")
    object AssignmentDetail: Route("assignment_detail"){
        fun createRoute(assignmentId: Int, subName: String) = "$route/$assignmentId/$subName"
    }
    object ViewAssignment: Route("view_assignment")
    object Print : Route("print")

    object Submit: Route("submit"){
        fun createRoute(assignmentId: Int) = "$route/$assignmentId"
    }
    object JoinOrCreate: Route("JoinOrCreate"){
        fun createRoute(joinOrCreate: Boolean) = "$route/$joinOrCreate"
    }
    object SubjectManagement: Route("subject_management")
    object UploadAssignment: Route("upload_assignment")
    object AddSubject: Route("add_subject")
}

