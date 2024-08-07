package com.example.notices.ui

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.notices.viewModels.DrawerViewModel

@Composable
fun SettingsScreen(
    drawerViewModel: DrawerViewModel = DrawerViewModel(),
    navController: NavController = rememberNavController(),
    modifier: Modifier = Modifier
) {
    BackHandler {
        drawerViewModel.selectedItem("All")
        navController.popBackStack()
    }
    Column (
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        androidx.compose.material3.Text(text = "About Us")
    }
}