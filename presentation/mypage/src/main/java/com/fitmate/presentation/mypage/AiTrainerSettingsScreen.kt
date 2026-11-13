package com.fitmate.presentation.mypage

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.fitmate.presentation.designsystem.theme.*

private data class AiToggleSetting(
    val title: String,
    val description: String? = null,
    val defaultOn: Boolean
)

private val aiSettings = listOf(
    AiToggleSetting(
        title = "AI 루틴 조정 제안",
        description = "운동 결과를 분석해 다음 루틴 변경을 제안해요.",
        defaultOn = true
    ),
    AiToggleSetting(
        title = "운동 후 난이도 질문",
        defaultOn = true
    ),
    AiToggleSetting(
        title = "AI 추천 이유 표시",
        defaultOn = true
    ),
    AiToggleSetting(
        title = "루틴 변경 자동 적용",
        description = "FitMate는 기본적으로 변경사항을 사용자 확인 후 적용해요.",
        defaultOn = false
    )
)

@Composable
fun AiTrainerSettingsScreen(
    onBack: () -> Unit = {}
) {
    val toggleStates = remember {
        aiSettings.map { mutableStateOf(it.defaultOn) }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(White)
    ) {
        SettingsHeader(
            title = "AI Trainer 설정",
            onBack = onBack
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp)
                .padding(top = 20.dp, bottom = 24.dp)
        ) {
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                color = White,
                border = BorderStroke(
                    width = 1.dp,
                    color = BorderDefault
                )
            ) {
                Column {
                    aiSettings.forEachIndexed { index, setting ->

                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    toggleStates[index].value =
                                        !toggleStates[index].value
                                }
                                .padding(
                                    horizontal = 16.dp,
                                    vertical = 16.dp
                                )
                        ) {
                            // 제목과 토글을 같은 줄에 배치
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                Text(
                                    text = setting.title,
                                    modifier = Modifier.weight(1f),
                                    fontFamily = FitMateFontFamily,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 15.sp,
                                    color = Gray900
                                )

                                FitMateSmallSwitch(
                                    checked = toggleStates[index].value,
                                    onCheckedChange = { isChecked ->
                                        toggleStates[index].value = isChecked
                                    }
                                )
                            }

                            // 설명이 있는 항목만 제목 아래에 표시
                            setting.description?.let { description ->
                                Spacer(
                                    modifier = Modifier.height(4.dp)
                                )

                                Text(
                                    text = description,
                                    fontFamily = FitMateFontFamily,
                                    fontSize = 12.sp,
                                    lineHeight = 16.sp,
                                    color = Gray500
                                )
                            }
                        }

                        // 마지막 항목을 제외하고 구분선 표시
                        if (index < aiSettings.lastIndex) {
                            HorizontalDivider(
                                thickness = 1.dp,
                                color = BorderDefault
                            )
                        }
                    }
                }
            }
        }
    }
}

@Preview(
    showBackground = true,
    showSystemUi = false,
    name = "AI Trainer 설정"
)
@Composable
private fun AiTrainerSettingsScreenPreview() {
    FitMateTheme {
        AiTrainerSettingsScreen()
    }
}
