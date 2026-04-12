package com.challange.atomictracker.feature.feed

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.staggeredgrid.LazyStaggeredGridState
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridItemSpan
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
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.challange.atomictracker.R
import com.challange.atomictracker.core.designsystem.components.FeedConnectionIndicator
import com.challange.atomictracker.core.designsystem.components.StockQuoteGridItem
import com.challange.atomictracker.core.designsystem.components.StockQuoteListItem
import com.challange.atomictracker.core.designsystem.theme.AtomicTrackerTheme
import com.challange.atomictracker.core.designsystem.theme.ThemeMode
import com.challange.atomictracker.core.designsystem.theme.ThemePickerMenuButton
import com.challange.atomictracker.core.designsystem.widgets.AtomicTrackerCircularLoader
import com.challange.atomictracker.core.designsystem.widgets.AtomicTrackerErrorMessage
import com.challange.atomictracker.core.designsystem.widgets.AtomicTrackerScaffold
import com.challange.atomictracker.core.domain.model.LiveFeedConnectionState
import com.challange.atomictracker.core.domain.model.Stock
import kotlinx.collections.immutable.persistentListOf

private val GridMinCellWidth = 120.dp

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun FeedScreen(
    viewModel: FeedViewModel = hiltViewModel(),
    onOpenDetail: (String) -> Unit,
    themeMode: ThemeMode = ThemeMode.FollowSystem,
    onThemeModeChange: (ThemeMode) -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val liveFeedConnectionState by viewModel.liveFeedConnectionState.collectAsStateWithLifecycle()
    FeedScreenContent(
        uiState = uiState,
        liveFeedConnectionState = liveFeedConnectionState,
        onSetLiveFeedEnabled = viewModel::setLiveFeedEnabled,
        onOpenDetail = onOpenDetail,
        themeMode = themeMode,
        onThemeModeChange = onThemeModeChange
    )
}

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun FeedScreenContent(
    uiState: FeedUiState,
    liveFeedConnectionState: LiveFeedConnectionState,
    onSetLiveFeedEnabled: (Boolean) -> Unit,
    onOpenDetail: (String) -> Unit,
    themeMode: ThemeMode = ThemeMode.FollowSystem,
    onThemeModeChange: (ThemeMode) -> Unit = {}
) {
    var isListView by rememberSaveable { mutableStateOf(true) }
    val feedGridState = rememberLazyStaggeredGridState()
    val topAppBarScrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    val gridColumns = remember(isListView) {
        when {
            isListView -> StaggeredGridCells.Fixed(1)
            else -> StaggeredGridCells.Adaptive(GridMinCellWidth)
        }
    }

    AtomicTrackerScaffold(
        title = stringResource(R.string.feed_title),
        scrollBehavior = topAppBarScrollBehavior,
        navigationIcon = {
            Row {
                Spacer(Modifier.size(12.dp))
                FeedConnectionIndicator(liveFeedConnectionState)
            }
        },
        actions = {
            ThemePickerMenuButton(
                themeMode = themeMode,
                onThemeModeChange = onThemeModeChange
            )
            IconButton(onClick = { isListView = !isListView }) {
                Icon(
                    imageVector = if (isListView) Icons.Default.GridView else Icons.AutoMirrored.Filled.ViewList,
                    contentDescription = if (isListView) {
                        stringResource(R.string.cd_layout_grid)
                    } else {
                        stringResource(R.string.cd_layout_list)
                    }
                )
            }

            ToggleFeedIcon(
                liveFeedConnectionState = liveFeedConnectionState,
                onSetLiveFeedEnabled = onSetLiveFeedEnabled
            )
        }
    ) { innerPadding ->
        when (uiState) {
            FeedUiState.Loading -> {
                AtomicTrackerCircularLoader(innerPadding)
            }

            is FeedUiState.Error -> {
                AtomicTrackerErrorMessage(uiState.message, padding = innerPadding)
            }

            is FeedUiState.Empty -> {
                AtomicTrackerErrorMessage(
                    stringResource(R.string.empty_feed),
                    padding = innerPadding,
                )
            }

            is FeedUiState.Success -> {
                FeedSuccessContent(
                    state = uiState,
                    isListView = isListView,
                    gridColumns = gridColumns,
                    feedGridState = feedGridState,
                    topAppBarScrollBehavior = topAppBarScrollBehavior,
                    onOpenDetail = onOpenDetail,
                    innerPadding = innerPadding
                )
            }
        }
    }
}

@Composable
private fun ToggleFeedIcon(
    liveFeedConnectionState: LiveFeedConnectionState,
    onSetLiveFeedEnabled: (Boolean) -> Unit
) {
    Box(Modifier.size(38.dp), contentAlignment = Alignment.Center) {
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
                        val connected = liveFeedConnectionState == LiveFeedConnectionState.Connected
                        Icon(
                            imageVector = when {
                                connected -> Icons.Default.PauseCircle
                                else -> Icons.Default.PlayCircle
                            },
                            contentDescription = when {
                                connected -> stringResource(R.string.cd_live_pause)
                                else -> stringResource(R.string.cd_live_resume)
                            },
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun FeedSuccessContent(
    state: FeedUiState.Success,
    isListView: Boolean,
    gridColumns: StaggeredGridCells,
    feedGridState: LazyStaggeredGridState,
    topAppBarScrollBehavior: TopAppBarScrollBehavior,
    onOpenDetail: (String) -> Unit,
    innerPadding: PaddingValues
) {
    LazyVerticalStaggeredGrid(
        columns = gridColumns,
        state = feedGridState,
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
            .nestedScroll(topAppBarScrollBehavior.nestedScrollConnection),
        contentPadding = innerPadding,
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalItemSpacing = 12.dp
    ) {
        val span = if (isListView) StaggeredGridItemSpan.SingleLane else StaggeredGridItemSpan.FullLine

        item(span = span) { Spacer(Modifier.size(8.dp)) }

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

        item(span = span) { Spacer(Modifier.size(32.dp)) }
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
    AtomicTrackerTheme(themeMode = ThemeMode.Dark) {
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
