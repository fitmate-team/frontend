
package com.fitmate.presentation.mypage

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.fitmate.presentation.designsystem.R
import com.fitmate.presentation.designsystem.theme.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.shape.CircleShape


@Composable
internal fun SettingsHeader(
    title: String,
    onBack: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    start = 9.dp,
                    end = 24.dp,
                    top = 12.dp,
                    bottom = 12.dp
                ),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // 클릭 영역은 40 × 40dp
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clickable(
                        role = Role.Button,
                        onClickLabel = "뒤로가기",
                        onClick = onBack
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(
                        R.drawable.ic_settings_chevron_left
                    ),
                    contentDescription = "뒤로가기",
                    tint = Gray900,
                    modifier = Modifier.size(
                        width = 13.dp,
                        height = 24.dp
                    )
                )
            }

            Spacer(modifier = Modifier.width(7.dp))

            Text(
                text = title,
                fontFamily = FitMateNumberFontFamily,
                fontWeight = FontWeight.Normal,
                fontSize = 20.sp,
                color = Gray900
            )
        }

        HorizontalDivider(
            thickness = 1.dp,
            color = BorderDefault
        )
    }
}


@Composable
internal fun FitMateSmallSwitch(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Box(
        modifier = Modifier
            .size(width = 36.dp, height = 18.dp)
            .border(
                width = 1.dp,
                color = Blue600,
                shape = RoundedCornerShape(50)
            )
            .background(
                color = if (checked) Blue600 else White,
                shape = RoundedCornerShape(50)
            )
            .clickable {
                onCheckedChange(!checked)
            }
            .padding(horizontal = 3.dp),
        contentAlignment = if (checked) {
            Alignment.CenterEnd
        } else {
            Alignment.CenterStart
        }
    ) {
        Box(
            modifier = Modifier
                .size(12.dp)
                .then(
                    if (checked) {
                        Modifier.background(
                            color = White,
                            shape = CircleShape
                        )
                    } else {
                        Modifier.border(
                            width = 1.dp,
                            color = Blue600,
                            shape = CircleShape
                        )
                    }
                )
        )
    }
}
