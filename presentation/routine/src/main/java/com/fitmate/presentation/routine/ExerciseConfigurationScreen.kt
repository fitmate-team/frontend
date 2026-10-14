package com.fitmate.presentation.routine

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.fitmate.presentation.designsystem.component.ExerciseChip
import com.fitmate.presentation.designsystem.component.FitMatePrimaryButton
import com.fitmate.presentation.designsystem.component.FitMateTitleTopBar
import com.fitmate.presentation.designsystem.component.FormFieldLabel
import com.fitmate.presentation.designsystem.component.IntensityField
import com.fitmate.presentation.designsystem.component.RestTimeButton
import com.fitmate.presentation.designsystem.component.WorkoutIntensityInput
import com.fitmate.presentation.designsystem.theme.Blue100
import com.fitmate.presentation.designsystem.theme.Blue200
import com.fitmate.presentation.designsystem.theme.Blue700
import com.fitmate.presentation.designsystem.theme.Blue900
import com.fitmate.presentation.designsystem.theme.FitMateTheme
import com.fitmate.presentation.designsystem.theme.Spacing1
import com.fitmate.presentation.designsystem.theme.Spacing2
import com.fitmate.presentation.designsystem.theme.Spacing3
import com.fitmate.presentation.designsystem.theme.Spacing4
import com.fitmate.presentation.designsystem.theme.Spacing5
import com.fitmate.presentation.designsystem.theme.Spacing6
import com.fitmate.presentation.designsystem.theme.TextSecondary
import com.fitmate.presentation.designsystem.theme.White
import com.fitmate.presentation.designsystem.theme.fitMateTextStyle

private val AiRecommendShape = RoundedCornerShape(14.dp)

// 3.7 운동 상세 설정. 위쪽 탭으로 고른 운동을 오가며 세트 / 횟수 / 중량 / 휴식을 입력한다
// 마지막 운동에서 다음을 누르면 설정 전체를 돌려준다
@Composable
fun ExerciseConfigurationScreen(
    exercises: List<String> = AiRecommendedExerciseNames,
    initialSettings: List<ExerciseSetting> = emptyList(),
    onBack: () -> Unit = {},
    onDone: (List<ExerciseSetting>) -> Unit = {}
) {
    var settings by remember {
        mutableStateOf(exercises.map { name -> initialSettings.firstOrNull { it.name == name } ?: ExerciseSetting(name) })
    }
    var currentIndex by remember { mutableIntStateOf(0) }
    val current = settings[currentIndex]

    fun update(transform: (ExerciseSetting) -> ExerciseSetting) {
        settings = settings.toMutableList().also { it[currentIndex] = transform(it[currentIndex]) }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(White)
    ) {
        FitMateTitleTopBar(title = "운동 설정", onBack = onBack) {
            Row(
                modifier = Modifier
                    .horizontalScroll(rememberScrollState())
                    .padding(horizontal = Spacing6),
                horizontalArrangement = Arrangement.spacedBy(Spacing1)
            ) {
                settings.forEachIndexed { index, setting ->
                    ExerciseChip(
                        text = shortNameOf(setting.name),
                        selected = index == currentIndex,
                        onClick = { currentIndex = index }
                    )
                }
            }
        }
        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = Spacing6)
        ) {
            Spacer(modifier = Modifier.height(Spacing5))
            FormFieldLabel(text = "운동 세부 강도 설정")
            Spacer(modifier = Modifier.height(Spacing3))
            WorkoutIntensityInput(
                fields = listOf(
                    IntensityField("세트", current.sets, { value -> update { it.copy(sets = value) } }),
                    IntensityField("횟수", current.reps, { value -> update { it.copy(reps = value) } }),
                    IntensityField("중량(kg)", current.weight, { value -> update { it.copy(weight = value) } }, KeyboardType.Decimal)
                )
            )
            Spacer(modifier = Modifier.height(Spacing6))
            FormFieldLabel(text = "휴식 시간")
            Spacer(modifier = Modifier.height(Spacing3))
            Row(horizontalArrangement = Arrangement.spacedBy(Spacing2)) {
                RestTimeOptions.forEach { option ->
                    RestTimeButton(
                        text = option,
                        selected = current.rest == option,
                        onClick = { update { it.copy(rest = option) } },
                        modifier = Modifier.weight(1f)
                    )
                }
            }
            Spacer(modifier = Modifier.height(29.dp))
            AiRecommendBox(exerciseName = shortNameOf(current.name))
            Spacer(modifier = Modifier.height(Spacing6))
        }
        FitMatePrimaryButton(
            text = "다음",
            onClick = {
                if (currentIndex < settings.lastIndex) currentIndex += 1 else onDone(settings)
            },
            modifier = Modifier.padding(start = Spacing6, end = Spacing6, bottom = Spacing6)
        )
    }
}

// AI 추천 값 안내 (예시 값)
@Composable
private fun AiRecommendBox(exerciseName: String) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(AiRecommendShape)
            .background(Blue100)
            .border(1.dp, Blue200, AiRecommendShape)
            .padding(horizontal = Spacing4, vertical = 14.dp)
    ) {
        Text(text = "AI 추천 — $exerciseName", style = fitMateTextStyle(12.sp, 18.sp, FontWeight.Bold), color = Blue700)
        Text(text = "4세트 · 8회 · 42.5kg · 휴식 90초", style = fitMateTextStyle(13.sp, 20.sp), color = Blue900)
        Text(text = "최근 4회 평균 성공률 94%를 기준으로 추천해요.", style = fitMateTextStyle(12.sp, 20.sp), color = TextSecondary)
    }
}

@Preview(name = "입력 전", widthDp = 394, heightDp = 892)
@Composable
private fun ExerciseConfigurationScreenPreview() {
    FitMateTheme { ExerciseConfigurationScreen() }
}

@Preview(name = "입력 후", widthDp = 394, heightDp = 892)
@Composable
private fun ExerciseConfigurationScreenFilledPreview() {
    FitMateTheme {
        ExerciseConfigurationScreen(
            initialSettings = listOf(ExerciseSetting("벤치 프레스", sets = "4", reps = "10", weight = "40", rest = "60초"))
        )
    }
}
