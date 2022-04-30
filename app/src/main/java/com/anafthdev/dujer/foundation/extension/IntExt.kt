package com.anafthdev.dujer.foundation.extension

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp

fun Int.isMinus(): Boolean = this < 0

fun Int.isPositive(): Boolean = this >= 0

/**
 * Convert an Int pixel value to Dp.
 */
@Composable
fun Int.toDp(): Dp = with(LocalDensity.current) { this@toDp.toDp() }

/**
 * Convert an Int pixel value to Dp.
 */
fun Int.toDp(density: Density): Dp = with(density) { this@toDp.toDp() }
