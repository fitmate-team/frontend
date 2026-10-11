package com.fitmate.presentation.workout

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.rounded.ArrowDropDown
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.Place
import androidx.compose.material.icons.outlined.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp

import com.fitmate.presentation.designsystem.R
import com.fitmate.presentation.designsystem.theme.*

enum class WorkoutPlace {
    GYM,
    HOME,
    OUTDOOR
}

@Composable
fun WorkoutLocationScreen(
    currentGym: String = "Fit Gym",
    gymOptions: List<String> = listOf(
        "Fit Gym",
        "별내 피트니스 센터"
    ),
    onBack: () -> Unit = {},
    onConfirm: (WorkoutPlace, String?) -> Unit = { _, _ -> },
    modifier: Modifier = Modifier
) {

    var selectedPlace by remember {
        mutableStateOf(WorkoutPlace.GYM)
    }

    var selectedGym by remember(currentGym) {
        mutableStateOf(currentGym)
    }

    var expanded by remember {
        mutableStateOf(false)
    }

    val isChanged =
        selectedPlace != WorkoutPlace.GYM ||
                selectedGym != currentGym

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(White)
    ) {

        // =========================
        // 상단 헤더
        // =========================

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(80.dp)
                .padding(horizontal = 24.dp),

            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            Icon(
                imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
                contentDescription = "뒤로가기",
                modifier = Modifier
                    .size(24.dp)
                    .clickable(onClick = onBack),
                tint = TextPrimary
            )

            Text(
                text = "운동 장소 변경",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = TextPrimary
            )
        }

        HorizontalDivider(
            color = BorderDefault
        )

        // =========================
        // 본문
        // =========================

        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(
                    horizontal = 24.dp,
                    vertical = 24.dp
                )
        ) {

            Text(
                text = "오늘은 어디에서 운동하나요?",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = TextPrimary
            )

            Spacer(
                modifier = Modifier.height(4.dp)
            )

            Text(
                text = "현재 루틴: $currentGym 기준 루틴",
                style = MaterialTheme.typography.bodySmall,
                color = TextSecondary
            )

            Spacer(
                modifier = Modifier.height(24.dp)
            )

            // =========================
            // 헬스장 카드
            // =========================

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        color = if (selectedPlace == WorkoutPlace.GYM)
                            Blue100
                        else
                            White,
                        shape = RoundedCornerShape(16.dp)
                    )
                    .border(
                        width = 1.2.dp,
                        color = if (selectedPlace == WorkoutPlace.GYM)
                            Blue500
                        else
                            BorderDefault,
                        shape = RoundedCornerShape(16.dp)
                    )
                    .clickable {
                        selectedPlace = WorkoutPlace.GYM
                    }
                    .padding(16.dp)
            ) {

                // 헬스장 정보
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {

                    WorkoutLocationIcon(
                        place = WorkoutPlace.GYM,
                        selected = selectedPlace == WorkoutPlace.GYM
                    )

                    Column {

                        Text(
                            text = "헬스장",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )

                        Text(
                            text = "다양한 머신과 프리웨이트",
                            style = MaterialTheme.typography.bodySmall,
                            color = TextSecondary
                        )
                    }
                }

                // 헬스장 선택 시 카드 내부 드롭다운
                if (selectedPlace == WorkoutPlace.GYM) {

                    Spacer(
                        modifier = Modifier.height(20.dp)
                    )

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(
                                White,
                                RoundedCornerShape(12.dp)
                            )
                            .border(
                                1.dp,
                                BorderDefault,
                                RoundedCornerShape(12.dp)
                            )
                    ) {

                        // 선택된 헬스장
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(48.dp)
                                .clickable {
                                    expanded = !expanded
                                }
                                .padding(horizontal = 16.dp),

                            verticalAlignment = Alignment.CenterVertically
                        ) {

                            Text(
                                text = selectedGym,
                                modifier = Modifier.weight(1f),
                                style = MaterialTheme.typography.bodyMedium,
                                color = TextPrimary
                            )

                            Icon(
                                imageVector = Icons.Rounded.ArrowDropDown,
                                contentDescription = "헬스장 목록",
                                tint = TextPrimary
                            )
                        }

                        // 펼쳐지는 목록
                        if (expanded) {

                            HorizontalDivider(
                                color = BorderDefault
                            )

                            gymOptions.forEachIndexed { index, gym ->

                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(48.dp)
                                        .clickable {
                                            selectedGym = gym
                                            expanded = false
                                        }
                                        .padding(horizontal = 16.dp),

                                    verticalAlignment = Alignment.CenterVertically
                                ) {

                                    Text(
                                        text = gym,
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = TextPrimary
                                    )
                                }

                                if (index != gymOptions.lastIndex) {
                                    HorizontalDivider(
                                        color = BorderDefault
                                    )
                                }
                            }
                        }
                    }
                }
            }

            Spacer(
                modifier = Modifier.height(12.dp)
            )

            // =========================
            // 집 카드
            // =========================

            WorkoutLocationOption(
                place = WorkoutPlace.HOME,
                selectedPlace = selectedPlace,
                title = "집",
                subtitle = "홈트레이닝",
                onClick = {
                    selectedPlace = WorkoutPlace.HOME
                    expanded = false
                }
            )

            Spacer(
                modifier = Modifier.height(12.dp)
            )

            // =========================
            // 야외 카드
            // =========================

            WorkoutLocationOption(
                place = WorkoutPlace.OUTDOOR,
                selectedPlace = selectedPlace,
                title = "야외",
                subtitle = "러닝, 걷기 등",
                onClick = {
                    selectedPlace = WorkoutPlace.OUTDOOR
                    expanded = false
                }
            )

            // =========================
            // 루틴 변경 안내
            // =========================

            if (isChanged) {

                Spacer(
                    modifier = Modifier.height(12.dp)
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            color = Color(0xFFFFF8E8),
                            shape = RoundedCornerShape(10.dp)
                        )
                        .border(
                            width = 1.dp,
                            color = Color(0xFFFFC04D),
                            shape = RoundedCornerShape(10.dp)
                        )
                        .padding(
                            horizontal = 12.dp,
                            vertical = 12.dp
                        ),

                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {

                    Icon(
                        imageVector = Icons.Outlined.Warning,
                        contentDescription = null,
                        tint = Color(0xFFFF9800),
                        modifier = Modifier.size(18.dp)
                    )

                    Text(
                        text = "현재 루틴에는 헬스장 머신 운동이 포함되어 있어요.",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color(0xFFE58A00)
                    )
                }
            }
        }

        // =========================
        // 하단 버튼
        // =========================

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    horizontal = 24.dp,
                    vertical = 16.dp
                )
        ) {

            PrimaryWorkoutButton(
                label = if (isChanged)
                    "오늘 루틴 조정하기"
                else
                    "완료",

                onClick = {
                    onConfirm(
                        selectedPlace,
                        if (selectedPlace == WorkoutPlace.GYM)
                            selectedGym
                        else
                            null
                    )
                }
            )
        }
    }
}

// =========================
// 집 / 야외 선택 카드
// =========================

@Composable
private fun WorkoutLocationOption(
    place: WorkoutPlace,
    selectedPlace: WorkoutPlace,
    title: String,
    subtitle: String,
    onClick: () -> Unit
) {

    val isSelected = place == selectedPlace

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(80.dp)
            .background(
                color = if (isSelected)
                    Blue100
                else
                    White,
                shape = RoundedCornerShape(16.dp)
            )
            .border(
                width = 1.2.dp,
                color = if (isSelected)
                    Blue500
                else
                    BorderDefault,
                shape = RoundedCornerShape(16.dp)
            )
            .clickable(onClick = onClick)
            .padding(16.dp),

        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {

        WorkoutLocationIcon(
            place = place,
            selected = isSelected
        )

        Column {

            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = TextPrimary
            )

            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodySmall,
                color = TextSecondary,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

// =========================
// 장소 아이콘
// =========================

@Composable
private fun WorkoutLocationIcon(
    place: WorkoutPlace,
    selected: Boolean
) {

    Box(
        modifier = Modifier
            .size(40.dp)
            .background(
                color = if (selected)
                    Blue500
                else
                    Gray50,
                shape = RoundedCornerShape(10.dp)
            )
            .border(
                width = if (selected) 0.dp else 1.dp,
                color = if (selected)
                    Color.Transparent
                else
                    BorderDefault,
                shape = RoundedCornerShape(10.dp)
            ),

        contentAlignment = Alignment.Center
    ) {

        when (place) {

            WorkoutPlace.GYM -> {

                Icon(
                    painter = painterResource(
                        R.drawable.ic_workout_fitness_center
                    ),
                    contentDescription = "헬스장",
                    modifier = Modifier.size(22.dp),
                    tint = if (selected)
                        White
                    else
                        Gray300
                )
            }

            WorkoutPlace.HOME -> {

                Icon(
                    imageVector = Icons.Rounded.Home,
                    contentDescription = "집",
                    modifier = Modifier.size(22.dp),
                    tint = if (selected)
                        White
                    else
                        Gray300
                )
            }

            WorkoutPlace.OUTDOOR -> {

                Icon(
                    imageVector = Icons.Rounded.Place,
                    contentDescription = "야외",
                    modifier = Modifier.size(22.dp),
                    tint = if (selected)
                        White
                    else
                        Gray300
                )
            }
        }
    }
}