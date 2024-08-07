package com.example.notices.data

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.automirrored.outlined.ExitToApp
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Work
import androidx.compose.material.icons.outlined.Book
import androidx.compose.material.icons.outlined.DateRange
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material.icons.outlined.Work

object NavigationDrawerItemsDataSource {
    val NoticeNavigationItemsTop: List<NavigationDrawerItem> = listOf(
        NavigationDrawerItem(
            title = "All",
            selectedIcon = Icons.Filled.Home,
            unselectedIcon = Icons.Outlined.Home,
        ),
        NavigationDrawerItem(
            title = "Library",
            selectedIcon = Icons.Filled.Book,
            unselectedIcon = Icons.Outlined.Book,
            navRoute ="SearchScreen?quickQuery=Library"

        ),
        NavigationDrawerItem(
            title = "Opportunity",
            selectedIcon = Icons.Filled.Work,
            unselectedIcon = Icons.Outlined.Work,
            navRoute ="SearchScreen?quickQuery=Opportunity"
        ),
        NavigationDrawerItem(
            title = "Event",
            selectedIcon = Icons.Filled.DateRange,
            unselectedIcon = Icons.Outlined.DateRange,
            navRoute ="SearchScreen?quickQuery=Event"
        )
    )
    val NoticeNavigationItemsBottom: List<NavigationDrawerItem> = listOf(
        NavigationDrawerItem(
            title = "Settings",
            selectedIcon = Icons.Filled.Settings,
            unselectedIcon = Icons.Outlined.Settings,
            navRoute = "Settings"
        ),
        NavigationDrawerItem(
            title = "About Us",
            selectedIcon = Icons.Filled.Info,
            unselectedIcon = Icons.Outlined.Info,
            navRoute = "AboutUs"
        ),
        NavigationDrawerItem(
            title = "Log Out",
            selectedIcon = Icons.AutoMirrored.Filled.ExitToApp,
            unselectedIcon = Icons.AutoMirrored.Outlined.ExitToApp
        )
    )
}
