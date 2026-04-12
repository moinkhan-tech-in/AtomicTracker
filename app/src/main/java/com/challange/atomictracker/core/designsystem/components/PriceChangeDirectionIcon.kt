package com.challange.atomictracker.core.designsystem.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.TrendingFlat
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowDropUp
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.challange.atomictracker.core.designsystem.theme.LocalAtomicTrackerColorScheme
import com.challange.atomictracker.core.domain.model.PriceDirection

@Composable
fun PriceChangeDirectionIcon(
    direction: PriceDirection,
    contentDescription: String? = null,
) {
    val tokens = LocalAtomicTrackerColorScheme.current

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
                    imageVector = Icons.AutoMirrored.Filled.TrendingFlat,
                    contentDescription = contentDescription,
                    tint = tokens.neutral,
                    modifier = Modifier.size(28.dp)
                )
            }

            PriceDirection.Up -> {
                Icon(
                    imageVector = Icons.Default.ArrowDropUp,
                    contentDescription = contentDescription,
                    tint = tokens.positive,
                    modifier = Modifier.size(28.dp)
                )
            }

            PriceDirection.Down -> {
                Icon(
                    imageVector = Icons.Default.ArrowDropDown,
                    contentDescription = contentDescription,
                    tint = tokens.negative,
                    modifier = Modifier.size(28.dp)
                )
            }
        }
    }
}
