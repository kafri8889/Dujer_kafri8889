package com.anafthdev.dujer.uicomponent.charting.bar.data

import androidx.compose.ui.graphics.Color
import com.anafthdev.dujer.foundation.extension.get
import com.anafthdev.dujer.uicomponent.charting.bar.model.BarData
import kotlin.random.Random

class BarDataSet(
	val barData: List<BarData>,
	val id: Int = Random.nextInt(),
	private var colors: Collection<Color> = listOf(Color.Black)
) {
	
	val barColors: ArrayList<Pair<BarData, Color>> = arrayListOf()
	
	init {
		require(colors.isNotEmpty()) {
			"empty colors"
		}
// 		require(barData.size == colors.size) {
//			"different size -> barData: ${barData.size}, colors: ${colors.size}"
//		}
		
		barData.forEachIndexed { i, data ->
			try {
				barColors.add(data to colors.get(i))
			} catch (e: Exception) {
				barColors.add(data to colors.get(0))
			}
		}
	}

}
