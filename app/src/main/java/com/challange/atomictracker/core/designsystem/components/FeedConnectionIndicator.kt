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
import com.challange.atomictracker.core.designsystem.theme.LocalAtomicTrackerTokens

@Composable
fun FeedConnectionIndicator(
    isConnected: Boolean
) {
    val color by animateColorAsState(
        when {
            isConnected -> LocalAtomicTrackerTokens.current.pricePositive
            else -> LocalAtomicTrackerTokens.current.priceNegative
        }
    )
    Spacer(
        modifier = Modifier
            .size(16.dp)
            .clip(CircleShape)
            .background(color),
    )
}