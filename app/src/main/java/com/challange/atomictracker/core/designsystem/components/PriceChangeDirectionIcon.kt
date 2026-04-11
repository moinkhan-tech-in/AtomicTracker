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
import com.challange.atomictracker.core.domain.model.PriceDirection
import com.challange.atomictracker.core.designsystem.theme.LocalAtomicTrackerTokens

@Composable
fun PriceChangeDirectionIcon(
    direction: PriceDirection,
    modifier: Modifier = Modifier,
    contentDescription: String? = null,
) {
    val tokens = LocalAtomicTrackerTokens.current

    AnimatedContent(
        targetState = direction,
        transitionSpec = {
            fadeIn(animationSpec = tween(220)) togetherWith fadeOut(animationSpec = tween(220))
        },
        label = "priceDirection",
    ) { dir ->
        when (dir) {
            PriceDirection.Neutral -> {
                Icon(
                    imageVector = Icons.AutoMirrored.Rounded.TrendingFlat,
                    contentDescription = contentDescription,
                    tint = tokens.neutral,
                    modifier = modifier,
                )
            }

            PriceDirection.Up -> {
                Icon(
                    imageVector = Icons.Rounded.ArrowUpward,
                    contentDescription = contentDescription,
                    tint = tokens.positive,
                    modifier = modifier,
                )
            }

            PriceDirection.Down -> {
                val flipRotation by animateFloatAsState(
                    targetValue = 180f,
                    animationSpec = tween(durationMillis = 320, easing = FastOutSlowInEasing),
                    label = "arrowDownRotation",
                )
                val tint by animateColorAsState(
                    targetValue = tokens.negative,
                    animationSpec = tween(220),
                    label = "arrowDownTint",
                )
                Icon(
                    imageVector = Icons.Rounded.ArrowUpward,
                    contentDescription = contentDescription,
                    tint = tint,
                    modifier = modifier.graphicsLayer { rotationZ = flipRotation },
                )
            }
        }
    }
}