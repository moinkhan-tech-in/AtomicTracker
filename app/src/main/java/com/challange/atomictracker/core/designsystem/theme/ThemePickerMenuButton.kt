package com.challange.atomictracker.core.designsystem.theme

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.outlined.BrightnessAuto
import androidx.compose.material.icons.outlined.DarkMode
import androidx.compose.material.icons.outlined.LightMode
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.challange.atomictracker.R

@Composable
fun ThemePickerMenuButton(
    themeMode: ThemeMode,
    onThemeModeChange: (ThemeMode) -> Unit,
    modifier: Modifier = Modifier,
) {
    var expanded by remember { mutableStateOf(false) }
    Box(modifier = modifier) {
        IconButton(onClick = { expanded = true }) {
            val icon = when (themeMode) {
                ThemeMode.Light -> Icons.Outlined.LightMode
                ThemeMode.Dark -> Icons.Outlined.DarkMode
                ThemeMode.FollowSystem -> Icons.Outlined.BrightnessAuto
            }
            Icon(
                imageVector = icon,
                contentDescription = null,
            )
        }
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
        ) {
            ThemeOptionMenuItem(
                label = stringResource(R.string.theme_light),
                selected = themeMode == ThemeMode.Light,
                onClick = {
                    onThemeModeChange(ThemeMode.Light)
                    expanded = false
                },
            )
            ThemeOptionMenuItem(
                label = stringResource(R.string.theme_dark),
                selected = themeMode == ThemeMode.Dark,
                onClick = {
                    onThemeModeChange(ThemeMode.Dark)
                    expanded = false
                },
            )
            ThemeOptionMenuItem(
                label = stringResource(R.string.theme_follow_system),
                selected = themeMode == ThemeMode.FollowSystem,
                onClick = {
                    onThemeModeChange(ThemeMode.FollowSystem)
                    expanded = false
                },
            )
        }
    }
}

@Composable
private fun ThemeOptionMenuItem(
    label: String,
    selected: Boolean,
    onClick: () -> Unit,
) {
    DropdownMenuItem(
        text = { Text(label) },
        onClick = onClick,
        leadingIcon = {
            Box(Modifier.width(24.dp)) {
                if (selected) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = null,
                        modifier = Modifier.size(20.dp),
                    )
                }
            }
        },
    )
}
