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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.fitmate.presentation.auth.AIAnalysisLoadingScreen
import com.fitmate.presentation.auth.AIRecommendationReasonScreen
import com.fitmate.presentation.auth.AIRoutineResultScreen
import com.fitmate.presentation.auth.AITrainerIntroScreen
import com.fitmate.presentation.auth.BasicBodyInfoScreen
import com.fitmate.presentation.auth.DetailedGoalScreen
import com.fitmate.presentation.auth.ENV_GYM
import com.fitmate.presentation.auth.ENV_HOME
import com.fitmate.presentation.auth.ExerciseAbilityScreen
import com.fitmate.presentation.auth.ExerciseExperienceScreen
import com.fitmate.presentation.auth.ExerciseRestrictionsScreen
import com.fitmate.presentation.auth.ExerciseSearchScreen
import com.fitmate.presentation.auth.GOAL_FAT_LOSS
import com.fitmate.presentation.auth.GymEquipmentScreen
import com.fitmate.presentation.auth.HomeEquipmentScreen
import com.fitmate.presentation.auth.LoginScreen
import com.fitmate.presentation.auth.OptionalBodyInfoScreen
import com.fitmate.presentation.auth.OutdoorEquipmentScreen
import com.fitmate.presentation.auth.SignupIntroScreen
import com.fitmate.presentation.auth.WorkoutDurationScreen
import com.fitmate.presentation.auth.WorkoutEnvironmentScreen
import com.fitmate.presentation.auth.WorkoutGoalScreen
import com.fitmate.presentation.auth.WorkoutScheduleScreen
import com.fitmate.presentation.home.HomeScreen
import com.fitmate.presentation.home.WatchSyncResultScreen
import com.fitmate.presentation.mypage.AiTrainerSettingsScreen
import com.fitmate.presentation.mypage.AppSettingsScreen
import com.fitmate.presentation.mypage.BodyProfileScreen
import com.fitmate.presentation.mypage.HomeEquipmentEditScreen
import com.fitmate.presentation.mypage.MyPageScreen
import com.fitmate.presentation.mypage.WorkoutEnvironmentScreen
import com.fitmate.presentation.record.GrowthAnalysisScreen
import com.fitmate.presentation.record.RecordScreen
import com.fitmate.presentation.record.WorkoutRecordDetailScreen
import com.fitmate.presentation.routine.AiHandsOffSetupScreen
import com.fitmate.presentation.routine.ExerciseConfigurationScreen
import com.fitmate.presentation.routine.ManualDaySelectionScreen
import com.fitmate.presentation.routine.ManualExerciseSelectionScreen
import com.fitmate.presentation.routine.RoutineAiLoadingScreen
import com.fitmate.presentation.routine.RoutineAiReasonScreen
import com.fitmate.presentation.routine.RoutineAiResultScreen
import com.fitmate.presentation.routine.RoutineCreationMethodScreen
import com.fitmate.presentation.routine.RoutineScreen
import com.fitmate.presentation.routine.WorkoutDetailScreen
import com.fitmate.presentation.workout.ActiveWorkoutScreen
import com.fitmate.presentation.workout.AltRoutineScreen
import com.fitmate.presentation.workout.RestTimerScreen
import com.fitmate.presentation.workout.RoutineAdjustScreen
import com.fitmate.presentation.workout.RoutineAppliedScreen
import com.fitmate.presentation.workout.TodayAnalysisScreen
import com.fitmate.presentation.workout.WeightRecordScreen
import com.fitmate.presentation.workout.WorkoutAiLoadingScreen
import com.fitmate.presentation.workout.WorkoutEvalScreen
import com.fitmate.presentation.workout.WorkoutLocationScreen
import com.fitmate.presentation.workout.WorkoutScreen

@Composable
fun FitMateNavHost() {

    val navController = rememberNavController()

    // 현재 선택된 화면 확인
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val showBottomBar = bottomNavItems.any { it.route == currentRoute }

    // 루틴 수동 생성 위저드 동안 화면 간에 공유되는 선택 상태
    var manualSelectedDayLabel by remember { mutableStateOf("월") }
    var manualSelectedExercises by remember { mutableStateOf(listOf<String>()) }
    var routineDetailDayLabel by remember { mutableStateOf("월") }
    var mypageEquipmentEditTitle by remember { mutableStateOf("홈트 기구 편집") }

    Scaffold(
        bottomBar = {
            if (showBottomBar) {
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
        }
    ) { innerPadding ->

        NavHost(
            navController = navController,
            startDestination = Route.LOGIN,
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
                HomeScreen(
                    onStartWorkout = { navController.navigate(Route.WORKOUT) },
                    onNavigateToWatchSync = { navController.navigate(Route.WATCH_SYNC_RESULT) }
                )
            }

            composable(Route.WATCH_SYNC_RESULT) {
                WatchSyncResultScreen(
                    onBack = { navController.popBackStack() },
                    onViewResult = { navController.navigate(Route.RECORD) }
                )
            }

            composable(Route.ROUTINE) {
                RoutineScreen(
                    onCreateRoutine = { navController.navigate(Route.ROUTINE_CREATE_METHOD) },
                    onOpenDay = { day ->
                        routineDetailDayLabel = day
                        navController.navigate(Route.ROUTINE_WORKOUT_DETAIL)
                    }
                )
            }

            composable(Route.ROUTINE_CREATE_METHOD) {
                RoutineCreationMethodScreen(
                    onBack = { navController.popBackStack() },
                    onAiHandsOff = { navController.navigate(Route.ROUTINE_AI_HANDSOFF_SETUP) },
                    onAiTogether = { navController.navigate(Route.ROUTINE_MANUAL_DAY_SELECT) }
                )
            }

            composable(Route.ROUTINE_AI_HANDSOFF_SETUP) {
                AiHandsOffSetupScreen(
                    onBack = { navController.popBackStack() },
                    onGenerate = { navController.navigate(Route.ROUTINE_AI_LOADING) }
                )
            }

            composable(Route.ROUTINE_AI_LOADING) {
                RoutineAiLoadingScreen(
                    onComplete = {
                        navController.navigate(Route.ROUTINE_AI_RESULT) {
                            popUpTo(Route.ROUTINE_AI_LOADING) { inclusive = true }
                        }
                    }
                )
            }

            composable(Route.ROUTINE_AI_RESULT) {
                RoutineAiResultScreen(
                    onUseRoutine = {
                        navController.navigate(Route.ROUTINE) {
                            popUpTo(Route.ROUTINE) { inclusive = false }
                        }
                    },
                    onWhy = { navController.navigate(Route.ROUTINE_AI_REASON) },
                    onRegenerate = { navController.navigate(Route.ROUTINE_AI_LOADING) }
                )
            }

            composable(Route.ROUTINE_AI_REASON) {
                RoutineAiReasonScreen(
                    onBack = { navController.popBackStack() },
                    onConfirm = { navController.popBackStack() }
                )
            }

            composable(Route.ROUTINE_MANUAL_DAY_SELECT) {
                ManualDaySelectionScreen(
                    onBack = { navController.popBackStack() },
                    onNext = { days ->
                        manualSelectedDayLabel = days.firstOrNull() ?: "월"
                        navController.navigate(Route.ROUTINE_MANUAL_EXERCISE_SELECT)
                    }
                )
            }

            composable(Route.ROUTINE_MANUAL_EXERCISE_SELECT) {
                ManualExerciseSelectionScreen(
                    dayLabel = manualSelectedDayLabel,
                    onBack = { navController.popBackStack() },
                    onNext = { exercises ->
                        manualSelectedExercises = exercises
                        navController.navigate(Route.ROUTINE_EXERCISE_CONFIG)
                    }
                )
            }

            composable(Route.ROUTINE_EXERCISE_CONFIG) {
                ExerciseConfigurationScreen(
                    exercises = manualSelectedExercises.ifEmpty { listOf("벤치프레스") },
                    onBack = { navController.popBackStack() },
                    onSave = {
                        navController.navigate(Route.ROUTINE) {
                            popUpTo(Route.ROUTINE) { inclusive = false }
                        }
                    }
                )
            }

            composable(Route.ROUTINE_WORKOUT_DETAIL) {
                WorkoutDetailScreen(
                    dayLabel = routineDetailDayLabel,
                    onBack = { navController.popBackStack() },
                    onStartWorkout = { navController.navigate(Route.WORKOUT) }
                )
            }

            composable(Route.WORKOUT) {
                WorkoutScreen(
                    onBack = { navController.popBackStack() },
                    onChangeLocation = { navController.navigate(Route.WORKOUT_LOCATION) },
                    onStartWorkout = { navController.navigate(Route.WORKOUT_ACTIVE) }
                )
            }

            composable(Route.WORKOUT_LOCATION) {
                WorkoutLocationScreen(
                    onBack = { navController.popBackStack() },
                    onReadjustRoutine = { navController.navigate(Route.WORKOUT_AI_LOADING) }
                )
            }

            composable(Route.WORKOUT_AI_LOADING) {
                WorkoutAiLoadingScreen(
                    onComplete = {
                        navController.navigate(Route.WORKOUT_ALT_ROUTINE) {
                            popUpTo(Route.WORKOUT_AI_LOADING) { inclusive = true }
                        }
                    }
                )
            }

            composable(Route.WORKOUT_ALT_ROUTINE) {
                AltRoutineScreen(
                    onBack = { navController.popBackStack() },
                    onUseAdjustedRoutine = {
                        navController.navigate(Route.WORKOUT_ACTIVE) {
                            popUpTo(Route.WORKOUT) { inclusive = false }
                        }
                    },
                    onEditManually = { navController.navigate(Route.ROUTINE_MANUAL_DAY_SELECT) }
                )
            }

            composable(Route.WORKOUT_ACTIVE) {
                ActiveWorkoutScreen(
                    onRest = { navController.navigate(Route.WORKOUT_REST_TIMER) },
                    onFinishWorkout = {
                        navController.navigate(Route.WORKOUT_WEIGHT_RECORD) {
                            popUpTo(Route.WORKOUT) { inclusive = false }
                        }
                    }
                )
            }

            composable(Route.WORKOUT_REST_TIMER) {
                RestTimerScreen(
                    onNextSet = { navController.popBackStack() },
                    onSkipRest = { navController.popBackStack() }
                )
            }

            composable(Route.WORKOUT_WEIGHT_RECORD) {
                WeightRecordScreen(
                    onBack = { navController.popBackStack() },
                    onRecordAndContinue = { navController.navigate(Route.WORKOUT_EVAL) },
                    onSkip = { navController.navigate(Route.WORKOUT_EVAL) }
                )
            }

            composable(Route.WORKOUT_EVAL) {
                WorkoutEvalScreen(
                    onBack = { navController.popBackStack() },
                    onViewAnalysis = { navController.navigate(Route.WORKOUT_ANALYSIS) }
                )
            }

            composable(Route.WORKOUT_ANALYSIS) {
                TodayAnalysisScreen(
                    onBack = { navController.popBackStack() },
                    onViewSuggestion = { navController.navigate(Route.WORKOUT_ROUTINE_ADJUST) }
                )
            }

            composable(Route.WORKOUT_ROUTINE_ADJUST) {
                RoutineAdjustScreen(
                    onBack = { navController.popBackStack() },
                    onApplySelected = { navController.navigate(Route.WORKOUT_ROUTINE_APPLIED) }
                )
            }

            composable(Route.WORKOUT_ROUTINE_APPLIED) {
                RoutineAppliedScreen(
                    onGoHome = {
                        navController.navigate(Route.HOME) {
                            popUpTo(Route.HOME) { inclusive = false }
                            launchSingleTop = true
                        }
                    }
                )
            }

            composable(Route.RECORD) {
                RecordScreen(
                    onOpenWorkoutDetail = { navController.navigate(Route.RECORD_WORKOUT_DETAIL) }
                )
            }

            composable(Route.RECORD_WORKOUT_DETAIL) {
                WorkoutRecordDetailScreen(
                    onBack = { navController.popBackStack() },
                    onViewGrowth = { navController.navigate(Route.RECORD_GROWTH_ANALYSIS) }
                )
            }

            composable(Route.RECORD_GROWTH_ANALYSIS) {
                GrowthAnalysisScreen(
                    onBack = { navController.popBackStack() }
                )
            }

            composable(Route.MYPAGE) {
                MyPageScreen(
                    onOpenMenu = { menu ->
                        when (menu) {
                            "내 신체정보" -> navController.navigate(Route.MYPAGE_BODY_PROFILE)
                            "내 운동환경" -> navController.navigate(Route.MYPAGE_WORKOUT_ENVIRONMENT)
                            "AI Trainer 설정" -> navController.navigate(Route.MYPAGE_AI_TRAINER_SETTINGS)
                            "앱 설정" -> navController.navigate(Route.MYPAGE_APP_SETTINGS)
                        }
                    }
                )
            }

            composable(Route.MYPAGE_BODY_PROFILE) {
                BodyProfileScreen(
                    onBack = { navController.popBackStack() },
                    onSave = { navController.popBackStack() }
                )
            }

            composable(Route.MYPAGE_WORKOUT_ENVIRONMENT) {
                WorkoutEnvironmentScreen(
                    onBack = { navController.popBackStack() },
                    onEditGym = {
                        mypageEquipmentEditTitle = "헬스장 기구 편집"
                        navController.navigate(Route.MYPAGE_HOME_EQUIPMENT_EDIT)
                    },
                    onEditHome = {
                        mypageEquipmentEditTitle = "홈트 기구 편집"
                        navController.navigate(Route.MYPAGE_HOME_EQUIPMENT_EDIT)
                    }
                )
            }

            composable(Route.MYPAGE_HOME_EQUIPMENT_EDIT) {
                HomeEquipmentEditScreen(
                    title = mypageEquipmentEditTitle,
                    onBack = { navController.popBackStack() },
                    onSave = { navController.popBackStack() }
                )
            }

            composable(Route.MYPAGE_AI_TRAINER_SETTINGS) {
                AiTrainerSettingsScreen(
                    onBack = { navController.popBackStack() }
                )
            }

            composable(Route.MYPAGE_APP_SETTINGS) {
                AppSettingsScreen(
                    onBack = { navController.popBackStack() },
                    onSave = { navController.popBackStack() },
                    onLogout = {
                        navController.navigate(Route.LOGIN) {
                            popUpTo(0) { inclusive = true }
                        }
                    },
                    onDeleteAccount = {
                        navController.navigate(Route.LOGIN) {
                            popUpTo(0) { inclusive = true }
                        }
                    }
                )
            }

            // 회원가입 / 로그인 플로우
            composable(Route.LOGIN) {
                LoginScreen(
                    onNavigateToSignup = { navController.navigate(Route.AI_TRAINER_INTRO) },
                    onLoginSuccess = {
                        navController.navigate(Route.HOME) {
                            popUpTo(Route.LOGIN) { inclusive = true }
                        }
                    }
                )
            }

            composable(Route.AI_TRAINER_INTRO) {
                AITrainerIntroScreen(
                    onNext = { navController.navigate(Route.SIGNUP_CREDENTIALS) }
                )
            }

            composable(Route.SIGNUP_CREDENTIALS) {
                SignupIntroScreen(
                    onBack = { navController.popBackStack() },
                    onNext = { navController.navigate(Route.BASIC_BODY_INFO) }
                )
            }

            composable(Route.BASIC_BODY_INFO) {
                BasicBodyInfoScreen(
                    onBack = { navController.popBackStack() },
                    onNext = { navController.navigate(Route.OPTIONAL_BODY_INFO) }
                )
            }

            composable(Route.OPTIONAL_BODY_INFO) {
                OptionalBodyInfoScreen(
                    onBack = { navController.popBackStack() },
                    onNext = { navController.navigate(Route.EXERCISE_EXPERIENCE) }
                )
            }

            composable(Route.EXERCISE_EXPERIENCE) {
                ExerciseExperienceScreen(
                    onBack = { navController.popBackStack() },
                    onNext = { navController.navigate(Route.WORKOUT_GOAL) }
                )
            }

            composable(Route.WORKOUT_GOAL) {
                WorkoutGoalScreen(
                    onBack = { navController.popBackStack() },
                    onNext = { selectedGoal ->
                        if (selectedGoal == GOAL_FAT_LOSS) {
                            navController.navigate(Route.DETAILED_GOAL)
                        } else {
                            navController.navigate(Route.WORKOUT_SCHEDULE)
                        }
                    }
                )
            }

            composable(Route.DETAILED_GOAL) {
                DetailedGoalScreen(
                    onBack = { navController.popBackStack() },
                    onNext = { navController.navigate(Route.WORKOUT_SCHEDULE) }
                )
            }

            composable(Route.WORKOUT_SCHEDULE) {
                WorkoutScheduleScreen(
                    onBack = { navController.popBackStack() },
                    onNext = { navController.navigate(Route.WORKOUT_DURATION) }
                )
            }

            composable(Route.WORKOUT_DURATION) {
                WorkoutDurationScreen(
                    onBack = { navController.popBackStack() },
                    onNext = { navController.navigate(Route.WORKOUT_ENVIRONMENT) }
                )
            }

            composable(Route.WORKOUT_ENVIRONMENT) {
                WorkoutEnvironmentScreen(
                    onBack = { navController.popBackStack() },
                    onNext = { selectedEnv ->
                        when (selectedEnv) {
                            ENV_GYM -> navController.navigate(Route.GYM_EQUIPMENT)
                            ENV_HOME -> navController.navigate(Route.HOME_EQUIPMENT)
                            else -> navController.navigate(Route.OUTDOOR_EQUIPMENT)
                        }
                    }
                )
            }

            composable(Route.GYM_EQUIPMENT) {
                GymEquipmentScreen(
                    onBack = { navController.popBackStack() },
                    onNext = { navController.navigate(Route.EXERCISE_RESTRICTIONS) }
                )
            }

            composable(Route.HOME_EQUIPMENT) {
                HomeEquipmentScreen(
                    onBack = { navController.popBackStack() },
                    onNext = { navController.navigate(Route.EXERCISE_RESTRICTIONS) }
                )
            }

            composable(Route.OUTDOOR_EQUIPMENT) {
                OutdoorEquipmentScreen(
                    onBack = { navController.popBackStack() },
                    onNext = { navController.navigate(Route.EXERCISE_RESTRICTIONS) }
                )
            }

            composable(Route.EXERCISE_RESTRICTIONS) {
                ExerciseRestrictionsScreen(
                    onBack = { navController.popBackStack() },
                    onAddExercise = { navController.navigate(Route.EXERCISE_SEARCH) },
                    onNext = { navController.navigate(Route.EXERCISE_ABILITY) }
                )
            }

            composable(Route.EXERCISE_SEARCH) {
                ExerciseSearchScreen(
                    onBack = { navController.popBackStack() },
                    onConfirm = { navController.popBackStack() }
                )
            }

            composable(Route.EXERCISE_ABILITY) {
                ExerciseAbilityScreen(
                    onBack = { navController.popBackStack() },
                    onNext = { navController.navigate(Route.AI_ANALYSIS_LOADING) }
                )
            }

            composable(Route.AI_ANALYSIS_LOADING) {
                AIAnalysisLoadingScreen(
                    onComplete = {
                        navController.navigate(Route.AI_ROUTINE_RESULT) {
                            popUpTo(Route.AI_ANALYSIS_LOADING) { inclusive = true }
                        }
                    }
                )
            }

            composable(Route.AI_ROUTINE_RESULT) {
                AIRoutineResultScreen(
                    onWhy = { navController.navigate(Route.AI_RECOMMENDATION_REASON) },
                    onUseRoutine = {
                        navController.navigate(Route.HOME) {
                            popUpTo(Route.LOGIN) { inclusive = true }
                        }
                    },
                    onRegenerate = { navController.navigate(Route.AI_ANALYSIS_LOADING) }
                )
            }

            composable(Route.AI_RECOMMENDATION_REASON) {
                AIRecommendationReasonScreen(
                    onBack = { navController.popBackStack() },
                    onConfirm = { navController.popBackStack() }
                )
            }
        }
    }
}
