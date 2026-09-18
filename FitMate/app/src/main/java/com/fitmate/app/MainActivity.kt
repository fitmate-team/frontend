package com.fitmate.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.fitmate.app.navigation.FitMateNavHost
import com.fitmate.presentation.designsystem.theme.FitMateTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            FitMateTheme {
                FitMateNavHost()
            }
        }
    }
}