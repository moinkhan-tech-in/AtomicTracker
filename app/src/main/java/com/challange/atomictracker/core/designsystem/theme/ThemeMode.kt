package com.challange.atomictracker.core.designsystem.theme

enum class ThemeMode {
    FollowSystem,
    Light,
    Dark;

    fun resolveDark(systemIsDark: Boolean): Boolean =
        when (this) {
            FollowSystem -> systemIsDark
            Light -> false
            Dark -> true
        }
}