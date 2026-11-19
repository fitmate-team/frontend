package com.fitmate.app.navigation

import android.util.Log
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.foundation.background
import androidx.compose.ui.graphics.Color
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.TextButton
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.ui.Alignment
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
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
import com.fitmate.presentation.routine.ManualRoutineDraft
import com.fitmate.presentation.routine.AiRecommendedExerciseNames
import com.fitmate.presentation.routine.RoutinePlaceChangeScreen
import com.fitmate.presentation.routine.RoutineScheduleChangeScreen
import com.fitmate.presentation.routine.SampleRoutineCondition
import com.fitmate.presentation.routine.RoutineScreen
import com.fitmate.presentation.routine.WorkoutDetailScreen
import com.fitmate.presentation.workout.setup.SelectableExercise
import com.fitmate.presentation.workout.setup.WorkoutLocationScreen
import com.fitmate.presentation.workout.setup.WorkoutPlace
import com.fitmate.presentation.workout.recommendatio.WorkoutRecommendationLoadingScreen
import com.fitmate.presentation.workout.recommendatio.WorkoutRecommendationScreen
import com.fitmate.presentation.workout.today.TodayWorkoutScreen
import com.fitmate.presentation.workout.setup.WorkoutWeightExercise
import com.fitmate.presentation.workout.setup.WorkoutExerciseSelectScreen
import com.fitmate.presentation.workout.setup.WorkoutExerciseSettingScreen
import com.fitmate.presentation.workout.session.WorkoutProgressScreen
import com.fitmate.presentation.workout.setup.WorkoutWeightSettingScreen
import com.fitmate.presentation.workout.result.WorkoutAnalysisScreen
import com.fitmate.presentation.workout.result.WorkoutAnalysisItem
import com.fitmate.presentation.workout.setup.WorkoutTimeChangeScreen

import com.fitmate.presentation.mypage.BodyProfileEditScreen
import com.fitmate.presentation.mypage.WorkoutEnvironmentSelectionScreen
import com.fitmate.presentation.mypage.GymListScreen
import com.fitmate.presentation.mypage.GymEditScreen
import com.fitmate.presentation.mypage.GymInfo
import com.fitmate.presentation.mypage.OutdoorEquipmentEditScreen
import com.fitmate.presentation.mypage.initialGymList

import com.fitmate.presentation.workout.today.WorkoutViewModel
import com.fitmate.presentation.workout.today.toUiState
import com.fitmate.presentation.workout.session.WorkoutSessionViewModel
import com.fitmate.presentation.workout.session.WorkoutSessionResult
import com.fitmate.domain.model.FinishWorkout
import com.fitmate.presentation.workout.session.WorkoutProgressExercise
import com.fitmate.presentation.workout.setup.ExerciseSetting
import com.fitmate.domain.model.WorkoutExercise
import com.fitmate.domain.model.ApplyWorkoutAdjustments
import com.fitmate.domain.model.ApplyWorkoutAdjustmentExercise
import java.time.Instant



private const val WORKOUT_PROGRESS_WITH_ID =
    "workout_progress/{workoutRecordId}/{dailyRoutineId}"

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

    var selectedWorkoutTitle by remember {
        mutableStateOf("")
    }

    var selectedWorkoutMinutes by remember {
        mutableIntStateOf(20)}

    var configuredWorkoutExercises by remember {
        mutableStateOf<List<ExerciseSetting>>(emptyList())
    }

    // 마이페이지
    var avoidedExercises by remember {
        mutableStateOf(listOf<String>())
    }
    var gyms by remember {
        mutableStateOf(initialGymList)
    }
    var homeEquipment by remember {
        mutableStateOf(
            listOf(
                "덤벨",
                "케틀벨",
                "저항 밴드",
                "파워랙/스쿼트랙",
                "줄넘기",
                "엘립티컬"
            )
        )
    }

    var outdoorEquipment by remember {
        mutableStateOf(
            listOf("줄넘기")
        )
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

            composable("workout_time_change") {
                WorkoutTimeChangeScreen(
                    initialMinutes = selectedWorkoutMinutes,
                    onBack = {
                        navController.popBackStack()
                    },
                    onNext = { minutes ->
                        selectedWorkoutMinutes = minutes
                        navController.navigate(Route.WORKOUT_LOCATION)
                    }
                )
            }

            // 중량 설정 화면
            composable(Route.WORKOUT_WEIGHT_SETTING) { backStackEntry ->

                // 오늘의 운동 ViewModel 공유
                val workoutEntry = remember(backStackEntry) {
                    navController.getBackStackEntry(Route.WORKOUT)
                }

                val workoutViewModel: WorkoutViewModel = hiltViewModel(workoutEntry)
                val workoutState by workoutViewModel.uiState.collectAsStateWithLifecycle()

                // 운동 가이드 ViewModel
                val guideViewModel: WorkoutSessionViewModel = hiltViewModel()
                val guideState by guideViewModel.uiState.collectAsStateWithLifecycle()

                val workout = workoutState.workout

                // 중량 설정이 필요한 운동만 추출
                val requiredExercises = workout?.exercises
                    ?.withIndex()
                    ?.filter { (_, exercise) ->
                        exercise.weightRequired && exercise.weightKg == null
                    }
                    .orEmpty()

                // exerciseCode가 아닌 exerciseId로 조회
                val exerciseIds = requiredExercises
                    .mapNotNull { (_, exercise) -> exercise.exerciseId }
                    .distinct()

                // 운동 가이드 API 조회
                LaunchedEffect(exerciseIds) {
                    if (exerciseIds.isNotEmpty()) {
                        guideViewModel.loadExerciseGuidesByIds(exerciseIds)
                    }
                }

                // 운동 가이드 API가 아직 완료되지 않은 상태도 로딩으로 처리
                val isGuideLoading = exerciseIds.isNotEmpty() &&
                        (
                                guideState.isGuideLoading ||
                                        exerciseIds.any { id ->
                                            id !in guideState.exerciseGuidesById
                                        } && guideState.guideErrorMessage == null
                                )

                // 실제 API 데이터로 화면 모델 구성
                val weightExercises = requiredExercises.map { (index, exercise) ->

                    val guide = guideState.exerciseGuidesById[exercise.exerciseId]

                    WorkoutWeightExercise(
                        id = index + 1,

                        name = guide?.exerciseName
                            ?: exercise.exerciseName
                            ?: exercise.exerciseCode,

                        description =
                            "처음 사용하는 중량을 설정해볼게요.\n" +
                                    "가벼운 무게부터 직접 수행해보고 적절한 중량을 입력해주세요.",

                        guide = guide?.guide.orEmpty()
                            .replace("\\n", "\n"),

                        bodyParts = listOfNotNull(guide?.primaryMuscle),

                        videoUrl = guide?.videoUrl.orEmpty()
                    )
                }

                // 중량 저장 성공 시 운동 시작
                LaunchedEffect(workoutState.isExerciseSaved) {
                    if (workoutState.isExerciseSaved) {
                        workoutViewModel.clearExerciseSaved()
                        workoutViewModel.startWorkout()
                    }
                }

                WorkoutWeightSettingScreen(
                    initialExerciseId = weightExercises.firstOrNull()?.id ?: 1,
                    exercises = weightExercises,
                    isGuideLoading = isGuideLoading,

                    onBack = {
                        navController.popBackStack()
                    },

                    onComplete = { weights ->
                        workoutViewModel.saveWorkoutWeights(weights)
                    }
                )
            }



            // 오늘의 운동
            composable(Route.WORKOUT) {

                val viewModel: WorkoutViewModel = hiltViewModel()
                val uiState by viewModel.uiState.collectAsStateWithLifecycle()


                LaunchedEffect(uiState.workoutRecordId) {
                    val recordId = uiState.workoutRecordId
                    val dailyRoutineId = uiState.workout?.dailyRoutineId

                    if (recordId != null && dailyRoutineId != null) {
                        navController.navigate(
                            "workout_progress/$recordId/$dailyRoutineId"
                        ) {
                            launchSingleTop = true
                        }
                    }
                }



                when {
                    uiState.isLoading -> {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator()
                        }
                    }

                    uiState.errorMessage != null -> {
                        Column(
                            modifier = Modifier.fillMaxSize(),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Text(
                                text = uiState.errorMessage
                                    ?: "운동 정보를 불러오지 못했어요."
                            )

                            Button(
                                onClick = viewModel::loadTodayWorkout
                            ) {
                                Text("다시 시도")
                            }
                        }
                    }


                    uiState.workout != null -> {
                        Box(
                            modifier = Modifier.fillMaxSize()
                        ) {
                            TodayWorkoutScreen(
                                state = uiState.workout!!.toUiState(),

                                onChangeLocation = {
                                    navController.navigate("workout_time_change")
                                },

                                onWeightSettingClick = { index ->
                                    navController.navigate(
                                        Route.WORKOUT_WEIGHT_SETTING
                                    )
                                },

                                onStartWorkout = {
                                    viewModel.startWorkout()
                                }
                            )

                            // 운동 시작 API 호출 중 로딩
                            if (uiState.isStarting) {
                                Box(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .background(
                                            Color.White.copy(alpha = 0.7f)
                                        ),
                                    contentAlignment = Alignment.Center
                                ) {
                                    CircularProgressIndicator()
                                }
                            }

                            // 운동 시작 실패 시 오류 표시
                            uiState.startErrorMessage?.let { message ->
                                AlertDialog(
                                    onDismissRequest = viewModel::clearStartError,
                                    title = {
                                        Text("운동 시작 실패")
                                    },
                                    text = {
                                        Text(message)
                                    },
                                    confirmButton = {
                                        TextButton(
                                            onClick = viewModel::clearStartError
                                        ) {
                                            Text("확인")
                                        }
                                    }
                                )
                            }
                        }
                    }


                    else -> {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator()
                        }
                    }
                }
            }




            composable(Route.WORKOUT_LOCATION) { backStackEntry ->

                val workoutEntry = remember(backStackEntry) {
                    navController.getBackStackEntry(Route.WORKOUT)
                }

                val viewModel: WorkoutViewModel = hiltViewModel(workoutEntry)
                val uiState by viewModel.uiState.collectAsStateWithLifecycle()

                LaunchedEffect(viewModel) {
                    viewModel.loadWorkoutEnvironments()
                }

                LaunchedEffect(uiState.isLocationUpdated) {
                    if (uiState.isLocationUpdated) {
                        viewModel.clearLocationUpdated()
                        navController.popBackStack(Route.WORKOUT, inclusive = false)
                    }
                }

                val environments = uiState.environments?.environments.orEmpty()

                val currentEnvironmentId =
                    uiState.workout?.workoutEnvironmentId
                        ?: uiState.environments?.scheduledEnvironmentId

                val currentEnvironment = environments.firstOrNull {
                    it.id == currentEnvironmentId
                }

                val currentPlace = when (
                    currentEnvironment?.locationType
                        ?: uiState.workout?.locationType
                ) {
                    "HOME" -> WorkoutPlace.HOME
                    "OUTDOOR" -> WorkoutPlace.OUTDOOR
                    else -> WorkoutPlace.GYM
                }

                val currentGym = currentEnvironment?.gymName
                    ?: uiState.workout?.locationName
                    ?: ""

                val gymEnvironments = environments.filter {
                    it.locationType == "GYM"
                }

                val gymNames = gymEnvironments.mapNotNull {
                    it.gymName
                }

                var pendingEnvironmentId by remember {
                    mutableStateOf<Long?>(null)
                }

                var showRerecommendationDialog by remember {
                    mutableStateOf(false)
                }

                LaunchedEffect(uiState.locationValidation) {
                    val validation = uiState.locationValidation ?: return@LaunchedEffect
                    val environmentId = pendingEnvironmentId ?: return@LaunchedEffect

                    if (validation.workoutEnvironmentId != environmentId) {
                        return@LaunchedEffect
                    }

                    if (validation.requiresRerecommendation) {
                        showRerecommendationDialog = true
                        viewModel.clearLocationValidation()
                        pendingEnvironmentId = null
                    } else {
                        viewModel.clearLocationValidation()
                        pendingEnvironmentId = null
                        viewModel.updateWorkoutLocation(environmentId)
                    }
                }



                WorkoutLocationScreen(
                    currentGym = currentGym,
                    gymOptions = gymNames,
                    initialPlace = currentPlace,
                    isLoading = uiState.isLocationLoading ||
                            uiState.isLocationSaving,

                    onBack = {
                        navController.popBackStack()
                    },

                    onConfirm = { place, gymName ->

                        val selectedEnvironment = environments.firstOrNull {
                            when (place) {
                                WorkoutPlace.GYM ->
                                    it.locationType == "GYM" &&
                                            it.gymName == gymName

                                WorkoutPlace.HOME ->
                                    it.locationType == "HOME"

                                WorkoutPlace.OUTDOOR ->
                                    it.locationType == "OUTDOOR"
                            }
                        }

                        if (selectedEnvironment != null) {
                            if (selectedEnvironment.id == currentEnvironmentId) {
                                navController.popBackStack()
                            } else {
                                pendingEnvironmentId = selectedEnvironment.id
                                viewModel.validateWorkoutLocation(
                                    selectedEnvironment.id
                                )
                            }
                        }
                    }
                )

                if (uiState.isLocationLoading || uiState.isLocationSaving) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color.White.copy(alpha = 0.7f)),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }

                uiState.locationErrorMessage?.let { message ->
                    AlertDialog(
                        onDismissRequest = viewModel::clearLocationError,
                        title = { Text("운동 장소 변경 실패") },
                        text = { Text(message) },
                        confirmButton = {
                            TextButton(onClick = viewModel::clearLocationError) {
                                Text("확인")
                            }
                        }
                    )
                }


                if (showRerecommendationDialog) {
                    AlertDialog(
                        onDismissRequest = {
                            showRerecommendationDialog = false
                        },
                        title = {
                            Text("운동 루틴 조정 필요")
                        },
                        text = {
                            Text(
                                "선택한 장소에서는 기존 운동 루틴을 " +
                                        "그대로 수행하기 어려울 수 있어요. " +
                                        "운동 루틴 재추천 기능이 연결된 후 변경할 수 있어요."
                            )
                        },
                        confirmButton = {
                            TextButton(
                                onClick = {
                                    showRerecommendationDialog = false
                                }
                            ) {
                                Text("확인")
                            }
                        }
                    )
                }

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


            composable(Route.WORKOUT_PREVIEW) { backStackEntry ->

                // 오늘의 운동 화면과 동일한 ViewModel 공유
                val workoutEntry = remember(backStackEntry) {
                    navController.getBackStackEntry(Route.WORKOUT)
                }

                val viewModel: WorkoutViewModel = hiltViewModel(workoutEntry)
                val uiState by viewModel.uiState.collectAsStateWithLifecycle()

                LaunchedEffect(uiState.workoutRecordId) {
                    val recordId = uiState.workoutRecordId
                    val dailyRoutineId = uiState.workout?.dailyRoutineId

                    if (recordId != null && dailyRoutineId != null) {
                        navController.navigate(
                            "workout_progress/$recordId/$dailyRoutineId"
                        ) {
                            launchSingleTop = true
                        }
                    }
                }

                when {
                    // 운동 정보 로딩 중
                    uiState.isLoading -> {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator()
                        }
                    }

                    // 운동 정보 조회 실패
                    uiState.errorMessage != null -> {
                        Column(
                            modifier = Modifier.fillMaxSize(),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Text(
                                text = uiState.errorMessage
                                    ?: "운동 정보를 불러오지 못했어요."
                            )

                            Button(
                                onClick = viewModel::loadTodayWorkout
                            ) {
                                Text("다시 시도")
                            }
                        }
                    }

                    // 서버에서 조회한 실제 운동 정보 표시
                    uiState.workout != null -> {
                        Box(
                            modifier = Modifier.fillMaxSize()
                        ) {
                            TodayWorkoutScreen(
                                state = uiState.workout!!.toUiState(),

                                onChangeLocation = {
                                    navController.navigate("workout_time_change")
                                },

                                onWeightSettingClick = {
                                    navController.navigate(
                                        Route.WORKOUT_WEIGHT_SETTING
                                    )
                                },

                                onStartWorkout = {
                                    viewModel.startWorkout()
                                }
                            )

                            // 운동 시작 API 로딩
                            if (uiState.isStarting) {
                                Box(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .background(
                                            Color.White.copy(alpha = 0.7f)
                                        ),
                                    contentAlignment = Alignment.Center
                                ) {
                                    CircularProgressIndicator()
                                }
                            }

                            // 운동 시작 실패
                            uiState.startErrorMessage?.let { message ->
                                AlertDialog(
                                    onDismissRequest = viewModel::clearStartError,
                                    title = {
                                        Text("운동 시작 실패")
                                    },
                                    text = {
                                        Text(message)
                                    },
                                    confirmButton = {
                                        TextButton(
                                            onClick = viewModel::clearStartError
                                        ) {
                                            Text("확인")
                                        }
                                    }
                                )
                            }
                        }
                    }

                    else -> {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("오늘의 운동 정보가 없어요.")
                        }
                    }
                }
            }


            composable(Route.WORKOUT_EXERCISE_SELECT) { backStackEntry ->

                val workoutEntry = remember(backStackEntry) {
                    navController.getBackStackEntry(Route.WORKOUT)
                }

                val viewModel: WorkoutViewModel = hiltViewModel(workoutEntry)
                val uiState by viewModel.uiState.collectAsStateWithLifecycle()

                LaunchedEffect(viewModel) {
                    viewModel.loadExerciseCatalog()
                }

                val exercises = uiState.exerciseCatalog.mapIndexed { index, item ->
                    SelectableExercise(
                        id = index + 1,
                        name = item.nameKo,
                        category = when (item.primaryMuscle.uppercase()) {
                            "CHEST" -> "가슴"
                            "BACK" -> "등"
                            "SHOULDER", "SHOULDERS" -> "어깨"
                            "BICEPS" -> "이두"
                            "LEGS", "LOWER_BODY" -> "하체"
                            "CARDIO" -> "유산소"
                            else -> item.primaryMuscle
                        },
                        equipment = item.exerciseType,
                        exerciseCode = item.exerciseCode
                    )
                }

                when {
                    uiState.isCatalogLoading -> {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator()
                        }
                    }

                    uiState.catalogErrorMessage != null -> {
                        Column(
                            modifier = Modifier.fillMaxSize(),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Text(
                                uiState.catalogErrorMessage
                                    ?: "운동 목록을 불러오지 못했어요."
                            )

                            Button(
                                onClick = viewModel::loadExerciseCatalog
                            ) {
                                Text("다시 시도")
                            }
                        }
                    }

                    else -> {
                        WorkoutExerciseSelectScreen(
                            exercises = exercises,
                            initialSelectedIds = emptySet(),
                            onBack = {
                                navController.popBackStack()
                            },
                            onNext = { selectedExercises ->
                                navController.currentBackStackEntry
                                    ?.savedStateHandle
                                    ?.set(
                                        "selected_exercise_codes",
                                        ArrayList(selectedExercises.map { it.exerciseCode })
                                    )

                                navController.navigate(Route.WORKOUT_EXERCISE_SETTING)
                            }
                        )
                    }
                }
            }



            composable(Route.WORKOUT_EXERCISE_SETTING) { backStackEntry ->

                // 오늘의 운동 화면과 동일한 ViewModel 공유
                val workoutEntry = remember(backStackEntry) {
                    navController.getBackStackEntry(Route.WORKOUT)
                }

                val viewModel: WorkoutViewModel = hiltViewModel(workoutEntry)
                val uiState by viewModel.uiState.collectAsStateWithLifecycle()

                // 이전 운동 선택 화면에서 전달한 운동 코드
                val selectedCodes = navController.previousBackStackEntry
                    ?.savedStateHandle
                    ?.get<ArrayList<String>>("selected_exercise_codes")
                    .orEmpty()

                // 선택한 운동 정보를 화면 모델로 변환
                val selectedExercises = uiState.exerciseCatalog
                    .filter { it.exerciseCode in selectedCodes }
                    .sortedBy { selectedCodes.indexOf(it.exerciseCode) }
                    .mapIndexed { index, item ->
                        SelectableExercise(
                            id = index + 1,
                            name = item.nameKo,
                            category = when (item.primaryMuscle.uppercase()) {
                                "CHEST" -> "가슴"
                                "BACK" -> "등"
                                "SHOULDER", "SHOULDERS" -> "어깨"
                                "BICEPS" -> "이두"
                                "LEGS", "LOWER_BODY" -> "하체"
                                "CARDIO" -> "유산소"
                                else -> item.primaryMuscle
                            },
                            equipment = item.exerciseType,
                            exerciseCode = item.exerciseCode
                        )
                    }

                // PUT 저장 성공 시 미리보기 화면으로 이동
                LaunchedEffect(uiState.isExerciseSaved) {
                    if (uiState.isExerciseSaved) {
                        viewModel.clearExerciseSaved()

                        navController.navigate(Route.WORKOUT_PREVIEW) {
                            launchSingleTop = true
                        }
                    }
                }

                WorkoutExerciseSettingScreen(
                    selectedExercises = selectedExercises,
                    isSaving = uiState.isExerciseSaving,
                    onBack = {
                        if (!uiState.isExerciseSaving) {
                            navController.popBackStack()
                        }
                    },
                    onComplete = { settings ->

                        configuredWorkoutExercises = settings

                        // 화면에서 입력한 운동 설정을 Domain 모델로 변환
                        val exercises = settings.mapNotNull { setting ->
                            val exerciseCode = setting.exercise.exerciseCode

                            if (exerciseCode.isBlank()) {
                                null
                            } else {
                                WorkoutExercise(
                                    exerciseCode = exerciseCode,
                                    weightKg = setting.weight.toIntOrNull(),
                                    reps = setting.reps.toIntOrNull(),
                                    sets = setting.sets.toIntOrNull(),
                                    durationSeconds = null,
                                    restSeconds = setting.restSeconds,
                                    appliedAdjustmentId = null
                                )
                            }
                        }

                        // 운동 코드가 누락되었다면 저장하지 않음
                        if (exercises.size != settings.size) {
                            return@WorkoutExerciseSettingScreen
                        }

                        // PUT API 호출
                        viewModel.saveWorkoutExercises(exercises)
                    }
                )

                uiState.exerciseSaveErrorMessage?.let { message ->
                    AlertDialog(
                        onDismissRequest = viewModel::clearExerciseSaveError,
                        title = {
                            Text("운동 설정 저장 실패")
                        },
                        text = {
                            Text(message)
                        },
                        confirmButton = {
                            TextButton(
                                onClick = viewModel::clearExerciseSaveError
                            ) {
                                Text("확인")
                            }
                        }
                    )
                }

                if (uiState.isExerciseSaving) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color.White.copy(alpha = 0.7f)),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }
            }



            // 운동 수행 화면
            composable(Route.WORKOUT_PROGRESS) {
                WorkoutProgressScreen(
                    onBack = {
                        navController.popBackStack()
                    },
                    onFinish = { _ ->
                        navController.navigate(Route.HOME) {
                            popUpTo(Route.WORKOUT_PROGRESS) {
                                inclusive = true
                            }
                            launchSingleTop = true
                        }
                    }
                )
            }



            // 운동 시작 API 성공 후 이동하는 화면
            composable(
                route = WORKOUT_PROGRESS_WITH_ID,
                arguments = listOf(
                    navArgument("workoutRecordId") {
                        type = NavType.LongType
                    },
                    navArgument("dailyRoutineId") {
                        type = NavType.LongType
                    }
                )
            ) { backStackEntry ->

                // 1. Navigation에서 ID 가져오기
                val workoutRecordId =
                    backStackEntry.arguments?.getLong("workoutRecordId")

                val dailyRoutineId =
                    backStackEntry.arguments?.getLong("dailyRoutineId")

                // 2. ViewModel 연결
                val viewModel: WorkoutSessionViewModel = hiltViewModel()
                val uiState by viewModel.uiState.collectAsStateWithLifecycle()


                val workoutBackStackEntry = remember(backStackEntry) {
                    navController.getBackStackEntry(Route.WORKOUT)
                }

                val workoutViewModel: WorkoutViewModel =
                    hiltViewModel(workoutBackStackEntry)


                val workoutState by workoutViewModel.uiState
                    .collectAsStateWithLifecycle()


                // 현재 루틴의 운동 가이드 조회
                val exerciseCodes = workoutState.workout
                    ?.exercises
                    ?.map { it.exerciseCode }
                    .orEmpty()

                LaunchedEffect(dailyRoutineId, exerciseCodes) {
                    if (dailyRoutineId != null && exerciseCodes.isNotEmpty()) {
                        viewModel.loadExerciseGuides(
                            dailyRoutineId = dailyRoutineId,
                            exerciseCodes = exerciseCodes
                        )
                    }
                }



                val progressExercises = workoutState.workout
                    ?.exercises
                    ?.mapIndexed { index, exercise ->

                        // 운동 카탈로그에서 이름 조회
                        val catalog = workoutState.exerciseCatalog
                            .find { it.exerciseCode == exercise.exerciseCode }

                        // GET API에서 받아온 운동 가이드
                        val guide = uiState.exerciseGuides[exercise.exerciseCode]

                        WorkoutProgressExercise(
                            id = index + 1,
                            name = guide?.exerciseName
                                ?: catalog?.nameKo
                                ?: exercise.exerciseCode,

                            weightKg = exercise.weightKg ?: 0,
                            reps = exercise.reps ?: 0,
                            sets = exercise.sets ?: 0,
                            restSeconds = exercise.restSeconds ?: 0,
                            durationSeconds = exercise.durationSeconds,

                            exerciseCode = exercise.exerciseCode,
                            exerciseId = guide?.exerciseId,

                            guide = guide?.guide.orEmpty()
                                .replace("\\n", "\n"),

                            videoUrl = guide?.videoUrl.orEmpty(),

                            bodyParts = listOfNotNull(
                                guide?.primaryMuscle
                            )
                        )
                    }
                    .orEmpty()


                // 3. 서버 저장 성공 시 홈으로 이동
                LaunchedEffect(uiState.isAdjustmentsApplied) {
                    if (uiState.isAdjustmentsApplied) {
                        navController.navigate(Route.HOME) {
                            popUpTo(Route.HOME) {
                                inclusive = false
                            }
                            launchSingleTop = true
                        }
                    }
                }


                Box(modifier = Modifier.fillMaxSize()) {

                    uiState.guideErrorMessage?.let { message ->
                        AlertDialog(
                            onDismissRequest = viewModel::clearGuideError,
                            title = {
                                Text("운동 가이드 조회 실패")
                            },
                            text = {
                                Text(message)
                            },
                            confirmButton = {
                                TextButton(
                                    onClick = viewModel::clearGuideError
                                ) {
                                    Text("확인")
                                }
                            }
                        )
                    }

                    if (workoutRecordId != null && dailyRoutineId != null) {

                        WorkoutProgressScreen(
                            exercises = progressExercises,
                            isWorkoutSaved = uiState.isFinished,
                            isFinishing = uiState.isFinishing,
                            finishErrorMessage = uiState.errorMessage,

                            recommendation = uiState.recommendation,
                            isRecommendationLoading = uiState.isRecommendationLoading,
                            recommendationErrorMessage = uiState.recommendationErrorMessage,

                            onLoadRecommendation = {
                                if (workoutRecordId != null && uiState.isFinished) {
                                    viewModel.loadWorkoutRecommendations(workoutRecordId)
                                }
                            },

                            onApplyAdjustments = { selectedItems ->
                                val recommendation = uiState.recommendation

                                if (recommendation != null && uiState.isFinished) {
                                    val request = ApplyWorkoutAdjustments(
                                        exercises = recommendation.exercises.mapIndexed { index, exercise ->
                                            val isSelected = selectedItems.any { it.id == index + 1 }

                                            ApplyWorkoutAdjustmentExercise(
                                                exerciseId = exercise.exerciseId,
                                                weightKg = exercise.recommended.weightKg,
                                                reps = exercise.recommended.reps,
                                                setCount = exercise.recommended.setCount,
                                                durationSeconds = exercise.recommended.durationSeconds,
                                                apply = isSelected
                                            )
                                        }
                                    )

                                    viewModel.applyWorkoutRecommendations(
                                        workoutRecordId = workoutRecordId,
                                        request = request
                                    )
                                }
                            },

                            onBack = {
                                navController.popBackStack()
                            },
                            onFinish = { result: WorkoutSessionResult ->

                                val request = FinishWorkout(
                                    dailyRoutineId = dailyRoutineId,
                                    workoutRecordId = workoutRecordId,
                                    endedAt = Instant.now().toString(),
                                    actualExerciseSeconds = result.exerciseSeconds,
                                    actualRestSeconds = result.restSeconds,
                                    minHeartRate = null,
                                    maxHeartRate = null,
                                    exercises = workoutState.workout
                                        ?.exercises
                                        ?.mapIndexed { index, exercise ->
                                            val completedSetCount =
                                                result.completedSets[index + 1] ?: 0

                                            exercise.copy(
                                                sets = completedSetCount
                                            )
                                        }
                                        .orEmpty(),
                                    perceivedDifficulty = result.difficulty.name
                                )

                                viewModel.finishWorkout(request)
                            }
                        )

                        if (uiState.isApplyingAdjustments) {
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .background(Color.White.copy(alpha = 0.7f)),
                                contentAlignment = Alignment.Center
                            ) {
                                CircularProgressIndicator()
                            }
                        }

                        uiState.applyErrorMessage?.let { message ->
                            AlertDialog(
                                onDismissRequest = viewModel::clearApplyError,
                                title = {
                                    Text("운동 강도 저장 실패")
                                },
                                text = {
                                    Text(message)
                                },
                                confirmButton = {
                                    TextButton(onClick = viewModel::clearApplyError) {
                                        Text("확인")
                                    }
                                }
                            )
                        }


                    } else {
                        Text("운동 기록 정보를 찾을 수 없어요.")
                    }

                    // 운동 기록 저장 중 로딩
                    if (uiState.isFinishing) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(
                                    Color.White.copy(alpha = 0.7f)
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator()
                        }
                    }


                    if (uiState.isGuideLoading && !uiState.isFinished) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(Color.White.copy(alpha = 0.7f)),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator()
                        }
                    }

                }
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
                            "운동 프로필" -> {
                                navController.navigate(Route.MYPAGE_BODY_PROFILE)
                            }

                            "내 운동 환경" -> {
                                navController.navigate(Route.MYPAGE_WORKOUT_ENVIRONMENT)
                            }

                            "AI Trainer 설정" -> {
                                navController.navigate(Route.MYPAGE_AI_TRAINER_SETTINGS)
                            }

                            "앱 설정" -> {
                                navController.navigate(Route.MYPAGE_APP_SETTINGS)
                            }
                        }
                    }
                )
            }

            composable(Route.MYPAGE_BODY_PROFILE) {
                BodyProfileScreen(
                    onBack = { navController.popBackStack() },
                    onSave = {
                        navController.navigate("mypage_body_profile_edit")
                    }
                )
            }

            composable("mypage_body_profile_edit") {
                BodyProfileEditScreen(
                    onBack = {
                        navController.popBackStack()
                    },
                    onSave = {
                        navController.popBackStack()
                    },
                    avoidedExercises = avoidedExercises,
                    onOpenExerciseSelect = {
                        navController.navigate("mypage_avoid_exercise_select")
                    },
                    onRemoveExercise = { exercise ->
                        avoidedExercises = avoidedExercises - exercise
                    }
                )
            }

            composable("mypage_avoid_exercise_select") {
                WorkoutExerciseSelectScreen(
                    initialSelectedIds = emptySet(),
                    buttonLabel = "다음",
                    onBack = {
                        navController.popBackStack()
                    },
                    onNext = { selectedExercises ->
                        avoidedExercises = selectedExercises.map { it.name }
                        navController.popBackStack()
                    }
                )
            }


            composable(Route.MYPAGE_WORKOUT_ENVIRONMENT) {
                WorkoutEnvironmentSelectionScreen(
                    onBack = {
                        navController.popBackStack()
                    },
                    onOpenGym = {
                        navController.navigate("mypage_gym_list")
                    },
                    onOpenHome = {
                        navController.navigate("mypage_home_equipment_edit")
                    },
                    onOpenOutdoor = {
                        navController.navigate("mypage_outdoor_equipment_edit")
                    }
                )
            }


            composable("mypage_gym_list") {
                GymListScreen(
                    gyms = gyms,
                    onBack = {
                        navController.popBackStack()
                    },
                    onEditGym = { gymId ->
                        navController.navigate("mypage_gym_edit/$gymId")
                    },
                    onAddGym = {
                        navController.navigate("mypage_gym_add")
                    },
                    onToggleFavorite = { gymId ->
                        gyms = gyms.map { gym ->
                            if (gym.id == gymId) {
                                gym.copy(
                                    isFavorite = !gym.isFavorite
                                )
                            } else {
                                gym
                            }
                        }
                    }
                )
            }

            composable("mypage_gym_edit/{gymId}") { entry ->
                val gymId = entry.arguments
                    ?.getString("gymId")
                    ?.toIntOrNull()

                val gym = gyms.firstOrNull { it.id == gymId }

                if (gym != null) {
                    GymEditScreen(
                        isAddMode = false,
                        initialName = gym.name,
                        initialEquipment = gym.equipment,
                        onBack = {
                            navController.popBackStack()
                        },
                        onSave = { name, equipment ->
                            gyms = gyms.map {
                                if (it.id == gym.id) {
                                    it.copy(
                                        name = name,
                                        equipment = equipment
                                    )
                                } else {
                                    it
                                }
                            }
                            navController.popBackStack()
                        }
                    )
                }
            }

            composable("mypage_gym_add") {
                GymEditScreen(
                    isAddMode = true,
                    onBack = {
                        navController.popBackStack()
                    },
                    onSave = { name, equipment ->
                        val newId = (gyms.maxOfOrNull { it.id } ?: 0) + 1

                        gyms = gyms + GymInfo(
                            id = newId,
                            name = name,
                            equipment = equipment
                        )

                        navController.popBackStack()
                    }
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

            composable("mypage_home_equipment_edit") {
                HomeEquipmentEditScreen(
                    initialEquipment = homeEquipment,
                    onBack = {
                        navController.popBackStack()
                    },
                    onSave = { updatedEquipment ->
                        homeEquipment = updatedEquipment
                        navController.popBackStack()
                    }
                )
            }

            composable("mypage_outdoor_equipment_edit") {
                OutdoorEquipmentEditScreen(
                    initialEquipment = outdoorEquipment,
                    onBack = {
                        navController.popBackStack()
                    },
                    onSave = { updatedEquipment ->
                        outdoorEquipment = updatedEquipment
                        navController.popBackStack()
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
