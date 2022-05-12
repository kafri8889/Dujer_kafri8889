package com.anafthdev.dujer.util

import android.content.Context
import android.widget.Toast
import com.anafthdev.dujer.foundation.extension.deviceLocale
import java.text.DateFormatSymbols

object AppUtil {
	
	const val ONE_YEAR_IN_MILLIS = 31557600000
	
	val shortMonths: Array<String> = DateFormatSymbols(deviceLocale).shortMonths
	
	val longMonths: Array<String> = DateFormatSymbols(deviceLocale).months
	
	fun Any?.toast(context: Context, length: Int = Toast.LENGTH_SHORT) = Toast.makeText(context, this.toString(), length).show()
	
}