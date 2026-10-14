package com.fitmate.presentation.designsystem.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.sp
import com.fitmate.presentation.designsystem.theme.FitMateTheme
import com.fitmate.presentation.designsystem.theme.Spacing2
import com.fitmate.presentation.designsystem.theme.Spacing3
import com.fitmate.presentation.designsystem.theme.Spacing6
import com.fitmate.presentation.designsystem.theme.TextPrimary
import com.fitmate.presentation.designsystem.theme.TextSecondary
import com.fitmate.presentation.designsystem.theme.White
import com.fitmate.presentation.designsystem.theme.fitMateDisplayTextStyle
import com.fitmate.presentation.designsystem.theme.fitMateTextStyle

// 입력 화면 공통 틀: 상단 바 + 큰 제목/설명 + 스크롤 콘텐츠 + 하단 버튼 (회원가입 단계 / 루틴 조건 변경)
// title이 null이면 제목 없이 콘텐츠부터 시작한다
@Composable
fun FitMateFormScaffold(
    topBar: @Composable () -> Unit,
    title: String?,
    bottomBar: @Composable ColumnScope.() -> Unit,
    subtitle: String? = null,
    topSpacing: Dp = Spacing6,
    contentTopSpacing: Dp = Spacing6,
    content: @Composable ColumnScope.() -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(White)
    ) {
        topBar()
        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = Spacing6)
        ) {
            Spacer(modifier = Modifier.height(topSpacing))
            if (title != null) FormTitle(text = title)
            if (subtitle != null) {
                Spacer(modifier = Modifier.height(Spacing2))
                Text(
                    text = subtitle,
                    style = fitMateTextStyle(14.sp, 20.sp),
                    color = TextSecondary
                )
            }
            Spacer(modifier = Modifier.height(contentTopSpacing))
            content()
            Spacer(modifier = Modifier.height(Spacing6))
        }
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = Spacing6, end = Spacing6, bottom = Spacing6),
            verticalArrangement = Arrangement.spacedBy(Spacing3),
            content = bottomBar
        )
    }
}

// 화면 큰 제목 (Wavve PADO 28)
@Composable
fun FormTitle(
    text: String,
    modifier: Modifier = Modifier
) {
    Text(
        text = text,
        style = fitMateDisplayTextStyle(28.sp, 36.sp, letterSpacing = (-0.3).sp),
        color = TextPrimary,
        modifier = modifier
    )
}

// 입력 항목 라벨 (성별 / 운동 가능한 요일 ...)
@Composable
fun FormFieldLabel(
    text: String,
    modifier: Modifier = Modifier
) {
    Text(
        text = text,
        style = fitMateTextStyle(14.sp, 20.sp, FontWeight.Bold),
        color = TextPrimary,
        modifier = modifier
    )
}

// 라벨 + 입력 필드 묶음. 라벨과 필드 사이 8dp
@Composable
fun FormLabeledField(
    label: String,
    modifier: Modifier = Modifier,
    field: @Composable () -> Unit
) {
    Column(modifier = modifier) {
        FormFieldLabel(text = label)
        Spacer(modifier = Modifier.height(Spacing2))
        field()
    }
}

@Preview(widthDp = 394, heightDp = 600)
@Composable
private fun FitMateFormScaffoldPreview() {
    FitMateTheme {
        FitMateFormScaffold(
            topBar = { FitMateTitleTopBar(title = "운동 시간 변경", onBack = {}) },
            title = "언제, 얼마나 운동할 수 있나요?",
            subtitle = "운동할 수 있는 요일과 한 번에 가능한 시간을 알려주세요.",
            bottomBar = { FitMatePrimaryButton(text = "완료", onClick = {}) }
        ) {
            FormLabeledField(label = "이번주 운동 가능한 요일") {
                DaySelectorRow(selectedDays = setOf("월", "목"), onToggle = {})
            }
        }
    }
}
