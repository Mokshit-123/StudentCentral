package com.example.notices

import ProfileScreen
import android.app.Activity
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.notices.data.BottomNavigationItemDataSource.bottomNavigationItems
import com.example.notices.data.noticeData.Notice
import com.example.notices.theme.NoticesTheme
import com.example.notices.ui.AboutUsScreen
import com.example.notices.ui.LoginScreen
import com.example.notices.ui.SettingsScreen
import com.example.notices.ui.noticesScreens.NoticeDetails
import com.example.notices.ui.noticesScreens.SearchScreen
import com.example.notices.viewModels.DrawerViewModel
import com.example.notices.viewModels.LoginUiState
import com.example.notices.viewModels.LoginViewModel
import com.example.notices.viewModels.NoticeViewModel
import com.google.gson.Gson
import java.net.URLDecoder

class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            NoticesTheme {
                val navController = rememberNavController()
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                var bottomBarState by rememberSaveable { mutableStateOf(true) }
                var drawerOpenState by rememberSaveable { mutableStateOf(false) }

                //making notice view model here so that it can be passed to notice screen and search screen
                val noticeViewModel: NoticeViewModel = viewModel(factory = NoticeViewModel.Factory)
                val drawerViewModel: DrawerViewModel = DrawerViewModel()

                val logInViewModel: LoginViewModel = viewModel(factory = LoginViewModel.factory)
                val loginUiState : LoginUiState = logInViewModel.loginUiState

                when(loginUiState){
                    is LoginUiState.LoggedIn->{
                        Surface(modifier = Modifier.fillMaxSize()) {
                            Scaffold(
                                bottomBar = {
                                    AnimatedVisibility(
                                        visible = bottomBarState && !drawerOpenState,
                                        enter = slideInVertically(initialOffsetY = { it }),
                                        exit = slideOutVertically(targetOffsetY = { it })
                                    ) {
                                        NavigationBar {
                                            bottomNavigationItems.forEach { bottomNavigationItem ->
                                                val isSelected = navBackStackEntry?.destination?.route == bottomNavigationItem.title
                                                NavigationBarItem(
                                                    selected = isSelected,
                                                    onClick = {
                                                        if (navController.currentDestination?.route != bottomNavigationItem.title) {
                                                            navController.navigate(bottomNavigationItem.title)
                                                        }
                                                    },
                                                    icon = {
                                                        Row {
                                                            Icon(
                                                                imageVector = if (isSelected) {
                                                                    bottomNavigationItem.selectedIcon
                                                                } else bottomNavigationItem.unselectedIcon,
                                                                contentDescription = null
                                                            )
                                                            AnimatedVisibility(
                                                                visible = isSelected
                                                            ) {
                                                                Text(
                                                                    text = bottomNavigationItem.title,
                                                                    modifier = Modifier.align(Alignment.CenterVertically)
                                                                )
                                                            }
                                                        }
                                                    }
                                                )
                                            }
                                        }
                                    }
                                }
                            ) {bottomPaddingValues ->
                                NavHost(navController = navController, startDestination = "Notices") {
                                    composable("Notices") {
                                        //selectedItem = "Notices"
                                        bottomBarState = true
                                        NoticeScreen(
                                            noticeViewModel = noticeViewModel,
                                            drawerViewModel = drawerViewModel,
                                            loginViewModel = logInViewModel,
                                            bottomBarStateUpdater = { state -> bottomBarState = state },
                                            drawerStateUpdater = { state -> drawerOpenState = state },
                                            navController = navController,
                                            bottomPaddingValues = bottomPaddingValues
                                        )
                                    }
                                    composable(
                                        route = "detailScreen/{notice}",
                                        arguments = listOf(navArgument("notice") { type = NavType.StringType })
                                    ) {
                                        val noticeJson = it.arguments?.getString("notice")
                                        val notice = Gson().fromJson(URLDecoder.decode(noticeJson, "UTF-8"), Notice::class.java)
                                        bottomBarState  = false
                                        NoticeDetails(notice = notice, bottomPadding = bottomPaddingValues)
                                    }
                                    composable("Attendance") {
                                        //selectedItem = "Attendance"
                                        bottomBarState = true
                                        Log.d("MainActivity", "onCreate: Starting AttendanceScreen")
                                        AttendanceScreen(bottomPaddingValues = bottomPaddingValues)
                                    }
                                    composable("Profile") {
                                        //selectedItem = "Profile"
                                        bottomBarState = true
                                        Log.d("MainActivity", "onCreate: Starting ProfileScreen")

                                        ProfileScreen( bottomPaddingValues = bottomPaddingValues)
                                    }
                                    composable(
                                        "SearchScreen?quickQuery={quickQuery}",
                                        arguments = listOf(
                                            navArgument("quickQuery") {
                                                type = NavType.StringType
                                                nullable = true
                                            }
                                        )
                                    ) {
                                            backStackEntry ->
                                        val quickQuery = backStackEntry.arguments?.getString("quickQuery")
                                        bottomBarState = false
                                        SearchScreen(
                                            navController = navController,
                                            drawerViewModel= drawerViewModel,
                                            noticeViewModel=noticeViewModel,
                                            quickQuery = quickQuery
                                        )
                                    }
                                    composable("Settings") {
                                        SettingsScreen(drawerViewModel = drawerViewModel, navController = navController)
                                    }
                                    composable("AboutUs") {
                                        AboutUsScreen(drawerViewModel=drawerViewModel, navController=navController)
                                    }
                                }
                                BackHandler(navController = navController)
                                ObserveNavigationChanges(navController = navController) { route ->
                                    bottomBarState = when (route) {
                                        "Notices", "Attendance", "Profile" -> true
                                        else -> false
                                    }
                                }
                            }
                        }
                    }
                    is LoginUiState.LoggedOut->{
                        LoginScreen()
                    }
                    else->{
                        LoginScreen()
                    }
                }
            }
        }
    }
}

@Composable
fun BackHandler(navController: NavController) {
    val activity = LocalContext.current as? Activity
    androidx.activity.compose.BackHandler(enabled = true) {
        if (navController.currentDestination?.route == "Notices") {
            activity?.finish()
        } else {
            navController.popBackStack()
        }
    }
}

@Composable
fun ObserveNavigationChanges(navController: NavController, onRouteChanged: (String) -> Unit) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    navBackStackEntry?.destination?.route?.let { route ->
        onRouteChanged(route)
    }
}
