package com.challange.atomictracker.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.challange.atomictracker.core.navigation.AtomicTrackerNavHost
import com.challange.atomictracker.core.ui.theme.AtomicTrackerTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AtomicTrackerTheme {
                AtomicTrackerNavHost()
            }
        }
    }
}
