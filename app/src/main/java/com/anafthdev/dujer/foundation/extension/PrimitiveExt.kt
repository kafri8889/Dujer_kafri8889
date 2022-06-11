package com.anafthdev.dujer.foundation.extension

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp

fun Int.isMinus(): Boolean = this < 0

fun Int.isPositive(): Boolean = this >= 0

fun Float.isMinus(): Boolean = this < 0

fun Float.isPositive(): Boolean = this >= 0

fun Double.isMinus(): Boolean = this < 0

fun Double.isPositive(): Boolean = this >= 0


fun Int.toPositive(): Int = if (this <= -1) this * -1 else this

fun Int.toNegative(): Int = if (this >= 0) this * -1 else this

fun Float.toPositive(): Float = if (this <= -1) this * -1 else this

fun Float.toNegative(): Float = if (this >= 0) this * -1 else this

fun Double.toPositive(): Double = if (this <= -1) this * -1 else this

fun Double.toNegative(): Double = if (this >= 0) this * -1 else this


/**
 * Convert an Int pixel value to Dp.
 */
@Composable
fun Int.toDp(): Dp = with(LocalDensity.current) { this@toDp.toDp() }

/**
 * Convert an Int pixel value to Dp.
 */
fun Int.toDp(density: Density): Dp = with(density) { this@toDp.toDp() }

/**
 * Convert an Int pixel value to Dp.
 */
@Composable
fun Float.toDp(): Dp = with(LocalDensity.current) { this@toDp.toDp() }

/**
 * Convert an Int pixel value to Dp.
 */
fun Float.toDp(density: Density): Dp = with(density) { this@toDp.toDp() }

/**
 * Convert an Int pixel value to Dp.
 */
@Composable
fun Double.toDp(): Dp = with(LocalDensity.current) { this@toDp.toDp() }

/**
 * Convert an Int pixel value to Dp.
 */
@Composable
fun Double.toDp(density: Density): Dp = with(density) { this@toDp.toDp() }
