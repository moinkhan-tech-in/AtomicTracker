package com.challange.atomictracker.core.navigation

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally

private const val NAV_ANIM_DURATION_MS = 300

/** Destination entering on forward navigation (e.g. detail slides in from the right). */
internal fun enterForward(): EnterTransition =
    slideInHorizontally(
        animationSpec = tween(NAV_ANIM_DURATION_MS, easing = FastOutSlowInEasing),
        initialOffsetX = { fullWidth -> fullWidth },
    )

/** Destination exiting on forward navigation (e.g. feed slides off to the left). */
internal fun exitForward(): ExitTransition =
    slideOutHorizontally(
        animationSpec = tween(NAV_ANIM_DURATION_MS, easing = FastOutSlowInEasing),
        targetOffsetX = { fullWidth -> -fullWidth },
    )

/** Destination entering on pop (e.g. feed returns from the left). */
internal fun popEnter(): EnterTransition =
    slideInHorizontally(
        animationSpec = tween(NAV_ANIM_DURATION_MS, easing = FastOutSlowInEasing),
        initialOffsetX = { fullWidth -> -fullWidth },
    )

/** Destination exiting on pop (e.g. detail slides off to the right). */
internal fun popExit(): ExitTransition =
    slideOutHorizontally(
        animationSpec = tween(NAV_ANIM_DURATION_MS, easing = FastOutSlowInEasing),
        targetOffsetX = { fullWidth -> fullWidth },
    )
