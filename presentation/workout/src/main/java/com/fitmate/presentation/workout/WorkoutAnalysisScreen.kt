package com.fitmate.presentation.workout

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.fitmate.presentation.designsystem.R
import com.fitmate.presentation.designsystem.theme.*
import kotlin.math.roundToInt

/** 실제 운동 기록으로 전달할 분석 항목. 유산소는 durationSeconds가 null이 아님. */
data class WorkoutAnalysisItem(
    val name: String,
    val weightKg: Int = 0,
    val reps: Int = 0,
    val totalSets: Int = 0,
    val completedSets: Int = 0,
    val durationSeconds: Int? = null,
    val completedDurationSeconds: Int = 0,
    val difficulty: String? = null
)

@Composable
fun WorkoutAnalysisScreen(
    items: List<WorkoutAnalysisItem>,
    totalSeconds: Long,
    restSeconds: Long? = null,
    minHeartRate: Int? = null,
    maxHeartRate: Int? = null,
    hasNextRoutine: Boolean = true,
    onBack: () -> Unit,
    onNext: () -> Unit,
    modifier: Modifier = Modifier
) {
    val plannedUnits = items.sumOf { if (it.durationSeconds != null) it.durationSeconds else it.totalSets }
    val completedUnits = items.sumOf {
        if (it.durationSeconds != null) it.completedDurationSeconds.coerceIn(0, it.durationSeconds)
        else it.completedSets.coerceIn(0, it.totalSets)
    }
    val percent = if (plannedUnits == 0) 0 else ((completedUnits.toFloat() / plannedUnits) * 100).roundToInt().coerceIn(0, 100)
    val watchConnected = minHeartRate != null && maxHeartRate != null

    Column(modifier = modifier.fillMaxSize().background(Color.White)) {
        Row(
            modifier = Modifier.fillMaxWidth().height(68.dp).padding(horizontal = 24.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_workout_back),
                contentDescription = "뒤로가기",
                tint = Color.Unspecified,
                modifier = Modifier.size(24.dp).clickable(onClick = onBack)
            )
            Spacer(Modifier.width(12.dp))
            Text("운동 분석", fontFamily = FitMateFontFamily, fontWeight = FontWeight.Bold,
                fontSize = 20.sp, color = Gray700)
        }
        HorizontalDivider(thickness = 1.dp, color = Gray200)

        Column(
            modifier = Modifier.weight(1f).fillMaxWidth().verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp).padding(top = 24.dp, bottom = 24.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(40.dp)
            ) {
                Box(modifier = Modifier.size(154.dp), contentAlignment = Alignment.Center) {
                    Canvas(modifier = Modifier.fillMaxSize()) {
                        val stroke = 15.dp.toPx()
                        drawArc(
                            color = Color(0xFFE2E6EA), startAngle = -90f, sweepAngle = 360f,
                            useCenter = false, style = Stroke(width = stroke)
                        )
                        drawArc(
                            color = Blue600, startAngle = -90f,
                            sweepAngle = 360f * percent / 100f, useCenter = false,
                            style = Stroke(width = stroke, cap = StrokeCap.Butt)
                        )
                    }
                    Row(verticalAlignment = Alignment.Bottom) {
                        Text("$percent", fontFamily = FitMateNumberFontFamily,
                            fontSize = 42.sp, color = Gray700, lineHeight = 48.sp)
                        Text("%", fontFamily = FitMateFontFamily, fontWeight = FontWeight.Bold,
                            fontSize = 13.sp, color = Gray700,
                            modifier = Modifier.padding(bottom = 6.dp))
                    }
                }
                Column(modifier = Modifier.weight(1f)) {
                    SummaryMetric("전체 수행률", "$percent%")
                    Spacer(Modifier.height(13.dp))
                    SummaryMetric("실제 운동 시간", "${totalSeconds / 60}분")
                    Spacer(Modifier.height(13.dp))
                    HorizontalDivider(thickness = 1.dp, color = Gray200)
                    Spacer(Modifier.height(12.dp))
                    Text(if (watchConnected) "심박수" else "총 휴식 시간",
                        fontFamily = FitMateFontFamily, fontSize = 12.sp, color = Gray500)
                    Spacer(Modifier.height(8.dp))
                    if (watchConnected) {
                        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                            Column(modifier = Modifier.weight(1f)) {
                                SummaryMetric("최저", "$minHeartRate", "bpm", small = true)
                            }
                            Column(modifier = Modifier.weight(1f)) {
                                SummaryMetric("최고", "$maxHeartRate", "bpm", small = true)
                            }
                        }
                    } else {
                        Text(restSeconds?.let { "${it / 60}분" } ?: "기록 없음",
                            fontFamily = if (restSeconds != null) FitMateNumberFontFamily else FitMateFontFamily,
                            fontSize = if (restSeconds != null) 18.sp else 14.sp, color = Gray700)
                    }
                }
            }
            Spacer(Modifier.height(24.dp))
            HorizontalDivider(thickness = 1.dp, color = Gray200)
            Spacer(Modifier.height(28.dp))
            Text("운동별 분석", fontFamily = FitMateFontFamily,
                fontWeight = FontWeight.Bold, fontSize = 17.sp, color = Gray700)
            Spacer(Modifier.height(12.dp))
            items.forEachIndexed { index, item ->
                AnalysisExerciseCard(index + 1, item)
                if (index != items.lastIndex) Spacer(Modifier.height(8.dp))
            }
        }

        Button(
            onClick = onNext,
            modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp)
                .padding(top = 12.dp, bottom = 16.dp).height(50.dp),
            shape = RoundedCornerShape(10.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Blue600)
        ) {
            Text(if (hasNextRoutine) "다음 운동 제안 보기" else "저장하기",
                fontFamily = FitMateFontFamily, fontWeight = FontWeight.Bold, fontSize = 16.sp)
        }
    }
}

@Composable
private fun SummaryMetric(label: String, value: String, unit: String = "", small: Boolean = false) {
    Text(label, fontFamily = FitMateFontFamily, fontSize = 12.sp, color = Gray500)
    Spacer(Modifier.height(4.dp))
    Row(verticalAlignment = Alignment.Bottom) {
        Text(value, fontFamily = if (small) FitMateFontFamily else FitMateNumberFontFamily,
            fontSize = if (small) 18.sp else 20.sp, lineHeight = 28.sp, color = Gray700)
        if (unit.isNotEmpty()) {
            Spacer(Modifier.width(3.dp))
            Text(unit, fontFamily = FitMateFontFamily, fontWeight = FontWeight.Bold,
                fontSize = 12.sp, color = Gray700, modifier = Modifier.padding(bottom = 2.dp))
        }
    }
}

@Composable
private fun AnalysisExerciseCard(number: Int, item: WorkoutAnalysisItem) {
    val cardio = item.durationSeconds != null
    val done = if (cardio) item.completedDurationSeconds.coerceIn(0, item.durationSeconds ?: 0)
    else item.completedSets.coerceIn(0, item.totalSets)
    val total = if (cardio) (item.durationSeconds ?: 0) else item.totalSets
    val segments = if (cardio) 4 else item.totalSets.coerceAtLeast(1)

    Column(
        modifier = Modifier.fillMaxWidth()
            .border(1.dp, BorderDefault, RoundedCornerShape(20.dp))
            .padding(start = 16.dp, end = 12.dp, top = 14.dp, bottom = 16.dp)
    ) {
        Row(verticalAlignment = Alignment.Top) {
            Box(
                modifier = Modifier.size(30.dp).background(Color(0xFFF0F2F4), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text("$number", fontFamily = FitMateFontFamily, fontSize = 12.sp, color = Gray700)
            }
            Spacer(Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = item.name,
                    modifier = Modifier.wrapContentHeight(align = Alignment.Top),
                    fontFamily = FitMateFontFamily,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    lineHeight = 14.sp,
                    color = Gray700,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    style = LocalTextStyle.current.copy(
                        platformStyle = PlatformTextStyle(
                            includeFontPadding = false
                        )
                    )
                )
                Spacer(Modifier.height(2.dp))
                Text(
                    if (cardio) "${(total + 59) / 60}분" else "${item.weightKg}kg / ${item.reps}회 × ${item.totalSets}세트",
                    fontFamily = FitMateFontFamily, fontSize = 12.sp, color = Gray500,
                    maxLines = 1, overflow = TextOverflow.Ellipsis
                )
            }
            Spacer(Modifier.width(8.dp))
            Box(
                modifier = Modifier.border(1.dp, BorderDefault, RoundedCornerShape(30.dp))
                    .padding(horizontal = 12.dp, vertical = 5.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(item.difficulty ?: "미평가", fontFamily = FitMateFontFamily,
                    fontSize = 12.sp, color = Gray700)
            }
        }
        Spacer(Modifier.height(8.dp))
        Row(verticalAlignment = Alignment.CenterVertically) {
            Row(modifier = Modifier.weight(1f), horizontalArrangement = Arrangement.spacedBy(3.dp)) {
                repeat(segments) { index ->
                    val filled = if (cardio) {
                        total > 0 && done.toFloat() / total >= (index + 1).toFloat() / segments
                    } else index < done
                    Box(
                        modifier = Modifier.weight(1f).height(10.dp)
                            .background(if (filled) Color(0xFF61B3FF) else Color(0xFFE2E6EA),
                                RoundedCornerShape(3.dp))
                    )
                }
            }
            Spacer(Modifier.width(12.dp))
            Text(
                if (cardio) "${done / 60}/${total / 60}분" else "$done/$total 세트",
                fontFamily = FitMateFontFamily, fontSize = 12.sp, color = Gray700
            )
        }
    }
}
