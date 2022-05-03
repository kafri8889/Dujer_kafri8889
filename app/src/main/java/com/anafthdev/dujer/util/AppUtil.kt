package com.anafthdev.dujer.util

import android.content.Context
import android.content.res.Resources
import android.widget.Toast
import java.text.DateFormatSymbols

object AppUtil {
	
	const val ONE_YEAR_IN_MILLIS = 31557600000
	
	val deviceLocale = Resources.getSystem().configuration.locales[0]
	
	val shortMonths: Array<String> = DateFormatSymbols(deviceLocale).shortMonths
	
	val longMonths: Array<String> = DateFormatSymbols(deviceLocale).months
	
	fun Any?.toast(context: Context, length: Int = Toast.LENGTH_SHORT) = Toast.makeText(context, this.toString(), length).show()
	
}