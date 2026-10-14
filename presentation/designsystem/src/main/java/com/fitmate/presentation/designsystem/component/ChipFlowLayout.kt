package com.fitmate.presentation.designsystem.component

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import com.fitmate.presentation.designsystem.theme.FitMateTheme
import com.fitmate.presentation.designsystem.theme.Spacing1
import com.fitmate.presentation.designsystem.theme.Spacing2

// 칩을 가로로 나열하다 폭이 넘치면 다음 줄로 넘긴다 (기구 / 부위 선택)
// foundation FlowRow는 모듈 간 Compose 버전 차이로 런타임 오류가 나서 직접 배치한다
@Composable
fun ChipFlowLayout(
    modifier: Modifier = Modifier,
    horizontalSpacing: Dp = Spacing1,
    verticalSpacing: Dp = Spacing2,
    content: @Composable () -> Unit
) {
    Layout(content = content, modifier = modifier) { measurables, constraints ->
        val hGap = horizontalSpacing.roundToPx()
        val vGap = verticalSpacing.roundToPx()
        val placeables = measurables.map { it.measure(constraints.copy(minWidth = 0, minHeight = 0)) }

        // 줄마다 들어갈 칩 인덱스를 나눈다
        val rows = mutableListOf<MutableList<Int>>()
        var rowWidth = 0
        placeables.forEachIndexed { index, placeable ->
            val needed = if (rows.isEmpty() || rows.last().isEmpty()) placeable.width else rowWidth + hGap + placeable.width
            if (rows.isEmpty() || needed > constraints.maxWidth) {
                rows.add(mutableListOf(index))
                rowWidth = placeable.width
            } else {
                rows.last().add(index)
                rowWidth = needed
            }
        }

        val rowHeights = rows.map { row -> row.maxOf { placeables[it].height } }
        val height = rowHeights.sum() + vGap * (rows.size - 1).coerceAtLeast(0)
        val width = if (constraints.hasBoundedWidth) constraints.maxWidth else rows.maxOfOrNull { row ->
            row.sumOf { placeables[it].width } + hGap * (row.size - 1)
        } ?: 0

        layout(width, height.coerceAtLeast(constraints.minHeight)) {
            var y = 0
            rows.forEachIndexed { rowIndex, row ->
                var x = 0
                row.forEach { index ->
                    val placeable = placeables[index]
                    placeable.placeRelative(x, y + (rowHeights[rowIndex] - placeable.height) / 2)
                    x += placeable.width + hGap
                }
                y += rowHeights[rowIndex] + vGap
            }
        }
    }
}

@Preview(widthDp = 346)
@Composable
private fun ChipFlowLayoutPreview() {
    FitMateTheme {
        ChipFlowLayout {
            listOf("허리", "무릎", "어깨", "손목", "발목", "목").forEach {
                ExerciseChip(text = it, selected = it == "무릎", onClick = {})
            }
        }
    }
}
