package com.challange.atomictracker.core.designsystem.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.challange.atomictracker.core.designsystem.theme.AtomicTrackerTheme
import com.challange.atomictracker.core.designsystem.theme.LocalAtomicTrackerColorScheme
import com.challange.atomictracker.domain.model.PriceDirection
import java.text.NumberFormat
import java.util.Locale

private const val PRICE_FLASH_DURATION_MS = 1000

@Composable
fun QuoteFlashPriceText(
    price: Double,
    change: Double,
    flashColor: Color,
    style: TextStyle,
    modifier: Modifier = Modifier,
    textAlign: TextAlign = TextAlign.Unspecified
) {
    val currencyFormat = remember { NumberFormat.getCurrencyInstance(Locale.US) }
    val priceText = remember(price) { currencyFormat.format(price) }
    val baseColor = MaterialTheme.colorScheme.onSurface
    val progress = remember { Animatable(1f) }

    LaunchedEffect(price, change) {
        if (change == 0.0) {
            progress.snapTo(1f)
            return@LaunchedEffect
        }
        progress.snapTo(0f)
        progress.animateTo(
            targetValue = 1f,
            animationSpec = tween(
                durationMillis = PRICE_FLASH_DURATION_MS,
                easing = FastOutLinearInEasing
            )
        )
    }

    val priceColor = lerp(flashColor, baseColor, progress.value)
    Text(
        text = priceText,
        modifier = modifier,
        style = style.copy(fontWeight = FontWeight.Bold),
        color = priceColor,
        textAlign = textAlign,
    )
}

@Composable
fun StockQuoteListItem(
    symbol: String,
    companyName: String,
    price: Double,
    change: Double,
    direction: PriceDirection,
    onClick: (() -> Unit)? = null
) {
    val tokens = LocalAtomicTrackerColorScheme.current
    val changeColor = when (direction) {
        PriceDirection.Up -> tokens.pricePositive
        PriceDirection.Down -> tokens.priceNegative
        PriceDirection.Neutral -> tokens.priceNeutral
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
        shape = MaterialTheme.shapes.medium,
        border = BorderStroke(.5.dp, color = MaterialTheme.colorScheme.outlineVariant),
        onClick = { onClick?.invoke() }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Column(modifier = Modifier.weight(1f, fill = false)) {
                Text(
                    text = symbol,
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onSurface,
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = companyName,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
            }
            Column(horizontalAlignment = Alignment.End) {
                QuoteFlashPriceText(
                    price = price,
                    change = change,
                    flashColor = changeColor,
                    style = MaterialTheme.typography.titleLarge,
                )
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(2.dp),
                ) {
                    val changeText = remember(change) {
                        String.format(Locale.US, "%+.2f", change)
                    }
                    Text(
                        text = changeText,
                        style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold),
                        color = changeColor,
                    )
                    PriceChangeDirectionIcon(direction = direction)
                }
            }
        }
    }
}

@Composable
fun StockQuoteGridItem(
    symbol: String,
    companyName: String,
    price: Double,
    change: Double,
    direction: PriceDirection,
    onClick: (() -> Unit)? = null
) {
    val tokens = LocalAtomicTrackerColorScheme.current
    val changeColor = when (direction) {
        PriceDirection.Up -> tokens.pricePositive
        PriceDirection.Down -> tokens.priceNegative
        PriceDirection.Neutral -> tokens.priceNeutral
    }

    Card(
        modifier = Modifier.fillMaxWidth().aspectRatio(1f),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
        shape = MaterialTheme.shapes.medium,
        border = BorderStroke(.5.dp, color = MaterialTheme.colorScheme.outlineVariant),
        onClick = { onClick?.invoke() }
    ) {
        Column(
            modifier = Modifier.fillMaxSize().padding(12.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                text = symbol,
                modifier = Modifier.fillMaxWidth(),
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onSurface,
                textAlign = TextAlign.Center,
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = companyName,
                modifier = Modifier.fillMaxWidth(),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                textAlign = TextAlign.Center,
            )
            Spacer(modifier = Modifier.height(6.dp))
            QuoteFlashPriceText(
                price = price,
                change = change,
                flashColor = changeColor,
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
            ) {
                val changeText = remember(change) {
                    String.format(Locale.US, "%+.2f", change)
                }
                Text(
                    text = changeText,
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    color = changeColor,
                )
                PriceChangeDirectionIcon(direction = direction)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun StockQuoteListItemPreview() {
    AtomicTrackerTheme {
        StockQuoteListItem(
            symbol = "AAPL",
            companyName = "Apple Inc.",
            price = 182.9,
            change = 1.25,
            direction = PriceDirection.Up,
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun StockQuoteGridItemPreview() {
    AtomicTrackerTheme {
        StockQuoteGridItem(
            symbol = "NVDA",
            companyName = "NVIDIA Corporation",
            price = 143.9,
            change = -2.15,
            direction = PriceDirection.Down
        )
    }
}
