package com.example.maruchapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.maruchapp.ui.navigation.AppNavigation
import com.example.maruchapp.ui.theme.MaruchAPPTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaruchAPPTheme {
                AppNavigation()
            }
        }
    }
}