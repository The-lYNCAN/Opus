package com.lyncan.opus.Presentation.ui.navigation

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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.lyncan.opus.Presentation.ui.screens.AttendanceDetailScreen
import com.lyncan.opus.Presentation.ui.screens.AttendanceScreen
import com.lyncan.opus.Presentation.ui.screens.GroupScreen
import com.lyncan.opus.Presentation.ui.screens.JoinOrCreate
import com.lyncan.opus.Presentation.ui.screens.LoginScreen
import com.lyncan.opus.Presentation.ui.screens.SubjectManagement
import com.lyncan.opus.Presentation.ui.screens.TimetableScreen
import com.lyncan.opus.Presentation.viewmodels.MainViewModel

@Composable
fun AppNavigation(){
    val navController = rememberNavController()
    val mainViewModel: MainViewModel = hiltViewModel<MainViewModel>()
    val isLoaded = mainViewModel.loaded.collectAsState()
    val localLayoutDirection = LocalLayoutDirection.current
//    val isLoggedIn = remember{mutableStateOf(mainViewModel.repo.isAuthenticated())}
//    val isLoggedIn by mainViewModel.isLoggedIn.collectAsState()
//    Log.d("AppNavigation", "isLoggedIn: $isLoggedIn")
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            bottomBar = {BottomBar(navController)},
            containerColor = Color.White, // Set to your previous background color (adjust as needed)
            contentColor = MaterialTheme.colorScheme.onSurface,
        ) { innerPadding ->

            NavHost(navController = navController, startDestination= Route.Login.route, modifier = Modifier.padding(
                start = innerPadding.calculateStartPadding(localLayoutDirection),
                end = innerPadding.calculateEndPadding(localLayoutDirection),
                bottom = innerPadding.calculateBottomPadding()
            )){
//            NavHost(navController = navController, startDestination=  Route.Home.route , modifier = Modifier.padding(innerPadding)){

                composable(Route.Login.route) {
                    LoginScreen(navController, onLogin={
//                        mainViewModel.setLogin(true)
                        navController.navigate(Route.Attendance.route)
                    }, mainViewModel)
                }
                composable(Route.Group.route) {
                    GroupScreen(navController, mainViewModel)
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

                    AttendanceDetailScreen()
                }
            }
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
                    navController.navigate(item.route) {
                        launchSingleTop = true
                        restoreState = true
                        popUpTo(navController.graph.startDestinationId) {
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