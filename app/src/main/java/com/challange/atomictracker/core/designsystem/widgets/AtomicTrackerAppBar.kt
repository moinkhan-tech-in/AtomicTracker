package com.challange.atomictracker.core.designsystem.widgets

import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AtomicTrackerAppBar(
    title: String,
    modifier: Modifier = Modifier,
    navigationIcon: @Composable () -> Unit = {},
    actions: @Composable RowScope.() -> Unit = {},
    colors: TopAppBarColors = TopAppBarDefaults.topAppBarColors(),
    scrollBehavior: TopAppBarScrollBehavior? = null,
) {
    TopAppBar(
        title = {
            Text(
                modifier = Modifier.padding(12.dp),
                text = title,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.titleLarge,
            )
        },
        modifier = modifier.padding(horizontal = 12.dp),
        navigationIcon = navigationIcon,
        actions = actions,
        colors = colors,
        scrollBehavior = scrollBehavior,
    )
}
