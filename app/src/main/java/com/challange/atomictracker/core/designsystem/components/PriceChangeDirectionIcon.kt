package com.challange.atomictracker.core.designsystem.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.TrendingFlat
import androidx.compose.material.icons.rounded.ArrowUpward
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalDensity
import com.challange.atomictracker.core.domain.model.PriceDirection
import com.challange.atomictracker.core.designsystem.theme.LocalAtomicTrackerTokens

@Composable
fun PriceChangeDirectionIcon(
    direction: PriceDirection,
    modifier: Modifier = Modifier,
    contentDescription: String? = null,
) {
    val tokens = LocalAtomicTrackerTokens.current
    val density = LocalDensity.current

    val isDirectional = direction != PriceDirection.Neutral
    val targetRotation = when (direction) {
        PriceDirection.Down -> 180f
        else -> 0f
    }
    val rotationY by animateFloatAsState(
        targetValue = if (isDirectional) targetRotation else 0f,
        animationSpec = tween(durationMillis = 320, easing = FastOutSlowInEasing)
    )
    val arrowTint by animateColorAsState(
        targetValue = when (direction) {
            PriceDirection.Down -> tokens.priceNegative
            PriceDirection.Up -> tokens.pricePositive
            PriceDirection.Neutral -> tokens.pricePositive
        },
        animationSpec = tween(220),
        label = "directionTint",
    )

    AnimatedContent(
        targetState = isDirectional,
        transitionSpec = {
            fadeIn(animationSpec = tween(220)) togetherWith fadeOut(animationSpec = tween(220))
        }
    ) { directional ->
        if (!directional) {
            Icon(
                imageVector = Icons.AutoMirrored.Rounded.TrendingFlat,
                contentDescription = contentDescription,
                tint = tokens.priceNeutral,
                modifier = modifier,
            )
        } else {
            Icon(
                imageVector = Icons.Rounded.ArrowUpward,
                contentDescription = contentDescription,
                tint = arrowTint,
                modifier = modifier.graphicsLayer {
                    this.rotationY = rotationY
                    cameraDistance = 16f * density.density
                },
            )
        }
    }
}