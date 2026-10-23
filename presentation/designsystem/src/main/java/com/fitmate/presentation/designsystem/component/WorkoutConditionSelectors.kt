package com.fitmate.presentation.designsystem.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Bolt
import androidx.compose.material.icons.rounded.EmojiEvents
import androidx.compose.material.icons.rounded.FitnessCenter
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.Timer
import androidx.compose.material.icons.rounded.WatchLater
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.fitmate.presentation.designsystem.theme.FitMateTheme
import com.fitmate.presentation.designsystem.theme.MinTouchTarget
import com.fitmate.presentation.designsystem.theme.Spacing3

// 운동 조건(요일 / 시간 / 장소) 선택 UI. 회원가입과 루틴 조건 변경 화면이 같이 쓴다

val WeekDayLabels = listOf("월", "화", "수", "목", "금", "토", "일")

// 요일 선택 박스 7개 (다중 선택)
@Composable
fun DaySelectorRow(
    selectedDays: Set<String>,
    onToggle: (String) -> Unit,
    modifier: Modifier = Modifier,
    boxHeight: Dp = MinTouchTarget
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        WeekDayLabels.forEach { day ->
            SelectableBox(
                text = day,
                selected = day in selectedDays,
                onClick = { onToggle(day) },
                height = boxHeight,
                modifier = Modifier.weight(1f)
            )
        }
    }
}

private data class DurationOption(val title: String, val description: String, val icon: ImageVector)

private val DurationOptions = listOf(
    DurationOption("20분", "짧고 집중적인 운동", Icons.Rounded.Bolt),
    DurationOption("30분", "간결하게 핵심만", Icons.Rounded.Timer),
    DurationOption("45분", "표준적인 운동 시간", Icons.Rounded.WatchLater),
    DurationOption("60분", "충분한 볼륨의 운동", Icons.Rounded.FitnessCenter),
    DurationOption("90분 이상", "고강도 집중 훈련", Icons.Rounded.EmojiEvents)
)

// 한 번에 운동할 수 있는 시간 카드 목록 (단일 선택)
@Composable
fun WorkoutDurationSelector(
    selected: String?,
    onSelect: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier, verticalArrangement = Arrangement.spacedBy(Spacing3)) {
        DurationOptions.forEach { option ->
            SelectOptionCard(
                icon = option.icon,
                title = option.title,
                description = option.description,
                selected = selected == option.title,
                onClick = { onSelect(option.title) },
                iconSize = 26.dp
            )
        }
    }
}

const val WORKOUT_PLACE_GYM = "헬스장"
const val WORKOUT_PLACE_HOME = "집"
const val WORKOUT_PLACE_OUTDOOR = "야외"

private data class PlaceOption(val title: String, val description: String, val icon: ImageVector, val iconSize: Dp)

private val PlaceOptions = listOf(
    PlaceOption(WORKOUT_PLACE_GYM, "다양한 머신과 프리웨이트", Icons.Rounded.FitnessCenter, 26.dp),
    PlaceOption(WORKOUT_PLACE_HOME, "홈트레이닝", Icons.Rounded.Home, 28.dp),
    PlaceOption(WORKOUT_PLACE_OUTDOOR, "러닝, 걷기 등", Icons.Rounded.FitnessCenter, 26.dp)
)

// 운동 장소 카드 목록 (단일 선택). 헬스장을 고르면 gymContent를 카드 안에 펼쳐 보여준다
@Composable
fun WorkoutPlaceSelector(
    selected: String?,
    onSelect: (String) -> Unit,
    modifier: Modifier = Modifier,
    gymContentTopPadding: Dp = 6.8.dp,
    gymContent: @Composable () -> Unit
) {
    Column(modifier = modifier, verticalArrangement = Arrangement.spacedBy(Spacing3)) {
        PlaceOptions.forEach { option ->
            SelectOptionCard(
                icon = option.icon,
                title = option.title,
                description = option.description,
                selected = selected == option.title,
                onClick = { onSelect(option.title) },
                iconSize = option.iconSize,
                expandedTopPadding = gymContentTopPadding,
                expandedContent = if (option.title == WORKOUT_PLACE_GYM) gymContent else null
            )
        }
    }
}

@Preview(widthDp = 346)
@Composable
private fun WorkoutConditionSelectorsPreview() {
    FitMateTheme {
        Column {
            DaySelectorRow(selectedDays = setOf("월", "목", "금", "일"), onToggle = {}, boxHeight = 44.dp)
            Spacer(modifier = Modifier.height(Spacing3))
            WorkoutDurationSelector(selected = "20분", onSelect = {})
        }
    }
}
