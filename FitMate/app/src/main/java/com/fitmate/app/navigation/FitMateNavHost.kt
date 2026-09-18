package com.fitmate.app.navigation

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.fitmate.presentation.home.HomeScreen
import com.fitmate.presentation.mypage.MyPageScreen
import com.fitmate.presentation.record.RecordScreen
import com.fitmate.presentation.routine.RoutineScreen
import com.fitmate.presentation.workout.WorkoutScreen

@Composable
fun FitMateNavHost() {

    val navController = rememberNavController()

    // 현재 선택된 화면 확인
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    Scaffold(
        bottomBar = {
            NavigationBar {

                bottomNavItems.forEach { item ->

                    NavigationBarItem(
                        selected = currentRoute == item.route,

                        onClick = {
                            navController.navigate(item.route) {

                                // 같은 화면이 여러 번 쌓이는 것 방지
                                launchSingleTop = true

                                // 바텀 네비 화면 상태 유지
                                popUpTo(Route.HOME) {
                                    saveState = true
                                }

                                // 이전에 선택했던 화면 상태 복원
                                restoreState = true
                            }
                        },

                        icon = {
                            Icon(
                                imageVector = item.icon,
                                contentDescription = item.label
                            )
                        },

                        label = {
                            Text(text = item.label)
                        }
                    )
                }
            }
        }
    ) { innerPadding ->

        NavHost(
            navController = navController,
            startDestination = Route.HOME,
            modifier = Modifier.padding(innerPadding),

            // 화면 전환 애니메이션 제거
            enterTransition = {
                EnterTransition.None
            },
            exitTransition = {
                ExitTransition.None
            },
            popEnterTransition = {
                EnterTransition.None
            },
            popExitTransition = {
                ExitTransition.None
            }
        ) {

            composable(Route.HOME) {
                HomeScreen()
            }

            composable(Route.ROUTINE) {
                RoutineScreen()
            }

            composable(Route.WORKOUT) {
                WorkoutScreen()
            }

            composable(Route.RECORD) {
                RecordScreen()
            }

            composable(Route.MYPAGE) {
                MyPageScreen()
            }
        }
    }
}