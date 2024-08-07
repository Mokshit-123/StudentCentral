package com.example.notices

import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Divider
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Menu
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.notices.data.NavigationDrawerItemsDataSource
import com.example.notices.ui.ShimmerLoadingScreen
import com.example.notices.ui.noticesScreens.NoticesList
import com.example.notices.viewModels.DrawerViewModel
import com.example.notices.viewModels.LoginViewModel
import com.example.notices.viewModels.NoticeUiState
import com.example.notices.viewModels.NoticeViewModel
import com.example.notices.viewModels.OfflineNoticeUiState
import com.google.gson.Gson
import kotlinx.coroutines.launch
import java.net.URLEncoder

@OptIn(ExperimentalMaterial3Api::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun NoticeScreen(
    noticeViewModel: NoticeViewModel,
    drawerViewModel: DrawerViewModel = DrawerViewModel(),
    loginViewModel: LoginViewModel,
    bottomBarStateUpdater: (Boolean) -> Unit = { },
    drawerStateUpdater: (Boolean) -> Unit = { },
    navController: NavController,
    bottomPaddingValues : PaddingValues = PaddingValues(0.dp)
) {
    Surface(
        modifier = Modifier.fillMaxSize()
    ) {

        //viewmodel imported from activity
        //val noticeViewModel: NoticeViewModel = viewModel(factory = NoticeViewModel.Factory)
        val noticeUiState = noticeViewModel.noticeUiState
        val offlineNoticeUiState = noticeViewModel.offlineNoticeUiState

        // Handle NavDrawer state here
        val navDrawerState: DrawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
        var selectedItemIndex by rememberSaveable { mutableStateOf(0) }
        val scope = rememberCoroutineScope()

        BackHandler(enabled = navDrawerState.isOpen) {
            scope.launch {
                navDrawerState.close()
            }
        }

        ModalNavigationDrawer(
            drawerContent = {
                ModalDrawerSheet {
                    Column{
                        Spacer(modifier = Modifier.height(12.dp))
                        NavigationDrawerItemsDataSource.NoticeNavigationItemsTop.forEach { item ->
                            val selected = item.title==drawerViewModel.selectedItem.value
                            NavigationDrawerItem (
                                label = { Text(text = item.title) },
                                selected = selected,
                                onClick = {
                                    drawerViewModel.selectedItem(item.title)
                                    scope.launch {
                                        navDrawerState.close()
                                        if (item.navRoute != null) {
                                            navController.navigate(item.navRoute)
                                        }
                                    }

                                },
                                icon = {
                                    Icon(
                                        imageVector = if (selected) {
                                            item.selectedIcon
                                        } else item.unselectedIcon,
                                        contentDescription = null
                                    )
                                },
                                modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
                            )
                        }
                        Spacer(modifier = Modifier.weight(1f))
                        Divider()
                        NavigationDrawerItemsDataSource.NoticeNavigationItemsBottom.forEach {item->
                            val selected = item.title==drawerViewModel.selectedItem.value
                            NavigationDrawerItem(
                                label = { Text(text = item.title) },
                                selected = selected,
                                onClick = {
                                    drawerViewModel.selectedItem(item.title)
                                    scope.launch {
                                        navDrawerState.close()
                                        if (item.navRoute != null) {
                                            navController.navigate(item.navRoute)
                                        }else if (item.title=="Log Out"){
                                            loginViewModel.logout()
                                        }
                                    }
                                },
                                icon = {
                                    Icon(
                                        imageVector = if (selected) {
                                            item.selectedIcon
                                        } else item.unselectedIcon,
                                        contentDescription = null
                                    )
                                },
                                modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
                            )
                        }
                    }
                }
            },
            drawerState = navDrawerState
        ) {
            // Update bottomBarState based on drawer state
            LaunchedEffect(navDrawerState.isOpen) {
                bottomBarStateUpdater(!navDrawerState.isOpen)
                drawerStateUpdater(navDrawerState.isOpen)
            }

            Scaffold(
                topBar = {
                    TopAppBar(
                        title = {
                            Text(
                                text = "Notices",
                                style = MaterialTheme.typography.headlineMedium
                            )
                                },
                        navigationIcon = {
                            IconButton(onClick = {
                                scope.launch {
                                    navDrawerState.open()
                                }
                            }) {
                                Icon(
                                    imageVector = Icons.Outlined.Menu,
                                    contentDescription = "Menu"
                                )
                            }
                        },
                        actions = {
                            IconButton(onClick = {
                                navController.navigate("SearchScreen")
                            }) {
                                Icon(imageVector = Icons.Outlined.Search, contentDescription = "search button")
                            }
                        }
                    )
                }
            ) {paddingValues->
                when (noticeUiState) {
                    is NoticeUiState.Loading -> {
                        Log.d("NoticeLoading", "onCreate: ")
                        ShimmerLoadingScreen(title = "Notice")
                    }
                    is NoticeUiState.Success -> {
                        val noticesRetrieved = noticeUiState.notices
                        Log.d("NoticeRetrieved", "onCreate: $noticesRetrieved")
                        NoticesList(
                            list = noticesRetrieved,
                            contentPadding = paddingValues,
                            bottomPadding = bottomPaddingValues,
                            onClick = { notice ->
                                val noticeJson = URLEncoder.encode(Gson().toJson(notice), "UTF-8")
                                navController.navigate("detailScreen/$noticeJson")
                            },
                            onRefresh = { noticeViewModel.refreshNotices() }
                        )
                    }
                    is NoticeUiState.Error -> {
                        if( offlineNoticeUiState is OfflineNoticeUiState.Retrieved){
                            val context = LocalContext.current
                            Toast.makeText(context, "Something went wrong!!", Toast.LENGTH_SHORT).show()
                            NoticesList(
                                list = offlineNoticeUiState.notices,
                                contentPadding = paddingValues,
                                bottomPadding = bottomPaddingValues,
                                onClick = { notice ->
                                    val noticeJson = URLEncoder.encode(Gson().toJson(notice), "UTF-8")
                                    navController.navigate("detailScreen/$noticeJson")
                                },
                                onRefresh = { noticeViewModel.refreshNotices() }
                            )
                        }else if(offlineNoticeUiState is OfflineNoticeUiState.Error){
                            val context = LocalContext.current
                            Toast.makeText(context, "Something went wrong!! No notices found!!", Toast.LENGTH_SHORT).show()
                            NoticesList(
                                list = listOf(),
                                contentPadding = paddingValues,
                                bottomPadding = bottomPaddingValues,
                                onRefresh = { noticeViewModel.refreshNotices() }
                            )
                        }
                    }
                }
            }
        }
    }
}