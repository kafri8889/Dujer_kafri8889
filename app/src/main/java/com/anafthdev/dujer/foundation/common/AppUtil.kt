package com.anafthdev.dujer.foundation.common

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import com.anafthdev.dujer.foundation.extension.deviceLocale
import java.text.DateFormatSymbols
import java.text.SimpleDateFormat
import java.util.*

object AppUtil {
	
	const val ONE_YEAR_IN_MILLIS = 31_557_600_000
	const val ONE_MONTH_IN_MILLIS = 2_629_800_000
	const val ONE_DAY_IN_MILLIS = 86_400_000
	const val ONE_HOUR_IN_MILLIS = 3_600_000
	
	val publicCalendar: Calendar = Calendar.getInstance()
	
	val minFilterYear = publicCalendar.apply {
		set(Calendar.YEAR, 2000)
	}.timeInMillis
	
	val filterDateDefault = minFilterYear to System.currentTimeMillis()
	
	val shortMonths: Array<String> = DateFormatSymbols.getInstance(deviceLocale).shortMonths
	
	val longMonths: Array<String> = DateFormatSymbols.getInstance(deviceLocale).months
	
	val timeFormatter = SimpleDateFormat("dd MMM yyyy HH-mm-ss", deviceLocale)
	val dateFormatter = SimpleDateFormat("dd MMM yyyy", deviceLocale)
	val monthYearFormatter = SimpleDateFormat("MMM yyyy", deviceLocale)
	val monthFormatter = SimpleDateFormat("MMM", deviceLocale)
	val yearFormatter = SimpleDateFormat("yyyy", deviceLocale)
	
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