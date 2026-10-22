package com.fitmate.app.navigation

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
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
import com.fitmate.presentation.home.HomeScreen
import com.fitmate.presentation.home.HomeUiState
import com.fitmate.presentation.home.TodayWorkout
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
import com.fitmate.presentation.routine.RoutineDayPreviewScreen
import com.fitmate.presentation.routine.ManualDayDetailScreen
import com.fitmate.presentation.routine.ManualRoutineDraft
import com.fitmate.presentation.routine.AiRecommendedExerciseNames
import com.fitmate.presentation.routine.RoutinePlaceChangeScreen
import com.fitmate.presentation.routine.RoutineScheduleChangeScreen
import com.fitmate.presentation.routine.SampleRoutineCondition
import com.fitmate.presentation.routine.RoutineScreen
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

    // 루틴 수동 생성 위저드 동안 화면 간에 공유되는 선택 상태
    // 루틴 직접 만들기 (AI와 함께 만들기) 동안 화면 간에 공유되는 상태
    var manualRoutineDraft by remember { mutableStateOf(ManualRoutineDraft()) }
    var manualEditingDay by remember { mutableStateOf("월") }
    var manualSelectedExercises by remember { mutableStateOf(listOf<String>()) }
    var routinePreviewDayLabel by remember { mutableStateOf("월") }
    var routineDetailDayLabel by remember { mutableStateOf("월") }
    var routineCondition by remember { mutableStateOf(SampleRoutineCondition) }
    var mypageEquipmentEditTitle by remember { mutableStateOf("홈트 기구 편집") }

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

            // 루틴 생성 API 연동 전이라 홈 / 루틴은 루틴 생성 이전 상태로 보여준다
            composable(Route.HOME) {
                HomeScreen(
                    state = HomeUiState(weekly = null, today = TodayWorkout.None),
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
                RoutineScreen(
                    routine = null,
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
                    condition = routineCondition,
                    onBack = { navController.popBackStack() },
                    onEditSchedule = { navController.navigate(Route.ROUTINE_SCHEDULE_CHANGE) },
                    onEditPlace = { navController.navigate(Route.ROUTINE_PLACE_CHANGE) },
                    onGenerate = { navController.navigate(Route.ROUTINE_AI_LOADING) }
                )
            }

            composable(Route.ROUTINE_PLACE_CHANGE) {
                RoutinePlaceChangeScreen(
                    condition = routineCondition,
                    onBack = { navController.popBackStack() },
                    onDone = { updated ->
                        routineCondition = updated
                        navController.popBackStack()
                    }
                )
            }

            composable(Route.ROUTINE_SCHEDULE_CHANGE) {
                RoutineScheduleChangeScreen(
                    condition = routineCondition,
                    onBack = { navController.popBackStack() },
                    onDone = { updated ->
                        routineCondition = updated
                        navController.popBackStack()
                    }
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
                    onOpenDay = { day ->
                        routinePreviewDayLabel = day
                        navController.navigate(Route.ROUTINE_AI_DAY_PREVIEW)
                    },
                    onUseRoutine = {
                        navController.navigate(Route.ROUTINE) {
                            popUpTo(Route.ROUTINE) { inclusive = false }
                        }
                    },
                    onWhy = { navController.navigate(Route.ROUTINE_AI_REASON) },
                    onRegenerate = { navController.navigate(Route.ROUTINE_AI_LOADING) }
                )
            }

            composable(Route.ROUTINE_AI_DAY_PREVIEW) {
                RoutineDayPreviewScreen(
                    dayLabel = routinePreviewDayLabel,
                    onBack = { navController.popBackStack() }
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
                    draft = manualRoutineDraft,
                    onBack = { navController.popBackStack() },
                    onDayClick = { day ->
                        manualEditingDay = day
                        manualSelectedExercises = manualRoutineDraft.days[day].orEmpty().map { it.name }
                        navController.navigate(Route.ROUTINE_MANUAL_EXERCISE_SELECT)
                    },
                    onOpenDay = { day ->
                        manualEditingDay = day
                        navController.navigate(Route.ROUTINE_MANUAL_DAY_DETAIL)
                    },
                    onSave = {
                        navController.navigate(Route.ROUTINE) {
                            popUpTo(Route.ROUTINE) { inclusive = false }
                        }
                    }
                )
            }

            composable(Route.ROUTINE_MANUAL_EXERCISE_SELECT) {
                ManualExerciseSelectionScreen(
                    initialSelected = manualSelectedExercises,
                    onBack = { navController.popBackStack() },
                    onAiRecommend = { navController.navigate(Route.ROUTINE_MANUAL_AI_LOADING) },
                    onNext = { exercises ->
                        manualSelectedExercises = exercises
                        navController.navigate(Route.ROUTINE_EXERCISE_CONFIG)
                    }
                )
            }

            // AI 추천이 끝나면 추천 운동이 선택된 검색 화면으로 돌아간다
            composable(Route.ROUTINE_MANUAL_AI_LOADING) {
                RoutineAiLoadingScreen(
                    onComplete = {
                        manualSelectedExercises = AiRecommendedExerciseNames
                        navController.navigate(Route.ROUTINE_MANUAL_EXERCISE_SELECT) {
                            popUpTo(Route.ROUTINE_MANUAL_EXERCISE_SELECT) { inclusive = true }
                        }
                    }
                )
            }

            composable(Route.ROUTINE_EXERCISE_CONFIG) {
                ExerciseConfigurationScreen(
                    exercises = manualSelectedExercises,
                    initialSettings = manualRoutineDraft.days[manualEditingDay].orEmpty(),
                    onBack = { navController.popBackStack() },
                    onDone = { settings ->
                        manualRoutineDraft = manualRoutineDraft.copy(days = manualRoutineDraft.days + (manualEditingDay to settings))
                        navController.navigate(Route.ROUTINE_MANUAL_DAY_SELECT) {
                            popUpTo(Route.ROUTINE_MANUAL_DAY_SELECT) { inclusive = false }
                            launchSingleTop = true
                        }
                    }
                )
            }

            composable(Route.ROUTINE_MANUAL_DAY_DETAIL) {
                ManualDayDetailScreen(
                    dayLabel = manualEditingDay,
                    exercises = manualRoutineDraft.days[manualEditingDay].orEmpty(),
                    onBack = { navController.popBackStack() }
                )
            }

            composable(Route.ROUTINE_WORKOUT_DETAIL) {
                WorkoutDetailScreen(
                    dayLabel = routineDetailDayLabel,
                    gymName = routineCondition.placeLabel,
                    onBack = { navController.popBackStack() },
                    onChangePlace = { navController.navigate(Route.ROUTINE_PLACE_CHANGE) },
                    onStartWorkout = { navController.navigate(Route.WORKOUT) }
                )
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
