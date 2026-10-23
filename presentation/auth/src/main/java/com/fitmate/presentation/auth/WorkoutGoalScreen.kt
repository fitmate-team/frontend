package com.fitmate.presentation.auth

import com.fitmate.domain.model.PrimaryGoal
import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.fitmate.presentation.designsystem.R
import com.fitmate.presentation.designsystem.component.FitMatePrimaryButton
import com.fitmate.presentation.designsystem.component.SelectOptionCard
import com.fitmate.presentation.designsystem.theme.FitMateTheme
import com.fitmate.presentation.designsystem.theme.Spacing3

private data class GoalOption(val goal: PrimaryGoal, val title: String, val description: String, @DrawableRes val iconRes: Int)

private val GoalOptions = listOf(
    GoalOption(PrimaryGoal.FAT_LOSS, "체지방 감량", "체지방을 줄이고 더 가벼운 몸을 만들어요", R.drawable.ic_goal_fat_loss),
    GoalOption(PrimaryGoal.MUSCLE_GAIN, "근육 성장", "근육량과 운동 볼륨을 늘려요", R.drawable.ic_goal_muscle_growth),
    GoalOption(PrimaryGoal.STRENGTH_GAIN, "근력 향상", "주요 운동의 수행 능력과 중량을 높여요", R.drawable.ic_goal_strength_gain),
    GoalOption(PrimaryGoal.BODY_RECOMPOSITION, "신체 재구성", "근육은 유지하거나 늘리고 체지방은 줄여요", R.drawable.ic_goal_body_recomposition),
    GoalOption(PrimaryGoal.FITNESS_IMPROVEMENT, "체력 향상", "심폐지구력과 전반적인 운동 능력을 높여요", R.drawable.ic_goal_endurance_improvement),
    GoalOption(PrimaryGoal.HABIT_FORMATION, "운동 습관 형성", "무리하지 않고 꾸준한 운동 습관을 만들어요", R.drawable.ic_goal_exercise_habit)
)

// 1.5 회원가입 (운동 목표)
@Composable
fun WorkoutGoalScreen(
    selectedGoal: PrimaryGoal?,
    onGoalSelect: (PrimaryGoal) -> Unit,
    onBack: () -> Unit,
    onNext: () -> Unit
) {
    SignupStepScaffold(
        step = 4,
        onBack = onBack,
        title = "가장 이루고 싶은 목표는\n무엇인가요?",
        subtitle = "모르거나 최근 측정하지 않았다면 건너뛸 수 있어요.",
        bottomBar = { FitMatePrimaryButton(text = "다음", onClick = onNext) }
    ) {
        GoalOptions.forEachIndexed { index, option ->
            if (index > 0) Spacer(modifier = Modifier.height(Spacing3))
            // 목표 아이콘은 48x48 컨테이너 기준으로 그려져 있다
            SelectOptionCard(
                icon = ImageVector.vectorResource(option.iconRes),
                title = option.title,
                description = option.description,
                selected = selectedGoal == option.goal,
                onClick = { onGoalSelect(option.goal) },
                iconSize = 48.dp
            )
        }
    }
}

@Preview(widthDp = 394, heightDp = 892)
@Composable
private fun WorkoutGoalScreenPreview() {
    FitMateTheme {
        WorkoutGoalScreen(selectedGoal = PrimaryGoal.MUSCLE_GAIN, onGoalSelect = {}, onBack = {}, onNext = {})
    }
}
