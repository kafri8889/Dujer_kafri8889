package com.anafthdev.dujer.uicomponent.charting.bar

import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.rememberSaveable

class BarChartState constructor(
	initialSelectedBarDataGroup: Int = -1,
	val lazyListState: LazyListState,
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
		fun Saver(
			lazyListState: LazyListState,
			onBarDataGroupChange: (id: Int) -> Unit
		): Saver<BarChartState, *> = Saver(
			save = { it.observableSelectedBarDataGroup },
			restore = {
				BarChartState(
					lazyListState = lazyListState,
					initialSelectedBarDataGroup = it,
					onBarDataGroupChange = onBarDataGroupChange
				)
			}
		)
	}
	
}

@Composable
fun rememberBarChartState(
	lazyListState: LazyListState = rememberLazyListState(),
	initialSelectedBarDataGroup: Int = -1,
	onBarDataGroupChange: (id: Int) -> Unit = {}
): BarChartState {
	return rememberSaveable(
		saver = BarChartState.Saver(
			onBarDataGroupChange = onBarDataGroupChange,
			lazyListState = lazyListState
		)
	) {
		BarChartState(
			lazyListState = lazyListState,
			initialSelectedBarDataGroup = initialSelectedBarDataGroup,
			onBarDataGroupChange = onBarDataGroupChange
		)
	}
}
