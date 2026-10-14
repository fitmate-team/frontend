package com.fitmate.presentation.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.fitmate.presentation.designsystem.component.ChipFlowLayout
import com.fitmate.presentation.designsystem.component.CircleCheck
import com.fitmate.presentation.designsystem.component.ExerciseChip
import com.fitmate.presentation.designsystem.component.FitMatePrimaryButton
import com.fitmate.presentation.designsystem.component.InfoBanner
import com.fitmate.presentation.designsystem.component.InfoBannerType
import com.fitmate.presentation.designsystem.theme.BorderDefault
import com.fitmate.presentation.designsystem.theme.FitMateTheme
import com.fitmate.presentation.designsystem.theme.ShapeMedium
import com.fitmate.presentation.designsystem.theme.Spacing3
import com.fitmate.presentation.designsystem.theme.Spacing4
import com.fitmate.presentation.designsystem.theme.Spacing8
import com.fitmate.presentation.designsystem.theme.TextPrimary
import com.fitmate.presentation.designsystem.theme.White
import com.fitmate.presentation.designsystem.theme.fitMateTextStyle

private val ConcernParts = listOf("허리", "무릎", "어깨", "손목", "발목", "목")
private val SectionSpacing = 28.dp

// 1.10 회원가입 (고려해야할 부분). 체크하면 부위 / 피하고 싶은 운동 입력이 펼쳐진다
// avoidExercises는 운동 검색 화면에서 고른 운동. 다시 누르면 목록에서 삭제한다
@Composable
fun ExerciseRestrictionsScreen(
    avoidExercises: List<String>,
    onBack: () -> Unit,
    onAddExercise: () -> Unit,
    onRemoveExercise: (String) -> Unit,
    onNext: () -> Unit
) {
    var hasConcern by rememberSaveable { mutableStateOf(false) }
    var selectedParts by rememberSaveableSelection()

    SignupStepScaffold(
        step = 9,
        onBack = onBack,
        title = "운동할 때 고려해야할\n부분이 있나요?",
        subtitle = "부담을 느끼는 부위나 피하고 싶은 운동을 설정하면 운동 추천\n시 고려할게요.",
        bottomBar = { FitMatePrimaryButton(text = "다음", onClick = onNext) }
    ) {
        ConcernCheckRow(checked = hasConcern, onClick = { hasConcern = !hasConcern })

        if (hasConcern) {
            Spacer(modifier = Modifier.height(Spacing8))
            SignupFieldLabel(text = "신경 쓰이는 부위")
            Spacer(modifier = Modifier.height(Spacing3))
            ChipFlowLayout {
                ConcernParts.forEach { part ->
                    ExerciseChip(
                        text = part,
                        selected = part in selectedParts,
                        onClick = { selectedParts = selectedParts.toggle(part) }
                    )
                }
            }

            Spacer(modifier = Modifier.height(SectionSpacing))
            SignupFieldLabel(text = "피하고 싶은 운동")
            Spacer(modifier = Modifier.height(Spacing3))
            ChipFlowLayout {
                ExerciseChip(text = "+ 운동 추가", selected = false, onClick = onAddExercise)
                avoidExercises.forEach { exercise ->
                    ExerciseChip(text = exercise, selected = true, onClick = { onRemoveExercise(exercise) })
                }
            }

            Spacer(modifier = Modifier.height(SectionSpacing))
            InfoBanner(
                text = "입력한 정보는 운동 선호 및 제한 설정에만 사용되며,\n의학적 진단이나 치료 목적으로 활용되지 않습니다.",
                type = InfoBannerType.Neutral
            )
        }
    }
}

@Composable
private fun ConcernCheckRow(checked: Boolean, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(54.dp)
            .clip(ShapeMedium)
            .background(White)
            .border(1.dp, BorderDefault, ShapeMedium)
            .clickable(onClick = onClick)
            .padding(horizontal = Spacing4),
        verticalAlignment = Alignment.CenterVertically
    ) {
        CircleCheck(checked = checked)
        Spacer(modifier = Modifier.width(Spacing3))
        Text(
            text = "고려해야할 부분이 있어요",
            style = fitMateTextStyle(15.sp, 22.5.sp),
            color = TextPrimary
        )
    }
}

@Preview(widthDp = 394, heightDp = 892)
@Composable
private fun ExerciseRestrictionsScreenPreview() {
    FitMateTheme {
        ExerciseRestrictionsScreen(
            avoidExercises = listOf("랫풀다운", "줄넘기"),
            onBack = {},
            onAddExercise = {},
            onRemoveExercise = {},
            onNext = {}
        )
    }
}
