package com.challange.atomictracker.feature.detail

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.challange.atomictracker.core.designsystem.components.PriceChangeDirectionIcon
import com.challange.atomictracker.core.designsystem.theme.AtomicTrackerTheme
import com.challange.atomictracker.core.designsystem.theme.ThemeMode
import com.challange.atomictracker.core.designsystem.theme.ThemePickerMenuButton
import com.challange.atomictracker.core.designsystem.theme.LocalAtomicTrackerColorScheme
import com.challange.atomictracker.core.designsystem.widgets.AtomicTrackerCircularLoader
import com.challange.atomictracker.core.designsystem.widgets.AtomicTrackerErrorMessage
import com.challange.atomictracker.core.designsystem.widgets.AtomicTrackerScaffold
import com.challange.atomictracker.core.domain.model.PriceDirection
import com.challange.atomictracker.core.domain.model.Stock
import java.text.NumberFormat
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(
    symbol: String,
    onBack: () -> Unit,
    viewModel: DetailViewModel = hiltViewModel(),
    themeMode: ThemeMode = ThemeMode.FollowSystem,
    onThemeModeChange: (ThemeMode) -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    DetailScreenContent(
        symbol = symbol,
        uiState = uiState,
        onBack = onBack,
        themeMode = themeMode,
        onThemeModeChange = onThemeModeChange,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreenContent(
    symbol: String,
    uiState: DetailUiState,
    onBack: () -> Unit,
    themeMode: ThemeMode = ThemeMode.FollowSystem,
    onThemeModeChange: (ThemeMode) -> Unit = {}
) {
    val title = when (uiState) {
        is DetailUiState.Success -> uiState.stock.symbol
        else -> symbol
    }
    AtomicTrackerScaffold(
        title = title,
        navigationIcon = {
            TextButton(onClick = onBack) {
                Text("Back")
            }
        },
        actions = {
            ThemePickerMenuButton(
                themeMode = themeMode,
                onThemeModeChange = onThemeModeChange
            )
        },
    ) { innerPadding ->
        Crossfade(uiState, label = "detailState") { state ->
            when (state) {
                DetailUiState.Loading -> {
                    AtomicTrackerCircularLoader(innerPadding)
                }

                is DetailUiState.Error -> {
                    AtomicTrackerErrorMessage(state.message, padding = innerPadding)
                }

                is DetailUiState.Success -> {
                    DetailQuoteBody(
                        stock = state.stock,
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding)
                            .padding(horizontal = 16.dp),
                    )
                }
            }
        }
    }
}

@Composable
private fun DetailQuoteBody(
    stock: Stock,
    modifier: Modifier = Modifier,
) {
    val currencyFormat = remember { NumberFormat.getCurrencyInstance(Locale.US) }
    val tokens = LocalAtomicTrackerColorScheme.current
    val changeColor = when (stock.priceDirection()) {
        PriceDirection.Up -> tokens.positive
        PriceDirection.Down -> tokens.negative
        PriceDirection.Neutral -> tokens.neutral
    }
    val priceText = remember(stock.price) { currencyFormat.format(stock.price) }
    val changeText = remember(stock.change) {
        String.format(Locale.US, "%+.2f", stock.change)
    }

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Top,
    ) {
        Text(
            text = stock.companyName,
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Last price",
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = priceText,
            style = MaterialTheme.typography.displaySmall,
            color = MaterialTheme.colorScheme.onSurface,
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Change",
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        Spacer(modifier = Modifier.height(8.dp))
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Text(
                text = changeText,
                style = MaterialTheme.typography.headlineSmall,
                color = changeColor,
            )
            PriceChangeDirectionIcon(
                direction = stock.priceDirection(),
                modifier = Modifier.size(32.dp),
                contentDescription = null,
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true)
@Composable
private fun DetailScreenSuccessPreview() {
    AtomicTrackerTheme {
        DetailScreenContent(
            symbol = "AAPL",
            uiState = DetailUiState.Success(
                Stock("AAPL", 189.42, 1.25, "Apple Inc."),
            ),
            onBack = {},
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true)
@Composable
private fun DetailScreenLoadingPreview() {
    AtomicTrackerTheme {
        DetailScreenContent(
            symbol = "NVDA",
            uiState = DetailUiState.Loading,
            onBack = {},
        )
    }
}
