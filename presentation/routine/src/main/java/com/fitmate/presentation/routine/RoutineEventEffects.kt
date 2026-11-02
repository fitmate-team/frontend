package com.fitmate.presentation.routine

import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.platform.LocalContext

// ViewModel의 일회성 이벤트(안내 문구 / 화면 이동)를 처리한다. 안내 문구는 토스트로 보여준다

@Composable
fun RoutineEventEffect(viewModel: RoutineViewModel) {
    val context = LocalContext.current
    LaunchedEffect(viewModel) {
        viewModel.events.collect { event ->
            when (event) {
                is RoutineEvent.Message -> Toast.makeText(context, event.text, Toast.LENGTH_SHORT).show()
            }
        }
    }
}

// 루틴 만들기 화면마다 호출한다. 현재 보이는 화면 하나만 이벤트를 받는다
@Composable
fun RoutineCreateEventEffect(
    viewModel: RoutineCreateViewModel,
    onRecommendationReady: () -> Unit = {},
    onAiExercisesReady: () -> Unit = {},
    onSaved: () -> Unit = {},
    onLoadingFailed: () -> Unit = {}
) {
    val context = LocalContext.current
    val currentOnRecommendationReady by rememberUpdatedState(onRecommendationReady)
    val currentOnAiExercisesReady by rememberUpdatedState(onAiExercisesReady)
    val currentOnSaved by rememberUpdatedState(onSaved)
    val currentOnLoadingFailed by rememberUpdatedState(onLoadingFailed)
    LaunchedEffect(viewModel) {
        viewModel.events.collect { event ->
            when (event) {
                is RoutineCreateEvent.Message -> Toast.makeText(context, event.text, Toast.LENGTH_SHORT).show()
                is RoutineCreateEvent.LoadingFailed -> {
                    Toast.makeText(context, event.text, Toast.LENGTH_SHORT).show()
                    currentOnLoadingFailed()
                }
                RoutineCreateEvent.RecommendationReady -> currentOnRecommendationReady()
                RoutineCreateEvent.AiExercisesReady -> currentOnAiExercisesReady()
                RoutineCreateEvent.Saved -> currentOnSaved()
            }
        }
    }
}
