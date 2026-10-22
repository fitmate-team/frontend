package com.fitmate.presentation.workout

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.PlayArrow
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.fitmate.presentation.designsystem.theme.*

/** 중량 설정 / 운동 진행 가이드에서 공통으로 사용하는 내용. */
@Composable
fun WorkoutGuideContent(
    guide: String,
    bodyParts: List<String>,
    videoUrl: String,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Text("운동 가이드 영상", fontSize = 14.sp, fontWeight = FontWeight.SemiBold, color = TextPrimary)
        Spacer(Modifier.height(8.dp))
        Box(
            modifier = Modifier.fillMaxWidth().aspectRatio(346f / 192f)
                .clip(RoundedCornerShape(20.dp)).background(Color(0xFFE9EEF3)),
            contentAlignment = Alignment.Center
        ) {
            // videoUrl은 추후 플레이어 연동 시 사용. 현재는 영상 자리 표시 UI.
            Icon(Icons.Rounded.PlayArrow, contentDescription = "영상 재생 자리 표시", modifier = Modifier.size(60.dp), tint = Color(0xFF616B76))
        }
        Spacer(Modifier.height(16.dp))
        Text("운동 가이드", fontSize = 14.sp, fontWeight = FontWeight.SemiBold, color = TextPrimary)
        Spacer(Modifier.height(8.dp))
        Box(
            modifier = Modifier.fillMaxWidth().border(1.dp, BorderDefault, RoundedCornerShape(12.dp))
                .background(Color(0xFFF8F9FA), RoundedCornerShape(12.dp))
                .padding(horizontal = 16.dp, vertical = 14.dp)
        ) {
            Text(guide, fontSize = 14.sp, lineHeight = 21.sp, color = TextSecondary)
        }
        Spacer(Modifier.height(16.dp))
        Text("주요 운동 부위", fontSize = 14.sp, fontWeight = FontWeight.SemiBold, color = TextPrimary)
        Spacer(Modifier.height(12.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
            bodyParts.forEach { part ->
                Box(
                    modifier = Modifier.border(1.dp, BorderDefault, RoundedCornerShape(50))
                        .padding(horizontal = 18.dp, vertical = 10.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(part, fontSize = 14.sp, color = TextPrimary)
                }
            }
        }
    }
}
