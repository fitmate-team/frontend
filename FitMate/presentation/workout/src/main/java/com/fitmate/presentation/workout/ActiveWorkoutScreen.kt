package com.fitmate.presentation.workout

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Flag
import androidx.compose.material.icons.filled.History
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay

private val ActiveBlue = Color(0xFF3E78AD)
private val EditBlue = Color(0xFF42688A)
private val ScreenBackground = Color(0xFFF8F9FA)
private val CardBorder = Color(0xFFE2E6EA)
private val TextMuted = Color(0xFF6C757D)
private val InProgressBg = Color(0xFFE1ECF4)

private enum class SetStatus { DONE, IN_PROGRESS, PENDING }

private class SetState(weight: Int, reps: Int, status: SetStatus) {
    var weight by mutableIntStateOf(weight)
    var reps by mutableIntStateOf(reps)
    var status by mutableStateOf(status)
}

private class ExerciseState(val name: String, val aiRecommended: Boolean, val previousRecord: String, val sets: List<SetState>)

private fun buildExercises() = listOf(
    ExerciseState(
        name = "벤치프레스",
        aiRecommended = true,
        previousRecord = "이전 기록 37.5kg × 8회",
        sets = listOf(
            SetState(40, 8, SetStatus.DONE),
            SetState(40, 8, SetStatus.DONE),
            SetState(40, 8, SetStatus.IN_PROGRESS),
            SetState(40, 8, SetStatus.PENDING)
        )
    ),
    ExerciseState(
        name = "숄더프레스",
        aiRecommended = false,
        previousRecord = "이전 기록 9kg × 10회",
        sets = listOf(
            SetState(10, 10, SetStatus.IN_PROGRESS),
            SetState(10, 10, SetStatus.PENDING),
            SetState(10, 10, SetStatus.PENDING)
        )
    )
)

@Composable
fun ActiveWorkoutScreen(
    onRest: () -> Unit = {},
    onFinishWorkout: () -> Unit = {}
) {
    val exercises = remember { mutableStateListOf(*buildExercises().toTypedArray()) }
    var exerciseIndex by remember { mutableIntStateOf(0) }
    var isEditMode by remember { mutableStateOf(false) }
    var showEditConfirm by remember { mutableStateOf(false) }
    var showExitConfirm by remember { mutableStateOf(false) }
    var elapsedSeconds by remember { mutableIntStateOf(18 * 60 + 42) }

    LaunchedEffect(Unit) {
        while (true) {
            delay(1000)
            elapsedSeconds++
        }
    }

    val exercise = exercises[exerciseIndex]
    val currentSetIndex = exercise.sets.indexOfFirst { it.status == SetStatus.IN_PROGRESS }
    val remainingSets = exercises.sumOf { ex -> ex.sets.count { it.status != SetStatus.DONE } }

    fun advanceAfterSet() {
        val idx = exercise.sets.indexOfFirst { it.status == SetStatus.IN_PROGRESS }
        if (idx == -1) return
        exercise.sets[idx].status = SetStatus.DONE
        isEditMode = false

        val nextInExercise = exercise.sets.getOrNull(idx + 1)
        if (nextInExercise != null) {
            nextInExercise.status = SetStatus.IN_PROGRESS
            onRest()
        } else if (exerciseIndex < exercises.lastIndex) {
            exerciseIndex++
            exercises[exerciseIndex].sets.firstOrNull()?.status = SetStatus.IN_PROGRESS
            onRest()
        } else {
            onFinishWorkout()
        }
    }

    fun goToNextExercise() {
        if (exerciseIndex < exercises.lastIndex) {
            exerciseIndex++
            exercises[exerciseIndex].sets.forEachIndexed { i, set ->
                set.status = if (i == 0) SetStatus.IN_PROGRESS else SetStatus.PENDING
            }
            isEditMode = false
        }
    }

    Column(modifier = Modifier.fillMaxSize().background(ScreenBackground)) {
        Column(
            modifier = Modifier.fillMaxWidth().background(if (isEditMode) EditBlue else ActiveBlue).padding(horizontal = 20.dp, vertical = 20.dp).padding(top = 32.dp)
        ) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.Top) {
                Column {
                    Text(text = if (isEditMode) "수정중" else "운동 중", color = Color(0xFFB0CBDC), fontWeight = FontWeight.Bold, fontSize = MaterialTheme.typography.labelSmall.fontSize)
                    if (!isEditMode) {
                        Text(text = formatElapsed(elapsedSeconds), color = Color.White, fontWeight = FontWeight.Bold, fontSize = MaterialTheme.typography.displaySmall.fontSize)
                    }
                }
                IconButton(
                    onClick = { if (!isEditMode) showEditConfirm = true },
                    modifier = Modifier.background(Color.White.copy(alpha = 0.12f), RoundedCornerShape(12.dp)).size(44.dp)
                ) {
                    Icon(imageVector = Icons.Filled.CheckCircle, contentDescription = "수정 모드", tint = Color.White)
                }
            }
        }

        Column(modifier = Modifier.weight(1f).verticalScroll(rememberScrollState()).padding(horizontal = 20.dp)) {
            Spacer(modifier = Modifier.height(16.dp))
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(text = exercise.name, fontWeight = FontWeight.Bold, fontSize = MaterialTheme.typography.headlineSmall.fontSize)
                if (exercise.aiRecommended) {
                    Surface(shape = RoundedCornerShape(100.dp), color = EditBlue) {
                        Text(text = "AI 추천", color = Color.White, fontSize = MaterialTheme.typography.labelSmall.fontSize, modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp))
                    }
                }
            }
            Spacer(modifier = Modifier.height(6.dp))
            Text(text = "${exercise.sets.first().weight}kg × ${exercise.sets.first().reps}회 · ${exercise.sets.size}세트", color = TextMuted, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(6.dp))
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                Icon(imageVector = Icons.Filled.History, contentDescription = null, tint = EditBlue, modifier = Modifier.size(14.dp))
                Text(text = exercise.previousRecord, color = EditBlue, fontWeight = FontWeight.Bold, fontSize = MaterialTheme.typography.labelMedium.fontSize)
            }

            Spacer(modifier = Modifier.height(16.dp))
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                color = Color.White,
                border = androidx.compose.foundation.BorderStroke(1.dp, CardBorder)
            ) {
                Column {
                    Row(modifier = Modifier.fillMaxWidth().background(ScreenBackground).padding(horizontal = 16.dp, vertical = 10.dp)) {
                        Text(text = "세트", color = TextMuted, fontWeight = FontWeight.Bold, fontSize = MaterialTheme.typography.labelSmall.fontSize, modifier = Modifier.width(40.dp))
                        Text(text = "중량", color = TextMuted, fontWeight = FontWeight.Bold, fontSize = MaterialTheme.typography.labelSmall.fontSize, modifier = Modifier.weight(1f))
                        Text(text = "횟수", color = TextMuted, fontWeight = FontWeight.Bold, fontSize = MaterialTheme.typography.labelSmall.fontSize, modifier = Modifier.weight(1f))
                        Text(text = "상태", color = TextMuted, fontWeight = FontWeight.Bold, fontSize = MaterialTheme.typography.labelSmall.fontSize, modifier = Modifier.width(56.dp))
                    }
                    exercise.sets.forEachIndexed { i, set ->
                        val isCurrentEditable = isEditMode && i == currentSetIndex
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(if (set.status == SetStatus.IN_PROGRESS) InProgressBg else Color.Transparent)
                                .padding(horizontal = 16.dp, vertical = 12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(text = "${i + 1}", color = if (set.status == SetStatus.IN_PROGRESS) EditBlue else TextMuted, fontWeight = FontWeight.Bold, modifier = Modifier.width(40.dp))
                            if (isCurrentEditable) {
                                OutlinedTextField(
                                    value = set.weight.toString(),
                                    onValueChange = { v -> v.toIntOrNull()?.let { set.weight = it } },
                                    modifier = Modifier.weight(1f).padding(end = 8.dp),
                                    textStyle = MaterialTheme.typography.bodyMedium,
                                    singleLine = true
                                )
                                OutlinedTextField(
                                    value = set.reps.toString(),
                                    onValueChange = { v -> v.toIntOrNull()?.let { set.reps = it } },
                                    modifier = Modifier.weight(1f).padding(end = 8.dp),
                                    textStyle = MaterialTheme.typography.bodyMedium,
                                    singleLine = true
                                )
                            } else {
                                Text(text = "${set.weight}kg", fontWeight = FontWeight.Bold, modifier = Modifier.weight(1f), color = if (set.status == SetStatus.PENDING) TextMuted else Color(0xFF16181A))
                                Text(text = "${set.reps}회", fontWeight = FontWeight.Bold, modifier = Modifier.weight(1f), color = if (set.status == SetStatus.PENDING) TextMuted else Color(0xFF16181A))
                            }
                            Box(modifier = Modifier.width(56.dp), contentAlignment = Alignment.CenterEnd) {
                                when (set.status) {
                                    SetStatus.DONE -> Icon(imageVector = Icons.Filled.CheckCircle, contentDescription = "완료", tint = EditBlue)
                                    SetStatus.IN_PROGRESS -> Surface(shape = RoundedCornerShape(100.dp), color = InProgressBg) {
                                        Text(text = "진행", color = EditBlue, fontWeight = FontWeight.Bold, fontSize = MaterialTheme.typography.labelSmall.fontSize, modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp))
                                    }
                                    SetStatus.PENDING -> Surface(shape = RoundedCornerShape(100.dp), color = ScreenBackground, border = androidx.compose.foundation.BorderStroke(1.dp, CardBorder)) {
                                        Text(text = "대기", color = TextMuted, fontWeight = FontWeight.Bold, fontSize = MaterialTheme.typography.labelSmall.fontSize, modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp))
                                    }
                                }
                            }
                        }
                    }
                }
            }

            if (isEditMode) {
                Spacer(modifier = Modifier.height(16.dp))
                androidx.compose.material3.OutlinedButton(
                    onClick = { isEditMode = false },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = EditBlue),
                    border = androidx.compose.foundation.BorderStroke(1.dp, EditBlue),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(text = "중량 · 횟수 수정 적용")
                }
            }
            Spacer(modifier = Modifier.height(24.dp))
        }

        Column(modifier = Modifier.background(ScreenBackground)) {
            Row(modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp, vertical = 10.dp), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                Surface(
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(14.dp),
                    color = Color(0xFFEDF0F2),
                    onClick = { advanceAfterSet() }
                ) {
                    Text(text = "세트 포기", color = EditBlue, fontWeight = FontWeight.Bold, textAlign = androidx.compose.ui.text.style.TextAlign.Center, modifier = Modifier.padding(vertical = 14.dp).fillMaxWidth())
                }
                Surface(
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(14.dp),
                    color = ActiveBlue,
                    onClick = { advanceAfterSet() }
                ) {
                    Text(text = "세트 완료", color = Color.White, fontWeight = FontWeight.Bold, textAlign = androidx.compose.ui.text.style.TextAlign.Center, modifier = Modifier.padding(vertical = 14.dp).fillMaxWidth())
                }
            }

            Box(modifier = Modifier.fillMaxWidth().height(1.dp).background(CardBorder))
            Row(modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp, vertical = 12.dp), horizontalArrangement = Arrangement.spacedBy(10.dp), verticalAlignment = Alignment.CenterVertically) {
                Surface(
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(10.dp),
                    color = Color.White,
                    border = androidx.compose.foundation.BorderStroke(1.dp, CardBorder),
                    onClick = { goToNextExercise() }
                ) {
                    Row(modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp), verticalAlignment = Alignment.CenterVertically) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(text = "다음 운동", color = TextMuted, fontSize = MaterialTheme.typography.labelSmall.fontSize)
                            Text(
                                text = exercises.getOrNull(exerciseIndex + 1)?.name ?: "마지막 운동",
                                fontWeight = FontWeight.Bold,
                                fontSize = MaterialTheme.typography.labelMedium.fontSize
                            )
                        }
                        Icon(imageVector = Icons.Filled.ChevronRight, contentDescription = null, tint = TextMuted)
                    }
                }
                Surface(
                    shape = RoundedCornerShape(10.dp),
                    color = InProgressBg,
                    border = androidx.compose.foundation.BorderStroke(1.dp, EditBlue),
                    onClick = { showExitConfirm = true }
                ) {
                    Row(modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        Icon(imageVector = Icons.Filled.Flag, contentDescription = null, tint = EditBlue, modifier = Modifier.size(16.dp))
                        Text(text = "운동 완료", color = EditBlue, fontWeight = FontWeight.Bold, fontSize = MaterialTheme.typography.labelMedium.fontSize)
                    }
                }
            }
        }
    }

    if (showEditConfirm) {
        AlertDialog(
            onDismissRequest = { showEditConfirm = false },
            title = { Text(text = "운동을 수정할까요?") },
            text = { Text(text = "수정을 완료하면은 다시 바꿀수 없어요.") },
            confirmButton = {
                TextButton(onClick = { isEditMode = true; showEditConfirm = false }) { Text(text = "확인") }
            },
            dismissButton = {
                TextButton(onClick = { showEditConfirm = false }) { Text(text = "취소") }
            }
        )
    }

    if (showExitConfirm) {
        AlertDialog(
            onDismissRequest = { showExitConfirm = false },
            title = { Text(text = "운동을 종료할까요?") },
            text = { Text(text = "아직 완료하지 않은 운동이 ${remainingSets}개 남아 있어요.\n지금 종료하면 남은 운동은 미완료로 기록돼요.") },
            confirmButton = {
                TextButton(onClick = { showExitConfirm = false; onFinishWorkout() }) { Text(text = "확인") }
            },
            dismissButton = {
                TextButton(onClick = { showExitConfirm = false }) { Text(text = "취소") }
            }
        )
    }
}

private fun formatElapsed(totalSeconds: Int): String {
    val h = totalSeconds / 3600
    val m = (totalSeconds % 3600) / 60
    val s = totalSeconds % 60
    return "%02d:%02d:%02d".format(h, m, s)
}
