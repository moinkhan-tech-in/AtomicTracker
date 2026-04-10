package com.challange.atomictracker.core.designsystem.widgets

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.safeContent
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AtomicTrackerScaffold(
    title: String,
    modifier: Modifier = Modifier,
    navigationIcon: @Composable () -> Unit = {},
    actions: @Composable RowScope.() -> Unit= {},
    content: @Composable (PaddingValues) -> Unit,
) {
    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            AtomicTrackerAppBar(
                title = title,
                navigationIcon = navigationIcon,
                actions = actions,
            )
        },
        content = content,
    )
}
