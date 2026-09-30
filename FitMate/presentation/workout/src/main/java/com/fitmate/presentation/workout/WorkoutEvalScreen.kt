package com.fitmate.presentation.workout

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.SentimentDissatisfied
import androidx.compose.material.icons.filled.SentimentNeutral
import androidx.compose.material.icons.filled.SentimentSatisfied
import androidx.compose.material.icons.filled.SentimentVeryDissatisfied
import androidx.compose.material.icons.filled.SentimentVerySatisfied
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

private val FitMateBlue = Color(0xFF3E78AD)
private val SelectedBlue = Color(0xFF42688A)
private val CardBorder = Color(0xFFE2E6EA)
private val TextMuted = Color(0xFF6C757D)
private val SelectedBg = Color(0xFFE1ECF4)

private data class EvalOption(val key: String, val icon: ImageVector, val title: String, val subtitle: String)

private val evalOptions = listOf(
    EvalOption("too_easy", Icons.Filled.SentimentVerySatisfied, "너무 쉬웠어요", "다음엔 더 높은 강도로!"),
    EvalOption("easy", Icons.Filled.SentimentSatisfied, "조금 여유 있었어요", "중량이나 횟수를 늘려볼까요"),
    EvalOption("moderate", Icons.Filled.SentimentNeutral, "적당히 힘들었어요", "현재 루틴을 유지해요"),
    EvalOption("hard", Icons.Filled.SentimentDissatisfied, "거의 한계였어요", "내일은 꼭 충분히 쉬세요"),
    EvalOption("too_hard", Icons.Filled.SentimentVeryDissatisfied, "목표 수행이 어려워졌어요", "강도를 조금 낮춰볼게요")
)

@Composable
fun WorkoutEvalScreen(
    onBack: () -> Unit = {},
    onViewAnalysis: (String) -> Unit = {}
) {
    var selected by remember { mutableStateOf("moderate") }

    Column(modifier = Modifier.fillMaxSize().background(Color.White)) {
        IconButton(onClick = onBack, modifier = Modifier.padding(4.dp)) {
            Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "뒤로")
        }

        Column(modifier = Modifier.weight(1f).padding(horizontal = 20.dp)) {
            Text(text = "오늘 운동은\n어땠나요?", fontWeight = FontWeight.Bold, fontSize = MaterialTheme.typography.headlineMedium.fontSize)
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "오늘의 체감 난이도를 선택해주세요.", color = TextMuted, fontWeight = FontWeight.Bold)

            Spacer(modifier = Modifier.height(24.dp))
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                evalOptions.forEach { option ->
                    val isSelected = option.key == selected
                    Surface(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        color = if (isSelected) SelectedBg else Color.White,
                        border = androidx.compose.foundation.BorderStroke(1.dp, if (isSelected) SelectedBlue else CardBorder),
                        onClick = { selected = option.key }
                    ) {
                        Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                            Icon(imageVector = option.icon, contentDescription = null, tint = if (isSelected) SelectedBlue else TextMuted, modifier = Modifier.size(28.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text(text = option.title, fontWeight = FontWeight.Bold, color = if (isSelected) SelectedBlue else Color(0xFF16181A))
                                Text(text = option.subtitle, color = TextMuted, fontSize = MaterialTheme.typography.labelMedium.fontSize)
                            }
                            if (isSelected) {
                                Icon(imageVector = Icons.Filled.CheckCircle, contentDescription = null, tint = SelectedBlue)
                            }
                        }
                    }
                }
            }
        }

        Column(modifier = Modifier.padding(20.dp)) {
            Button(
                onClick = { onViewAnalysis(selected) },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = FitMateBlue),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(text = "운동 분석 보기")
            }
        }
    }
}
