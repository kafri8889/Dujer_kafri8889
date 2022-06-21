package com.anafthdev.dujer.util

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import com.anafthdev.dujer.foundation.extension.deviceLocale
import java.text.DateFormatSymbols
import java.util.*

object AppUtil {
	
	const val ONE_YEAR_IN_MILLIS = 31557600000
	const val ONE_MONTH_IN_MILLIS = 2629800000
	
	val publicCalendar: Calendar = Calendar.getInstance()
	
	val minFilterYear = publicCalendar.apply {
		set(Calendar.YEAR, 2000)
	}.timeInMillis
	
	val filterDateDefault = minFilterYear to System.currentTimeMillis()
	
	val shortMonths: Array<String> = DateFormatSymbols.getInstance(deviceLocale).shortMonths
	
	val longMonths: Array<String> = DateFormatSymbols.getInstance(deviceLocale).months
	
	fun Any?.toast(context: Context, length: Int = Toast.LENGTH_SHORT) {
		Handler(Looper.getMainLooper()).post {
			Toast.makeText(context, this.toString(), length).show()
		}
	}
	
	@Composable
	fun Any?.toast(length: Int = Toast.LENGTH_SHORT) {
		Toast.makeText(LocalContext.current, this.toString(), length).show()
	}
	
}