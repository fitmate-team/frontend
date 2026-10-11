package com.fitmate.presentation.workout

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.fitmate.presentation.designsystem.theme.*

data class SelectableExercise(
    val id: Int,
    val name: String,
    val category: String,
    val equipment: String
)

private val exerciseCategories = listOf(
    "전체", "가슴", "등", "어깨", "이두", "하체", "유산소"
)

private val sampleExercises = listOf(
    SelectableExercise(1, "벤치 프레스", "가슴", "프리웨이트"),
    SelectableExercise(2, "인클라인 덤벨프레스", "가슴", "프리웨이트"),
    SelectableExercise(3, "숄더 프레스", "어깨", "프리웨이트"),
    SelectableExercise(4, "체스트 프레스", "가슴", "머신/케이블"),
    SelectableExercise(5, "레그 프레스", "하체", "머신/케이블"),
    SelectableExercise(6, "랫풀다운", "등", "머신/케이블"),
    SelectableExercise(7, "줄넘기", "유산소", "전신"),
    SelectableExercise(8, "러닝머신", "유산소", "전신"),
    SelectableExercise(9, "바벨 컬", "이두", "프리웨이트")
)

@Composable
fun WorkoutExerciseSelectScreen(
    modifier: Modifier = Modifier,
    exercises: List<SelectableExercise> = sampleExercises,
    initialSelectedIds: Set<Int> = setOf(1, 3, 4),
    onBack: () -> Unit = {},
    onNext: (List<SelectableExercise>) -> Unit = {}
) {
    var searchQuery by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf("전체") }

    val selectedIds = remember(initialSelectedIds) {
        mutableStateListOf<Int>().apply {
            addAll(initialSelectedIds)
        }
    }

    val filteredExercises = exercises.filter { exercise ->
        val categoryMatches =
            selectedCategory == "전체" ||
                    exercise.category == selectedCategory

        val searchMatches =
            exercise.name.contains(searchQuery, ignoreCase = true)

        categoryMatches && searchMatches
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(White)
    ) {

        // 상단 검색 영역
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    start = 20.dp,
                    end = 32.dp,
                    top = 16.dp,
                    bottom = 12.dp
                ),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
                contentDescription = "뒤로가기",
                modifier = Modifier
                    .size(24.dp)
                    .clickable { onBack() },
                tint = TextPrimary
            )

            Row(
                modifier = Modifier
                    .weight(1f)
                    .height(48.dp)
                    .border(
                        1.dp,
                        BorderDefault,
                        RoundedCornerShape(12.dp)
                    )
                    .padding(horizontal = 14.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    imageVector = Icons.Rounded.Search,
                    contentDescription = null,
                    modifier = Modifier.size(20.dp),
                    tint = TextSecondary
                )

                BasicTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    modifier = Modifier.weight(1f),
                    singleLine = true,
                    textStyle = MaterialTheme.typography.bodyMedium.copy(
                        color = TextPrimary
                    ),
                    decorationBox = { innerTextField ->
                        Box {
                            if (searchQuery.isEmpty()) {
                                Text(
                                    text = "운동 검색",
                                    color = TextSecondary,
                                    style = MaterialTheme.typography.bodyMedium
                                )
                            }
                            innerTextField()
                        }
                    }
                )
            }
        }

        // 부위별 필터 칩
        LazyRow(
            modifier = Modifier.fillMaxWidth(),
            contentPadding = PaddingValues(horizontal = 18.dp),
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            items(exerciseCategories) { category ->

                val selected = selectedCategory == category

                Box(
                    modifier = Modifier
                        .border(
                            width = 1.dp,
                            color = if (selected) Blue500 else BorderDefault,
                            shape = RoundedCornerShape(50)
                        )
                        .background(
                            color = if (selected) Blue100 else White,
                            shape = RoundedCornerShape(50)
                        )
                        .clickable {
                            selectedCategory = category
                        }
                        .padding(
                            horizontal = 18.dp,
                            vertical = 10.dp
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = category,
                        color = if (selected) Blue600 else TextPrimary,
                        style = MaterialTheme.typography.bodySmall,
                        fontWeight = if (selected) {
                            FontWeight.SemiBold
                        } else {
                            FontWeight.Normal
                        }
                    )
                }
            }
        }

        HorizontalDivider(
            modifier = Modifier.padding(top = 16.dp),
            color = BorderDefault
        )

        // 운동 목록
        LazyColumn(
            modifier = Modifier.weight(1f),
            contentPadding = PaddingValues(
                start = 24.dp,
                end = 24.dp,
                top = 24.dp,
                bottom = 16.dp
            ),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(
                items = filteredExercises,
                key = { it.id }
            ) { exercise ->

                val selected = exercise.id in selectedIds

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(72.dp)
                        .border(
                            width = 1.dp,
                            color = if (selected) Blue500 else BorderDefault,
                            shape = RoundedCornerShape(16.dp)
                        )
                        .background(
                            color = if (selected) Blue100 else White,
                            shape = RoundedCornerShape(16.dp)
                        )
                        .clickable {
                            if (selected) {
                                selectedIds.remove(exercise.id)
                            } else {
                                selectedIds.add(exercise.id)
                            }
                        }
                        .padding(horizontal = 24.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Box(
                        modifier = Modifier
                            .size(30.dp)
                            .border(
                                width = 1.dp,
                                color = if (selected) Blue500 else BorderDefault,
                                shape = RoundedCornerShape(10.dp)
                            )
                            .background(
                                color = if (selected) Blue500 else White,
                                shape = RoundedCornerShape(10.dp)
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        if (selected) {
                            Icon(
                                imageVector = Icons.Rounded.Check,
                                contentDescription = null,
                                tint = White,
                                modifier = Modifier.size(18.dp)
                            )
                        } else {
                            Text(
                                text = "+",
                                color = TextSecondary,
                                style = MaterialTheme.typography.titleMedium
                            )
                        }
                    }

                    Spacer(Modifier.width(24.dp))

                    Column {
                        Text(
                            text = exercise.name,
                            color = TextPrimary,
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.SemiBold
                        )

                        Spacer(Modifier.height(4.dp))

                        Text(
                            text = "${exercise.category} · ${exercise.equipment}",
                            color = TextSecondary,
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                }
            }
        }

        // 하단 버튼
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(White)
                .padding(
                    start = 24.dp,
                    end = 24.dp,
                    top = 14.dp,
                    bottom = 24.dp
                )
        ) {
            PrimaryWorkoutButton(
                label = "선택한 운동 설정하기 (${selectedIds.size})",
                onClick = {
                    onNext(
                        exercises.filter { it.id in selectedIds }
                    )
                }
            )
        }
    }
}