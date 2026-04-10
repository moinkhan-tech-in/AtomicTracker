package com.challange.atomictracker.feature.detail

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.challange.atomictracker.core.designsystem.widgets.AtomicTrackerScaffold

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(
    id: String,
    onBack: () -> Unit
) {
    DetailScreenContent(
        id = id,
        onBack = onBack
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreenContent(id: String, onBack: () -> Unit) {
    AtomicTrackerScaffold(
        title = "Detail",
        navigationIcon = {
            TextButton(onClick = onBack) {
                Text("Back")
            }
        },
    ) { innerPadding ->
        Text(
            text = "Item id: $id",
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp),
        )
    }
}
