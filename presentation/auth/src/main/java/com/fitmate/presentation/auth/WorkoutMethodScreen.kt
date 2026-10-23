package com.fitmate.presentation.auth

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.DirectionsRun
import androidx.compose.material.icons.automirrored.rounded.DirectionsWalk
import androidx.compose.material.icons.automirrored.rounded.TrendingUp
import androidx.compose.material.icons.rounded.AccessibilityNew
import androidx.compose.material.icons.rounded.Balance
import androidx.compose.material.icons.rounded.Bolt
import androidx.compose.material.icons.rounded.CalendarMonth
import androidx.compose.material.icons.rounded.FitnessCenter
import androidx.compose.material.icons.rounded.LocalFireDepartment
import androidx.compose.material.icons.rounded.Timer
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.fitmate.domain.model.GoalStrategy
import com.fitmate.domain.model.PrimaryGoal
import com.fitmate.presentation.designsystem.component.FitMatePrimaryButton
import com.fitmate.presentation.designsystem.component.SelectOptionCard
import com.fitmate.presentation.designsystem.theme.FitMateTheme
import com.fitmate.presentation.designsystem.theme.Spacing3

private data class MethodOption(
    val strategy: GoalStrategy,
    val title: String,
    val description: String,
    val icon: ImageVector
)

// 서버의 goalStrategy는 목표마다 고를 수 있는 방식이 다르다
// 체지방 감량 외 목표의 문구는 디자인 확정 전 임시 문구
private val MethodOptions = listOf(
    MethodOption(GoalStrategy.FAT_LOSS_STRENGTH_FOCUS, "근력 운동 중심", "근육량을 유지하면서 웨이트 비중을 높여요", Icons.Rounded.FitnessCenter),
    MethodOption(GoalStrategy.FAT_LOSS_CARDIO_FOCUS, "유산소 중심", "러닝, 사이클 등 유산소 비중을 높여요", Icons.AutoMirrored.Rounded.DirectionsRun),
    MethodOption(GoalStrategy.FAT_LOSS_BALANCED, "균형형", "근력 운동과 유산소를 균형 있게 진행해요", Icons.Rounded.Balance),

    MethodOption(GoalStrategy.MUSCLE_GAIN_HYPERTROPHY_FOCUS, "근비대 중심", "충분한 볼륨으로 근육 크기를 키워요", Icons.Rounded.FitnessCenter),
    MethodOption(GoalStrategy.MUSCLE_GAIN_BODY_PART_FOCUS, "부위 집중", "키우고 싶은 부위를 집중적으로 단련해요", Icons.Rounded.AccessibilityNew),
    MethodOption(GoalStrategy.MUSCLE_GAIN_FULL_BODY_BALANCED, "전신 균형", "전신 근육을 고르게 성장시켜요", Icons.Rounded.Balance),

    MethodOption(GoalStrategy.STRENGTH_BIG_THREE_FOCUS, "3대 운동 중심", "스쿼트, 벤치프레스, 데드리프트 중량을 높여요", Icons.Rounded.FitnessCenter),
    MethodOption(GoalStrategy.STRENGTH_UPPER_BODY_FOCUS, "상체 근력 중심", "상체 운동의 수행 능력을 높여요", Icons.Rounded.AccessibilityNew),
    MethodOption(GoalStrategy.STRENGTH_LOWER_BODY_FOCUS, "하체 근력 중심", "하체 운동의 수행 능력을 높여요", Icons.AutoMirrored.Rounded.DirectionsWalk),
    MethodOption(GoalStrategy.STRENGTH_OVERALL, "전반적 근력", "전신 근력을 고르게 높여요", Icons.Rounded.Balance),

    MethodOption(GoalStrategy.RECOMPOSITION_FAT_LOSS_PRIORITY, "감량 우선", "체지방 감량에 조금 더 집중해요", Icons.Rounded.LocalFireDepartment),
    MethodOption(GoalStrategy.RECOMPOSITION_MUSCLE_GAIN_PRIORITY, "근육 증가 우선", "근육량 증가에 조금 더 집중해요", Icons.Rounded.FitnessCenter),
    MethodOption(GoalStrategy.RECOMPOSITION_BALANCED, "균형형", "감량과 근육 증가를 균형 있게 진행해요", Icons.Rounded.Balance),

    MethodOption(GoalStrategy.FITNESS_CARDIORESPIRATORY_FOCUS, "심폐지구력 중심", "러닝, 사이클 등으로 심폐 능력을 높여요", Icons.AutoMirrored.Rounded.DirectionsRun),
    MethodOption(GoalStrategy.FITNESS_MUSCULAR_ENDURANCE_FOCUS, "근지구력 중심", "오래 버티는 근육의 힘을 길러요", Icons.Rounded.Timer),
    MethodOption(GoalStrategy.FITNESS_OVERALL, "전반적 체력", "심폐지구력과 근지구력을 고르게 높여요", Icons.Rounded.Balance),

    MethodOption(GoalStrategy.HABIT_SHORT_AND_FREQUENT, "짧고 자주", "짧은 운동을 자주 하며 습관을 만들어요", Icons.Rounded.Bolt),
    MethodOption(GoalStrategy.HABIT_THREE_TIMES_A_WEEK, "주 3회 꾸준히", "일주일에 세 번 규칙적으로 운동해요", Icons.Rounded.CalendarMonth),
    MethodOption(GoalStrategy.HABIT_GRADUAL_INTENSITY, "점진적 강도", "가볍게 시작해서 조금씩 강도를 높여요", Icons.AutoMirrored.Rounded.TrendingUp)
)

// 1.6 회원가입 (운동 방식). 앞 단계에서 고른 목표에 해당하는 방식만 보여준다
@Composable
fun WorkoutMethodScreen(
    goal: PrimaryGoal?,
    selectedStrategy: GoalStrategy?,
    onStrategySelect: (GoalStrategy) -> Unit,
    onBack: () -> Unit,
    onNext: () -> Unit
) {
    val options = MethodOptions.filter { it.strategy.goal == goal }

    SignupStepScaffold(
        step = 5,
        onBack = onBack,
        title = "어떤 방식으로 운동할까요?",
        subtitle = "운동 비중을 선택하면 AI가 루틴 구성에 반영해요.",
        bottomBar = { FitMatePrimaryButton(text = "다음", onClick = onNext) }
    ) {
        options.forEachIndexed { index, option ->
            if (index > 0) Spacer(modifier = Modifier.height(Spacing3))
            SelectOptionCard(
                icon = option.icon,
                title = option.title,
                description = option.description,
                selected = selectedStrategy == option.strategy,
                onClick = { onStrategySelect(option.strategy) },
                iconSize = 28.dp,
                height = 88.dp
            )
        }
    }
}

@Preview(widthDp = 394, heightDp = 892)
@Composable
private fun WorkoutMethodScreenPreview() {
    FitMateTheme {
        WorkoutMethodScreen(
            goal = PrimaryGoal.FAT_LOSS,
            selectedStrategy = GoalStrategy.FAT_LOSS_BALANCED,
            onStrategySelect = {},
            onBack = {},
            onNext = {}
        )
    }
}
