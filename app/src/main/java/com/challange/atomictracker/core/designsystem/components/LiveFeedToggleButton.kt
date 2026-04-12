package com.challange.atomictracker.core.designsystem.components

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PauseCircle
import androidx.compose.material.icons.filled.PlayCircle
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.challange.atomictracker.R
import com.challange.atomictracker.core.domain.model.LiveFeedConnectionState

@Composable
fun LiveFeedToggleButton(
    liveFeedConnectionState: LiveFeedConnectionState,
    onSetLiveFeedEnabled: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    Box(modifier.size(38.dp), contentAlignment = Alignment.Center) {
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
