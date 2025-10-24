package com.example.kaamkarlo.uI.navigation

import android.util.Log
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.MenuBook
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
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavType
import androidx.navigation.compose.rememberNavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.navArgument
import com.example.kaamkarlo.uI.screens.AssignmentDetail
import com.example.kaamkarlo.uI.screens.AssignmentScreen
import com.example.kaamkarlo.uI.screens.HomeScreen
import com.example.kaamkarlo.uI.screens.LoginScreen
import com.example.kaamkarlo.uI.screens.GroupScreen
import com.example.kaamkarlo.uI.screens.PrintScreen
import com.example.kaamkarlo.uI.screens.Submit
import com.example.kaamkarlo.uI.screens.ViewAssignment
import com.example.kaamkarlo.ui.screens.AddSubject
import com.example.kaamkarlo.ui.screens.JoinOrCreate
import com.example.kaamkarlo.ui.screens.SubjectManagement
import com.example.kaamkarlo.ui.screens.UploadAssignment
import com.example.kaamkarlo.viewmodels.MainViewModel

@Composable
fun AppNavigation(){
    val navController = rememberNavController()
    val context = LocalContext.current
    val mainViewModel: MainViewModel = hiltViewModel<MainViewModel>()
    val isLoaded = mainViewModel.loaded.collectAsState()

    var isLoggedIn = remember{mutableStateOf(mainViewModel.repo.isAuthenticated())}
    Log.d("AppNavigation", "isLoggedIn: ${isLoggedIn}")
    if(isLoaded.value) {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            bottomBar = {BottomBar(navController)},
            containerColor = Color.White, // Set to your previous background color (adjust as needed)
            contentColor = MaterialTheme.colorScheme.onSurface,
        ) { innerPadding ->

            NavHost(navController = navController, startDestination= if(isLoggedIn.value) Route.Home.route else Route.Login.route, modifier = Modifier.padding(innerPadding)){
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
            }
        }
    }else{
        Text("Suck My dick")
    }

}

@Composable
fun BottomBar(navController: NavController) {
    val items = listOf(
        NavItem("Home", Icons.Filled.Home, "Home"),
        NavItem("Assignment", Icons.Filled.MenuBook, "Assignment"),
//        NavItem(Route.Print.route, 0, "Print Screen"),
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