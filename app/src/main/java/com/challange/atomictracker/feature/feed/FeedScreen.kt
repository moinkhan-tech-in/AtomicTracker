package com.challange.atomictracker.feature.feed

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.challange.atomictracker.core.ui.components.AtomicTrackerScaffold
import com.challange.atomictracker.core.ui.theme.AtomicTrackerTheme

@Composable
fun FeedScreen(
    onOpenDetail: (String) -> Unit
) {
    FeedScreenContent(onOpenDetail = onOpenDetail,)
}

@Composable
fun FeedScreenContent(
    onOpenDetail: (String) -> Unit = {}
) {
    AtomicTrackerScaffold(title = "Feed") { innerPadding ->
        Button(
            onClick = { onOpenDetail("demo-item-1") },
            modifier = Modifier
                .padding(innerPadding)
                .padding(16.dp),
        ) {
            Text("Open detail (safe args)")
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun FeedScreenPreview() {
    AtomicTrackerTheme {
        FeedScreenContent()
    }
}
