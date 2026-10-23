package com.fitmate.presentation.designsystem.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Info
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.fitmate.presentation.designsystem.theme.Blue100
import com.fitmate.presentation.designsystem.theme.Blue700
import com.fitmate.presentation.designsystem.theme.BorderDefault
import com.fitmate.presentation.designsystem.theme.FitMateTheme
import com.fitmate.presentation.designsystem.theme.Gray300
import com.fitmate.presentation.designsystem.theme.Gray700
import com.fitmate.presentation.designsystem.theme.ShapeMedium
import com.fitmate.presentation.designsystem.theme.Spacing1
import com.fitmate.presentation.designsystem.theme.Spacing3
import com.fitmate.presentation.designsystem.theme.TextSecondary
import com.fitmate.presentation.designsystem.theme.White
import com.fitmate.presentation.designsystem.theme.fitMateTextStyle

enum class InfoBannerType(
    internal val containerColor: Color,
    internal val iconColor: Color,
    internal val textColor: Color,
    internal val iconInset: Dp
) {
    // 파란 배경 안내 (신체 측정 정보)
    Brand(Blue100, Blue700, Blue700, 10.8.dp),

    // 흰 배경 회색 안내 (고려해야할 부분 면책 문구)
    Neutral(White, Gray300, TextSecondary, 14.8.dp)
}

// i 아이콘 + 설명 안내 박스. 텍스트는 아이콘 종류와 관계없이 왼쪽 40.8dp에서 시작한다
// title이 있으면 굵은 제목 아래에 작은 회색 설명을 붙인다
@Composable
fun InfoBanner(
    text: String,
    modifier: Modifier = Modifier,
    type: InfoBannerType = InfoBannerType.Brand,
    title: String? = null
) {
    val iconSize = 18.dp
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(ShapeMedium)
            .background(type.containerColor)
            .border(1.dp, BorderDefault, ShapeMedium)
            .padding(start = type.iconInset, end = 10.8.dp, bottom = 15.2.dp)
    ) {
        Icon(
            imageVector = Icons.Rounded.Info,
            contentDescription = null,
            tint = type.iconColor,
            modifier = Modifier
                .padding(top = type.iconInset)
                .size(iconSize)
        )
        Spacer(modifier = Modifier.width(40.8.dp - type.iconInset - iconSize))
        Column(modifier = Modifier.padding(top = 10.8.dp)) {
            if (title != null) {
                Text(text = title, style = fitMateTextStyle(13.sp, 20.sp, FontWeight.Bold), color = type.textColor)
                Spacer(modifier = Modifier.height(Spacing1))
                Text(text = text, style = fitMateTextStyle(11.sp, 20.sp), color = Gray700)
            } else {
                Text(text = text, style = fitMateTextStyle(13.sp, 20.sp), color = type.textColor)
            }
        }
    }
}

@Preview
@Composable
private fun InfoBannerPreview() {
    FitMateTheme {
        Column(verticalArrangement = Arrangement.spacedBy(Spacing3)) {
            InfoBanner(text = "최근 측정한 정보를 입력할수록\n더 세밀한 운동 추천에 활용할 수 있어요.")
            InfoBanner(
                text = "입력한 정보는 운동 선호 및 제한 설정에만 사용되며,\n의학적 진단이나 치료 목적으로 활용되지 않습니다.",
                type = InfoBannerType.Neutral
            )
        }
    }
}
