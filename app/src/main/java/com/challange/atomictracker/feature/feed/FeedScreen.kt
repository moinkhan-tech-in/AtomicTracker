package com.challange.atomictracker.feature.feed

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.staggeredgrid.LazyStaggeredGridState
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.foundation.lazy.staggeredgrid.rememberLazyStaggeredGridState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ViewList
import androidx.compose.material.icons.filled.GridView
import androidx.compose.material.icons.filled.PauseCircle
import androidx.compose.material.icons.filled.PlayCircle
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.challange.atomictracker.core.designsystem.components.FeedConnectionIndicator
import com.challange.atomictracker.core.designsystem.components.StockQuoteGridItem
import com.challange.atomictracker.core.designsystem.components.StockQuoteListItem
import com.challange.atomictracker.core.designsystem.theme.AtomicTrackerTheme
import com.challange.atomictracker.core.designsystem.widgets.AtomicTrackerCircularLoader
import com.challange.atomictracker.core.designsystem.widgets.AtomicTrackerErrorMessage
import com.challange.atomictracker.core.designsystem.widgets.AtomicTrackerScaffold
import com.challange.atomictracker.core.domain.model.LiveFeedConnectionState
import com.challange.atomictracker.core.domain.model.Stock
import kotlinx.collections.immutable.persistentListOf

private val GridMinCellWidth = 100.dp

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun FeedScreen(
    viewModel: FeedViewModel = hiltViewModel(),
    onOpenDetail: (String) -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val liveFeedConnectionState by viewModel.liveFeedConnectionState.collectAsStateWithLifecycle()
    FeedScreenContent(
        uiState = uiState,
        liveFeedConnectionState = liveFeedConnectionState,
        onSetLiveFeedEnabled = viewModel::setLiveFeedEnabled,
        onOpenDetail = onOpenDetail,
    )
}

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun FeedScreenContent(
    uiState: FeedUiState,
    liveFeedConnectionState: LiveFeedConnectionState,
    onSetLiveFeedEnabled: (Boolean) -> Unit,
    onOpenDetail: (String) -> Unit
) {
    var isListView by rememberSaveable { mutableStateOf(true) }
    val feedGridState = rememberLazyStaggeredGridState()
    val gridColumns = remember(isListView) {
        when {
            isListView -> StaggeredGridCells.Fixed(1)
            else -> StaggeredGridCells.Adaptive(GridMinCellWidth)
        }
    }

    AtomicTrackerScaffold(
        title = "Stocks",
        navigationIcon = { FeedConnectionIndicator(liveFeedConnectionState) },
        actions = {
            IconButton(onClick = { isListView = !isListView }) {
                Icon(
                    imageVector = if (isListView) Icons.Default.GridView else Icons.AutoMirrored.Filled.ViewList,
                    contentDescription = if (isListView) "Grid view" else "List view"
                )
            }

            ToggleFeedIcon(
                liveFeedConnectionState = liveFeedConnectionState,
                onSetLiveFeedEnabled = onSetLiveFeedEnabled
            )
        }
    ) { innerPadding ->
        Crossfade(uiState) { state ->
            when (state) {
                FeedUiState.Loading -> {
                    AtomicTrackerCircularLoader(innerPadding)
                }

                is FeedUiState.Error -> {
                    AtomicTrackerErrorMessage(state.message, padding = innerPadding)
                }

                is FeedUiState.Empty -> {
                    AtomicTrackerErrorMessage("No stocks to show", padding = innerPadding)
                }

                is FeedUiState.Success -> {
                    FeedSuccessContent(
                        state = state,
                        isListView = isListView,
                        gridColumns = gridColumns,
                        feedGridState = feedGridState,
                        onOpenDetail = onOpenDetail,
                        innerPadding = innerPadding
                    )
                }
            }
        }
    }
}

@Composable
private fun ToggleFeedIcon(
    liveFeedConnectionState: LiveFeedConnectionState,
    onSetLiveFeedEnabled: (Boolean) -> Unit
) {
    Box(Modifier.size(32.dp), contentAlignment = Alignment.Center) {
        Crossfade(liveFeedConnectionState, modifier = Modifier.size(24.dp)) {
            when (it) {
                LiveFeedConnectionState.Connecting -> {
                    CircularProgressIndicator(Modifier.size(24.dp))
                }

                else -> {
                    IconButton(
                        onClick = {
                            when (liveFeedConnectionState) {
                                LiveFeedConnectionState.Disconnected -> onSetLiveFeedEnabled(true)
                                LiveFeedConnectionState.Connected -> onSetLiveFeedEnabled(false)
                                LiveFeedConnectionState.Connecting -> {}
                            }
                        },
                    ) {
                        Icon(
                            imageVector = if (liveFeedConnectionState == LiveFeedConnectionState.Connected) Icons.Default.PauseCircle else Icons.Default.PlayCircle,
                            contentDescription = if (liveFeedConnectionState == LiveFeedConnectionState.Connected) "Pause live updates" else "Resume live updates"
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun FeedSuccessContent(
    state: FeedUiState.Success,
    isListView: Boolean,
    gridColumns: StaggeredGridCells,
    feedGridState: LazyStaggeredGridState,
    onOpenDetail: (String) -> Unit,
    innerPadding: PaddingValues
) {
    LazyVerticalStaggeredGrid(
        columns = gridColumns,
        state = feedGridState,
        modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp),
        contentPadding = innerPadding,
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalItemSpacing = 12.dp
    ) {
        if (isListView) {
            items(items = state.stocks, key = { it.symbol }) { stock ->
                StockQuoteListItem(
                    symbol = stock.symbol,
                    companyName = stock.companyName,
                    price = stock.price,
                    change = stock.change,
                    direction = stock.priceDirection(),
                    onClick = { onOpenDetail(stock.symbol) },
                )
            }
        } else {
            items(items = state.stocks, key = { it.symbol }) { stock ->
                StockQuoteGridItem(
                    symbol = stock.symbol,
                    companyName = stock.companyName,
                    price = stock.price,
                    change = stock.change,
                    direction = stock.priceDirection(),
                    onClick = { onOpenDetail(stock.symbol) },
                )
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
                    Stock("AAPL", 189.42, 1.2, "Apple Inc."),
                    Stock("TSLA", 172.10, -3.4, "Tesla, Inc."),
                ),
            ),
            liveFeedConnectionState = LiveFeedConnectionState.Connected,
            onSetLiveFeedEnabled = {},
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
                    Stock("AAPL", 189.42, 1.2, "Apple Inc."),
                    Stock("TSLA", 172.10, -3.4, "Tesla, Inc."),
                ),
            ),
            liveFeedConnectionState = LiveFeedConnectionState.Connected,
            onSetLiveFeedEnabled = {},
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
            liveFeedConnectionState = LiveFeedConnectionState.Connecting,
            onSetLiveFeedEnabled = {},
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
            liveFeedConnectionState = LiveFeedConnectionState.Disconnected,
            onSetLiveFeedEnabled = {},
            onOpenDetail = {},
        )
    }
}
