package com.example.notices.data

import androidx.compose.ui.graphics.vector.ImageVector

data class NavigationDrawerItem(
    val title: String,
    val selectedIcon : ImageVector,
    val unselectedIcon : ImageVector,
    val navRoute: String? = null
)
