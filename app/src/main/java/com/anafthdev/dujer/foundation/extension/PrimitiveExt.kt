package com.anafthdev.dujer.foundation.extension

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import kotlin.math.roundToInt

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


fun Collection<Int>.averageInt(ifNaN: (() -> Double)? = null): Double {
	var sum = 0.0
	forEach { sum += it }
	
	return (sum / size).let {
		return@let if (it.isNaN() && ifNaN != null) ifNaN() else it
	}
}

fun Collection<Float>.averageFloat(ifNaN: (() -> Double)? = null): Double {
	var sum = 0.0
	forEach { sum += it }
	
	return (sum / size).let {
		return@let if (it.isNaN() && ifNaN != null) ifNaN() else it
	}
}

fun Collection<Double>.averageDouble(ifNaN: (() -> Double)? = null): Double {
	var sum = 0.0
	forEach { sum += it }
	
	return (sum / size).let {
		return@let if (it.isNaN() && ifNaN != null) ifNaN() else it
	}
}


fun Float.roundToInt(onNaN: (Float) -> Int): Int {
	return try { roundToInt() } catch (e: Exception) { return onNaN(this) }
}

fun Double.roundToInt(onNaN: (Double) -> Int): Int {
	return try { roundToInt() } catch (e: Exception) { return onNaN(this) }
}


/**
 * convert to percent
 *
 * 12.5 percentOf 5 = 40.0%
 *
 * 14.6 percentOf 4.2 = 28.7671%
 */
fun Float.percentOf(value: Float, ifNaN: (() -> Float)? = null): Float {
	val percent = value.div(this).times(100)
	return if ((ifNaN != null) and percent.isNaN()) ifNaN!!() else percent
}

fun Float.percentOf(value: Int, ifNaN: (() -> Float)? = null): Float {
	val percent = value.div(this).times(100)
	return if ((ifNaN != null) and percent.isNaN()) ifNaN!!() else percent
}

fun Float.roundPercentOf(value: Float): Int {
	return value.div(this).times(100).roundToInt()
}

fun Float.roundPercentOf(value: Int): Int {
	return value.div(this).times(100).roundToInt()
}

/**
 * convert to percent
 *
 * 12 percentOf 5 = 41.67%
 *
 * 14 percentOf 4 = 28.57%
 */
fun Int.percentOf(value: Float): Float {
	return value.div(this).times(100)
}

fun Int.roundPercentOf(value: Float): Int {
	return value.div(this).times(100).roundToInt()
}

/**
 * convert to percent
 *
 * 12.5 percentOf 5 = 40.0%
 *
 * 14.6 percentOf 4.2 = 28.7671%
 */
fun Double.percentOf(value: Double, ifNaN: (() -> Double)? = null): Double {
	val percent = value.div(this).times(100)
	return if ((ifNaN != null) and percent.isNaN()) ifNaN!!() else percent
}

fun Double.percentOf(value: Int, ifNaN: (() -> Double)? = null): Double {
	val percent = value.div(this).times(100)
	return if ((ifNaN != null) and percent.isNaN()) ifNaN!!() else percent
}

fun Double.roundPercentOf(value: Double): Int {
	return value.div(this).times(100).roundToInt()
}

fun Double.roundPercentOf(value: Int): Int {
	return value.div(this).times(100).roundToInt()
}


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
