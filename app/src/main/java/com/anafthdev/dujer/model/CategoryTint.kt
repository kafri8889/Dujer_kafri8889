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
		
		val tint_4 = CategoryTint(
			iconTint = Color(0xFF266275).toArgb(),
			backgroundTint = Color(0xFFD4E2E6).toArgb()
		)
		
		val tint_5 = CategoryTint(
			iconTint = Color(0xFF58A7A7).toArgb(),
			backgroundTint = Color(0xFFE2F1F1).toArgb()
		)
		
		val tint_6 = CategoryTint(
			iconTint = Color(0xFF76B17E).toArgb(),
			backgroundTint = Color(0xFFF1FFF3).toArgb()
		)
		
		val tint_7 = CategoryTint(
			iconTint = Color(0xFFF02D55).toArgb(),
			backgroundTint = Color(0xFFF7E2E7).toArgb()
		)
		
		val tint_8 = CategoryTint(
			iconTint = Color(0xFF5F871C).toArgb(),
			backgroundTint = Color(0xFFE1EAD2).toArgb()
		)
		
		val tint_9 = CategoryTint(
			iconTint = Color(0xFFA15A30).toArgb(),
			backgroundTint = Color(0xFFF0E0D7).toArgb()
		)
		
		val tint_10 = CategoryTint(
			iconTint = Color(0xFFDDB300).toArgb(),
			backgroundTint = Color(0xFFFDF4CC).toArgb()
		)
		
		val tint_11 = CategoryTint(
			iconTint = Color(0xFFAE214A).toArgb(),
			backgroundTint = Color(0xFFF3D3DC).toArgb()
		)
		
		val tint_12 = CategoryTint(
			iconTint = Color(0xFFE65C00).toArgb(),
			backgroundTint = Color(0xFFFFE0CC).toArgb()
		)
		
		val tint_13 = CategoryTint(
			iconTint = Color(0xFF5F9679).toArgb(),
			backgroundTint = Color(0xFFE1EDE7).toArgb()
		)
		
		val tint_14 = CategoryTint(
			iconTint = Color(0xFF3A5073).toArgb(),
			backgroundTint = Color(0xFFD9DEE6).toArgb()
		)
		
		val tint_15 = CategoryTint(
			iconTint = Color(0xFFC3A692).toArgb(),
			backgroundTint = Color(0xFFF7F1EC).toArgb()
		)
		
		val tint_16 = CategoryTint(
			iconTint = Color(0xFFA87373).toArgb(),
			backgroundTint = Color(0xFFF2E7E7).toArgb()
		)
		
		val tint_17 = CategoryTint(
			iconTint = Color(0xFF7CB150).toArgb(),
			backgroundTint = Color(0xFFE8FCD9).toArgb()
		)
		
		val tint_18 = CategoryTint(
			iconTint = Color(0xFFC73EA8).toArgb(),
			backgroundTint = Color(0xFFF8DAF1).toArgb()
		)
		
		val tint_19 = CategoryTint(
			iconTint = Color(0xFF95971E).toArgb(),
			backgroundTint = Color(0xFFEDEED3).toArgb()
		)
		
		val tint_20 = CategoryTint(
			iconTint = Color(0xFF1B7F6A).toArgb(),
			backgroundTint = Color(0xFFD2E8E4).toArgb()
		)
		
		val values = listOf(
			tint_1,
			tint_2,
			tint_3,
			tint_4,
			tint_5,
			tint_6,
			tint_7,
			tint_8,
			tint_9,
			tint_10,
			tint_11,
			tint_12,
			tint_13,
			tint_14,
			tint_15,
			tint_16,
			tint_17,
			tint_18,
			tint_19,
			tint_20,
		)
		
		fun getRandomTint(): CategoryTint = values.random()
	}
}
