package com.fitmate.app.navigation

object Route {
    const val HOME = "home"
    const val ROUTINE = "routine"
    const val WORKOUT = "workout"
    const val RECORD = "record"
    const val MYPAGE = "mypage"

    // Auth / signup wizard
    const val LOGIN = "login"
    const val AI_TRAINER_INTRO = "signup/intro"
    const val SIGNUP_CREDENTIALS = "signup/credentials"
    const val BASIC_BODY_INFO = "signup/basic-body-info"
    const val OPTIONAL_BODY_INFO = "signup/optional-body-info"
    const val EXERCISE_EXPERIENCE = "signup/exercise-experience"
    const val WORKOUT_GOAL = "signup/workout-goal"
    const val DETAILED_GOAL = "signup/detailed-goal"
    const val WORKOUT_SCHEDULE = "signup/workout-schedule"
    const val WORKOUT_DURATION = "signup/workout-duration"
    const val WORKOUT_ENVIRONMENT = "signup/workout-environment"
    const val GYM_EQUIPMENT = "signup/gym-equipment"
    const val HOME_EQUIPMENT = "signup/home-equipment"
    const val OUTDOOR_EQUIPMENT = "signup/outdoor-equipment"
    const val EXERCISE_RESTRICTIONS = "signup/exercise-restrictions"
    const val EXERCISE_SEARCH = "signup/exercise-search"
    const val EXERCISE_ABILITY = "signup/exercise-ability"
    const val AI_ANALYSIS_LOADING = "signup/ai-analysis-loading"
    const val AI_ROUTINE_RESULT = "signup/ai-routine-result"
    const val AI_RECOMMENDATION_REASON = "signup/ai-recommendation-reason"

    // Home
    const val WATCH_SYNC_RESULT = "home/watch-sync-result"

    // Routine
    const val ROUTINE_CREATE_METHOD = "routine/create-method"
    const val ROUTINE_AI_HANDSOFF_SETUP = "routine/ai-handsoff-setup"
    const val ROUTINE_AI_LOADING = "routine/ai-loading"
    const val ROUTINE_AI_RESULT = "routine/ai-result"
    const val ROUTINE_AI_REASON = "routine/ai-reason"
    const val ROUTINE_MANUAL_DAY_SELECT = "routine/manual-day-select"
    const val ROUTINE_MANUAL_EXERCISE_SELECT = "routine/manual-exercise-select"
    const val ROUTINE_EXERCISE_CONFIG = "routine/exercise-config"
    const val ROUTINE_WORKOUT_DETAIL = "routine/workout-detail"

    // Workout
    const val WORKOUT_LOCATION = "workout/location"
    const val WORKOUT_AI_LOADING = "workout/ai-loading"
    const val WORKOUT_ALT_ROUTINE = "workout/alt-routine"
    const val WORKOUT_ACTIVE = "workout/active"
    const val WORKOUT_REST_TIMER = "workout/rest-timer"
    const val WORKOUT_WEIGHT_RECORD = "workout/weight-record"
    const val WORKOUT_EVAL = "workout/eval"
    const val WORKOUT_ANALYSIS = "workout/analysis"
    const val WORKOUT_ROUTINE_ADJUST = "workout/routine-adjust"
    const val WORKOUT_ROUTINE_APPLIED = "workout/routine-applied"

    // Record
    const val RECORD_WORKOUT_DETAIL = "record/workout-detail"
    const val RECORD_GROWTH_ANALYSIS = "record/growth-analysis"

    // MyPage
    const val MYPAGE_BODY_PROFILE = "mypage/body-profile"
    const val MYPAGE_WORKOUT_ENVIRONMENT = "mypage/workout-environment"
    const val MYPAGE_HOME_EQUIPMENT_EDIT = "mypage/home-equipment-edit"
    const val MYPAGE_AI_TRAINER_SETTINGS = "mypage/ai-trainer-settings"
    const val MYPAGE_APP_SETTINGS = "mypage/app-settings"
}
