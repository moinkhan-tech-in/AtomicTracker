package com.challange.atomictracker.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.challange.atomictracker.core.connectivity.NetworkConnectivityObserver
import com.challange.atomictracker.core.designsystem.theme.AtomicTrackerRoot
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var networkConnectivityObserver: NetworkConnectivityObserver

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val isOnline by networkConnectivityObserver.isOnline.collectAsStateWithLifecycle(false)
            AtomicTrackerRoot(isOnline = isOnline)
        }
    }
}
