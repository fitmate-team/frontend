
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
import androidx.compose.runtime.*
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
fun AppSettingsScreen(
    onBack: () -> Unit = {},
    onSave: () -> Unit = {},
    onLogout: () -> Unit = {},
    onDeleteAccount: () -> Unit = {}
) {
    var watchConnected by remember { mutableStateOf(true) }
    var healthConnected by remember { mutableStateOf(true) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(White)
    ) {
        SettingsHeader(
            title = "앱 설정",
            onBack = onBack
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp)
                .padding(top = 20.dp, bottom = 24.dp)
        ) {
            IntegrationSettingCard(
                iconRes = R.drawable.ic_settings_watch,
                title = "Galaxy Watch / Wear OS",
                description = "Galaxy Watch 또는 Wear OS 기기와 연동하여 심박수, 소모 칼로리, 운동 중 실시간 데이터를 자동으로 기록할 수 있어요.",
                checked = watchConnected,
                onCheckedChange = { watchConnected = it }
            )

            Spacer(Modifier.height(12.dp))

            IntegrationSettingCard(
                iconRes = R.drawable.ic_settings_health,
                title = "건강 데이터 연동",
                description = "Samsung Health, Google Fit 등의 건강 앱과 연동하면 수면, 걸음 수, 심박수 데이터를 AI 분석에 활용할 수 있어요.",
                checked = healthConnected,
                onCheckedChange = { healthConnected = it }
            )

            Spacer(Modifier.height(18.dp))

            SettingsActionButton(
                text = "로그아웃",
                onClick = onLogout,
                textColor = Gray700
            )

            Spacer(Modifier.height(8.dp))

            SettingsActionButton(
                text = "회원 탈퇴",
                onClick = onDeleteAccount,
                textColor = Red500
            )
        }
    }
}

@Composable
private fun IntegrationSettingCard(
    iconRes: Int,
    title: String,
    description: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        color = White,
        border = BorderStroke(1.dp, BorderDefault)
    ) {
        Column(
            modifier = Modifier.padding(
                horizontal = 16.dp,
                vertical = 16.dp
            )
        ) {

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(42.dp)
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
                        modifier = Modifier.size(32.dp)
                    )
                }

                Spacer(Modifier.width(12.dp))

                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = if (checked) "연결됨" else "연결 안 됨",
                            fontFamily = FitMateFontFamily,
                            fontSize = 12.sp,
                            color = Gray500
                        )

                        FitMateSmallSwitch(
                            checked = checked,
                            onCheckedChange = onCheckedChange
                        )
                    }

                    Text(
                        text = title,
                        fontFamily = FitMateFontFamily,
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp,
                        color = Gray900
                    )
                }
            }


            Spacer(Modifier.height(12.dp))

            HorizontalDivider(
                thickness = 1.dp,
                color = BorderDefault
            )

            Spacer(Modifier.height(12.dp))

            Text(
                text = description,
                modifier = Modifier.padding(horizontal = 8.dp),
                fontFamily = FitMateFontFamily,
                fontSize = 12.sp,
                lineHeight = 16.sp,
                color = Gray500
            )
        }
    }
}

@Composable
private fun SettingsActionButton(
    text: String,
    onClick: () -> Unit,
    textColor: Color
) {
    Surface(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        color = White,
        border = BorderStroke(1.dp, BorderDefault)
    ) {
        Box(
            modifier = Modifier.padding(vertical = 13.dp),
            contentAlignment = Alignment.Center

        ) {
            Text(
                text = text,
                fontFamily = FitMateFontFamily,
                fontSize = 16.sp,
                color = textColor
            )
        }
    }
}

@Preview(
    showBackground = true,
    showSystemUi = false,
    name = "앱 설정"
)
@Composable
private fun AppSettingsScreenPreview() {
    FitMateTheme {
        AppSettingsScreen()
    }
}
