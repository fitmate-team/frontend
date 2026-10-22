package com.fitmate.presentation.routine

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Event
import androidx.compose.material.icons.rounded.Flag
import androidx.compose.material.icons.rounded.LocationOn
import androidx.compose.material.icons.rounded.WatchLater
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.fitmate.presentation.designsystem.component.FitMateFormScaffold
import com.fitmate.presentation.designsystem.component.FitMatePrimaryButton
import com.fitmate.presentation.designsystem.component.FitMateTitleTopBar
import com.fitmate.presentation.designsystem.component.InfoBanner
import com.fitmate.presentation.designsystem.component.SettingChangeItem
import com.fitmate.presentation.designsystem.theme.BorderDefault
import com.fitmate.presentation.designsystem.theme.FitMateTheme
import com.fitmate.presentation.designsystem.theme.ShapeLarge
import com.fitmate.presentation.designsystem.theme.Spacing6
import com.fitmate.presentation.designsystem.theme.White

// 3.4 루틴 조건 수정. 요일 / 운동 시간은 시간 변경 화면, 운동 장소는 장소 변경 화면에서 바꾼다
@Composable
fun AiHandsOffSetupScreen(
    condition: RoutineCondition = SampleRoutineCondition,
    onBack: () -> Unit = {},
    onEditSchedule: () -> Unit = {},
    onEditPlace: () -> Unit = {},
    onGenerate: () -> Unit = {}
) {
    FitMateFormScaffold(
        topBar = { FitMateTitleTopBar(title = "루틴 조건", onBack = onBack) },
        title = "변경된 조건이 있나요?",
        bottomBar = { FitMatePrimaryButton(text = "AI 루틴 만들기", onClick = onGenerate, height = 50.dp) }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(ShapeLarge)
                .background(White)
                .border(1.dp, BorderDefault, ShapeLarge)
        ) {
            SettingChangeItem(icon = Icons.Rounded.Flag, label = "목표", value = condition.goal, onEditClick = null)
            SettingChangeItem(icon = Icons.Rounded.Event, label = "요일", value = condition.daysLabel, onEditClick = onEditSchedule)
            SettingChangeItem(icon = Icons.Rounded.WatchLater, label = "운동 시간", value = condition.duration, onEditClick = onEditSchedule)
            SettingChangeItem(icon = Icons.Rounded.LocationOn, label = "운동 장소", value = condition.placeLabel, onEditClick = onEditPlace)
        }
        Spacer(modifier = Modifier.height(Spacing6))
        InfoBanner(text = "현재 설정을 기준으로 FitMate가 주간 루틴을 다시\n구성해요.")
    }
}

@Preview(widthDp = 394, heightDp = 892)
@Composable
private fun AiHandsOffSetupScreenPreview() {
    FitMateTheme { AiHandsOffSetupScreen() }
}
