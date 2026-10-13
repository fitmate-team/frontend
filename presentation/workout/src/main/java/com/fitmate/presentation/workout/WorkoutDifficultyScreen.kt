package com.fitmate.presentation.workout

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.fitmate.presentation.designsystem.R
import com.fitmate.presentation.designsystem.theme.*

/** 운동 종료 후 사용자가 느낀 난이도. */
enum class WorkoutDifficulty(
    val title: String,
    val description: String,
    val iconRes: Int
) {
    VERY_EASY(
        "너무 쉬웠어요.",
        "다음에는 더 높은 강도로.",
        R.drawable.ic_workout_level_very_easy
    ),
    EASY(
        "조금 여유 있었어요.",
        "중량이나 횟수를 늘려볼게요.",
        R.drawable.ic_workout_level_easy
    ),
    MODERATE(
        "적당히 힘들었어요.",
        "현재 루틴을 유지해요.",
        R.drawable.ic_workout_level_normal
    ),
    HARD(
        "거의 한계였어요.",
        "내일은 꼭 충분히 쉬세요.",
        R.drawable.ic_workout_level_hard
    ),
    TOO_HARD(
        "목표 수행이 어려워요.",
        "강도를 조금 낮춰볼게요.",
        R.drawable.ic_workout_level_very_hard
    )
}

@Composable
fun WorkoutDifficultyScreen(
    onBack: () -> Unit,
    onAnalyze: (WorkoutDifficulty) -> Unit,
    modifier: Modifier = Modifier
) {
    var selected by remember { mutableStateOf<WorkoutDifficulty?>(null) }

    Column(modifier = modifier.fillMaxSize().background(Color.White)) {
        Row(
            modifier = Modifier.fillMaxWidth().height(68.dp).padding(horizontal = 24.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier.size(24.dp).clickable(onClick = onBack),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_workout_back),
                    contentDescription = "뒤로가기",
                    tint = Color.Unspecified,
                    modifier = Modifier.size(24.dp)
                )
            }
            Spacer(Modifier.width(12.dp))
            Text(
                "운동 기록", fontFamily = FitMateNumberFontFamily,
                fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Gray700
            )
        }
        HorizontalDivider(color = Gray200, thickness = 1.dp)

        Column(
            modifier = Modifier.weight(1f).fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp).padding(top = 24.dp, bottom = 20.dp)
        ) {
            Text(
                "오늘 운동은 어떠셨나요?",
                fontFamily = FitMateNumberFontFamily,
                fontWeight = FontWeight.Bold,
                fontSize = 24.sp,
                color = Gray700,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(Modifier.height(8.dp))
            Text(
                "오늘의 체감 난이도를 선택해주세요.",
                fontFamily = FitMateFontFamily,
                fontSize = 14.sp,
                color = Gray300
            )
            Spacer(Modifier.height(36.dp))

            WorkoutDifficulty.entries.forEachIndexed { index, difficulty ->
                val isSelected = selected == difficulty
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(
                            width = 1.dp,
                            color = if (isSelected) Blue600 else BorderDefault,
                            shape = RoundedCornerShape(12.dp)
                        )
                        .background(
                            color = if (isSelected) Color(0xFFE1F2FF) else Color.White,
                            shape = RoundedCornerShape(12.dp)
                        )
                        .clickable { selected = difficulty }
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(48.dp)
                            .background(
                                if (isSelected) Blue600 else Color.White,
                                RoundedCornerShape(10.dp)
                            )
                            .border(
                                1.dp,
                                if (isSelected) Blue600 else BorderDefault,
                                RoundedCornerShape(10.dp)
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            painter = painterResource(difficulty.iconRes),
                            contentDescription = difficulty.title,
                            modifier = Modifier.size(28.dp),
                            tint = if (isSelected) Color.White else Gray300
                        )
                    }
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            difficulty.title,
                            fontFamily = FitMateFontFamily,
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp,
                            color = Gray700
                        )
                        Spacer(Modifier.height(4.dp))
                        Text(
                            difficulty.description,
                            fontFamily = FitMateFontFamily,
                            fontSize = 12.sp,
                            color = Gray300
                        )
                    }
                }
                if (index != WorkoutDifficulty.entries.lastIndex) {
                    Spacer(Modifier.height(8.dp))
                }
            }
        }

        Button(
            onClick = { selected?.let(onAnalyze) },
            enabled = selected != null,
            modifier = Modifier.fillMaxWidth()
                .padding(horizontal = 24.dp)
                .padding(top = 12.dp, bottom = 16.dp)
                .height(50.dp),
            shape = RoundedCornerShape(10.dp),
            border = BorderStroke(1.dp, if (selected != null) Blue600 else BorderDefault),
            colors = ButtonDefaults.buttonColors(
                containerColor = Blue600,
                disabledContainerColor = Gray50,
                disabledContentColor = Gray500
            )
        ) {
            Text(
                "운동 분석하기",
                fontFamily = FitMateFontFamily,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
            )
        }
    }
}
