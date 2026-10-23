package com.fitmate.presentation.routine

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.CalendarMonth
import androidx.compose.material.icons.rounded.FitnessCenter
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.KeyboardArrowDown
import androidx.compose.material.icons.automirrored.rounded.TrendingUp
import androidx.compose.material.icons.rounded.WatchLater
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.fitmate.presentation.designsystem.R
import com.fitmate.presentation.designsystem.component.FitMateFormScaffold
import com.fitmate.presentation.designsystem.component.FitMatePrimaryButton
import com.fitmate.presentation.designsystem.component.FitMateTitleTopBar
import com.fitmate.presentation.designsystem.component.InfoBanner
import com.fitmate.presentation.designsystem.theme.Blue100
import com.fitmate.presentation.designsystem.theme.Blue200
import com.fitmate.presentation.designsystem.theme.Blue500
import com.fitmate.presentation.designsystem.theme.Blue700
import com.fitmate.presentation.designsystem.theme.BorderDefault
import com.fitmate.presentation.designsystem.theme.FitMateTheme
import com.fitmate.presentation.designsystem.theme.Gray50
import com.fitmate.presentation.designsystem.theme.Gray700
import com.fitmate.presentation.designsystem.theme.ShapeLarge
import com.fitmate.presentation.designsystem.theme.Spacing2
import com.fitmate.presentation.designsystem.theme.Spacing3
import com.fitmate.presentation.designsystem.theme.Spacing4
import com.fitmate.presentation.designsystem.theme.Spacing5
import com.fitmate.presentation.designsystem.theme.TextPrimary
import com.fitmate.presentation.designsystem.theme.TextSecondary
import com.fitmate.presentation.designsystem.theme.White
import com.fitmate.presentation.designsystem.theme.fitMateTextStyle

private data class ReferenceInfo(val label: String, val value: String, val icon: @Composable () -> ImageVector)

private data class CompositionReason(val title: String, val detail: String)

private data class DayReason(val dayLabel: String, val title: String, val focus: String, val detail: String)

// 서버 연동 전까지 사용하는 예시 데이터
private val SampleReferenceInfos = listOf(
    ReferenceInfo("운동 목표", "체지방 감량 · 균형형") { Icons.AutoMirrored.Rounded.TrendingUp },
    ReferenceInfo("운동 경험", "초급") { ImageVector.vectorResource(R.drawable.ic_weightlifting) },
    ReferenceInfo("운동 가능 횟수", "주 4회") { Icons.Rounded.CalendarMonth },
    ReferenceInfo("운동 가능 시간", "회당 약 60분") { Icons.Rounded.WatchLater },
    ReferenceInfo("운동 환경", "헬스장") { Icons.Rounded.Home },
    ReferenceInfo("신체 정보", "175cm / 72kg") { Icons.Rounded.FitnessCenter }
)

private val SampleCompositionReasons = listOf(
    CompositionReason(
        "복합운동 중심",
        "바벨 벤치 프레스, 랫풀다운, 바벨 백 스쿼트를 각 운동일의 중심으로 배치해 가슴, 등, 하체의 주요 복합 움직임을 다룹니다. 각 부위에는 보조 운동을 더해 세션별 운동 구성을 완성했습니다."
    ),
    CompositionReason(
        "초급자 맞춤 강도",
        "운동 경험이 초급인 점을 고려해 머신과 프리웨이트를 함께 배치하고, 각 운동은 무리 없는 중량과 반복 수로 시작하도록 구성했습니다."
    ),
    CompositionReason(
        "체지방 감량을 위한 유산소",
        "주말에는 전신 운동과 러닝을 배치해 소모 칼로리를 높이고, 근력 운동일 사이에 충분한 회복 시간을 두었습니다."
    )
)

private val SampleDayReasons = listOf(
    DayReason("월", "상체", "가슴 · 어깨 · 삼두 중심", "벤치프레스, 숄더프레스, 체스트프레스를 중심으로 가슴과 어깨의 주요 근육을 함께 운동하도록 구성했어요."),
    DayReason("목", "하체", "햄스트링 · 대퇴사두근 중심", "스쿼트, 레그프레스, 레그컬로 허벅지 앞뒤 근육을 고르게 운동하도록 구성했어요."),
    DayReason("금", "상체", "가슴 · 어깨 · 삼두 중심", "랫풀다운, 시티드로우, 바벨컬로 등과 팔 근육을 함께 운동하도록 구성했어요."),
    DayReason("일", "전신", "전신 중심", "러닝으로 심폐지구력을 높이고 체지방 감량을 돕도록 구성했어요.")
)

private val InfoCardShape = RoundedCornerShape(14.dp)
private val DayBadgeShape = RoundedCornerShape(10.dp)
private val DividerWidth = 1.3.dp

// 3.6 이번 주 추천 루틴 - 추천 이유
@Composable
fun RoutineAiReasonScreen(
    onBack: () -> Unit = {},
    onConfirm: () -> Unit = {}
) {
    var expandedDay by rememberSaveable { mutableStateOf<String?>(SampleDayReasons.first().dayLabel) }

    FitMateFormScaffold(
        topBar = { FitMateTitleTopBar(title = "추천 이유", onBack = onBack) },
        title = null,
        bottomBar = { FitMatePrimaryButton(text = "확인", onClick = onConfirm) },
        topSpacing = 0.dp,
        contentTopSpacing = 0.dp
    ) {
        RobotSpeech(text = "이런 이유로 이번 루틴을 추천했어요.")
        Spacer(modifier = Modifier.height(30.dp))
        SectionTitle(text = "AI가 참고한 정보")
        Spacer(modifier = Modifier.height(Spacing3))
        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            SampleReferenceInfos.chunked(2).forEach { row ->
                Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    row.forEach { info ->
                        ReferenceInfoCard(info = info, modifier = Modifier.weight(1f))
                    }
                }
            }
        }
        Spacer(modifier = Modifier.height(40.dp))
        SectionTitle(text = "그래서 이렇게 구성했어요")
        Spacer(modifier = Modifier.height(Spacing3))
        // 카드 폭은 340dp로, 오른쪽이 6dp 더 비어 있다
        Column(
            modifier = Modifier.padding(end = 6.dp),
            verticalArrangement = Arrangement.spacedBy(Spacing2)
        ) {
            SampleCompositionReasons.forEach { reason -> CompositionReasonCard(reason = reason) }
        }
        Spacer(modifier = Modifier.height(38.dp))
        SectionTitle(text = "요일별 추천 이유")
        Spacer(modifier = Modifier.height(Spacing3))
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(ShapeLarge)
                .background(White)
                .border(DividerWidth, BorderDefault, ShapeLarge)
        ) {
            SampleDayReasons.forEachIndexed { index, reason ->
                DayReasonItem(
                    reason = reason,
                    expanded = expandedDay == reason.dayLabel,
                    showTopDivider = index > 0,
                    onClick = { expandedDay = if (expandedDay == reason.dayLabel) null else reason.dayLabel }
                )
            }
        }
        Spacer(modifier = Modifier.height(32.dp))
        InfoBanner(
            title = "앞으로 추천은 더 달라질 수 있어요.",
            text = "실제 운동 기록과 체감 난이도가 쌓이면 중량, 반복 수,\n세트 수와 운동 구성을 조정해요."
        )
    }
}

// 로봇 + 말풍선
@Composable
private fun RobotSpeech(text: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // 로봇은 화면 왼쪽 8dp까지 붙는다
        Image(
            painter = painterResource(R.drawable.img_robot),
            contentDescription = null,
            modifier = Modifier
                .offset(x = (-16).dp)
                .size(width = 100.dp, height = 101.dp)
        )
        Spacer(modifier = Modifier.weight(1f))
        Box(
            modifier = Modifier
                .width(264.dp)
                .height(64.dp)
                .clip(ShapeLarge)
                .background(Blue100)
                .border(1.dp, Blue500, ShapeLarge)
                .padding(horizontal = Spacing5),
            contentAlignment = Alignment.CenterStart
        ) {
            Text(text = text, style = fitMateTextStyle(12.sp, 20.sp), color = TextPrimary)
        }
    }
}

@Composable
private fun SectionTitle(text: String) {
    Text(text = text, style = fitMateTextStyle(16.sp, 20.sp, FontWeight.Bold), color = TextPrimary)
}

@Composable
private fun ReferenceInfoCard(info: ReferenceInfo, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .height(66.dp)
            .clip(InfoCardShape)
            .background(White)
            .border(DividerWidth, BorderDefault, InfoCardShape)
            .padding(horizontal = 14.dp, vertical = Spacing3)
    ) {
        Row(
            modifier = Modifier.padding(bottom = 2.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(imageVector = info.icon(), contentDescription = null, tint = Blue500, modifier = Modifier.size(18.dp))
            Spacer(modifier = Modifier.width(6.dp))
            Text(text = info.label, style = fitMateTextStyle(11.sp, 16.5.sp, FontWeight.Bold), color = Gray700)
        }
        Text(text = info.value, style = fitMateTextStyle(13.sp, 18.sp), color = TextPrimary)
    }
}

// 루틴 구성 이유 (항목명 + 설명)
@Composable
private fun CompositionReasonCard(reason: CompositionReason) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = 107.dp)
            .clip(InfoCardShape)
            .background(White)
            .border(DividerWidth, BorderDefault, InfoCardShape)
            .padding(start = 15.3.dp, top = 13.3.dp, end = 15.3.dp, bottom = 15.dp)
    ) {
        Text(text = reason.title, style = fitMateTextStyle(13.sp, 16.5.sp, FontWeight.Bold), color = Gray700)
        Spacer(modifier = Modifier.height(6.5.dp))
        // 디자인은 GamtanRoad Thin. 기본 폰트로 대체하는 동안은 Light로 가독성을 맞춘다
        Text(text = reason.detail, style = fitMateTextStyle(12.sp, 18.sp, FontWeight.Light), color = TextPrimary)
    }
}

// 요일별 추천 이유 (펼치면 자세한 설명)
@Composable
private fun DayReasonItem(
    reason: DayReason,
    expanded: Boolean,
    showTopDivider: Boolean,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .drawBehind {
                if (showTopDivider) {
                    drawLine(BorderDefault, Offset(0f, 0f), Offset(size.width, 0f), DividerWidth.toPx())
                }
            }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable(onClick = onClick)
                .padding(horizontal = Spacing4, vertical = 14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(DayBadgeShape)
                    .background(if (expanded) Blue500 else Gray50),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = reason.dayLabel,
                    style = fitMateTextStyle(12.sp, 18.sp, if (expanded) FontWeight.Bold else FontWeight.Normal),
                    color = if (expanded) White else TextSecondary
                )
            }
            Spacer(modifier = Modifier.width(Spacing3))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = reason.title,
                    style = fitMateTextStyle(14.sp, 21.sp, if (expanded) FontWeight.Bold else FontWeight.Normal),
                    color = if (expanded) Blue700 else TextPrimary
                )
                Text(text = reason.focus, style = fitMateTextStyle(12.sp, 18.sp), color = TextSecondary)
            }
            Spacer(modifier = Modifier.width(Spacing3))
            Icon(
                imageVector = Icons.Rounded.KeyboardArrowDown,
                contentDescription = if (expanded) "접기" else "펼치기",
                tint = TextSecondary,
                modifier = Modifier
                    .size(18.dp)
                    .rotate(if (expanded) 180f else 0f)
            )
        }
        if (expanded) {
            Box(modifier = Modifier.padding(start = 64.dp, end = Spacing4, bottom = Spacing4)) {
                Text(
                    text = reason.detail,
                    style = fitMateTextStyle(12.sp, 20.sp),
                    color = TextSecondary,
                    modifier = Modifier
                        .drawBehind {
                            drawLine(Blue200, Offset(0f, 0f), Offset(0f, size.height), DividerWidth.toPx())
                        }
                        .padding(start = Spacing3)
                )
            }
        }
    }
}

@Preview(widthDp = 394, heightDp = 1014)
@Composable
private fun RoutineAiReasonScreenPreview() {
    FitMateTheme { RoutineAiReasonScreen() }
}
