package com.lyncan.opus.uI.navigation

import android.util.Log
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Group
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.lyncan.opus.uI.screens.AssignmentDetail
import com.lyncan.opus.uI.screens.AssignmentScreen
import com.lyncan.opus.uI.screens.GroupScreen
import com.lyncan.opus.uI.screens.HomeScreen
import com.lyncan.opus.uI.screens.LoginScreen
import com.lyncan.opus.uI.screens.PrintScreen
import com.lyncan.opus.uI.screens.Submit
import com.lyncan.opus.uI.screens.ViewAssignment
import com.lyncan.opus.ui.screens.AddSubject
import com.lyncan.opus.ui.screens.AttendanceDetailScreen
import com.lyncan.opus.ui.screens.AttendanceScreen
import com.lyncan.opus.ui.screens.JoinOrCreate
import com.lyncan.opus.ui.screens.SubjectManagement
import com.lyncan.opus.ui.screens.TimetableScreen
import com.lyncan.opus.ui.screens.UploadAssignment
import com.lyncan.opus.viewmodels.MainViewModel

@Composable
fun AppNavigation(){
    val navController = rememberNavController()
    val context = LocalContext.current
    val mainViewModel: MainViewModel = hiltViewModel<MainViewModel>()
    val isLoaded = mainViewModel.loaded.collectAsState()
    val localLayoutDirection = LocalLayoutDirection.current
    var isLoggedIn = remember{mutableStateOf(mainViewModel.repo.isAuthenticated())}
    Log.d("AppNavigation", "isLoggedIn: ${isLoggedIn}")
    if(isLoaded.value) {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            bottomBar = {BottomBar(navController)},
            containerColor = Color.White, // Set to your previous background color (adjust as needed)
            contentColor = MaterialTheme.colorScheme.onSurface,
        ) { innerPadding ->

            NavHost(navController = navController, startDestination= if(isLoggedIn.value) Route.Attendance.route else Route.Login.route, modifier = Modifier.padding(
                start = innerPadding.calculateStartPadding(localLayoutDirection),
                end = innerPadding.calculateEndPadding(localLayoutDirection),
                bottom = innerPadding.calculateBottomPadding()
            )){
//            NavHost(navController = navController, startDestination=  Route.Home.route , modifier = Modifier.padding(innerPadding)){


                composable(Route.Home.route) {
                    HomeScreen(navController, mainViewModel)
                }
                composable(Route.Login.route) {
                    LoginScreen(navController, onLogin={
                        isLoggedIn.value = true

                    }, mainViewModel)
                }
                composable(Route.Group.route) {
                    GroupScreen(navController, mainViewModel)
                }
                composable(Route.Assignment.route){
                    AssignmentScreen(navController, mainViewModel)
                }
                composable (Route.AssignmentDetail.route+"/{Assignment_Id_Key}/{SubName}", arguments = listOf(
                    navArgument("Assignment_Id_Key"){type= NavType.IntType},
                    navArgument("SubName"){type= NavType.StringType}
                )) {backStackEntry ->
                    val assignmentId = backStackEntry.arguments?.getInt("Assignment_Id_Key")
                    val subName = backStackEntry.arguments?.getString("SubName")

                    AssignmentDetail(navController, mainViewModel, assignmentId, subName)
                }
                composable (Route.ViewAssignment.route) {
                    ViewAssignment(navController, mainViewModel)
                }
                composable(Route.Print.route) {
                    PrintScreen(navController, mainViewModel)
                }
                composable(Route.Submit.route+"/{Assignment_Id_Key}", arguments = listOf(
                    navArgument("Assignment_Id_Key"){type= NavType.IntType}
                )) { backStackEntry ->
                    val assignmentId = backStackEntry.arguments?.getInt("Assignment_Id_Key")

                    Submit(navController, mainViewModel, assignmentId!!)
                }
                composable (Route.JoinOrCreate.route+"/{JoinOrCreate}", arguments = listOf(
                    navArgument("JoinOrCreate"){type= NavType.BoolType}
                )){ backStackEntry ->
                    val joinOrCreate = backStackEntry.arguments?.getBoolean("JoinOrCreate")
                    JoinOrCreate(navController, mainViewModel, joinOrCreate!!)
                }
                composable (Route.SubjectManagement.route){
                    SubjectManagement(navController, mainViewModel)
                }
                composable(Route.UploadAssignment.route){
                    UploadAssignment(navController)
                }
                composable(Route.AddSubject.route) {
                    AddSubject(navController)
                }
                composable(Route.Attendance.route){
                    AttendanceScreen(navController)
                }
                composable (Route.TimeTable.route){
                    TimetableScreen()
                }
                composable(Route.AttendanceDetail.route+"/{Subject_Id_Key}", arguments = listOf(
                    navArgument("Subject_Id_Key"){type= NavType.IntType}
                )) { backStackEntry ->
                    val subjectId = backStackEntry.arguments?.getInt("Subject_Id_Key")

                    AttendanceDetailScreen(subjectId)
                }
            }
        }
    }else{
        Text("Suck My dick")
    }

}

@Composable
fun BottomBar(navController: NavController) {
    val items = listOf(
//        NavItem(Route.Home.route, Icons.Filled.Home, "Home"),
//        NavItem(Route.Assignment.route , Icons.Filled.MenuBook, "Assignment"),
        NavItem(Route.Attendance.route, Icons.Filled.Group, "Attendance"),
        NavItem(Route.Group.route, Icons.Filled.Group, "Group Management"),
    )
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    NavigationBar {
        items.forEach { item ->
            NavigationBarItem(
                selected = currentRoute == item.route,
                icon = { Icon(imageVector = item.icon, contentDescription = item.label) },
                onClick = {
                    navController.navigate(item.route){
                        popUpTo(navController.graph.startDestinationId){
                            saveState = true
                        }
                    }
                }

            )
        }
    }
}

data class NavItem(
    val route: String,
    val icon: ImageVector,
    val label: String
)