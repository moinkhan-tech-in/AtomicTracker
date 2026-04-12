package com.challange.atomictracker.core.designsystem.components

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.sp
import com.challange.atomictracker.common.blink
import com.challange.atomictracker.core.domain.model.LiveFeedConnectionState

@Composable
fun FeedConnectionIndicator(
    connectionState: LiveFeedConnectionState
) {
    val emoji = when (connectionState) {
        LiveFeedConnectionState.Connected -> "🟢"
        LiveFeedConnectionState.Connecting -> "🟡"
        LiveFeedConnectionState.Disconnected -> "🔴"
    }
    Text(
        text = emoji,
        modifier = Modifier.blink(enabled = connectionState == LiveFeedConnectionState.Connected),
        style = MaterialTheme.typography.titleMedium,
        fontSize = 18.sp,
    )
}
