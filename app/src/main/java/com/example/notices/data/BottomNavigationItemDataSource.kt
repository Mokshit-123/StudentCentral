package com.example.notices.data

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Assessment
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.outlined.Assessment
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.Person

object BottomNavigationItemDataSource {
    val bottomNavigationItems = listOf(
        BottomNavigationItems(
            "Notices",
            Icons.Filled.Notifications,
            Icons.Outlined.Notifications
        ),
        BottomNavigationItems(
            "Attendance",
            Icons.Filled.Assessment,
            Icons.Outlined.Assessment
        ),
        BottomNavigationItems(
            "Profile",
            Icons.Filled.Person,
            Icons.Outlined.Person
        )
    )
}