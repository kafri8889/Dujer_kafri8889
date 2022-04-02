package com.anafthdev.dujer.model

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb

/**
 * tint in ARGB
 */
data class CategoryTint(
	val iconTint: Int,
	val backgroundTint: Int
) {
	companion object {
		
		val tint_1 = CategoryTint(
			iconTint = Color(0xFF81B2CA).toArgb(),
			backgroundTint = Color(0xFFEDF4F7).toArgb()
		)
		
		val tint_2 = CategoryTint(
			iconTint = Color(0xFF42887C).toArgb(),
			backgroundTint = Color(0xFFE4EEEC).toArgb()
		)
		
		val tint_3 = CategoryTint(
			iconTint = Color(0xFF836F81).toArgb(),
			backgroundTint = Color(0xFFEDEBED).toArgb()
		)
		
		val values = listOf(
			tint_1,
			tint_2,
			tint_3,
		)
		
		fun getRandomTint(): CategoryTint = values.random()
	}
}
