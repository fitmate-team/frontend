
package com.fitmate.presentation.mypage

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.fitmate.presentation.designsystem.theme.*
import com.fitmate.presentation.designsystem.R



data class GymInfo(
    val id: Int,
    val name: String,
    val equipment: List<String>,
    val isFavorite: Boolean = false
)

val initialGymList = listOf(
    GymInfo(
        id = 1,
        name = "FitGym",
        equipment = listOf(
            "덤벨", "바벨", "EZ바", "케틀벨",
            "벤치", "랫풀다운", "레그 프레스",
            "러닝머신", "실내 자전거",
            "스텝박스", "케이블", "스쿼트 랙"
        ),
        isFavorite = true
    ),
    GymInfo(
        id = 2,
        name = "벨네피트니스센터",
        equipment = listOf(
            "덤벨", "바벨", "EZ바", "케틀벨",
            "벤치", "랫풀다운", "레그 프레스",
            "러닝머신", "실내 자전거",
            "스텝박스", "케이블", "스쿼트 랙"
        )
    )
)

@Composable
fun GymListScreen(
    gyms: List<GymInfo> = initialGymList,
    onBack: () -> Unit = {},
    onEditGym: (Int) -> Unit = {},
    onAddGym: () -> Unit = {},
    onToggleFavorite: (Int) -> Unit = {}
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(White)
    ) {
        SettingsHeader(
            title = "내 헬스장",
            onBack = onBack
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp, vertical = 20.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            gyms.sortedWith(
                compareByDescending<GymInfo> { it.isFavorite }
            ).forEach { gym ->
                GymListCard(
                    gym = gym,
                    onEdit = { onEditGym(gym.id) },
                    onToggleFavorite = { onToggleFavorite(gym.id) }
                )
            }



            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                onClick = onAddGym,
                shape = RoundedCornerShape(12.dp),
                color = White,
                border = BorderStroke(1.dp, Blue600)
            ) {
                Box(
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "헬스장 추가",
                        fontFamily = FitMateFontFamily,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Blue600
                    )
                }
            }
        }
    }
}

@Composable
private fun GymListCard(
    gym: GymInfo,
    onEdit: () -> Unit,
    onToggleFavorite: () -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        color = White,
        border = BorderStroke(1.dp, BorderDefault)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {

                Row(
                    modifier = Modifier.weight(1f),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(5.dp)
                ) {
                    Text(
                        text = gym.name,
                        fontFamily = FitMateFontFamily,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Gray900
                    )

                    Icon(
                        painter = painterResource(
                            if (gym.isFavorite) {
                                R.drawable.ic_mypage_star_filled
                            } else {
                                R.drawable.ic_mypage_star_outline
                            }
                        ),
                        contentDescription = if (gym.isFavorite) {
                            "즐겨찾기 해제"
                        } else {
                            "즐겨찾기 설정"
                        },
                        tint = Color.Unspecified,
                        modifier = Modifier
                            .size(18.dp)
                            .clickable(onClick = onToggleFavorite)
                    )
                }

                Surface(
                    onClick = onEdit,
                    shape = RoundedCornerShape(100.dp),
                    color = White,
                    border = BorderStroke(1.dp, BorderDefault)
                ) {
                    Row(
                        modifier = Modifier.padding(
                            horizontal = 13.dp,
                            vertical = 3.dp
                        ),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.ic_profile_edit),
                            contentDescription = null,
                            tint = Color.Unspecified,
                            modifier = Modifier.size(12.dp)
                        )

                        Text(
                            text = "운동 기구 수정",
                            fontFamily = FitMateFontFamily,
                            fontSize = 12.sp,
                            color = Gray500
                        )
                    }
                }
            }

            Spacer(Modifier.height(8.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(1.dp)
                    .background(Gray300)
            )

            Spacer(Modifier.height(16.dp))

            Row(
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                gym.equipment.take(3).forEach { equipment ->
                    GymEquipmentChip(text = equipment)
                }

                if (gym.equipment.size > 3) {
                    GymEquipmentChip(
                        text = "+${gym.equipment.size - 3}"
                    )
                }
            }
        }
    }
}

@Composable
private fun GymEquipmentChip(
    text: String
) {
    Surface(
        shape = RoundedCornerShape(100.dp),
        color = White,
        border = BorderStroke(1.dp, BorderDefault)
    ) {
        Text(
            text = text,
            modifier = Modifier.padding(
                horizontal = 18.dp,
                vertical = 8.dp
            ),
            fontFamily = FitMateFontFamily,
            fontSize = 14.sp,
            color = Gray900
        )
    }
}
