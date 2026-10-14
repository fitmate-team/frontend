package com.fitmate.presentation.auth

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.listSaver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.sp
import com.fitmate.presentation.designsystem.component.FitMateFormScaffold
import com.fitmate.presentation.designsystem.component.FormFieldLabel
import com.fitmate.presentation.designsystem.component.FormLabeledField
import com.fitmate.presentation.designsystem.component.ProgressTopBar
import com.fitmate.presentation.designsystem.theme.Gray600
import com.fitmate.presentation.designsystem.theme.Spacing6
import com.fitmate.presentation.designsystem.theme.fitMateTextStyle

internal const val SIGNUP_TOTAL_STEPS = 10

// 회원가입 단계 화면 공통 틀: 진행률 상단 바 + 타이틀/설명 + 스크롤 콘텐츠 + 하단 버튼
@Composable
internal fun SignupStepScaffold(
    step: Int,
    onBack: () -> Unit,
    title: String,
    bottomBar: @Composable ColumnScope.() -> Unit,
    subtitle: String? = null,
    contentTopSpacing: Dp = Spacing6,
    content: @Composable ColumnScope.() -> Unit
) {
    SignupScaffold(
        topBar = { ProgressTopBar(step = step, totalSteps = SIGNUP_TOTAL_STEPS, onBack = onBack) },
        title = title,
        subtitle = subtitle,
        contentTopSpacing = contentTopSpacing,
        bottomBar = bottomBar,
        content = content
    )
}

@Composable
internal fun SignupScaffold(
    topBar: @Composable () -> Unit,
    title: String,
    bottomBar: @Composable ColumnScope.() -> Unit,
    subtitle: String? = null,
    contentTopSpacing: Dp = Spacing6,
    content: @Composable ColumnScope.() -> Unit
) {
    FitMateFormScaffold(
        topBar = topBar,
        title = title,
        bottomBar = bottomBar,
        subtitle = subtitle,
        contentTopSpacing = contentTopSpacing,
        content = content
    )
}

@Composable
internal fun SignupFieldLabel(
    text: String,
    modifier: Modifier = Modifier
) = FormFieldLabel(text = text, modifier = modifier)

// 입력 필드 아래 안내 문구 (아이디는 8~16자로 ...)
@Composable
internal fun SignupHelperText(
    text: String,
    modifier: Modifier = Modifier
) {
    Text(
        text = text,
        style = fitMateTextStyle(12.sp, 16.sp, FontWeight.Thin),
        color = Gray600,
        modifier = modifier
    )
}

@Composable
internal fun SignupLabeledField(
    label: String,
    modifier: Modifier = Modifier,
    field: @Composable () -> Unit
) = FormLabeledField(label = label, modifier = modifier, field = field)

// 화면을 벗어났다 돌아와도 유지되는 다중 선택 상태 (칩 선택)
@Composable
internal fun rememberSaveableSelection(): MutableState<Set<String>> = rememberSaveable(
    saver = listSaver(save = { it.value.toList() }, restore = { mutableStateOf(it.toSet()) })
) { mutableStateOf(emptySet()) }

internal fun Set<String>.toggle(item: String): Set<String> = if (item in this) this - item else this + item
