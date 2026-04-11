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
import androidx.compose.ui.unit.dp

@Composable
fun ThemePickerMenuButton(
    themeMode: ThemeMode,
    onThemeModeChange: (ThemeMode) -> Unit,
    modifier: Modifier = Modifier,
) {
    var expanded by remember { mutableStateOf(false) }
    Box(modifier = modifier) {
        IconButton(onClick = { expanded = true }) {
            val (icon, description) = when (themeMode) {
                ThemeMode.Light -> Icons.Outlined.LightMode to "Theme: light"
                ThemeMode.Dark -> Icons.Outlined.DarkMode to "Theme: dark"
                ThemeMode.FollowSystem -> Icons.Outlined.BrightnessAuto to "Theme: follow system"
            }
            Icon(
                imageVector = icon,
                contentDescription = description,
            )
        }
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
        ) {
            ThemeOptionMenuItem(
                label = "Light",
                selected = themeMode == ThemeMode.Light,
                onClick = {
                    onThemeModeChange(ThemeMode.Light)
                    expanded = false
                },
            )
            ThemeOptionMenuItem(
                label = "Dark",
                selected = themeMode == ThemeMode.Dark,
                onClick = {
                    onThemeModeChange(ThemeMode.Dark)
                    expanded = false
                },
            )
            ThemeOptionMenuItem(
                label = "Follow system",
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
