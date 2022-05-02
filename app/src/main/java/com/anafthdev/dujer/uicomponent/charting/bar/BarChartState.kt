package com.anafthdev.dujer.uicomponent.charting.bar

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.rememberSaveable

class BarChartState constructor(
	initialSelectedBarDataGroup: Int = -1,
	private val onBarDataGroupChange: (id: Int) -> Unit = {}
) {
	
	private val selectedBarDataState = mutableStateOf(initialSelectedBarDataGroup)
	val observableSelectedBarDataGroup get() = selectedBarDataState.value
	
	init {
		onBarDataGroupChange(initialSelectedBarDataGroup)
	}
	
	fun update(selectedBarDataGroup: Int) {
		selectedBarDataState.value = selectedBarDataGroup
		onBarDataGroupChange(selectedBarDataGroup)
	}
	
	companion object {
		fun Saver(onBarDataGroupChange: (id: Int) -> Unit): Saver<BarChartState, *> = Saver(
			save = { it.observableSelectedBarDataGroup },
			restore = {
				BarChartState(
					initialSelectedBarDataGroup = it,
					onBarDataGroupChange = onBarDataGroupChange
				)
			}
		)
	}
	
}

@Composable
fun rememberBarChartState(
	initialSelectedBarDataGroup: Int = -1,
	onBarDataGroupChange: (id: Int) -> Unit = {}
): BarChartState {
	return rememberSaveable(
		saver = BarChartState.Saver(
			onBarDataGroupChange = onBarDataGroupChange
		)
	) {
		BarChartState(
			initialSelectedBarDataGroup = initialSelectedBarDataGroup,
			onBarDataGroupChange = onBarDataGroupChange
		)
	}
}
