
package com.fitmate.presentation.mypage

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.constrainHeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.fitmate.presentation.designsystem.theme.*

private enum class EquipmentEditType {
    GYM, HOME, OUTDOOR
}

private val gymEquipmentGroups = listOf(
    "프리웨이트" to listOf("덤벨", "바벨", "EZ바", "케틀벨"),
    "밴드/저항" to listOf("저항 밴드", "미니 밴드"),
    "벤치/랙" to listOf("파워랙/스쿼트랙", "풀업바/철봉"),
    "보조 기구" to listOf("AB 롤러", "스텝박스"),
    "머신/케이블" to listOf(
        "케이블", "체스트 프레스", "팩덱", "랫풀다운",
        "숄더 프레스", "어시스트 풀업", "백 익스텐션",
        "레그 프레스", "레그 익스텐션", "레그 컬",
        "힙 어덕션", "힙 어브덕션", "암 컬"
    ),
    "유산소" to listOf(
        "줄넘기", "러닝머신", "스테어마스터",
        "로잉 머신", "실내 자전거", "엘립티컬"
    )
)

private val homeEquipmentGroups = listOf(
    "프리웨이트" to listOf("덤벨", "바벨", "EZ바", "케틀벨"),
    "밴드/저항" to listOf("저항 밴드", "미니 밴드"),
    "벤치/랙" to listOf("벤치", "파워랙/스쿼트랙", "풀업바/철봉"),
    "보조 기구" to listOf("AB 롤러", "스텝박스"),
    "유산소" to listOf(
        "줄넘기", "러닝머신", "로잉 머신",
        "실내 자전거", "엘립티컬"
    )
)

private val outdoorEquipmentGroups = listOf(
    "유산소" to listOf("줄넘기", "자전거")
)

// 기존 네비게이션에서 호출하는 함수 이름 유지
@Composable
fun GymEditScreen(
    isAddMode: Boolean,
    initialName: String = "",
    initialEquipment: List<String> = emptyList(),
    onBack: () -> Unit = {},
    onSave: (String, List<String>) -> Unit = { _, _ -> }
) {
    EquipmentEditContent(
        type = EquipmentEditType.GYM,
        isAddMode = isAddMode,
        initialName = initialName,
        initialEquipment = initialEquipment,
        onBack = onBack,
        onSave = onSave
    )
}

@Composable
fun HomeEquipmentEditScreen(
    initialEquipment: List<String> = emptyList(),
    onBack: () -> Unit = {},
    onSave: (List<String>) -> Unit = {}
) {
    EquipmentEditContent(
        type = EquipmentEditType.HOME,
        initialEquipment = initialEquipment,
        onBack = onBack,
        onSave = { _, equipment -> onSave(equipment) }
    )
}

@Composable
fun OutdoorEquipmentEditScreen(
    initialEquipment: List<String> = emptyList(),
    onBack: () -> Unit = {},
    onSave: (List<String>) -> Unit = {}
) {
    EquipmentEditContent(
        type = EquipmentEditType.OUTDOOR,
        initialEquipment = initialEquipment,
        onBack = onBack,
        onSave = { _, equipment -> onSave(equipment) }
    )
}

@Composable
private fun EquipmentEditContent(
    type: EquipmentEditType,
    isAddMode: Boolean = false,
    initialName: String = "",
    initialEquipment: List<String> = emptyList(),
    onBack: () -> Unit,
    onSave: (String, List<String>) -> Unit
) {
    val isGym = type == EquipmentEditType.GYM

    val title = when (type) {
        EquipmentEditType.GYM ->
            if (isAddMode) "내 헬스장 추가" else "내 헬스장 수정"
        EquipmentEditType.HOME -> "집"
        EquipmentEditType.OUTDOOR -> "야외"
    }

    val equipmentGroups = when (type) {
        EquipmentEditType.GYM -> gymEquipmentGroups
        EquipmentEditType.HOME -> homeEquipmentGroups
        EquipmentEditType.OUTDOOR -> outdoorEquipmentGroups
    }

    var gymName by remember(type, isAddMode, initialName) {
        mutableStateOf(initialName)
    }

    val selectedEquipment = remember(type, isAddMode, initialName, initialEquipment) {
        mutableStateListOf<String>().apply {
            addAll(initialEquipment)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(White)
    ) {
        SettingsHeader(
            title = title,
            onBack = onBack
        )

        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp)
                .padding(top = 20.dp, bottom = 20.dp),
            verticalArrangement = Arrangement.spacedBy(18.dp)
        ) {
            // 헬스장 화면에서만 이름 입력 표시
            if (isGym) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    EquipmentFormLabel("헬스장 명")

                    Surface(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp),
                        color = White,
                        shape = RoundedCornerShape(10.dp),
                        border = BorderStroke(1.dp, BorderDefault)
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(horizontal = 14.dp),
                            contentAlignment = Alignment.CenterStart
                        ) {
                            BasicTextField(
                                value = gymName,
                                onValueChange = { gymName = it },
                                singleLine = true,
                                modifier = Modifier.fillMaxWidth(),
                                textStyle = androidx.compose.ui.text.TextStyle(
                                    fontFamily = FitMateFontFamily,
                                    fontSize = 14.sp,
                                    color = Gray900
                                ),
                                decorationBox = { inner ->
                                    Box {
                                        if (gymName.isEmpty()) {
                                            Text(
                                                text = "헬스장 이름을 입력해주세요.",
                                                fontFamily = FitMateFontFamily,
                                                fontSize = 14.sp,
                                                color = Gray500
                                            )
                                        }
                                        inner()
                                    }
                                }
                            )
                        }
                    }
                }
            }

            equipmentGroups.forEach { (groupName, options) ->
                Column(
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    EquipmentFormLabel(groupName)

                    EquipmentChipFlow(
                        options = options,
                        selected = selectedEquipment,
                        isGym = isGym,
                        onToggle = { name ->
                            if (name in selectedEquipment) {
                                selectedEquipment.remove(name)
                            } else {
                                selectedEquipment.add(name)
                            }
                        }
                    )
                }
            }
        }

        // 공통 저장 버튼
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(White)
                .padding(horizontal = 24.dp, vertical = 14.dp)
        ) {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                onClick = {
                    val trimmedName = gymName.trim()

                    if (!isGym || trimmedName.isNotEmpty()) {
                        onSave(trimmedName, selectedEquipment.toList())
                    }
                },
                color = if (isGym && gymName.isBlank()) Gray500 else Blue600,
                shape = RoundedCornerShape(12.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Text(
                        text = "저장",
                        fontFamily = FitMateNumberFontFamily,
                        fontSize = 16.sp,
                        color = White
                    )
                }
            }
        }
    }
}

@Composable
private fun EquipmentFormLabel(text: String) {
    Text(
        text = text,
        fontFamily = FitMateFontFamily,
        fontSize = 14.sp,
        fontWeight = FontWeight.Bold,
        color = Gray900
    )
}

@Composable
private fun EquipmentChipFlow(
    options: List<String>,
    selected: List<String>,
    isGym: Boolean,
    onToggle: (String) -> Unit
) {
    Layout(
        content = {
            options.forEach { option ->
                val isSelected = option in selected
                val chipShape = RoundedCornerShape(100.dp)

                Box(
                    modifier = Modifier
                        .padding(vertical = 2.dp)
                        .background(
                            color = if (isSelected) Blue100 else White,
                            shape = chipShape
                        )
                        .border(
                            width = 1.dp,
                            color = if (isSelected) Blue500 else BorderDefault,
                            shape = chipShape
                        )
                        .clip(chipShape)
                        .clickable { onToggle(option) }
                        .padding(
                            horizontal = if (isGym) 18.dp else 14.dp,
                            vertical = if (isGym) 8.dp else 7.dp
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = option,
                        fontFamily = FitMateFontFamily,
                        fontSize = if (isGym) 14.sp else 12.sp,
                        fontWeight = if (isSelected) {
                            FontWeight.Bold
                        } else {
                            FontWeight.Normal
                        },
                        color = if (isSelected) Blue600 else Gray900
                    )
                }
            }
        },
        modifier = Modifier.fillMaxWidth()
    ) { measurables, constraints ->
        val horizontalGap = 4.dp.roundToPx()
        val verticalGap = 8.dp.roundToPx()
        val maxWidth = constraints.maxWidth

        val placeables = measurables.map { measurable ->
            measurable.measure(
                Constraints(
                    minWidth = 0,
                    maxWidth = maxWidth,
                    minHeight = 0,
                    maxHeight = Constraints.Infinity
                )
            )
        }

        val positions = mutableListOf<Pair<Int, Int>>()
        var x = 0
        var y = 0
        var rowHeight = 0

        placeables.forEach { placeable ->
            if (x > 0 && x + placeable.width > maxWidth) {
                x = 0
                y += rowHeight + verticalGap
                rowHeight = 0
            }

            positions.add(x to y)
            x += placeable.width + horizontalGap
            rowHeight = maxOf(rowHeight, placeable.height)
        }

        layout(
            width = maxWidth,
            height = constraints.constrainHeight(y + rowHeight)
        ) {
            placeables.forEachIndexed { index, placeable ->
                val (px, py) = positions[index]
                placeable.placeRelative(px, py)
            }
        }
    }
}
