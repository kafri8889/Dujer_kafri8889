package com.anafthdev.dujer.foundation.common

import androidx.compose.ui.text.input.TextFieldValue
import com.anafthdev.dujer.foundation.extension.addStringBefore
import com.anafthdev.dujer.foundation.extension.deviceLocale
import com.anafthdev.dujer.foundation.extension.replace
import com.anafthdev.dujer.foundation.extension.takeDigitString
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.*

object TextFieldDateFormatter {
	
	private const val ddMMyyyy = "ddMMyyyy"
	
	/**
	 * format "ddMMyyyy" to "dd-MMM-yyyy"
	 * @return formatted string, ex: "11-01-2007" or "11-0M-YYYY"
	 */
	fun format(
		fieldValue: TextFieldValue,
		minYear: Int = 2000,
		maxYear: Int = 3000
	): String {
		val builder = StringBuilder()
		
		val s = fieldValue.text.replace(
			oldValue = listOf(" ", ".", ",", "-", "d", "M", "y"),
			newValue = ""
		)
		
		if (s.length != 8) {
			for (i in 0 until 8) {
				builder.append(
					try {
						s[i]
					} catch (e: Exception) {
						ddMMyyyy[i]
					}
				)
			}
		} else builder.append(s)
		
		repeat(3) {
			when (it) {
				0 -> {
					val day = try {
						"${builder[0]}${builder[1]}".toInt()
					} catch (e: Exception) {
						null
					}
					
					if (day != null) {
						val dayMax = day
							.coerceIn(
								minimumValue = 1,
								maximumValue = 31,
							)
							.toString()
						
						builder.replace(
							0,
							2,
							if (dayMax.length == 1) "0$dayMax" else dayMax
						)
					}
				}
				1 -> {
					val month = try {
						"${builder[2]}${builder[3]}".toInt()
					} catch (e: Exception) {
						null
					}
					
					if (month != null) {
						val monthMax = month
							.coerceIn(
								minimumValue = 1,
								maximumValue = 12,
							)
							.toString()
						
						builder.replace(
							2,
							4,
							if (monthMax.length == 1) "0$monthMax" else monthMax
						)
					}
				}
				2 -> {
					val year = try {
						"${builder[4]}${builder[5]}${builder[6]}${builder[7]}".toInt()
					} catch (e: Exception) {
						null
					}
					
					if (year != null) {
						val yearMaxMin = year.coerceIn(
							minimumValue = minYear,
							maximumValue = maxYear
						).toString()
						
						builder.replace(4, 6, yearMaxMin.substring(0, 2))
						builder.replace(6, 8, yearMaxMin.substring(2, 4))
					}
				}
			}
		}
		
		return builder.toString()
			.addStringBefore("-", 2)  // dd-MMyyyy
			.addStringBefore("-", 5)	// dd-MM-yyyy
	}
	
	/**
	 * format time in milli second to formatted date
	 * @return formatted string, ex: "11-01-2007"
	 */
	fun format(timeInMillis: Long): String {
		return SimpleDateFormat("dd-MM-yyyy", deviceLocale).format(timeInMillis)
	}
	
	/**
	 * check if formattedDate is valid
	 *
	 * "01-01-2000" -> valid
	 * "01-0M-YYYY" -> not valid
	 * @param formattedDate "01-01-2000"
	 */
	fun isValid(formattedDate: String): Boolean {
		return formattedDate.takeDigitString().length == 8
	}
	
	/**
	 * convert formatted string date to time in millis
	 * @param formattedDate "01-01-2000"
	 * @return time in millis
	 */
	fun parse(formattedDate: String): Long {
		val date = "${formattedDate[6]}${formattedDate[7]}${formattedDate[8]}${formattedDate[9]}-"
			.plus("${formattedDate[3]}${formattedDate[4]}-")
			.plus("${formattedDate[0]}${formattedDate[1]}")
			.plus("T00:00")
		
		return LocalDateTime.parse(date)
			.atZone(ZoneId.of(TimeZone.getDefault().id, ZoneId.SHORT_IDS))
			.toInstant()
			.toEpochMilli()
	}
	
}