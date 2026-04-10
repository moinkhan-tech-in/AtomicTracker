package com.challange.atomictracker.feature.feed

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.challange.atomictracker.core.domain.model.Stock
import com.challange.atomictracker.core.ui.widgets.AtomicTrackerCircularLoader
import com.challange.atomictracker.core.ui.widgets.AtomicTrackerErrorMessage
import com.challange.atomictracker.core.ui.widgets.AtomicTrackerScaffold
import com.challange.atomictracker.core.ui.components.StockQuoteGridItem
import com.challange.atomictracker.core.ui.theme.AtomicTrackerTheme
import kotlinx.collections.immutable.persistentListOf
import java.text.NumberFormat
import java.util.Locale

private val GridMinCellWidth = 120.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FeedScreen(
    viewModel: FeedViewModel = hiltViewModel(),
    onOpenDetail: (String) -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    FeedScreenContent(
        uiState = uiState,
        onOpenDetail = onOpenDetail,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FeedScreenContent(
    uiState: FeedUiState,
    onOpenDetail: (String) -> Unit,
) {
    val currencyFormat = remember { NumberFormat.getCurrencyInstance(Locale.US) }

    AtomicTrackerScaffold(title = "Stocks") { innerPadding ->
        Crossfade(uiState) { state ->
            when (state) {
            FeedUiState.Loading -> {
                AtomicTrackerCircularLoader(innerPadding)
            }

            is FeedUiState.Error -> {
                AtomicTrackerErrorMessage(state.message)
            }

            is FeedUiState.Empty -> {
                AtomicTrackerErrorMessage("No stocks to show")
            }

            is FeedUiState.Success  -> {
                LazyVerticalGrid(
                    columns = GridCells.Adaptive(minSize = GridMinCellWidth),
                    modifier = Modifier.fillMaxSize().padding(innerPadding),
                    contentPadding = PaddingValues(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                ) {
                    items(items = state.stocks, key = { it.symbol }) { stock ->
                        val priceText = remember(stock.price) {
                            currencyFormat.format(stock.price)
                        }
                        StockQuoteGridItem(
                            symbol = stock.symbol,
                            priceText = priceText,
                            direction = stock.priceDirection(),
                            onClick = { onOpenDetail(stock.symbol) },
                        )
                    }
                }
            }
        }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true)
@Composable
private fun FeedScreenSuccessPreview() {
    AtomicTrackerTheme {
        FeedScreenContent(
            uiState = FeedUiState.Success(
                persistentListOf(
                    Stock("AAPL", 189.42, 1.2),
                    Stock("TSLA", 172.10, -3.4),
                ),
            ),
            onOpenDetail = {},
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true)
@Composable
private fun FeedScreenSuccessDarkPreview() {
    AtomicTrackerTheme(darkTheme = true) {
        FeedScreenContent(
            uiState = FeedUiState.Success(
                persistentListOf(
                    Stock("AAPL", 189.42, 1.2),
                    Stock("TSLA", 172.10, -3.4),
                ),
            ),
            onOpenDetail = {},
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true)
@Composable
private fun FeedScreenLoadingPreview() {
    AtomicTrackerTheme {
        FeedScreenContent(
            uiState = FeedUiState.Loading,
            onOpenDetail = {},
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true)
@Composable
private fun FeedScreenErrorPreview() {
    AtomicTrackerTheme {
        FeedScreenContent(
            uiState = FeedUiState.Error("Network unavailable"),
            onOpenDetail = {},
        )
    }
}
