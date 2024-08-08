package com.example.notices.ui.noticesScreens

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import com.example.notices.data.attendanceData.Attendance
import com.example.notices.ui.attendanceScreens.OverallAttendance


//@RequiresApi(Build.VERSION_CODES.O)
//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun TopBar(
//    list: List<Notice>,
//    title: String,
//    onClick: (notice: Notice) -> Unit = { },
//    onRefresh:()->Unit = {}
//) {
//    Surface {
//        //making NavDrawer state
//        val navDrawerState: DrawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
//        var selectedItemIndex by rememberSaveable { mutableStateOf(0) }
//        val scope = rememberCoroutineScope()
//
//        BackHandler(enabled = navDrawerState.isOpen) {
//            scope.launch {
//                navDrawerState.close()
//            }
//        }
//
//        ModalNavigationDrawer(
//            drawerContent = {
//                ModalDrawerSheet {
//                    NavigationDrawerItemsDataSource.NoticeNavigationItemsTop.forEachIndexed { index, item ->
//                        NavigationDrawerItem(
//                            label = { Text(text = item.title) },
//                            selected = index == selectedItemIndex,
//                            onClick = {
//                                selectedItemIndex = index
//                                scope.launch {
//                                    navDrawerState.close()
//                                }
//                            },
//                            icon =
//                            {
//                                Icon(
//                                    imageVector = if (selectedItemIndex == index) {
//                                        item.selectedIcon
//                                    } else item.unselectedIcon,
//
//                                    contentDescription = null
//                                )
//                            },
//                            modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
//                        )
//                    }
//                }
//            },
//            drawerState = navDrawerState
//        ) {
//            Scaffold(
//                topBar = {
//                    TopAppBar(
//                        title = { Text(text = title) },
//                        navigationIcon = {
//                            IconButton(onClick = {
//                                scope.launch {
//                                    navDrawerState.open()
//                                }
//                            }) {
//                                Icon(
//                                    imageVector = Icons.Outlined.Menu,
//                                    contentDescription = "Menu"
//                                )
//                            }
//                        }
//                    )
//                }
//            ){
//                NoticesList(
//                    list = list,
//                    contentPadding = it,
//                    onClick = onClick,
//                    onRefresh = onRefresh
//                )
//            }
//        }
//    }
//}

//@RequiresApi(Build.VERSION_CODES.O)
//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun TopBar(
//    notice: Notice,
//    title: String,
//) {
//    Surface {
//        //making NavDrawer state
//        val navDrawerState: DrawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
//        var selectedItemIndex by rememberSaveable { mutableStateOf(0) }
//        val scope = rememberCoroutineScope()
//
//        ModalNavigationDrawer(
//            drawerContent = {
//                ModalDrawerSheet {
//                    NavigationDrawerItemsDataSource.NoticeNavigationItemsTop.forEachIndexed { index, item ->
//                        NavigationDrawerItem(
//                            label = { Text(text = item.title) },
//                            selected = index == selectedItemIndex,
//                            onClick = {
//                                selectedItemIndex = index
//                                scope.launch {
//                                    navDrawerState.close()
//                                }
//                            },
//                            icon =
//                            {
//                                Icon(
//                                    imageVector = if (selectedItemIndex == index) {
//                                        item.selectedIcon
//                                    } else item.unselectedIcon,
//
//                                    contentDescription = null
//                                )
//                            },
//                            modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
//                        )
//                    }
//                }
//            },
//            drawerState = navDrawerState
//        ) {
//            Scaffold(
//                topBar = {
//                    TopAppBar(
//                        title = { Text(text = title) },
//                        navigationIcon = {
//                            IconButton(onClick = {
//                                scope.launch {
//                                    navDrawerState.open()
//                                }
//                            }) {
//                                Icon(
//                                    imageVector = Icons.Outlined.Menu,
//                                    contentDescription = "Menu"
//                                )
//                            }
//                        }
//                    )
//                }
//            ){
//                NoticeDetails(
//                    notice = notice,
//                    contentPadding = it
//                )
//            }
//        }
//    }
//}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(
    title:String,
    attendanceList : List<Attendance>,
    bottomPaddingValues : PaddingValues,
    onRefresh: () -> Unit = {}
){
    Surface {
        Scaffold (
            topBar = {
                TopAppBar(
                    title = {
                        Text(
                            text = "Attendance",
                            style = MaterialTheme.typography.headlineMedium
                        )
                    }
                )
            }
        ){
            OverallAttendance(
                attendanceList = attendanceList,
                contentPadding = PaddingValues(top = it.calculateTopPadding(), bottom = bottomPaddingValues.calculateBottomPadding()),
                onRefresh = onRefresh
            )
        }
    }
}