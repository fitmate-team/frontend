
package com.fitmate.presentation.mypage

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
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
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.fitmate.presentation.designsystem.R
import com.fitmate.presentation.designsystem.theme.*

private enum class ProfileEditSection {
    BASIC, GOAL, METHOD, EXPERIENCE, SCHEDULE, INJURY
}

@Composable
fun BodyProfileEditScreen(
    onBack: () -> Unit = {},
    onSave: () -> Unit = {},
    avoidedExercises: List<String> = emptyList(),
    onOpenExerciseSelect: () -> Unit = {},
    onRemoveExercise: (String) -> Unit = {}
) {
    var expandedSection by remember {
        mutableStateOf<ProfileEditSection?>(null)
    }

    var gender by remember { mutableStateOf("여성") }
    var age by remember { mutableStateOf("28") }
    var height by remember { mutableStateOf("163") }

    var goal by remember { mutableStateOf("체지방 감량") }
    var method by remember { mutableStateOf("균형형") }
    var experience by remember { mutableStateOf("중급") }
    var exerciseStatus by remember { mutableStateOf("처음 시작") }

    var selectedDays by remember {
        mutableStateOf(setOf("월", "목", "금", "일"))
    }
    var duration by remember { mutableStateOf("60분") }

    var injuryParts by remember {
        mutableStateOf(emptySet<String>())
    }


    val days = listOf("월", "화", "수", "목", "금", "토", "일")
    val dayCount = selectedDays.size

    val injurySummary = if (injuryParts.isEmpty()) {
        "없음"
    } else {
        injuryParts.joinToString(" · ")
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(White)
    ) {
        SettingsHeader(
            title = "운동 프로필 수정",
            onBack = onBack
        )

        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp)
                .padding(top = 24.dp, bottom = 20.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {

            // 1. 기본 정보
            ProfileAccordionCard(
                title = "기본 정보",
                summary = "$gender · ${age}세 · ${height}cm",
                iconRes = R.drawable.ic_mypage_profile,
                expanded = expandedSection == ProfileEditSection.BASIC,
                onClick = {
                    expandedSection =
                        if (expandedSection == ProfileEditSection.BASIC) null
                        else ProfileEditSection.BASIC
                }
            ) {
                Text(
                    text = "성별",
                    fontFamily = FitMateFontFamily,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = Gray900
                )

                Spacer(Modifier.height(4.dp))

                Row(
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    listOf("남성", "여성").forEach { option ->
                        ProfileChoiceChip(
                            text = option,
                            selected = gender == option,
                            modifier = Modifier.weight(1f),
                            height = 42.dp,
                            horizontalPadding = 18.dp,
                            cornerRadius = 100.dp,
                            fontSize = 14.sp,
                            onClick = { gender = option }
                        )
                    }
                }

                Spacer(Modifier.height(10.dp))

                ProfileInputRow(
                    label = "나이",
                    value = age,
                    suffix = "세",
                    onValueChange = { age = it }
                )

                Spacer(Modifier.height(12.dp))

                ProfileInputRow(
                    label = "키",
                    value = height,
                    suffix = "cm",
                    onValueChange = { height = it }
                )
            }

            // 2. 운동 목표
            ProfileAccordionCard(
                title = "운동 목표",
                summary = goal,
                iconRes = R.drawable.ic_profile_goal,
                expanded = expandedSection == ProfileEditSection.GOAL,
                onClick = {
                    expandedSection =
                        if (expandedSection == ProfileEditSection.GOAL) null
                        else ProfileEditSection.GOAL
                }
            ) {
                ProfileOptionList(
                    options = listOf(
                        "체지방 감량" to "체지방을 줄이고 싶어요",
                        "근육량 증가" to "근육을 키우고 싶어요",
                        "체력 향상" to "전반적인 체력을 높이고 싶어요",
                        "건강 유지" to "건강한 운동 습관을 만들고 싶어요"
                    ),
                    selected = goal,
                    onSelect = { goal = it }
                )
            }

            // 3. 운동 방법
            ProfileAccordionCard(
                title = "운동 방법",
                summary = method,
                iconRes = R.drawable.ic_profile_method,
                expanded = expandedSection == ProfileEditSection.METHOD,
                onClick = {
                    expandedSection =
                        if (expandedSection == ProfileEditSection.METHOD) null
                        else ProfileEditSection.METHOD
                }
            ) {
                ProfileOptionList(
                    options = listOf(
                        "근력 운동 중심" to "웨이트 운동 비중을 높여요",
                        "유산소 중심" to "러닝·사이클 등 유산소 비중을 높여요",
                        "균형형" to "근력 운동과 유산소를 균형 있게 진행해요"
                    ),
                    selected = method,
                    onSelect = { method = it }
                )
            }

            // 4. 운동 경력
            ProfileAccordionCard(
                title = "운동 경력",
                summary = experience,
                iconRes = R.drawable.ic_profile_experience,
                expanded = expandedSection == ProfileEditSection.EXPERIENCE,
                onClick = {
                    expandedSection =
                        if (expandedSection == ProfileEditSection.EXPERIENCE) null
                        else ProfileEditSection.EXPERIENCE
                }
            ) {
                ProfileOptionList(
                    options = listOf(
                        "처음이에요" to "거의 운동해본 적이 없어요",
                        "초급" to "운동 경력 1년 미만",
                        "중급" to "운동 경력 1~3년",
                        "상급" to "운동 경력 3년 이상"
                    ),
                    selected = experience,
                    onSelect = { experience = it }
                )

                Spacer(Modifier.height(16.dp))

                Text(
                    text = "현재 운동 상태",
                    fontFamily = FitMateFontFamily,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = Gray900
                )

                Spacer(Modifier.height(12.dp))

                Row(
                    horizontalArrangement = Arrangement.spacedBy(5.dp)
                ) {
                    listOf(
                        "처음 시작",
                        "꾸준히 운동 중",
                        "다시 운동 시작"
                    ).forEach { option ->
                        ProfileChoiceChip(
                            text = option,
                            selected = exerciseStatus == option,
                            modifier = Modifier.weight(1f),
                            height = 35.dp,
                            horizontalPadding = 4.dp,
                            cornerRadius = 100.dp,
                            fontSize = 14.sp,
                            onClick = { exerciseStatus = option }
                        )
                    }
                }
            }

            // 5. 운동 가능 조건
            ProfileAccordionCard(
                title = "운동 가능 조건",
                summary = "주 ${dayCount}회 · $duration",
                iconRes = R.drawable.ic_profile_calendar,
                expanded = expandedSection == ProfileEditSection.SCHEDULE,
                onClick = {
                    expandedSection =
                        if (expandedSection == ProfileEditSection.SCHEDULE) null
                        else ProfileEditSection.SCHEDULE
                }
            ) {
                Text(
                    text = "운동 가능한 요일",
                    fontFamily = FitMateFontFamily,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = Gray900
                )

                Spacer(Modifier.height(8.dp))

                Row(
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    days.forEach { day ->
                        ProfileChoiceChip(
                            text = day,
                            selected = day in selectedDays,
                            modifier = Modifier.weight(1f),
                            height = 40.dp,
                            cornerRadius = 12.dp,
                            fontSize = 14.sp,
                            onClick = {
                                selectedDays = if (day in selectedDays) {
                                    selectedDays - day
                                } else {
                                    selectedDays + day
                                }
                            }
                        )
                    }
                }

                Spacer(Modifier.height(38.dp))

                Text(
                    text = "한 번에 운동할 수 있는 시간",
                    fontFamily = FitMateFontFamily,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = Gray900
                )

                Spacer(Modifier.height(8.dp))

                listOf("20분", "30분", "45분", "60분").forEach { option ->
                    ProfileSelectableRow(
                        title = option,
                        selected = duration == option,
                        onClick = { duration = option }
                    )
                    Spacer(Modifier.height(6.dp))
                }
            }

            // 6. 부담 부위
            ProfileAccordionCard(
                title = "부담 부위",
                summary = injurySummary,
                iconRes = R.drawable.ic_profile_injury,
                expanded = expandedSection == ProfileEditSection.INJURY,
                onClick = {
                    expandedSection =
                        if (expandedSection == ProfileEditSection.INJURY) null
                        else ProfileEditSection.INJURY
                }
            ) {

                Spacer(Modifier.height(9.dp))

                listOf(
                    listOf("허리", "무릎", "어깨"),
                    listOf("손목", "발목", "목")
                ).forEach { rowItems ->
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        rowItems.forEach { part ->
                            ProfileChoiceChip(
                                text = part,
                                selected = part in injuryParts,
                                modifier = Modifier.weight(1f),
                                height = 38.dp,
                                horizontalPadding = 12.dp,
                                cornerRadius = 100.dp,
                                fontSize = 13.sp,
                                onClick = {
                                    injuryParts = if (part in injuryParts) {
                                        injuryParts - part
                                    } else {
                                        injuryParts + part
                                    }
                                }
                            )
                        }
                    }
                    Spacer(Modifier.height(4.dp))
                }

                Spacer(Modifier.height(16.dp))

                Text(
                    text = "피하고 싶은 운동",
                    fontFamily = FitMateFontFamily,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = Gray900
                )

                Spacer(Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Surface(
                        onClick = onOpenExerciseSelect,
                        color = White,
                        shape = RoundedCornerShape(100.dp),
                        border = BorderStroke(1.dp, BorderDefault)
                    ) {
                        Row(
                            modifier = Modifier.padding(
                                horizontal = 12.dp,
                                vertical = 8.dp
                            ),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(5.dp)
                        ) {
                            Icon(
                                painter = painterResource(R.drawable.ic_profile_add),
                                contentDescription = null,
                                tint = Color.Unspecified,
                                modifier = Modifier.size(12.dp)
                            )

                            Text(
                                text = "운동 추가",
                                fontFamily = FitMateFontFamily,
                                fontSize = 12.sp,
                                color = Gray500
                            )
                        }
                    }

                    avoidedExercises.forEach { exercise ->
                        Surface(
                            onClick = { onRemoveExercise(exercise) },
                            color = Blue100,
                            shape = RoundedCornerShape(100.dp),
                            border = BorderStroke(1.dp, Blue500)
                        ) {
                            Box(
                                modifier = Modifier.padding(
                                    horizontal = 12.dp,
                                    vertical = 8.dp
                                ),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = exercise,
                                    fontFamily = FitMateFontFamily,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Blue600,
                                    maxLines = 1
                                )
                            }
                        }
                    }
                }
            }
        }

        // 하단 저장 버튼
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(White)
                .padding(
                    horizontal = 24.dp,
                    vertical = 16.dp
                )
        ) {
            Surface(
                onClick = onSave,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(44.dp),
                color = Blue600,
                shape = RoundedCornerShape(9.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Text(
                        text = "변경사항 저장",
                        fontFamily = FitMateFontFamily,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        color = White
                    )
                }
            }
        }
    }
}

@Composable
private fun ProfileAccordionCard(
    title: String,
    summary: String,
    iconRes: Int,
    expanded: Boolean,
    onClick: () -> Unit,
    content: @Composable ColumnScope.() -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = White,
        shape = RoundedCornerShape(14.dp),
        border = BorderStroke(1.dp, BorderDefault)
    ) {
        Column(
            modifier = Modifier.padding(
                horizontal = 16.dp,
                vertical = 14.dp
            )
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable(onClick = onClick),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(42.dp)
                        .background(
                            color = Gray50,
                            shape = RoundedCornerShape(12.dp)
                        )
                        .border(
                            width = 1.dp,
                            color = BorderDefault,
                            shape = RoundedCornerShape(12.dp)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        painter = painterResource(iconRes),
                        contentDescription = null,
                        tint = Color.Unspecified,
                        modifier = Modifier.size(18.dp)
                    )
                }

                Spacer(Modifier.width(12.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = title,
                        fontFamily = FitMateFontFamily,
                        fontSize = 12.sp,
                        color = Gray500
                    )

                    Text(
                        text = summary,
                        fontFamily = FitMateFontFamily,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = Gray900,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                Icon(
                    painter = painterResource(
                        R.drawable.ic_record_dropdown_arrow
                    ),
                    contentDescription =
                        if (expanded) "접기" else "펼치기",
                    tint = Gray600,
                    modifier = Modifier
                        .size(20.dp)
                        .graphicsLayer {
                            rotationZ = if (expanded) 180f else 0f
                        }
                )
            }

            if (expanded) {
                Spacer(Modifier.height(12.dp))

                HorizontalDivider(
                    thickness = 1.dp,
                    color = BorderDefault
                )

                Spacer(Modifier.height(8.dp))

                content()
            }
        }
    }
}

@Composable
private fun ProfileChoiceChip(
    text: String,
    selected: Boolean,
    modifier: Modifier = Modifier,
    height: Dp = 40.dp,
    horizontalPadding: Dp = 12.dp,
    cornerRadius: Dp = 100.dp,
    fontSize: TextUnit = 14.sp,
    selectedFontWeight: FontWeight = FontWeight.Bold,
    unselectedFontWeight: FontWeight = FontWeight.Normal,
    onClick: () -> Unit
) {
    Surface(
        modifier = modifier.height(height),
        onClick = onClick,
        shape = RoundedCornerShape(cornerRadius),
        color = if (selected) Blue100 else White,
        border = BorderStroke(
            1.dp,
            if (selected) Blue500 else BorderDefault
        )
    ) {
        Box(
            modifier = Modifier.padding(
                horizontal = horizontalPadding
            ),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = text,
                fontFamily = FitMateFontFamily,
                fontSize = fontSize,
                fontWeight = if (selected) {
                    selectedFontWeight
                } else {
                    unselectedFontWeight
                },
                color = if (selected) Blue600 else Gray500,
                maxLines = 1,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
private fun ProfileSelectableRow(
    title: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        onClick = onClick,
        shape = RoundedCornerShape(10.dp),
        color = if (selected) Blue100 else White,
        border = BorderStroke(
            1.dp,
            if (selected) Blue500 else BorderDefault
        )
    ) {
        Text(
            text = title,
            modifier = Modifier.padding(
                horizontal = 12.dp,
                vertical = 10.dp
            ),
            fontFamily = FitMateFontFamily,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            color = Gray900
        )
    }
}

@Composable
private fun ProfileOptionList(
    options: List<Pair<String, String>>,
    selected: String,
    onSelect: (String) -> Unit
) {
    options.forEach { (title, description) ->
        Surface(
            modifier = Modifier.fillMaxWidth(),
            onClick = { onSelect(title) },
            shape = RoundedCornerShape(16.dp),
            color = if (selected == title) Blue100 else White,
            border = BorderStroke(
                1.dp,
                if (selected == title) Blue500 else BorderDefault
            )
        ) {
            Column(
                modifier = Modifier.padding(
                    horizontal = 12.dp,
                    vertical = 10.dp
                )
            ) {
                Text(
                    text = title,
                    fontFamily = FitMateFontFamily,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = Gray900
                )

                Text(
                    text = description,
                    fontFamily = FitMateFontFamily,
                    fontSize = 12.sp,
                    color = Gray500
                )
            }
        }

        Spacer(Modifier.height(8.dp))
    }
}

@Composable
private fun ProfileInputRow(
    label: String,
    value: String,
    suffix: String,
    numeric: Boolean = true,
    onValueChange: (String) -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = White,
        shape = RoundedCornerShape(12.dp),
        border = BorderStroke(1.dp, BorderDefault)
    ) {
        Row(
            modifier = Modifier.padding(
                horizontal = 16.dp,
                vertical = 10.dp
            ),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = label,
                modifier = Modifier.weight(1f),
                fontFamily = FitMateFontFamily,
                fontSize = 12.sp,
                color = Gray900
            )

            BasicTextField(
                value = value,
                onValueChange = { input ->
                    onValueChange(
                        if (numeric) input.filter { it.isDigit() }
                        else input
                    )
                },
                modifier = Modifier.width(90.dp),
                singleLine = true,
                textStyle = androidx.compose.ui.text.TextStyle(
                    fontFamily = FitMateFontFamily,
                    fontSize = 14.sp,
                    color = Gray900,
                    textAlign = TextAlign.End
                ),
                keyboardOptions = KeyboardOptions(
                    keyboardType =
                        if (numeric) KeyboardType.Number
                        else KeyboardType.Text
                )
            )

            if (suffix.isNotEmpty()) {
                Spacer(Modifier.width(8.dp))

                Text(
                    text = suffix,
                    fontFamily = FitMateFontFamily,
                    fontSize = 12.sp,
                    color = Gray500
                )
            }
        }
    }
}

@Preview(
    showBackground = true,
    showSystemUi = false,
    name = "운동 프로필 수정"
)
@Composable
private fun BodyProfileEditScreenPreview() {
    FitMateTheme {
        BodyProfileEditScreen()
    }
}
