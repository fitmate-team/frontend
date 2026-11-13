package com.fitmate.presentation.mypage

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.fitmate.presentation.designsystem.R
import com.fitmate.presentation.designsystem.theme.*

@Composable
fun BodyProfileScreen(
    onBack: () -> Unit = {},
    onSave: () -> Unit = {}
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(White)
    ) {
        SettingsHeader(
            title = "운동 프로필",
            onBack = onBack
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp)
                .padding(top = 16.dp, bottom = 34.dp)
        ) {

            // 기본 정보 제목 + 프로필 수정 버튼
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "기본 정보",
                    fontFamily = FitMateFontFamily,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    color = Gray900
                )

                Surface(
                    onClick = onSave,
                    shape = RoundedCornerShape(50),
                    color = White,
                    border = BorderStroke(
                        width = 1.dp,
                        color = BorderDefault
                    )
                ) {
                    Row(
                        modifier = Modifier.padding(
                            horizontal = 12.dp,
                            vertical = 6.dp
                        ),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Icon(
                            painter = painterResource(
                                id = R.drawable.ic_profile_edit
                            ),
                            contentDescription = null,
                            tint = Color.Unspecified,
                            modifier = Modifier.size(14.dp)
                        )

                        Text(
                            text = "프로필 수정",
                            fontFamily = FitMateFontFamily,
                            fontSize = 12.sp,
                            color = Gray500
                        )
                    }
                }
            }

            // 기본 정보 제목 영역과 카드 사이 간격 11dp
            Spacer(Modifier.height(11.dp))

            // 기본 정보 카드
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
                    BodyProfileInfoRow(
                        label = "성별",
                        value = "여성"
                    )

                    HorizontalDivider(
                        thickness = 1.dp,
                        color = BorderDefault
                    )

                    BodyProfileInfoRow(
                        label = "나이",
                        value = "28세"
                    )

                    HorizontalDivider(
                        thickness = 1.dp,
                        color = BorderDefault
                    )

                    BodyProfileInfoRow(
                        label = "키",
                        value = "163cm"
                    )

                    HorizontalDivider(
                        thickness = 1.dp,
                        color = BorderDefault
                    )

                    BodyProfileInfoRow(
                        label = "몸무게",
                        value = "70.0kg"
                    )
                }
            }

            Spacer(Modifier.height(39.dp))

            // 운동 설정 제목
            Text(
                text = "운동 설정",
                fontFamily = FitMateFontFamily,
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp,
                color = Gray900
            )

            Spacer(Modifier.height(12.dp))

            // 운동 목표
            BodyProfileSettingCard(
                iconRes = R.drawable.ic_profile_goal,
                label = "운동 목표",
                value = "체지방 감량"
            )

            Spacer(Modifier.height(8.dp))

            // 운동 방법
            BodyProfileSettingCard(
                iconRes = R.drawable.ic_profile_method,
                label = "운동 방법",
                value = "균형형"
            )

            Spacer(Modifier.height(8.dp))

            // 운동 경력
            BodyProfileSettingCard(
                iconRes = R.drawable.ic_profile_experience,
                label = "운동 경력",
                value = "중급"
            )

            Spacer(Modifier.height(8.dp))

            // 운동 가능 조건
            BodyProfileSettingCard(
                iconRes = R.drawable.ic_profile_calendar,
                label = "운동 가능 조건",
                value = "주 4회 · 60분"
            )

            Spacer(Modifier.height(8.dp))

            // 부담 부위
            BodyProfileSettingCard(
                iconRes = R.drawable.ic_profile_injury,
                label = "부담 부위",
                value = "없음"
            )
        }
    }
}

@Composable
private fun BodyProfileInfoRow(
    label: String,
    value: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                horizontal = 24.dp,
                vertical = 15.dp
            ),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            fontFamily = FitMateFontFamily,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            color = Gray500
        )

        Text(
            text = value,
            fontFamily = FitMateFontFamily,
            fontSize = 12.sp,
            color = Gray900
        )
    }
}

@Composable
private fun BodyProfileSettingCard(
    iconRes: Int,
    label: String,
    value: String
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        color = White,
        border = BorderStroke(
            width = 1.dp,
            color = BorderDefault
        )
    ) {
        Row(
            modifier = Modifier.padding(
                horizontal = 12.dp,
                vertical = 12.dp
            ),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .background(
                        color = Gray50,
                        shape = RoundedCornerShape(12.dp)
                    )
                    .border(
                        width = 1.dp,
                        color = BorderDefault,
                        shape = RoundedCornerShape(12.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(id = iconRes),
                    contentDescription = null,
                    tint = Color.Unspecified,
                    modifier = Modifier.size(20.dp)
                )
            }

            Spacer(Modifier.width(12.dp))

            Column {
                Text(
                    text = label,
                    fontFamily = FitMateFontFamily,
                    fontSize = 12.sp,
                    color = Gray500
                )

                Text(
                    text = value,
                    fontFamily = FitMateFontFamily,
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp,
                    color = Gray900
                )
            }
        }
    }
}

@Preview(
    showBackground = true,
    showSystemUi = false,
    name = "운동 프로필"
)
@Composable
private fun BodyProfileScreenPreview() {
    FitMateTheme {
        BodyProfileScreen()
    }
}
