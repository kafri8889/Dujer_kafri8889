package com.anafthdev.dujer.uicomponent.charting.bar

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.listSaver
import androidx.compose.runtime.saveable.rememberSaveable
import com.anafthdev.dujer.uicomponent.charting.bar.data.BarDataSet
import com.anafthdev.dujer.uicomponent.charting.bar.model.BarData

class BarChartState constructor(
	initialSelectedBarDataGroup: Int = -1
) {
	
	private val selectedBarDataState = mutableStateOf(initialSelectedBarDataGroup)
	val observableSelectedBarDataGroup get() = selectedBarDataState.value
	
	fun update(selectedBarDataGroup: Int) {
		selectedBarDataState.value = selectedBarDataGroup
	}
	
	companion object {
		val Saver: Saver<BarChartState, *> = listSaver(
			save = { listOf(it.observableSelectedBarDataGroup) },
			restore = { listSave ->
				BarChartState(
					initialSelectedBarDataGroup = listSave[0]
				)
			}
		)
	}
	
}

@Composable
fun rememberBarChartState(selectedBarDataSet: BarDataSet = BarDataSet(listOf(BarData.default))): BarChartState {
	return rememberSaveable(saver = BarChartState.Saver) {
		BarChartState(
			initialSelectedBarDataGroup = selectedBarDataSet.id
		)
	}
}
