package com.anafthdev.dujer.foundation.uicomponent.charting.bar.ext

import com.anafthdev.dujer.foundation.extension.transpose
import com.anafthdev.dujer.foundation.uicomponent.charting.bar.data.BarDataSet
import com.anafthdev.dujer.foundation.uicomponent.charting.bar.model.BarData
import timber.log.Timber

fun Collection<BarDataSet>.groupBarData(): List<Pair<Int, List<BarData>>> {
	val result = arrayListOf<Pair<Int, List<BarData>>>()
	
	val barDataList = map { it.barData }
	val innerListSize = barDataList[0].size
	
	Timber.i("barDataList: $barDataList")
	Timber.i("innerListSize: $innerListSize")
	
	require(barDataList.all { it.size == innerListSize }) {
		"the size of each element must be the same"
	}
	
	for (i in 0 until innerListSize) {
		result.add(i to barDataList.transpose()[i])
	}
	
	Timber.i("result: $result")
	
	return result
}
