package com.fitmate.presentation.auth

import com.fitmate.domain.model.CurrentExerciseStatus
import com.fitmate.domain.model.ExerciseLevel
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.DirectionsRun
import androidx.compose.material.icons.automirrored.rounded.DirectionsWalk
import androidx.compose.material.icons.rounded.FitnessCenter
import androidx.compose.material.icons.rounded.EmojiEmotions
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.fitmate.presentation.designsystem.component.ExerciseChip
import com.fitmate.presentation.designsystem.component.FitMatePrimaryButton
import com.fitmate.presentation.designsystem.component.SelectOptionCard
import com.fitmate.presentation.designsystem.theme.FitMateTheme
import com.fitmate.presentation.designsystem.theme.Gray700
import com.fitmate.presentation.designsystem.theme.Spacing2
import com.fitmate.presentation.designsystem.theme.Spacing3
import com.fitmate.presentation.designsystem.theme.TextPrimary
import com.fitmate.presentation.designsystem.theme.fitMateTextStyle

private data class ExperienceOption(val level: ExerciseLevel, val title: String, val description: String, val icon: ImageVector)

private val ExperienceOptions = listOf(
    ExperienceOption(ExerciseLevel.FIRST_TIME, "처음이에요", "거의 운동해본 적이 없어요", Icons.Rounded.EmojiEmotions),
    ExperienceOption(ExerciseLevel.BEGINNER, "초급", "운동 경력 1년 미만", Icons.AutoMirrored.Rounded.DirectionsWalk),
    ExperienceOption(ExerciseLevel.INTERMEDIATE, "중급", "운동 경력 1~3년", Icons.AutoMirrored.Rounded.DirectionsRun),
    ExperienceOption(ExerciseLevel.ADVANCED, "상급", "운동 경력 3년 이상", Icons.Rounded.FitnessCenter)
)

private val WorkoutStatuses = listOf(
    CurrentExerciseStatus.STARTING to "처음 시작",
    CurrentExerciseStatus.REGULARLY_EXERCISING to "꾸준히 운동 중",
    CurrentExerciseStatus.RESTARTING to "다시 운동 시작"
)

// 1.4 회원가입 (운동 경험)
@Composable
fun ExerciseExperienceScreen(
    selectedExperience: ExerciseLevel?,
    selectedStatus: CurrentExerciseStatus?,
    onExperienceSelect: (ExerciseLevel) -> Unit,
    onStatusSelect: (CurrentExerciseStatus) -> Unit,
    onBack: () -> Unit,
    onNext: () -> Unit
) {
    SignupStepScaffold(
        step = 3,
        onBack = onBack,
        title = "운동 경험이 어느 정도인가요?",
        bottomBar = { FitMatePrimaryButton(text = "다음", onClick = onNext) }
    ) {
        ExperienceOptions.forEachIndexed { index, option ->
            if (index > 0) Spacer(modifier = Modifier.height(Spacing3))
            SelectOptionCard(
                icon = option.icon,
                title = option.title,
                description = option.description,
                selected = selectedExperience == option.level,
                onClick = { onExperienceSelect(option.level) }
            )
        }

        Spacer(modifier = Modifier.height(48.dp))
        Text(
            text = "현재 운동 상태는 무엇인가요?",
            style = fitMateTextStyle(16.sp, 20.sp, FontWeight.Bold),
            color = TextPrimary
        )
        Spacer(modifier = Modifier.height(Spacing3))
        Row(horizontalArrangement = Arrangement.spacedBy(Spacing2)) {
            WorkoutStatuses.forEach { (status, label) ->
                ExerciseChip(
                    text = label,
                    selected = selectedStatus == status,
                    onClick = { onStatusSelect(status) },
                    unselectedTextColor = Gray700
                )
            }
        }
    }
}

@Preview(widthDp = 394, heightDp = 892)
@Composable
private fun ExerciseExperienceScreenPreview() {
    FitMateTheme {
        ExerciseExperienceScreen(
            selectedExperience = ExerciseLevel.BEGINNER,
            selectedStatus = null,
            onExperienceSelect = {},
            onStatusSelect = {},
            onBack = {},
            onNext = {}
        )
    }
}
