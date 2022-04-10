package com.anafthdev.dujer.foundation.extension

import androidx.compose.material.*

@OptIn(ExperimentalMaterialApi::class)
val ModalBottomSheetState.currentFraction: Float
	get() {
		val fraction = progress.fraction
		val targetValue = targetValue
		val currentValue = currentValue
		
		return when {
			currentValue == ModalBottomSheetValue.Hidden && targetValue == ModalBottomSheetValue.Hidden -> 0f
			currentValue == ModalBottomSheetValue.Expanded && targetValue == ModalBottomSheetValue.Expanded -> 1f
			currentValue == ModalBottomSheetValue.Hidden && targetValue == ModalBottomSheetValue.Expanded -> fraction
			else -> 1f - fraction
		}
	}
