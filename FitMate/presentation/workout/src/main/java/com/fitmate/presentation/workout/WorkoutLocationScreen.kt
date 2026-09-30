package com.fitmate.presentation.workout

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.DirectionsRun
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.OtherHouses
import androidx.compose.material.icons.filled.Warning
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
private val ScreenBackground = Color(0xFFF8F9FA)
private val CardBorder = Color(0xFFE2E6EA)
private val TextMuted = Color(0xFF6C757D)
private val SelectedBg = Color(0xFFE1ECF4)
private val SelectedBorder = Color(0xFF42688A)

private data class WorkoutLocation(val key: String, val label: String, val sub: String, val icon: ImageVector)

private val locations = listOf(
    WorkoutLocation("gym", "Fit Gym", "내 헬스장", Icons.Filled.FitnessCenter),
    WorkoutLocation("home", "집", "홈트", Icons.Filled.Home),
    WorkoutLocation("outdoor", "야외", "러닝 / 걷기", Icons.Filled.DirectionsRun),
    WorkoutLocation("other_gym", "다른 헬스장", "다른 헬스장", Icons.Filled.OtherHouses)
)

@Composable
fun WorkoutLocationScreen(
    currentRoutineLabel: String = "Fit Gym 기준 루틴",
    onBack: () -> Unit = {},
    onReadjustRoutine: (String) -> Unit = {}
) {
    var selected by remember { mutableStateOf("gym") }

    Column(modifier = Modifier.fillMaxSize().background(Color.White)) {
        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(top = 4.dp)) {
            IconButton(onClick = onBack) {
                Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "뒤로")
            }
            Text(text = "오늘 운동 장소", fontWeight = FontWeight.SemiBold, fontSize = MaterialTheme.typography.titleMedium.fontSize)
        }
        androidx.compose.foundation.layout.Box(modifier = Modifier.fillMaxWidth().height(1.dp).background(CardBorder))

        Column(modifier = Modifier.weight(1f).padding(horizontal = 16.dp, vertical = 28.dp)) {
            Text(text = "오늘은 어디에서\n운동하나요?", fontSize = MaterialTheme.typography.headlineMedium.fontSize)
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "현재 루틴: $currentRoutineLabel", color = TextMuted, fontSize = MaterialTheme.typography.bodySmall.fontSize)

            Spacer(modifier = Modifier.height(20.dp))
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.height(300.dp)
            ) {
                items(locations) { loc ->
                    val isSelected = loc.key == selected
                    Surface(
                        modifier = Modifier.aspectRatio(1.3f),
                        shape = RoundedCornerShape(16.dp),
                        color = if (isSelected) SelectedBg else Color.White,
                        border = androidx.compose.foundation.BorderStroke(1.dp, if (isSelected) SelectedBorder else CardBorder),
                        onClick = { selected = loc.key }
                    ) {
                        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                            androidx.compose.foundation.layout.Box(
                                modifier = Modifier.size(44.dp).background(if (isSelected) FitMateBlue else ScreenBackground, RoundedCornerShape(12.dp)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(imageVector = loc.icon, contentDescription = null, tint = if (isSelected) Color.White else TextMuted)
                            }
                            Column {
                                Text(text = loc.label, fontWeight = FontWeight.SemiBold, color = if (isSelected) SelectedBorder else Color(0xFF16181A))
                                Text(text = loc.sub, color = TextMuted, fontSize = MaterialTheme.typography.labelMedium.fontSize)
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))
            if (selected != "gym") {
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    color = Color(0xFFFEF9EC),
                    border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFF0C040))
                ) {
                    Row(modifier = Modifier.padding(horizontal = 16.dp, vertical = 14.dp), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                        Icon(imageVector = Icons.Filled.Warning, contentDescription = null, tint = Color(0xFFD97706), modifier = Modifier.size(20.dp))
                        Text(text = "현재 루틴에는 헬스장 머신 운동이 포함되어 있어요.", color = Color(0xFF92400E), fontSize = MaterialTheme.typography.bodySmall.fontSize)
                    }
                }
            }
        }

        Column(modifier = Modifier.padding(16.dp)) {
            Button(
                onClick = { onReadjustRoutine(selected) },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = FitMateBlue),
                shape = RoundedCornerShape(12.dp),
                enabled = selected != "gym"
            ) {
                Text(text = "오늘 루틴 다시 맞추기")
            }
        }
    }
}
