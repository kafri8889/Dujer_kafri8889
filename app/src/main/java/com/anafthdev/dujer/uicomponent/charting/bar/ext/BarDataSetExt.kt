package com.anafthdev.dujer.uicomponent.charting.bar.ext

import com.anafthdev.dujer.foundation.extension.getBy
import com.anafthdev.dujer.foundation.extension.groupByIndex
import com.anafthdev.dujer.uicomponent.charting.bar.data.BarDataSet
import com.anafthdev.dujer.uicomponent.charting.bar.model.BarData
import timber.log.Timber

fun Collection<BarDataSet>.groupBarData(): List<Pair<Int, List<BarData>>> {
	val result = arrayListOf<Pair<Int, List<BarData>>>()
	
	val barDataList = this.getBy { it.barData }
	val innerListSize = barDataList[0].size
	
	Timber.i("barDataList: $barDataList")
	Timber.i("innerListSize: $innerListSize")
	
	require(barDataList.all { it.size == innerListSize }) {
		"the size of each element must be the same"
	}
	
	for (i in 0 until innerListSize) {
		result.add(i to barDataList.groupByIndex()[i])
	}
	
	Timber.i("result: $result")
	
	return result
}
