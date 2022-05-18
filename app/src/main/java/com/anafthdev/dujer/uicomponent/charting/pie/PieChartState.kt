package com.anafthdev.dujer.uicomponent.charting.pie

import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.rememberSaveable

class PieChartState constructor(
	initialSelectedPieIndex: Int? = null,
	private val onPieSelected: (index: Int?) -> Unit = {}
) {
	
	private val selectedPieIndex: MutableState<Int?> = mutableStateOf(initialSelectedPieIndex)
	val observableSelectedPieIndex get() = selectedPieIndex.value
	
	init {
		onPieSelected(initialSelectedPieIndex)
	}
	
	fun update(selectedBarDataGroup: Int) {
		selectedPieIndex.value = selectedBarDataGroup
		onPieSelected(selectedBarDataGroup)
	}
	
	companion object {
		fun Saver(
			onPieSelected: (id: Int?) -> Unit
		): Saver<PieChartState, *> = Saver(
			save = { it.observableSelectedPieIndex },
			restore = {
				PieChartState(
					initialSelectedPieIndex = it,
					onPieSelected = onPieSelected
				)
			}
		)
	}
	
}

@Composable
fun rememberPieChartState(
	lazyListState: LazyListState = rememberLazyListState(),
	initialSelectedPieIndex: Int? = null,
	onPieSelected: (id: Int?) -> Unit = {}
): PieChartState {
	return rememberSaveable(
		saver = PieChartState.Saver(
			onPieSelected = onPieSelected,
		)
	) {
		PieChartState(
			initialSelectedPieIndex = initialSelectedPieIndex,
			onPieSelected = onPieSelected
		)
	}
}