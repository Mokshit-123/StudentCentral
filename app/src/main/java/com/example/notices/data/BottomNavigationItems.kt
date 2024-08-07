package com.example.notices.data

import androidx.compose.ui.graphics.vector.ImageVector

data class BottomNavigationItems(
    val title : String,
    val selectedIcon : ImageVector,
    val unselectedIcon : ImageVector,
    val hasNotification: Boolean = false
)
