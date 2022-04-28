package com.anafthdev.dujer.uicomponent.charting.bar

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.listSaver
import androidx.compose.runtime.saveable.rememberSaveable
import com.anafthdev.dujer.uicomponent.charting.bar.model.BarData
import com.google.gson.Gson

class BarChartState constructor(
	initialSelectedBarDataState: BarData = BarData.default
) {
	
	private val selectedBarDataState = mutableStateOf(initialSelectedBarDataState)
	val observableSelectedBarDataState get() = selectedBarDataState.value
	
	fun update(selectedBarData: BarData) {
		selectedBarDataState.value = selectedBarData
	}
	
	companion object {
		val Saver: Saver<BarChartState, *> = listSaver(
			save = { listOf(it.observableSelectedBarDataState.toJsonString()) },
			restore = { listSave ->
				BarChartState(
					initialSelectedBarDataState = Gson().fromJson(listSave[0], BarData::class.java)
				)
			}
		)
	}
	
}

@Composable
fun rememberBarChartState(selectedBarData: BarData = BarData.default): BarChartState {
	return rememberSaveable(saver = BarChartState.Saver) {
		BarChartState(
			initialSelectedBarDataState = selectedBarData
		)
	}
}
