package com.fitmate.presentation.designsystem.component

import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.fitmate.presentation.designsystem.theme.FitMateTheme
import com.fitmate.presentation.designsystem.theme.Spacing1
import com.fitmate.presentation.designsystem.theme.Spacing2
import com.fitmate.presentation.designsystem.theme.Spacing6
import com.fitmate.presentation.designsystem.theme.White

// 검색 목록에 보이는 운동. filter는 부위 칩(가슴 / 등 ...) 중 하나
data class SearchableExercise(
    val name: String,
    val description: String,
    val filter: String
)

const val EXERCISE_FILTER_ALL = "전체"

val ExerciseSearchFilters = listOf(
    EXERCISE_FILTER_ALL, "가슴", "등", "어깨", "이두", "하체/둔근", "코어", "전신", "삼두"
)

// 운동 검색 화면 공통 레이아웃: 검색 상단 바 + 부위 칩 + 선택 목록 + 하단 버튼
// 하단 버튼은 선택 개수에 따라 문구가 바뀌는 곳이 있어서 호출하는 쪽에서 그린다
@Composable
fun ExerciseSearchLayout(
    exercises: List<SearchableExercise>,
    selected: List<String>,
    onToggle: (String) -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
    bottomBar: @Composable () -> Unit
) {
    var query by rememberSaveable { mutableStateOf("") }
    var filter by rememberSaveable { mutableStateOf(EXERCISE_FILTER_ALL) }

    val visibleExercises = exercises.filter { exercise ->
        (filter == EXERCISE_FILTER_ALL || exercise.filter == filter) &&
            (query.isBlank() || exercise.name.replace(" ", "").contains(query.replace(" ", "")))
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(White)
    ) {
        SearchTopBar(query = query, onQueryChange = { query = it }, onBack = onBack) {
            Row(
                modifier = Modifier
                    .horizontalScroll(rememberScrollState())
                    .padding(horizontal = Spacing6),
                horizontalArrangement = Arrangement.spacedBy(Spacing1)
            ) {
                ExerciseSearchFilters.forEach { option ->
                    ExerciseChip(text = option, selected = filter == option, onClick = { filter = option })
                }
            }
        }
        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            contentPadding = PaddingValues(horizontal = Spacing6, vertical = Spacing6),
            verticalArrangement = Arrangement.spacedBy(Spacing2)
        ) {
            items(visibleExercises, key = { it.name }) { exercise ->
                SelectableExerciseItem(
                    title = exercise.name,
                    description = exercise.description,
                    selected = exercise.name in selected,
                    onClick = { onToggle(exercise.name) }
                )
            }
        }
        Column(modifier = Modifier.padding(start = Spacing6, end = Spacing6, bottom = Spacing6)) {
            bottomBar()
        }
    }
}

@Preview(widthDp = 394, heightDp = 892)
@Composable
private fun ExerciseSearchLayoutPreview() {
    FitMateTheme {
        ExerciseSearchLayout(
            exercises = listOf(
                SearchableExercise("벤치 프레스", "가슴 · 프리웨이트", "가슴"),
                SearchableExercise("랫풀다운", "등 · 머신/케이블", "등")
            ),
            selected = listOf("벤치 프레스"),
            onToggle = {},
            onBack = {}
        ) {
            FitMatePrimaryButton(text = "선택한 운동 설정하기 (1)", onClick = {})
        }
    }
}
