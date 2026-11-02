package com.fitmate.app.navigation

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.fitmate.domain.model.ExerciseLocation
import com.fitmate.presentation.designsystem.component.FitMateBottomBar
import com.fitmate.presentation.auth.AITrainerIntroScreen
import com.fitmate.presentation.auth.BasicBodyInfoScreen
import com.fitmate.presentation.auth.ENV_GYM
import com.fitmate.presentation.auth.ENV_HOME
import com.fitmate.presentation.auth.ENV_OUTDOOR
import com.fitmate.presentation.auth.ExerciseAbilityScreen
import com.fitmate.presentation.auth.ExerciseExperienceScreen
import com.fitmate.presentation.auth.ExerciseRestrictionsScreen
import com.fitmate.presentation.auth.ExerciseSearchScreen
import com.fitmate.presentation.auth.GymEquipmentScreen
import com.fitmate.presentation.auth.HomeEquipmentScreen
import com.fitmate.presentation.auth.LoginEventEffect
import com.fitmate.presentation.auth.LoginScreen
import com.fitmate.presentation.auth.LoginViewModel
import com.fitmate.presentation.auth.OptionalBodyInfoScreen
import com.fitmate.presentation.auth.OutdoorEquipmentScreen
import com.fitmate.presentation.auth.SignupEventEffect
import com.fitmate.presentation.auth.SignupIntroScreen
import com.fitmate.presentation.auth.SignupStep
import com.fitmate.presentation.auth.SignupUiState
import com.fitmate.presentation.auth.SignupViewModel
import com.fitmate.presentation.auth.WorkoutEnvironmentScreen
import com.fitmate.presentation.auth.WorkoutGoalScreen
import com.fitmate.presentation.auth.WorkoutMethodScreen
import com.fitmate.presentation.auth.WorkoutScheduleScreen
import com.fitmate.presentation.home.HomeMessageEffect
import com.fitmate.presentation.home.HomeScreen
import com.fitmate.presentation.home.HomeViewModel
import com.fitmate.presentation.home.WatchSyncResultScreen
import com.fitmate.presentation.mypage.AiTrainerSettingsScreen
import com.fitmate.presentation.mypage.AppSettingsScreen
import com.fitmate.presentation.mypage.BodyProfileScreen
import com.fitmate.presentation.mypage.HomeEquipmentEditScreen
import com.fitmate.presentation.mypage.MyPageScreen
import com.fitmate.presentation.mypage.WorkoutEnvironmentScreen
import com.fitmate.presentation.record.RecordScreen

import com.fitmate.presentation.routine.AiHandsOffSetupScreen
import com.fitmate.presentation.routine.ExerciseConfigurationScreen
import com.fitmate.presentation.routine.ManualDaySelectionScreen
import com.fitmate.presentation.routine.ManualExerciseSelectionScreen
import com.fitmate.presentation.routine.RoutineAiLoadingScreen
import com.fitmate.presentation.routine.RoutineAiReasonScreen
import com.fitmate.presentation.routine.RoutineAiResultScreen
import com.fitmate.presentation.routine.RoutineCreationMethodScreen
import com.fitmate.presentation.routine.RoutineDayPreviewScreen
import com.fitmate.presentation.routine.ManualDayDetailScreen
import com.fitmate.presentation.routine.RoutinePlaceChangeScreen
import com.fitmate.presentation.routine.RoutineScheduleChangeScreen
import com.fitmate.presentation.routine.RoutineCreateEventEffect
import com.fitmate.presentation.routine.RoutineCreateUiState
import com.fitmate.presentation.routine.RoutineCreateViewModel
import com.fitmate.presentation.routine.RoutineEventEffect
import com.fitmate.presentation.routine.RoutineScreen
import com.fitmate.presentation.routine.RoutineViewModel
import com.fitmate.presentation.routine.WorkoutDetailScreen
import com.fitmate.presentation.workout.SelectableExercise
import com.fitmate.presentation.workout.WorkoutLocationScreen
import com.fitmate.presentation.workout.WorkoutPlace
import com.fitmate.presentation.workout.WorkoutRecommendationLoadingScreen
import com.fitmate.presentation.workout.WorkoutRecommendationScreen
import com.fitmate.presentation.workout.TodayWorkoutScreen
import com.fitmate.presentation.workout.WorkoutExerciseSelectScreen
import com.fitmate.presentation.workout.WorkoutExerciseSettingScreen
import com.fitmate.presentation.workout.WorkoutProgressScreen
import com.fitmate.presentation.workout.WorkoutWeightSettingScreen
import com.fitmate.presentation.workout.WorkoutAnalysisScreen
import com.fitmate.presentation.workout.WorkoutAnalysisItem


@Composable
fun FitMateNavHost() {

    val navController = rememberNavController()

    // 현재 선택된 화면 확인
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    // 탭 화면과 탭 안의 상세 화면(오늘의 운동)에서 하단 탭바를 보여준다
    val bottomBarTabKey = when {
        bottomNavItems.any { it.key == currentRoute } -> currentRoute
        currentRoute == Route.ROUTINE_WORKOUT_DETAIL -> Route.ROUTINE
        currentRoute == Route.ROUTINE_MANUAL_DAY_DETAIL -> Route.ROUTINE
        else -> null
    }
    val showBottomBar = bottomBarTabKey != null

    // 루틴 탭에서 연 요일 상세 (요일 문구)
    var routineDetailDayLabel by remember { mutableStateOf("월") }
    var mypageEquipmentEditTitle by remember { mutableStateOf("홈트 기구 편집") }

    var selectedWorkoutTitle by remember {
        mutableStateOf("")
    }

    Scaffold(
        bottomBar = {
            if (showBottomBar) {
                FitMateBottomBar(
                    items = bottomNavItems,
                    selectedKey = bottomBarTabKey,
                    onSelect = { item -> navController.navigateToTab(item.key) }
                )
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

            // 홈 / 루틴 탭은 들어올 때마다 서버에서 다시 불러온다
            composable(Route.HOME) {
                val viewModel: HomeViewModel = hiltViewModel()
                val state by viewModel.uiState.collectAsStateWithLifecycle()
                LaunchedEffect(viewModel) { viewModel.refresh() }
                HomeMessageEffect(viewModel)
                HomeScreen(
                    state = state,
                    onStartWorkout = { navController.navigate(Route.WORKOUT) },
                    onAddWorkout = { navController.navigateToTab(Route.ROUTINE) },
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
                val viewModel: RoutineViewModel = hiltViewModel()
                val state by viewModel.uiState.collectAsStateWithLifecycle()
                LaunchedEffect(viewModel) { viewModel.refresh() }
                RoutineEventEffect(viewModel)
                RoutineScreen(
                    routine = state.routine,
                    isLoading = state.isLoading,
                    onCreateRoutine = { navController.navigate(Route.ROUTINE_CREATE_GRAPH) },
                    onOpenDay = { day ->
                        // 쉬는 날은 상세가 없다
                        if (day in state.dayDetails) {
                            routineDetailDayLabel = day
                            navController.navigate(Route.ROUTINE_WORKOUT_DETAIL)
                        }
                    }
                )
            }

            routineCreateGraph(navController)

            // 루틴 탭 위에 열리는 요일 상세. 루틴 탭의 RoutineViewModel을 같이 쓴다
            composable(Route.ROUTINE_WORKOUT_DETAIL) { entry ->
                val routineEntry = remember(entry) { navController.getBackStackEntry(Route.ROUTINE) }
                val viewModel: RoutineViewModel = hiltViewModel(routineEntry)
                val state by viewModel.uiState.collectAsStateWithLifecycle()
                state.dayDetails[routineDetailDayLabel]?.let { detail ->
                    WorkoutDetailScreen(
                        detail = detail,
                        isToday = routineDetailDayLabel == state.todayLabel,
                        placeLabel = state.placeLabel,
                        onBack = { navController.popBackStack() },
                        // 오늘 운동 장소 변경은 오늘의 운동 장소 변경 화면에서 한다
                        onChangePlace = { navController.navigate(Route.WORKOUT_LOCATION) },
                        onStartWorkout = { navController.navigate(Route.WORKOUT) }
                    )
                }
            }

            // 오늘의 운동
            composable(Route.WORKOUT) {
                TodayWorkoutScreen(
                    onChangeLocation = {
                        navController.navigate(Route.WORKOUT_LOCATION)
                    },

                    onWeightSettingClick = { index ->
                        val exerciseId = when (index) {
                            0 -> 1  // 벤치프레스
                            2 -> 2  // 체스트프레스
                            else -> null
                        }

                        exerciseId?.let { id ->
                            navController.navigate(
                                "${Route.WORKOUT_WEIGHT_SETTING}/$id"
                            )
                        }
                    },

                    onStartWorkout = {
                        navController.navigate(Route.WORKOUT_PROGRESS)
                    }
                )
            }


            composable(Route.WORKOUT_LOCATION) {

                WorkoutLocationScreen(
                    onBack = {
                        navController.popBackStack()
                    },

                    onConfirm = { place, gym ->

                        if (place == WorkoutPlace.GYM && gym == "Fit Gym") {

                            // 기존 장소 그대로 → 오늘의 운동으로 복귀
                            navController.popBackStack()

                        } else {

                            // 운동 장소 변경 → AI 루틴 재추천 로딩
                            navController.navigate(
                                Route.WORKOUT_RECOMMENDATION_LOADING
                            )
                        }
                    }
                )
            }

            composable(Route.WORKOUT_RECOMMENDATION_LOADING) {
                WorkoutRecommendationLoadingScreen(
                    onComplete = {
                        navController.navigate(Route.WORKOUT_RECOMMENDATION) {
                            popUpTo(Route.WORKOUT_RECOMMENDATION_LOADING) {
                                inclusive = true
                            }
                        }
                    }
                )
            }

            composable(Route.WORKOUT_RECOMMENDATION) {
                WorkoutRecommendationScreen(
                    onBack = {
                        navController.popBackStack()
                    },
                    onPreviewClick = {
                        navController.navigate(Route.WORKOUT_PREVIEW)
                    },
                    onStartWorkout = {
                        navController.navigate(Route.WORKOUT_PROGRESS)
                    },
                    onEditClick = {
                        navController.navigate(Route.WORKOUT_EXERCISE_SELECT)
                    }
                )
            }

            composable(Route.WORKOUT_PREVIEW) {
                TodayWorkoutScreen(
                    onWeightSettingClick = { index ->
                        navController.navigate(
                            "${Route.WORKOUT_WEIGHT_SETTING}/$index"
                        )
                    }
                )
            }

            composable(Route.WORKOUT_EXERCISE_SELECT) {
                WorkoutExerciseSelectScreen(
                    onBack = {
                        navController.popBackStack()
                    },
                    onNext = { selectedExercises ->
                        navController.currentBackStackEntry
                            ?.savedStateHandle
                            ?.set(
                                "selected_exercise_ids",
                                selectedExercises.map { it.id }
                            )

                        navController.navigate(Route.WORKOUT_EXERCISE_SETTING)
                    }
                )
            }

            composable(Route.WORKOUT_EXERCISE_SETTING) {
                val selectedIds = navController.previousBackStackEntry
                    ?.savedStateHandle
                    ?.get<List<Int>>("selected_exercise_ids")
                    .orEmpty()

                val selectedExercises = listOf(
                    SelectableExercise(1, "벤치 프레스", "가슴", "프리웨이트"),
                    SelectableExercise(2, "인클라인 덤벨프레스", "가슴", "프리웨이트"),
                    SelectableExercise(3, "숄더 프레스", "어깨", "프리웨이트"),
                    SelectableExercise(4, "체스트 프레스", "가슴", "머신/케이블"),
                    SelectableExercise(5, "레그 프레스", "하체", "머신/케이블"),
                    SelectableExercise(6, "랫풀다운", "등", "머신/케이블"),
                    SelectableExercise(7, "줄넘기", "유산소", "전신"),
                    SelectableExercise(8, "러닝머신", "유산소", "전신"),
                    SelectableExercise(9, "바벨 컬", "이두", "프리웨이트")
                ).filter { it.id in selectedIds }

                WorkoutExerciseSettingScreen(
                    selectedExercises = selectedExercises,
                    onBack = {
                        navController.popBackStack()
                    },
                    onComplete = { settings ->
                        navController.navigate(Route.WORKOUT_PREVIEW)
                    }
                )
            }

            composable(
                route = "${Route.WORKOUT_WEIGHT_SETTING}/{exerciseId}",
                arguments = listOf(
                    navArgument("exerciseId") {
                        type = NavType.IntType
                    }
                )
            ) { backStackEntry ->

                val exerciseId = backStackEntry.arguments
                    ?.getInt("exerciseId") ?: 0

                WorkoutWeightSettingScreen(
                    initialExerciseId = exerciseId,
                    onBack = {
                        navController.popBackStack()
                    },
                    onComplete = { weights ->
                        navController.navigate(Route.WORKOUT_PROGRESS)
                    }
                )
            }

            // 운동 수행 화면
            composable(Route.WORKOUT_PROGRESS) {
                WorkoutProgressScreen(
                    onBack = {
                        navController.popBackStack()
                    },
                    onFinish = { _ ->
                        // 운동 조정 적용 완료 후 홈으로 이동하고 운동 화면 기록 제거
                        navController.navigate(Route.HOME) {
                            popUpTo(Route.WORKOUT_PROGRESS) { inclusive = true }
                            launchSingleTop = true
                        }
                    }
                )
            }


            composable(Route.RECORD) {
                RecordScreen(
                    onOpenWorkoutDetail = { title ->
                        selectedWorkoutTitle = title
                        navController.navigate(Route.RECORD_WORKOUT_ANALYSIS)
                    }
                )
            }

            composable(Route.RECORD_WORKOUT_ANALYSIS) {
                WorkoutAnalysisScreen(
                    items = listOf(
                        WorkoutAnalysisItem(
                            name = "벤치프레스",
                            weightKg = 30,
                            reps = 12,
                            totalSets = 3,
                            completedSets = 3,
                            difficulty = "적당함"
                        ),
                        WorkoutAnalysisItem(
                            name = "숄더프레스",
                            weightKg = 15,
                            reps = 12,
                            totalSets = 3,
                            completedSets = 2,
                            difficulty = "어려움"
                        ),
                        WorkoutAnalysisItem(
                            name = "체스트프레스",
                            weightKg = 20,
                            reps = 12,
                            totalSets = 3,
                            completedSets = 3,
                            difficulty = "쉬움"
                        )
                    ),
                    totalSeconds = 3300L,
                    restSeconds = 600L,
                    hasNextRoutine = false,
                    onBack = {
                        navController.popBackStack()
                    },
                    onNext = {
                        navController.popBackStack()
                    }
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
                val viewModel: LoginViewModel = hiltViewModel()
                val isLoading by viewModel.isLoading.collectAsStateWithLifecycle()
                LoginEventEffect(viewModel) {
                    navController.navigate(Route.HOME) {
                        popUpTo(Route.LOGIN) { inclusive = true }
                    }
                }
                LoginScreen(
                    onNavigateToSignup = { navController.navigate(Route.SIGNUP_GRAPH) },
                    onLogin = viewModel::login,
                    isLoading = isLoading
                )
            }

            signupGraph(navController)
        }
    }
}

// 루틴 만들기 화면 (방식 선택 ~ 저장). 그래프 범위의 RoutineCreateViewModel 하나에 조건 / 추천 결과 / 초안을 모은다
private fun NavGraphBuilder.routineCreateGraph(navController: NavHostController) {
    navigation(startDestination = Route.ROUTINE_CREATE_METHOD, route = Route.ROUTINE_CREATE_GRAPH) {

        routineCreateComposable(navController, Route.ROUTINE_CREATE_METHOD) { _, _ ->
            RoutineCreationMethodScreen(
                onBack = { navController.popBackStack() },
                onAiHandsOff = { navController.navigate(Route.ROUTINE_AI_HANDSOFF_SETUP) },
                onAiTogether = { navController.navigate(Route.ROUTINE_MANUAL_DAY_SELECT) }
            )
        }

        routineCreateComposable(navController, Route.ROUTINE_AI_HANDSOFF_SETUP) { viewModel, state ->
            LaunchedEffect(viewModel) { viewModel.ensureLoaded() }
            state.condition?.let { condition ->
                AiHandsOffSetupScreen(
                    condition = condition,
                    onBack = { navController.popBackStack() },
                    onEditSchedule = { navController.navigate(Route.ROUTINE_SCHEDULE_CHANGE) },
                    onEditPlace = { navController.navigate(Route.ROUTINE_PLACE_CHANGE) },
                    onGenerate = { navController.navigate(Route.ROUTINE_AI_LOADING) }
                )
            }
        }

        routineCreateComposable(navController, Route.ROUTINE_PLACE_CHANGE) { viewModel, state ->
            state.condition?.let { condition ->
                RoutinePlaceChangeScreen(
                    condition = condition,
                    savedGyms = state.savedGyms,
                    onBack = { navController.popBackStack() },
                    onDone = { updated ->
                        viewModel.updateCondition(updated)
                        navController.popBackStack()
                    }
                )
            }
        }

        routineCreateComposable(navController, Route.ROUTINE_SCHEDULE_CHANGE) { viewModel, state ->
            state.condition?.let { condition ->
                RoutineScheduleChangeScreen(
                    condition = condition,
                    onBack = { navController.popBackStack() },
                    onDone = { updated ->
                        viewModel.updateCondition(updated)
                        navController.popBackStack()
                    }
                )
            }
        }

        routineCreateComposable(
            navController,
            Route.ROUTINE_AI_LOADING,
            onRecommendationReady = {
                navController.navigate(Route.ROUTINE_AI_RESULT) {
                    popUpTo(Route.ROUTINE_AI_LOADING) { inclusive = true }
                }
            },
            onLoadingFailed = { navController.popBackStack() }
        ) { viewModel, _ ->
            LaunchedEffect(viewModel) { viewModel.recommendWeeklyRoutine() }
            RoutineAiLoadingScreen()
        }

        routineCreateComposable(navController, Route.ROUTINE_AI_RESULT) { viewModel, state ->
            state.aiResult?.let { result ->
                RoutineAiResultScreen(
                    tags = result.tags,
                    summary = result.summary,
                    days = result.days,
                    onOpenDay = { day ->
                        viewModel.openPreview(day)
                        navController.navigate(Route.ROUTINE_AI_DAY_PREVIEW)
                    },
                    onUseRoutine = viewModel::saveRecommendedRoutine,
                    onWhy = { navController.navigate(Route.ROUTINE_AI_REASON) },
                    onRegenerate = {
                        navController.navigate(Route.ROUTINE_AI_LOADING) {
                            popUpTo(Route.ROUTINE_AI_RESULT) { inclusive = true }
                        }
                    }
                )
            }
        }

        routineCreateComposable(navController, Route.ROUTINE_AI_DAY_PREVIEW) { _, state ->
            state.aiResult?.dayDetails?.get(state.previewDay)?.let { detail ->
                RoutineDayPreviewScreen(
                    detail = detail,
                    onBack = { navController.popBackStack() }
                )
            }
        }

        routineCreateComposable(navController, Route.ROUTINE_AI_REASON) { _, state ->
            state.aiResult?.let { result ->
                RoutineAiReasonScreen(
                    referenceInfo = result.referenceInfo,
                    compositionReasons = result.compositionReasons,
                    dayReasons = result.dayReasons,
                    onBack = { navController.popBackStack() },
                    onConfirm = { navController.popBackStack() }
                )
            }
        }

        routineCreateComposable(navController, Route.ROUTINE_MANUAL_DAY_SELECT) { viewModel, state ->
            LaunchedEffect(viewModel) { viewModel.ensureLoaded() }
            ManualDaySelectionScreen(
                draft = state.draft,
                onBack = { navController.popBackStack() },
                onDayClick = { day ->
                    viewModel.startEditingDay(day)
                    navController.navigate(Route.ROUTINE_MANUAL_EXERCISE_SELECT)
                },
                onOpenDay = { day ->
                    viewModel.openDay(day)
                    navController.navigate(Route.ROUTINE_MANUAL_DAY_DETAIL)
                },
                onSave = {
                    if (viewModel.canSaveManualRoutine()) navController.navigate(Route.ROUTINE_MANUAL_SAVING)
                }
            )
        }

        routineCreateComposable(navController, Route.ROUTINE_MANUAL_EXERCISE_SELECT) { viewModel, state ->
            // 운동 장소를 불러온 뒤에 그 장소에서 할 수 있는 운동을 불러온다
            LaunchedEffect(state.condition) { viewModel.loadAvailableExercises() }
            ManualExerciseSelectionScreen(
                exercises = state.searchableExercises,
                initialSelected = state.selectedExerciseNames,
                onBack = { navController.popBackStack() },
                onAiRecommend = { navController.navigate(Route.ROUTINE_MANUAL_AI_LOADING) },
                onNext = { exercises ->
                    viewModel.configureExercises(exercises)
                    navController.navigate(Route.ROUTINE_EXERCISE_CONFIG)
                }
            )
        }

        // AI 추천이 끝나면 추천 운동이 선택된 검색 화면으로 돌아간다
        routineCreateComposable(
            navController,
            Route.ROUTINE_MANUAL_AI_LOADING,
            onAiExercisesReady = {
                navController.navigate(Route.ROUTINE_MANUAL_EXERCISE_SELECT) {
                    popUpTo(Route.ROUTINE_MANUAL_EXERCISE_SELECT) { inclusive = true }
                }
            },
            onLoadingFailed = { navController.popBackStack() }
        ) { viewModel, _ ->
            LaunchedEffect(viewModel) { viewModel.recommendExercises() }
            RoutineAiLoadingScreen()
        }

        routineCreateComposable(navController, Route.ROUTINE_EXERCISE_CONFIG) { viewModel, state ->
            ExerciseConfigurationScreen(
                settings = state.configSettings,
                recommendations = state.recommendations,
                onSettingChange = viewModel::onSettingChange,
                onBack = { navController.popBackStack() },
                onDone = {
                    if (viewModel.finishConfiguration()) {
                        navController.navigate(Route.ROUTINE_MANUAL_DAY_SELECT) {
                            popUpTo(Route.ROUTINE_MANUAL_DAY_SELECT) { inclusive = false }
                            launchSingleTop = true
                        }
                    }
                }
            )
        }

        routineCreateComposable(navController, Route.ROUTINE_MANUAL_DAY_DETAIL) { _, state ->
            ManualDayDetailScreen(
                dayLabel = state.editingDay,
                exercises = state.draft.days[state.editingDay].orEmpty(),
                onBack = { navController.popBackStack() }
            )
        }

        // 요일별 AI 요약 후 저장하는 동안 보여준다
        routineCreateComposable(
            navController,
            Route.ROUTINE_MANUAL_SAVING,
            onLoadingFailed = { navController.popBackStack() }
        ) { viewModel, _ ->
            LaunchedEffect(viewModel) { viewModel.saveManualRoutine() }
            RoutineAiLoadingScreen()
        }
    }
}

// routine/create 그래프 범위의 RoutineCreateViewModel과 화면 상태를 넘겨주는 목적지
// 루틴 저장이 끝나면 루틴 만들기 화면을 모두 닫고 나의 루틴으로 돌아간다
private fun NavGraphBuilder.routineCreateComposable(
    navController: NavHostController,
    route: String,
    onRecommendationReady: () -> Unit = {},
    onAiExercisesReady: () -> Unit = {},
    onLoadingFailed: () -> Unit = {},
    content: @Composable (RoutineCreateViewModel, RoutineCreateUiState) -> Unit
) {
    composable(route) { entry ->
        val graphEntry = remember(entry) { navController.getBackStackEntry(Route.ROUTINE_CREATE_GRAPH) }
        val viewModel: RoutineCreateViewModel = hiltViewModel(graphEntry)
        val state by viewModel.uiState.collectAsStateWithLifecycle()
        RoutineCreateEventEffect(
            viewModel,
            onRecommendationReady = onRecommendationReady,
            onAiExercisesReady = onAiExercisesReady,
            onSaved = {
                navController.navigate(Route.ROUTINE) {
                    popUpTo(Route.ROUTINE) { inclusive = false }
                }
            },
            onLoadingFailed = onLoadingFailed
        )
        content(viewModel, state)
    }
}

// 회원가입 단계 화면. 모든 단계가 그래프 범위의 SignupViewModel 하나에 입력값을 모은다
private fun NavGraphBuilder.signupGraph(navController: NavHostController) {
    navigation(startDestination = Route.SIGNUP_CREDENTIALS, route = Route.SIGNUP_GRAPH) {

        // 입력값 확인을 통과했을 때만 다음 화면으로 간다
        fun SignupViewModel.next(step: SignupStep, route: String) {
            if (validate(step)) navController.navigate(route)
        }

        signupComposable(navController, Route.SIGNUP_CREDENTIALS) { viewModel, state ->
            SignupIntroScreen(
                loginId = state.loginId,
                password = state.password,
                passwordConfirm = state.passwordConfirm,
                idCheck = state.idCheck,
                onLoginIdChange = viewModel::onLoginIdChange,
                onPasswordChange = viewModel::onPasswordChange,
                onPasswordConfirmChange = viewModel::onPasswordConfirmChange,
                onCheckDuplicateId = viewModel::checkLoginId,
                onBack = { navController.popBackStack() },
                onNext = { viewModel.next(SignupStep.Account, Route.AI_TRAINER_INTRO) }
            )
        }

        composable(Route.AI_TRAINER_INTRO) {
            AITrainerIntroScreen(
                onNext = { navController.navigate(Route.BASIC_BODY_INFO) }
            )
        }

        signupComposable(navController, Route.BASIC_BODY_INFO) { viewModel, state ->
            BasicBodyInfoScreen(
                gender = state.gender,
                age = state.age,
                height = state.height,
                weight = state.weight,
                onGenderSelect = viewModel::onGenderSelect,
                onAgeChange = viewModel::onAgeChange,
                onHeightChange = viewModel::onHeightChange,
                onWeightChange = viewModel::onWeightChange,
                onBack = { navController.popBackStack() },
                onNext = { viewModel.next(SignupStep.BasicInfo, Route.OPTIONAL_BODY_INFO) }
            )
        }

        signupComposable(navController, Route.OPTIONAL_BODY_INFO) { viewModel, state ->
            OptionalBodyInfoScreen(
                muscleMass = state.skeletalMuscleMass,
                bodyFatPercent = state.bodyFatPercentage,
                bodyFatMass = state.bodyFatMass,
                onMuscleMassChange = viewModel::onSkeletalMuscleMassChange,
                onBodyFatPercentChange = viewModel::onBodyFatPercentageChange,
                onBodyFatMassChange = viewModel::onBodyFatMassChange,
                onBack = { navController.popBackStack() },
                onNext = { viewModel.next(SignupStep.OptionalBodyInfo, Route.EXERCISE_EXPERIENCE) },
                onSkip = {
                    viewModel.skipOptionalBodyInfo()
                    navController.navigate(Route.EXERCISE_EXPERIENCE)
                }
            )
        }

        signupComposable(navController, Route.EXERCISE_EXPERIENCE) { viewModel, state ->
            ExerciseExperienceScreen(
                selectedExperience = state.exerciseLevel,
                selectedStatus = state.currentStatus,
                onExperienceSelect = viewModel::onExerciseLevelSelect,
                onStatusSelect = viewModel::onCurrentStatusSelect,
                onBack = { navController.popBackStack() },
                onNext = { viewModel.next(SignupStep.Experience, Route.WORKOUT_GOAL) }
            )
        }

        signupComposable(navController, Route.WORKOUT_GOAL) { viewModel, state ->
            WorkoutGoalScreen(
                selectedGoal = state.primaryGoal,
                onGoalSelect = viewModel::onPrimaryGoalSelect,
                onBack = { navController.popBackStack() },
                onNext = { viewModel.next(SignupStep.Goal, Route.WORKOUT_METHOD) }
            )
        }

        signupComposable(navController, Route.WORKOUT_METHOD) { viewModel, state ->
            WorkoutMethodScreen(
                goal = state.primaryGoal,
                selectedStrategy = state.goalStrategy,
                onStrategySelect = viewModel::onGoalStrategySelect,
                onBack = { navController.popBackStack() },
                onNext = { viewModel.next(SignupStep.Method, Route.WORKOUT_SCHEDULE) }
            )
        }

        signupComposable(navController, Route.WORKOUT_SCHEDULE) { viewModel, state ->
            WorkoutScheduleScreen(
                selectedDays = state.availableDays,
                selectedDuration = state.durationLabel,
                onDayToggle = viewModel::onDayToggle,
                onDurationSelect = viewModel::onDurationSelect,
                onBack = { navController.popBackStack() },
                onNext = { viewModel.next(SignupStep.Schedule, Route.WORKOUT_ENVIRONMENT) }
            )
        }

        signupComposable(navController, Route.WORKOUT_ENVIRONMENT) { viewModel, state ->
            WorkoutEnvironmentScreen(
                selectedEnv = when (state.location) {
                    ExerciseLocation.GYM -> ENV_GYM
                    ExerciseLocation.HOME -> ENV_HOME
                    ExerciseLocation.OUTDOOR -> ENV_OUTDOOR
                    null -> null
                },
                gymName = state.gymName,
                onEnvSelect = viewModel::onLocationSelect,
                onGymNameChange = viewModel::onGymNameChange,
                onBack = { navController.popBackStack() },
                onNext = {
                    val route = when (state.location) {
                        ExerciseLocation.GYM -> Route.GYM_EQUIPMENT
                        ExerciseLocation.HOME -> Route.HOME_EQUIPMENT
                        else -> Route.OUTDOOR_EQUIPMENT
                    }
                    viewModel.next(SignupStep.Place, route)
                }
            )
        }

        signupComposable(navController, Route.GYM_EQUIPMENT) { viewModel, state ->
            GymEquipmentScreen(
                equipments = state.equipments[ExerciseLocation.GYM].orEmpty(),
                selectedCodes = state.selectedEquipmentCodes,
                isLoading = state.isEquipmentLoading,
                onToggle = viewModel::onEquipmentToggle,
                onRetry = viewModel::retryEquipments,
                onBack = { navController.popBackStack() },
                onNext = { viewModel.next(SignupStep.Equipment, Route.EXERCISE_RESTRICTIONS) }
            )
        }

        signupComposable(navController, Route.HOME_EQUIPMENT) { viewModel, state ->
            HomeEquipmentScreen(
                equipments = state.equipments[ExerciseLocation.HOME].orEmpty(),
                selectedCodes = state.selectedEquipmentCodes,
                isLoading = state.isEquipmentLoading,
                onToggle = viewModel::onEquipmentToggle,
                onRetry = viewModel::retryEquipments,
                onBack = { navController.popBackStack() },
                onNext = { viewModel.next(SignupStep.Equipment, Route.EXERCISE_RESTRICTIONS) }
            )
        }

        signupComposable(navController, Route.OUTDOOR_EQUIPMENT) { viewModel, state ->
            OutdoorEquipmentScreen(
                equipments = state.equipments[ExerciseLocation.OUTDOOR].orEmpty(),
                selectedCodes = state.selectedEquipmentCodes,
                isLoading = state.isEquipmentLoading,
                onToggle = viewModel::onEquipmentToggle,
                onRetry = viewModel::retryEquipments,
                onBack = { navController.popBackStack() },
                onNext = { viewModel.next(SignupStep.Equipment, Route.EXERCISE_RESTRICTIONS) }
            )
        }

        signupComposable(navController, Route.EXERCISE_RESTRICTIONS) { viewModel, state ->
            ExerciseRestrictionsScreen(
                hasConcern = state.hasConcern,
                selectedParts = state.avoidBodyAreas,
                avoidExercises = state.avoidExercises,
                onConcernToggle = viewModel::onConcernToggle,
                onPartToggle = viewModel::onBodyAreaToggle,
                onBack = { navController.popBackStack() },
                onAddExercise = { navController.navigate(Route.EXERCISE_SEARCH) },
                onRemoveExercise = viewModel::onAvoidExerciseRemove,
                onNext = { navController.navigate(Route.EXERCISE_ABILITY) }
            )
        }

        signupComposable(navController, Route.EXERCISE_SEARCH) { viewModel, state ->
            ExerciseSearchScreen(
                exercises = state.searchableExercises,
                initialSelected = state.avoidExercises,
                onBack = { navController.popBackStack() },
                onConfirm = { selected ->
                    viewModel.onAvoidExercisesChange(selected)
                    navController.popBackStack()
                }
            )
        }

        // 회원가입 마지막 단계. 가입 + 자동 로그인이 끝나면 홈으로 가고 로그인 / 회원가입 화면은 스택에서 지운다
        signupComposable(
            navController,
            Route.EXERCISE_ABILITY,
            onCompleted = {
                navController.navigate(Route.HOME) {
                    popUpTo(Route.LOGIN) { inclusive = true }
                }
            },
            onNeedLogin = { navController.popBackStack(Route.LOGIN, inclusive = false) }
        ) { viewModel, state ->
            ExerciseAbilityScreen(
                records = state.recordItems,
                isSubmitting = state.isSubmitting,
                onRecordChange = viewModel::onRecordChange,
                onBack = { navController.popBackStack() },
                onAddRecord = { navController.navigate(Route.EXERCISE_RECORD_SEARCH) },
                onComplete = { viewModel.submit(skipRecords = false) },
                onSkip = { viewModel.submit(skipRecords = true) }
            )
        }

        signupComposable(navController, Route.EXERCISE_RECORD_SEARCH) { viewModel, state ->
            ExerciseSearchScreen(
                exercises = state.searchableExercises,
                initialSelected = state.recordExercises,
                onBack = { navController.popBackStack() },
                onConfirm = { selected ->
                    viewModel.onRecordExercisesChange(selected)
                    navController.popBackStack()
                }
            )
        }
    }
}

// signup 그래프 범위의 SignupViewModel과 화면 상태를 넘겨주는 목적지
private fun NavGraphBuilder.signupComposable(
    navController: NavHostController,
    route: String,
    onCompleted: () -> Unit = {},
    onNeedLogin: () -> Unit = {},
    content: @Composable (SignupViewModel, SignupUiState) -> Unit
) {
    composable(route) { entry ->
        val graphEntry = remember(entry) { navController.getBackStackEntry(Route.SIGNUP_GRAPH) }
        val viewModel: SignupViewModel = hiltViewModel(graphEntry)
        val state by viewModel.uiState.collectAsStateWithLifecycle()
        SignupEventEffect(viewModel, onCompleted = onCompleted, onNeedLogin = onNeedLogin)
        content(viewModel, state)
    }
}

// 하단 탭 이동. 같은 탭이 여러 번 쌓이지 않게 하고, 탭별 화면 상태를 유지 / 복원한다
// 홈은 백스택 루트라서, 저장/복원을 쓰면 직전 탭 스택이 그대로 복원돼 홈으로 돌아오지 않는다
private fun NavHostController.navigateToTab(route: String) {
    if (route == Route.HOME) {
        popBackStack(Route.HOME, inclusive = false)
        return
    }
    navigate(route) {
        launchSingleTop = true
        popUpTo(Route.HOME) { saveState = true }
        restoreState = true
    }
}
