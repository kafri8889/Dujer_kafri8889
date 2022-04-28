package com.anafthdev.dujer.uicomponent.charting.bar.model

import com.google.gson.Gson
import com.google.gson.annotations.SerializedName

data class BarData(
	@SerializedName("x") var x: Float,
	@SerializedName("y") var y: Float
) {
	
	fun toJsonString(): String {
		return Gson().toJson(this)
	}
	
	companion object {
		val default = BarData(-1f, -1f)
	}
}
