package com.fitmate.presentation.workout

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.runtime.LaunchedEffect
import kotlinx.coroutines.delay

@Composable
fun WorkoutRecommendationLoadingScreen(
    modifier: Modifier = Modifier,
    onComplete: () -> Unit = {}
) {
    // 3초 후 다음 화면으로 이동
    LaunchedEffect(Unit) {
        delay(3000L)
        onComplete()
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color.White),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            painter = painterResource(
                id = com.fitmate.presentation.designsystem.R.drawable.img_fitmate_robot
            ),
            contentDescription = "FitMate AI",
            modifier = Modifier.size(150.dp)
        )

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = "회원님에게 맞는 운동을\n분석하고 있어요.",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black,
            textAlign = TextAlign.Center,
            lineHeight = 32.sp
        )

        Spacer(modifier = Modifier.height(32.dp))

        CircularProgressIndicator(
            modifier = Modifier.size(40.dp),
            color = Color(0xFF007BE9),
            strokeWidth = 4.dp
        )
    }
}