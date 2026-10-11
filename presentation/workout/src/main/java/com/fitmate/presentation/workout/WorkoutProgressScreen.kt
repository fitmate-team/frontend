package com.fitmate.presentation.workout

import android.os.SystemClock
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay

private val ProgressBlue = Color(0xFF087FF0)
private val ProgressGray = Color(0xFFAAB2B8)
private val ProgressBorder = Color(0xFFE2E6EA)

/** 서버 연동 전 화면 테스트용 모델. durationSeconds != null 이면 유산소 타이머 운동. */
data class WorkoutProgressExercise(
    val id: Int,
    val name: String,
    val weightKg: Int = 0,
    val reps: Int = 8,
    val sets: Int = 4,
    val restSeconds: Int = 30,
    val durationSeconds: Int? = null,
    val guide: String = "허리를 곧게 펴고 올바른 자세를 유지해주세요.\n무리하지 않고 천천히 움직여주세요.",
    val bodyParts: List<String> = emptyList(),
    val videoUrl: String = ""
)

private enum class WorkoutPhase { ACTIVE, REST, FINISHED }
private enum class WorkoutDialog { NEXT, QUIT, FINISH }

@Composable
fun WorkoutProgressScreen(
    exercises: List<WorkoutProgressExercise> = listOf(
        WorkoutProgressExercise(1, "벤치프레스", 40, 8, 4, 30, bodyParts = listOf("가슴", "삼두", "어깨")),
        WorkoutProgressExercise(2, "인클라인 덤벨프레스", 20, 8, 4, 30, bodyParts = listOf("가슴", "어깨")),
        WorkoutProgressExercise(3, "체스트프레스", 40, 8, 4, 30, bodyParts = listOf("가슴", "삼두")),
        WorkoutProgressExercise(4, "줄넘기", durationSeconds = 15 * 60, bodyParts = listOf("전신"))
    ),
    onBack: () -> Unit = {},
    onFinish: (totalSeconds: Long) -> Unit = {},
    modifier: Modifier = Modifier
) {
    if (exercises.isEmpty()) return
    var currentIndex by remember { mutableIntStateOf(0) }
    val completedSets = remember { mutableStateMapOf<Int, Int>() }
    var phase by remember { mutableStateOf(WorkoutPhase.ACTIVE) }
    var paused by remember { mutableStateOf(false) }
    var activeSeconds by remember { mutableLongStateOf(0L) }
    var totalSeconds by remember { mutableLongStateOf(0L) }
    var restRemaining by remember { mutableIntStateOf(0) }
    var pendingIndex by remember { mutableIntStateOf(0) }
    var dialog by remember { mutableStateOf<WorkoutDialog?>(null) }
    var showGuide by remember { mutableStateOf(false) }

    val exercise = exercises[currentIndex]
    val completed = completedSets[exercise.id] ?: 0
    val isCardio = exercise.durationSeconds != null
    val isLast = currentIndex == exercises.lastIndex
    val allSetsDone = !isCardio && completed >= exercise.sets

    fun switchExercise(index: Int) {
        currentIndex = index
        phase = WorkoutPhase.ACTIVE
        paused = false
        activeSeconds = 0L
        restRemaining = 0
    }

    // 실제 경과시간 기반: 백그라운드 지연에도 초 단위 오차가 누적되지 않음.
    LaunchedEffect(phase, paused, currentIndex) {
        var last = SystemClock.elapsedRealtime()
        var carryMs = 0L
        while (phase != WorkoutPhase.FINISHED) {
            delay(100L)
            val now = SystemClock.elapsedRealtime()
            val elapsed = now - last
            last = now
            if (!paused) {
                carryMs += elapsed
                val whole = carryMs / 1000L
                if (whole > 0) {
                    carryMs %= 1000L
                    totalSeconds += whole
                    if (phase == WorkoutPhase.ACTIVE) {
                        activeSeconds += whole
                    } else {
                        restRemaining = (restRemaining - whole.toInt()).coerceAtLeast(0)
                        if (restRemaining == 0) {
                            phase = WorkoutPhase.ACTIVE
                            activeSeconds = 0L
                            paused = false
                        }
                    }
                }
            }
        }
    }

    if (showGuide) {
        WorkoutProgressGuideScreen(
            exercise = exercise,
            onBack = { showGuide = false }
        )
        return
    }

    Column(modifier.fillMaxSize().background(Color.White)) {
        Row(
            Modifier.fillMaxWidth().height(72.dp).padding(horizontal = 20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = { dialog = WorkoutDialog.QUIT }) {
                Icon(Icons.AutoMirrored.Rounded.ArrowBack, "뒤로가기")
            }
            Text("운동 진행", fontSize = 20.sp, fontWeight = FontWeight.Bold)
        }
        LazyRow(
            modifier = Modifier.fillMaxWidth(),
            contentPadding = PaddingValues(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(0.dp)
        ) {
            itemsIndexed(exercises) { index, item ->
                Column(
                    modifier = Modifier.clickable {
                        if (index != currentIndex) {
                            pendingIndex = index
                            dialog = WorkoutDialog.NEXT
                        }
                    }.padding(horizontal = 12.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        item.name,
                        color = if (index == currentIndex) ProgressBlue else Color.DarkGray,
                        fontSize = 13.sp,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        fontWeight = if (index == currentIndex) FontWeight.Bold else FontWeight.Normal
                    )
                    Spacer(Modifier.height(12.dp))
                    Box(
                        Modifier.width(82.dp).height(3.dp)
                            .background(if (index == currentIndex) ProgressBlue else Color.Transparent)
                    )
                }
            }
        }
        HorizontalDivider(color = ProgressBorder)

        Column(
            modifier = Modifier.weight(1f).verticalScroll(rememberScrollState())
                .padding(horizontal = 22.dp, vertical = 24.dp)
        ) {
            Text(exercise.name, fontSize = 24.sp, fontWeight = FontWeight.Bold)
            Text(
                when {
                    isCardio -> "유산소 운동 진행 중"
                    allSetsDone -> "${exercise.sets}세트 완료"
                    phase == WorkoutPhase.REST -> "${completed}세트 완료"
                    else -> "${(completed + 1).coerceAtMost(exercise.sets)}세트 진행 중"
                },
                color = Color.Gray, fontSize = 13.sp
            )
            Spacer(Modifier.height(22.dp))

            if (isCardio) {
                Column(
                    Modifier.fillMaxWidth().border(1.dp, ProgressBorder, RoundedCornerShape(14.dp))
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("유산소 운동 시간", color = Color.Gray, fontSize = 13.sp)
                    Spacer(Modifier.height(18.dp))
                    Box(
                        Modifier.size(218.dp).border(4.dp, ProgressBlue, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(formatTime(activeSeconds), color = ProgressBlue, fontSize = 32.sp, fontWeight = FontWeight.Bold)
                            IconButton(onClick = { paused = !paused }) {
                                Icon(if (paused) Icons.Rounded.PlayArrow else Icons.Rounded.Pause,
                                    if (paused) "재생" else "일시정지", tint = ProgressBlue)
                            }
                        }
                    }
                    Spacer(Modifier.height(20.dp))
                    Text("총 운동 시간  |  ${formatTime(totalSeconds)}", color = Color.Gray, fontSize = 13.sp)
                }
            } else {
                Column(
                    Modifier.fillMaxWidth().border(1.dp, ProgressBorder, RoundedCornerShape(14.dp))
                        .padding(horizontal = 18.dp, vertical = 32.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text("${exercise.weightKg}", color = ProgressBlue, fontSize = 56.sp, fontWeight = FontWeight.Bold)
                        Text(" kg", color = Color(0xFF195183), fontSize = 15.sp, fontWeight = FontWeight.Bold)
                        Text("  ×  ", color = ProgressGray, fontSize = 32.sp)
                        Text("${exercise.reps}", color = ProgressBlue, fontSize = 56.sp, fontWeight = FontWeight.Bold)
                        Text(" 회", color = Color(0xFF195183), fontSize = 15.sp, fontWeight = FontWeight.Bold)
                    }
                    Spacer(Modifier.height(28.dp))
                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
                        repeat(exercise.sets) { index ->
                            val done = index < completed
                            val now = index == completed && !allSetsDone
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Box(
                                    Modifier.size(28.dp).border(1.5.dp,
                                        if (done || now) ProgressBlue else ProgressGray, CircleShape)
                                        .background(if (done || now) ProgressBlue else Color.White, CircleShape),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(if (done) "✓" else if (now) "${index + 1}" else "",
                                        color = Color.White, fontSize = 14.sp)
                                }
                                Spacer(Modifier.height(6.dp))
                                Text(
                                    when { done -> "완료"; now -> "진행"; else -> "${index + 1}" },
                                    color = if (now) ProgressBlue else ProgressGray,
                                    fontSize = 12.sp
                                )
                            }
                        }
                    }
                }
                Spacer(Modifier.height(16.dp))
                Row(
                    Modifier.fillMaxWidth().border(1.dp, Color(0xFFD7E9FF), RoundedCornerShape(10.dp))
                        .padding(14.dp), verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(Modifier.weight(1f)) {
                        Text(if (phase == WorkoutPhase.REST) "휴게 시간" else "운동 시간", color = Color.Gray, fontSize = 12.sp)
                        Text(
                            if (phase == WorkoutPhase.REST) formatTime(restRemaining.toLong()) else formatTime(totalSeconds),
                            color = ProgressBlue, fontSize = 30.sp, fontWeight = FontWeight.Bold
                        )
                    }
                    OutlinedIconButton(onClick = { paused = !paused }) {
                        Icon(if (paused) Icons.Rounded.PlayArrow else Icons.Rounded.Pause,
                            if (paused) "재생" else "일시정지", tint = ProgressBlue)
                    }
                }
            }
            Spacer(Modifier.height(12.dp))
            Row(
                Modifier.fillMaxWidth().border(1.dp, ProgressBorder, RoundedCornerShape(10.dp))
                    .clickable { showGuide = true }.padding(14.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(Icons.Rounded.PlayCircle, null, tint = ProgressBlue, modifier = Modifier.size(19.dp))
                Spacer(Modifier.width(10.dp))
                Text("가이드 영상 확인하기", fontSize = 13.sp, fontWeight = FontWeight.Medium)
                Spacer(Modifier.weight(1f))
                Icon(Icons.Rounded.ChevronRight, null, tint = ProgressBorder)
            }
        }

        Column(Modifier.fillMaxWidth().padding(horizontal = 22.dp, vertical = 12.dp)) {
            Button(
                onClick = {
                    when {
                        isCardio || allSetsDone -> {
                            if (isLast) dialog = WorkoutDialog.FINISH
                            else { pendingIndex = currentIndex + 1; switchExercise(pendingIndex) }
                        }
                        phase == WorkoutPhase.REST -> {
                            phase = WorkoutPhase.ACTIVE
                            activeSeconds = 0L
                            paused = false
                        }
                        else -> {
                            completedSets[exercise.id] = (completed + 1).coerceAtMost(exercise.sets)
                            if (completed + 1 < exercise.sets) {
                                phase = WorkoutPhase.REST
                                restRemaining = exercise.restSeconds
                                paused = false
                            } else {
                                phase = WorkoutPhase.ACTIVE
                                activeSeconds = 0L
                            }
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth().height(52.dp),
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.buttonColors(containerColor = ProgressBlue)
            ) {
                Text(
                    when {
                        isCardio || allSetsDone -> if (isLast) "운동 완료" else "다음 운동"
                        phase == WorkoutPhase.REST -> "세트 시작"
                        else -> "세트 완료"
                    }, fontWeight = FontWeight.Bold
                )
            }
            Spacer(Modifier.height(10.dp))
            OutlinedButton(
                onClick = { dialog = WorkoutDialog.QUIT },
                modifier = Modifier.fillMaxWidth().height(48.dp),
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = ProgressGray),
                border = BorderStroke(1.dp, ProgressBorder)
            ) { Text("운동 그만하기") }
        }
    }

    dialog?.let { kind ->
        val title = when (kind) {
            WorkoutDialog.NEXT -> "다음 운동으로 넘어갈까요?"
            WorkoutDialog.QUIT -> "운동을 종료할까요?"
            WorkoutDialog.FINISH -> "운동을 완료할까요?"
        }
        val message = when (kind) {
            WorkoutDialog.NEXT -> "아직 완료하지 않은 세트가 남아있어요.\n지금 종료하면 남은 세트는 세트 포기로 기록돼요."
            WorkoutDialog.QUIT -> "아직 완료하지 않은 운동이 남아있어요.\n지금 종료하면 남은 운동은 미완료로 기록돼요."
            WorkoutDialog.FINISH -> "운동 기록을 저장하고 종료할까요?"
        }
        AlertDialog(
            onDismissRequest = { dialog = null },
            title = { Text(title, fontSize = 17.sp, fontWeight = FontWeight.Bold) },
            text = { Text(message, fontSize = 13.sp, color = Color.Gray) },
            confirmButton = {
                Button(
                    onClick = {
                        dialog = null
                        when (kind) {
                            WorkoutDialog.NEXT -> switchExercise(pendingIndex)
                            WorkoutDialog.QUIT, WorkoutDialog.FINISH -> {
                                phase = WorkoutPhase.FINISHED
                                onFinish(totalSeconds)
                            }
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = ProgressBlue)
                ) { Text("확인") }
            },
            dismissButton = {
                OutlinedButton(onClick = { dialog = null }) { Text("취소") }
            },
            shape = RoundedCornerShape(24.dp)
        )
    }
}

/** 중량 설정 페이지의 가이드 텍스트/운동 부위 정보를 그대로 재사용하는 안내 화면. */
@Composable
private fun WorkoutProgressGuideScreen(
    exercise: WorkoutProgressExercise,
    onBack: () -> Unit
) {
    Column(Modifier.fillMaxSize().background(Color.White)) {
        Row(Modifier.fillMaxWidth().height(72.dp).padding(horizontal = 16.dp), verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = onBack) { Icon(Icons.AutoMirrored.Rounded.ArrowBack, "뒤로가기") }
            Text("운동 가이드", fontSize = 20.sp, fontWeight = FontWeight.Bold)
        }
        HorizontalDivider(color = ProgressBorder)
        Column(Modifier.weight(1f).verticalScroll(rememberScrollState()).padding(24.dp)) {
            Text(exercise.name, fontSize = 26.sp, fontWeight = FontWeight.Bold)
            Spacer(Modifier.height(8.dp))
            Text("올바른 자세를 확인하고 운동을 진행해주세요.", fontSize = 14.sp, color = Color.Gray)
            Spacer(Modifier.height(32.dp))
            Text("운동 가이드 영상", fontWeight = FontWeight.Bold)
            Spacer(Modifier.height(12.dp))
            Box(
                Modifier.fillMaxWidth().height(190.dp).background(Color(0xFFF8F9FA), RoundedCornerShape(16.dp)),
                contentAlignment = Alignment.Center
            ) { Icon(Icons.Rounded.PlayCircle, "영상", tint = ProgressBlue, modifier = Modifier.size(48.dp)) }
            Spacer(Modifier.height(26.dp))
            Text("운동 가이드", fontWeight = FontWeight.Bold)
            Spacer(Modifier.height(12.dp))
            Text(
                exercise.guide,
                modifier = Modifier.fillMaxWidth().background(Color(0xFFF8F9FA), RoundedCornerShape(12.dp)).padding(16.dp),
                fontSize = 14.sp, lineHeight = 23.sp
            )
            Spacer(Modifier.height(24.dp))
            Text("주요 운동 부위", fontWeight = FontWeight.Bold)
            Spacer(Modifier.height(12.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                exercise.bodyParts.forEach { part ->
                    Text(part, modifier = Modifier.border(1.dp, ProgressBorder, RoundedCornerShape(24.dp))
                        .padding(horizontal = 14.dp, vertical = 8.dp), fontSize = 13.sp)
                }
            }
        }
        Button(
            onClick = onBack,
            modifier = Modifier.fillMaxWidth().padding(20.dp).height(52.dp),
            colors = ButtonDefaults.buttonColors(containerColor = ProgressBlue),
            shape = RoundedCornerShape(10.dp)
        ) { Text("다시 운동하기") }
    }
}

private fun formatTime(seconds: Long): String {
    val safe = seconds.coerceAtLeast(0L)
    return "%02d:%02d:%02d".format(safe / 3600, (safe / 60) % 60, safe % 60)
}
