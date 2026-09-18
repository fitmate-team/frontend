package com.fitmate.app.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Person
import androidx.compose.ui.graphics.vector.ImageVector

data class BottomNavItem(
    val label: String,
    val route: String,
    val icon: ImageVector
)

val bottomNavItems = listOf(
    BottomNavItem(
        label = "홈",
        route = Route.HOME,
        icon = Icons.Default.Home
    ),
    BottomNavItem(
        label = "루틴",
        route = Route.ROUTINE,
        icon = Icons.Default.List
    ),
    BottomNavItem(
        label = "운동",
        route = Route.WORKOUT,
        icon = Icons.Default.FitnessCenter
    ),
    BottomNavItem(
        label = "기록",
        route = Route.RECORD,
        icon = Icons.Default.BarChart
    ),
    BottomNavItem(
        label = "마이",
        route = Route.MYPAGE,
        icon = Icons.Default.Person
    )
)