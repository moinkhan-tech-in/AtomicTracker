package com.challange.atomictracker.core.navigation

import kotlinx.serialization.Serializable

/** Type-safe destinations for Compose Navigation (Kotlin Serialization — Compose “Safe Args”). */

@Serializable
data object FeedRoute

@Serializable
data class DetailRoute(val symbol: String)
