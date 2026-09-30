package com.fitmate.presentation.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay

private val Blue700 = Color(0xFF3E78AD)
private val Blue400 = Color(0xFF9BC0DE)
private val Gray200 = Color(0xFFE2E6EA)
private val Gray300 = Color(0xFFA7AEB3)

@Composable
fun AIAnalysisLoadingScreen(
    onComplete: () -> Unit
) {
    LaunchedEffect(Unit) {
        delay(1600)
        onComplete()
    }

    Column(
        modifier = Modifier.fillMaxSize().background(Blue700).padding(horizontal = 28.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box(
            modifier = Modifier
                .size(140.dp)
                .background(Gray200, RoundedCornerShape(17.dp))
                .border(BorderStroke(1.dp, Gray300), RoundedCornerShape(17.dp))
        )

        Text(
            "회원님에게 맞는 운동을\n분석하고 있어요",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(top = 36.dp)
        )

        Column(
            modifier = Modifier
                .padding(top = 36.dp)
                .fillMaxWidth()
                .background(Color.White.copy(alpha = 0.08f), RoundedCornerShape(16.dp))
                .padding(20.dp)
        ) {
            listOf("운동 목표 분석", "운동 경험 확인", "운동 가능 시간 확인", "사용 가능한 기구 확인").forEach { label ->
                Row(modifier = Modifier.padding(bottom = 14.dp), verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier.size(24.dp).background(Color.White.copy(alpha = 0.9f), RoundedCornerShape(12.dp)),
                        contentAlignment = Alignment.Center
                    ) { Icon(Icons.Default.Check, contentDescription = null, tint = Blue700, modifier = Modifier.size(14.dp)) }
                    Text(label, fontSize = 14.sp, color = Color.White, modifier = Modifier.padding(start = 12.dp))
                }
            }
        }

        Box(
            modifier = Modifier
                .padding(top = 28.dp)
                .fillMaxWidth()
                .height(4.dp)
                .background(Color.White.copy(alpha = 0.15f), RoundedCornerShape(2.dp))
        ) {
            Box(modifier = Modifier.fillMaxWidth(0.88f).height(4.dp).background(Blue400, RoundedCornerShape(2.dp)))
        }
        Text("잠시만 기다려주세요.", fontSize = 13.sp, color = Color.White.copy(alpha = 0.45f), modifier = Modifier.padding(top = 20.dp))
    }
}
