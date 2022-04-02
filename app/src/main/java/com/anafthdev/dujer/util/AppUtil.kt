package com.anafthdev.dujer.util

import android.content.Context
import android.content.res.Resources
import android.os.Build
import android.widget.Toast
import java.text.DateFormatSymbols
import java.util.*

object AppUtil {
	
	val shortMonths: Array<String> = DateFormatSymbols(
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) Resources.getSystem().configuration.locales[0]
		else Resources.getSystem().configuration.locale
	).shortMonths
	
	val longMonths: Array<String> = DateFormatSymbols(
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) Resources.getSystem().configuration.locales[0]
		else Resources.getSystem().configuration.locale
	).months
	
	fun Any?.toast(context: Context, length: Int = Toast.LENGTH_SHORT) = Toast.makeText(context, this.toString(), length).show()
	
}