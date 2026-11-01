
package com.fitmate.presentation.mypage

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.fitmate.presentation.designsystem.R
import com.fitmate.presentation.designsystem.theme.*

private enum class ExercisePlace {
    HOME, OUTDOOR, GYM
}

@Composable
fun WorkoutEnvironmentSelectionScreen(
    onBack: () -> Unit = {},
    onOpenGym: () -> Unit = {},
    onOpenHome: () -> Unit = {},
    onOpenOutdoor: () -> Unit = {}
) {
    var isEditing by remember { mutableStateOf(false) }

    var primaryPlace by remember {
        mutableStateOf(ExercisePlace.GYM)
    }

    var draftPlace by remember {
        mutableStateOf(primaryPlace)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(White)
    ) {
        SettingsHeader(
            title = "내 운동 환경",
            onBack = {
                if (isEditing) {
                    isEditing = false
                    draftPlace = primaryPlace
                } else {
                    onBack()
                }
            }
        )

        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp)
                .padding(top = 12.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                if (!isEditing) {
                    Surface(
                        onClick = {
                            draftPlace = primaryPlace
                            isEditing = true
                        },
                        shape = RoundedCornerShape(100.dp),
                        color = White,
                        border = BorderStroke(
                            1.dp,
                            BorderDefault
                        )
                    ) {
                        Row(
                            modifier = Modifier.padding(
                                horizontal = 13.dp,
                                vertical = 2.dp
                            ),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Icon(
                                painter = painterResource(
                                    R.drawable.ic_profile_edit
                                ),
                                contentDescription = null,
                                tint = Color.Unspecified,
                                modifier = Modifier.size(12.dp)
                            )

                            Text(
                                text = "운동 환경 수정",
                                fontFamily = FitMateFontFamily,
                                fontSize = 12.sp,
                                color = Gray500
                            )
                        }
                    }
                }
            }

            EnvironmentPlaceCard(
                title = "집",
                description = "홈트레이닝",
                iconRes = R.drawable.ic_mypage_home,
                isPrimary = (if (isEditing) draftPlace else primaryPlace) ==
                        ExercisePlace.HOME,
                isEditing = isEditing,
                onClick = {
                    if (isEditing) {
                        draftPlace = ExercisePlace.HOME
                    } else {
                        onOpenHome()
                    }
                }
            )

            EnvironmentPlaceCard(
                title = "야외",
                description = "러닝, 걷기 등",
                iconRes = R.drawable.ic_mypage_fitness_center,
                isPrimary = (if (isEditing) draftPlace else primaryPlace) ==
                        ExercisePlace.OUTDOOR,
                isEditing = isEditing,
                onClick = {
                    if (isEditing) {
                        draftPlace = ExercisePlace.OUTDOOR
                    } else {
                        onOpenOutdoor()
                    }
                }
            )

            EnvironmentPlaceCard(
                title = "헬스장",
                description = "다양한 머신과 프리웨이트",
                iconRes = R.drawable.ic_mypage_fitness_center,
                isPrimary = (if (isEditing) draftPlace else primaryPlace) ==
                        ExercisePlace.GYM,
                isEditing = isEditing,
                onClick = {
                    if (isEditing) {
                        draftPlace = ExercisePlace.GYM
                    } else {
                        onOpenGym()
                    }
                }
            )

            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                color = Blue100,
                border = BorderStroke(1.dp, Blue200)
            ) {
                Column(
                    modifier = Modifier.padding(14.dp)
                ) {
                    Text(
                        text = "운동 환경도 루틴에 반영돼요.",
                        fontFamily = FitMateFontFamily,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = Blue700
                    )


                    Text(
                        text = "FitMate는 등록된 운동장소와 운동기구를 확인해 실제로 할 수 있는 운동으로 루틴을 구성해요.",
                        fontFamily = FitMateFontFamily,
                        fontSize = 12.sp,
                        color = Blue900
                    )
                }
            }
        }

        if (isEditing) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        horizontal = 16.dp,
                        vertical = 14.dp
                    )
            ) {
                Surface(
                    onClick = {
                        primaryPlace = draftPlace
                        isEditing = false
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp),
                    color = Blue600,
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Box(
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "변경사항 저장",
                            fontFamily = FitMateNumberFontFamily,
                            fontSize = 16.sp,
                            color = White
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun EnvironmentPlaceCard(
    title: String,
    description: String,
    iconRes: Int,
    isPrimary: Boolean,
    isEditing: Boolean,
    onClick: () -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        onClick = onClick,
        enabled = true,
        shape = RoundedCornerShape(16.dp),
        color = White,
        border = BorderStroke(
            1.dp,
            BorderDefault
        )
    ) {
        Row(
            modifier = Modifier.padding(
                horizontal = 16.dp,
                vertical = 16.dp
            ),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(45.6.dp)
                    .border(
                        1.dp,
                        color = BorderDefault,
                        shape = RoundedCornerShape(12.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(iconRes),
                    contentDescription = null,
                    tint = Color.Unspecified,
                    modifier = Modifier.size(26.dp)
                )
            }

            Spacer(Modifier.width(16.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = title,
                    fontFamily = FitMateFontFamily,
                    fontSize = 16.sp,
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

            if (isPrimary) {
                Surface(
                    shape = RoundedCornerShape(10.dp),
                    color = White,
                    border = BorderStroke(
                        1.dp,
                        Blue500
                    )
                ) {
                    Text(
                        text = "주로 이용",
                        modifier = Modifier.padding(
                            horizontal = 9.dp,
                            vertical = 3.dp
                        ),
                        fontFamily = FitMateFontFamily,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = Blue600
                    )
                }
            }
        }
    }
}
