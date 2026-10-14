package com.fitmate.presentation.designsystem.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ViewList
import androidx.compose.material.icons.automirrored.rounded.ViewList
import androidx.compose.material.icons.outlined.BarChart
import androidx.compose.material.icons.outlined.FitnessCenter
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.rounded.BarChart
import androidx.compose.material.icons.rounded.FitnessCenter
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.fitmate.presentation.designsystem.theme.Blue500
import com.fitmate.presentation.designsystem.theme.Blue600
import com.fitmate.presentation.designsystem.theme.BorderDefault
import com.fitmate.presentation.designsystem.theme.FitMateTheme
import com.fitmate.presentation.designsystem.theme.Gray300
import com.fitmate.presentation.designsystem.theme.IconSizeDefault
import com.fitmate.presentation.designsystem.theme.MinTouchTarget
import com.fitmate.presentation.designsystem.theme.Spacing1
import com.fitmate.presentation.designsystem.theme.Spacing3
import com.fitmate.presentation.designsystem.theme.TextSecondary
import com.fitmate.presentation.designsystem.theme.White
import com.fitmate.presentation.designsystem.theme.fitMateTextStyle

data class FitMateBottomBarItem(
    val key: String,
    val label: String,
    val selectedIcon: ImageVector,
    val icon: ImageVector
)

// 홈 / 루틴 / 운동 / 기록 / 마이 탭 (선택된 탭은 채워진 아이콘)
val FitMateMainTabs = listOf(
    FitMateBottomBarItem("home", "홈", Icons.Rounded.Home, Icons.Outlined.Home),
    FitMateBottomBarItem("routine", "루틴", Icons.AutoMirrored.Rounded.ViewList, Icons.AutoMirrored.Outlined.ViewList),
    FitMateBottomBarItem("workout", "운동", Icons.Rounded.FitnessCenter, Icons.Outlined.FitnessCenter),
    FitMateBottomBarItem("record", "기록", Icons.Rounded.BarChart, Icons.Outlined.BarChart),
    FitMateBottomBarItem("mypage", "마이", Icons.Rounded.Person, Icons.Outlined.Person)
)

@Composable
fun FitMateBottomBar(
    items: List<FitMateBottomBarItem>,
    selectedKey: String?,
    onSelect: (FitMateBottomBarItem) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(White)
            .drawBehind {
                val y = 0.5.dp.toPx()
                drawLine(BorderDefault, Offset(0f, y), Offset(size.width, y), 1.dp.toPx())
            }
            .windowInsetsPadding(WindowInsets.navigationBars)
            .padding(vertical = Spacing3)
    ) {
        items.forEach { item ->
            val selected = item.key == selectedKey
            Column(
                modifier = Modifier
                    .weight(1f)
                    .height(MinTouchTarget)
                    .clickable { onSelect(item) },
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Icon(
                    imageVector = if (selected) item.selectedIcon else item.icon,
                    contentDescription = null,
                    tint = if (selected) Blue500 else Gray300,
                    modifier = Modifier.size(IconSizeDefault)
                )
                Spacer(modifier = Modifier.height(Spacing1))
                Text(
                    text = item.label,
                    style = fitMateTextStyle(11.sp, 16.5.sp, if (selected) FontWeight.Bold else FontWeight.Normal),
                    color = if (selected) Blue600 else TextSecondary
                )
            }
        }
    }
}

@Preview(widthDp = 394)
@Composable
private fun FitMateBottomBarPreview() {
    FitMateTheme {
        FitMateBottomBar(items = FitMateMainTabs, selectedKey = "home", onSelect = {})
    }
}
