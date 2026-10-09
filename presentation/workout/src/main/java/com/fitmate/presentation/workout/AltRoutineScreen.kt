package com.fitmate.presentation.workout

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.KeyboardDoubleArrowDown
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

private val FitMateBlue = Color(0xFF3E78AD)
private val HeaderBlue = Color(0xFF42688A)
private val ScreenBackground = Color(0xFFF8F9FA)
private val CardBorder = Color(0xFFE2E6EA)
private val TextMuted = Color(0xFF6C757D)

private data class Swap(val from: String, val to: String)

private val swaps = listOf(
    Swap("레그프레스", "덤벨 스쿼트"),
    Swap("랫풀다운", "밴드 랫풀다운"),
    Swap("케이블 로우", "덤벨 로우")
)

@Composable
fun AltRoutineScreen(
    locationLabel: String = "집",
    onBack: () -> Unit = {},
    onUseAdjustedRoutine: () -> Unit = {},
    onEditManually: () -> Unit = {}
) {
    Column(modifier = Modifier.fillMaxSize().background(ScreenBackground)) {
        Column(modifier = Modifier.fillMaxWidth().background(HeaderBlue).padding(20.dp)) {
            IconButton(onClick = onBack, modifier = Modifier.padding(start = 0.dp)) {
                Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "뒤로", tint = Color.White)
            }
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                Icon(imageVector = Icons.Filled.AutoAwesome, contentDescription = null, tint = Color(0xFFB0CBDC), modifier = Modifier.size(14.dp))
                Text(text = "ROUTINE ADJUSTMENT", color = Color(0xFFB0CBDC), fontWeight = FontWeight.Bold, fontSize = MaterialTheme.typography.labelSmall.fontSize)
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "${locationLabel}에서 할 수 있게\n루틴을 바꿨어요", color = Color.White, fontWeight = FontWeight.Bold, fontSize = MaterialTheme.typography.headlineSmall.fontSize)
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "사용 가능한 운동기구에 맞춰\n운동 구성을 조정했어요.", color = Color(0xFFB0CBDC), fontWeight = FontWeight.Bold, fontSize = MaterialTheme.typography.bodySmall.fontSize)
        }

        Column(modifier = Modifier.weight(1f).verticalScroll(rememberScrollState()).padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            swaps.forEach { swap ->
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp),
                    color = Color.White,
                    border = androidx.compose.foundation.BorderStroke(1.dp, CardBorder)
                ) {
                    Column(
                        modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp, vertical = 16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(text = swap.from, color = TextMuted, fontSize = MaterialTheme.typography.bodySmall.fontSize, textDecoration = androidx.compose.ui.text.style.TextDecoration.LineThrough)
                        Icon(imageVector = Icons.Filled.KeyboardDoubleArrowDown, contentDescription = null, tint = HeaderBlue)
                        Text(text = swap.to, color = HeaderBlue, fontWeight = FontWeight.Bold, fontSize = MaterialTheme.typography.titleMedium.fontSize)
                    }
                }
            }

            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                color = Color(0xFFF0FBF0),
                border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFB7DFB7))
            ) {
                Row(modifier = Modifier.padding(horizontal = 20.dp, vertical = 14.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    Icon(imageVector = Icons.Filled.CheckCircle, contentDescription = null, tint = Color(0xFF4CAF50))
                    Column {
                        Text(text = "플랭크", fontWeight = FontWeight.SemiBold)
                        Text(text = "그대로 수행 가능", color = Color(0xFF388E3C), fontSize = MaterialTheme.typography.labelMedium.fontSize)
                    }
                }
            }

            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                color = Color(0xFFE1ECF4),
                border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFB0CBDC))
            ) {
                Row(modifier = Modifier.padding(horizontal = 16.dp, vertical = 14.dp), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    Icon(imageVector = Icons.Filled.AutoAwesome, contentDescription = null, tint = HeaderBlue, modifier = Modifier.size(18.dp))
                    Text(text = "오늘 사용할 수 있는 기구를 기준으로 비슷한 근육군을 자극할 수 있는 운동으로 변경했어요.", color = Color(0xFF27384A), fontWeight = FontWeight.Bold, fontSize = MaterialTheme.typography.bodySmall.fontSize)
                }
            }
        }

        Column(modifier = Modifier.background(ScreenBackground).padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Button(
                onClick = onUseAdjustedRoutine,
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = FitMateBlue),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(text = "변경된 루틴으로 운동하기")
            }
            OutlinedButton(
                onClick = onEditManually,
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = HeaderBlue),
                border = androidx.compose.foundation.BorderStroke(1.dp, HeaderBlue),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(text = "직접 수정하기")
            }
        }
    }
}
