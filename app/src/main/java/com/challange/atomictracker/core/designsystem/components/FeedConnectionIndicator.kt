package com.challange.atomictracker.core.designsystem.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.challange.atomictracker.common.blink
import com.challange.atomictracker.core.designsystem.theme.LocalAtomicTrackerColorScheme
import com.challange.atomictracker.core.domain.model.LiveFeedConnectionState

@Composable
fun FeedConnectionIndicator(
    connectionState: LiveFeedConnectionState
) {
    val tokens = LocalAtomicTrackerColorScheme.current
    val color by animateColorAsState(
        when (connectionState) {
            LiveFeedConnectionState.Connected -> tokens.positive
            LiveFeedConnectionState.Connecting -> tokens.neutral
            LiveFeedConnectionState.Disconnected -> tokens.negative
        }
    )
    Spacer(
        modifier = Modifier
            .size(16.dp)
            .clip(CircleShape)
            .background(color)
            .blink(enabled = connectionState == LiveFeedConnectionState.Connected),
    )
}
