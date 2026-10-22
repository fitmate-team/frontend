package com.fitmate.presentation.designsystem.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material.icons.outlined.Visibility
import androidx.compose.material.icons.outlined.VisibilityOff
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.fitmate.presentation.designsystem.theme.Blue500
import com.fitmate.presentation.designsystem.theme.BorderDefault
import com.fitmate.presentation.designsystem.theme.FitMateTheme
import com.fitmate.presentation.designsystem.theme.Gray500
import com.fitmate.presentation.designsystem.theme.IconSizeMedium
import com.fitmate.presentation.designsystem.theme.MinTouchTarget
import com.fitmate.presentation.designsystem.theme.ShapeMedium
import com.fitmate.presentation.designsystem.theme.Spacing2
import com.fitmate.presentation.designsystem.theme.Spacing3
import com.fitmate.presentation.designsystem.theme.Spacing4
import com.fitmate.presentation.designsystem.theme.TextPrimary
import com.fitmate.presentation.designsystem.theme.White
import com.fitmate.presentation.designsystem.theme.fitMateTextStyle

private val TextFieldHeight = 52.dp
private val InputTextStyle = fitMateTextStyle(16.sp, 24.sp)
private val ActionButtonShape = RoundedCornerShape(10.dp)

// 아이콘 + 텍스트 입력 필드 (아이디 / 비밀번호). isPassword면 눈 아이콘으로 보기 전환
@Composable
fun FitMateTextField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    modifier: Modifier = Modifier,
    leadingIcon: ImageVector? = null,
    isPassword: Boolean = false,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    textStyle: TextStyle = InputTextStyle,
    height: Dp = TextFieldHeight,
    iconSpacing: Dp = if (isPassword) 7.dp else 10.dp,
    endPadding: Dp = Spacing4,
    trailingContent: (@Composable () -> Unit)? = null
) {
    var passwordVisible by remember { mutableStateOf(false) }

    BasicTextField(
        value = value,
        onValueChange = onValueChange,
        singleLine = true,
        textStyle = textStyle.copy(color = TextPrimary),
        cursorBrush = SolidColor(Blue500),
        visualTransformation = if (isPassword && !passwordVisible) {
            PasswordVisualTransformation()
        } else {
            VisualTransformation.None
        },
        keyboardOptions = if (isPassword) {
            keyboardOptions.copy(keyboardType = KeyboardType.Password)
        } else {
            keyboardOptions
        },
        modifier = modifier,
        decorationBox = { innerTextField ->
            FieldContainer(height = height, endPadding = endPadding) {
                if (leadingIcon != null) {
                    Icon(
                        imageVector = leadingIcon,
                        contentDescription = null,
                        tint = Gray500,
                        modifier = Modifier.size(IconSizeMedium)
                    )
                    Spacer(modifier = Modifier.width(iconSpacing))
                }
                Box(modifier = Modifier.weight(1f)) {
                    if (value.isEmpty()) {
                        Text(text = placeholder, style = textStyle, color = Gray500)
                    }
                    innerTextField()
                }
                if (isPassword) {
                    Spacer(modifier = Modifier.width(iconSpacing))
                    Icon(
                        imageVector = if (passwordVisible) Icons.Outlined.Visibility else Icons.Outlined.VisibilityOff,
                        contentDescription = if (passwordVisible) "비밀번호 숨기기" else "비밀번호 보기",
                        tint = Gray500,
                        modifier = Modifier
                            .size(IconSizeMedium)
                            .clickable { passwordVisible = !passwordVisible }
                    )
                }
                trailingContent?.invoke()
            }
        }
    )
}

// 입력 필드 안에 들어가는 작은 버튼 (중복확인)
@Composable
fun TextFieldActionButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .size(width = 70.dp, height = 32.dp)
            .clip(ActionButtonShape)
            .background(White)
            .border(1.dp, BorderDefault, ActionButtonShape)
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            style = fitMateTextStyle(14.sp, 20.sp),
            color = TextPrimary
        )
    }
}

// 숫자 + 단위 입력 필드 (나이 / 키 / 몸무게). 입력 전에는 '-'로 표시
// 숫자만 입력 가능. Decimal이면 소수점 하나까지 허용
@Composable
fun UnitTextField(
    value: String,
    onValueChange: (String) -> Unit,
    unit: String,
    modifier: Modifier = Modifier,
    keyboardType: KeyboardType = KeyboardType.Number
) {
    val valueStyle = fitMateTextStyle(16.sp, 24.sp, FontWeight.Bold)
    val allowDecimal = keyboardType == KeyboardType.Decimal

    BasicTextField(
        value = value,
        onValueChange = { input ->
            if (input.isNumericInput(allowDecimal)) onValueChange(input)
        },
        singleLine = true,
        textStyle = valueStyle.copy(color = TextPrimary),
        cursorBrush = SolidColor(Blue500),
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
        modifier = modifier,
        decorationBox = { innerTextField ->
            FieldContainer(height = TextFieldHeight, endPadding = Spacing4) {
                Box(modifier = Modifier.weight(1f)) {
                    if (value.isEmpty()) {
                        Text(text = "-", style = valueStyle, color = TextPrimary)
                    }
                    innerTextField()
                }
                Spacer(modifier = Modifier.width(Spacing2))
                Text(text = unit, style = InputTextStyle, color = Gray500)
            }
        }
    )
}

// 운동 검색창 (돋보기 아이콘 + 입력)
@Composable
fun FitMateSearchField(
    query: String,
    onQueryChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    placeholder: String = "운동 검색"
) {
    val searchTextStyle = fitMateTextStyle(16.sp, 24.sp, fontFamily = FontFamily.Default)
    BasicTextField(
        value = query,
        onValueChange = onQueryChange,
        singleLine = true,
        textStyle = searchTextStyle.copy(color = TextPrimary),
        cursorBrush = SolidColor(Blue500),
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
        modifier = modifier,
        decorationBox = { innerTextField ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(MinTouchTarget)
                    .clip(ShapeMedium)
                    .background(White)
                    .border(1.dp, BorderDefault, ShapeMedium)
                    .padding(start = 14.dp, end = Spacing4),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Outlined.Search,
                    contentDescription = null,
                    tint = Gray500,
                    modifier = Modifier.size(IconSizeMedium)
                )
                Spacer(modifier = Modifier.width(Spacing2))
                Box(modifier = Modifier.weight(1f)) {
                    if (query.isEmpty()) {
                        Text(text = placeholder, style = searchTextStyle, color = Gray500)
                    }
                    innerTextField()
                }
            }
        }
    )
}

private fun String.isNumericInput(allowDecimal: Boolean): Boolean =
    if (allowDecimal) {
        all { it.isDigit() || it == '.' } && count { it == '.' } <= 1
    } else {
        all { it.isDigit() }
    }

@Composable
private fun FieldContainer(
    height: Dp,
    endPadding: Dp,
    content: @Composable RowScope.() -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(height)
            .clip(ShapeMedium)
            .background(White)
            .border(1.dp, BorderDefault, ShapeMedium)
            .padding(start = Spacing4, end = endPadding),
        verticalAlignment = Alignment.CenterVertically,
        content = content
    )
}

@Preview
@Composable
private fun FitMateTextFieldPreview() {
    FitMateTheme {
        Column(verticalArrangement = Arrangement.spacedBy(Spacing3)) {
            FitMateTextField(
                value = "",
                onValueChange = {},
                placeholder = "아이디",
                leadingIcon = Icons.Outlined.Person,
                endPadding = Spacing2,
                trailingContent = { TextFieldActionButton(text = "중복확인", onClick = {}) }
            )
            FitMateTextField(
                value = "",
                onValueChange = {},
                placeholder = "비밀번호",
                leadingIcon = Icons.Outlined.Lock,
                isPassword = true
            )
            UnitTextField(value = "", onValueChange = {}, unit = "세")
            UnitTextField(value = "175", onValueChange = {}, unit = "cm")
        }
    }
}
