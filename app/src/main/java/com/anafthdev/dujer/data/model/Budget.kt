package com.anafthdev.dujer.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Budget(
	val id: Int,
	val max: Double,
	val category: Category,
	val remaining: Double = 0.0,
	val isMaxReached: Boolean = false,
): Parcelable {
	
	companion object {
		val defalut = Budget(
			id = -1,
			max = 0.0,
			category = Category.default,
			remaining = 0.0,
			isMaxReached = false
		)
	}
	
}
