package com.anafthdev.dujer.foundation.common.financial_sorter

import android.icu.util.Calendar
import com.anafthdev.dujer.data.*
import com.anafthdev.dujer.data.db.model.Financial
import com.anafthdev.dujer.foundation.extension.deviceLocale
import com.anafthdev.dujer.foundation.extension.forEachMap
import com.anafthdev.dujer.util.AppUtil
import com.anafthdev.dujer.util.AppUtil.dateFormatter
import timber.log.Timber
import java.text.SimpleDateFormat
import javax.inject.Inject

class FinancialSorter @Inject constructor() {
	
	private val monthFormatter = SimpleDateFormat("MMM", deviceLocale)
	private val months = AppUtil.shortMonths
	
	private val calendar = Calendar.getInstance()
	
	fun beginSort(
		sortType: SortType,
		groupType: GroupType,
		filterDate: Pair<Long, Long>,
		selectedMonth: List<Int>,
		financials: List<Financial>
	): FinancialGroupData {
		val optimizedStartDate = calendar.apply {
			timeInMillis = filterDate.first
			set(Calendar.HOUR_OF_DAY, 23)
			set(Calendar.MINUTE, 59)
		}.timeInMillis
		
		val optimizedEndDate = calendar.apply {
			timeInMillis = filterDate.second
			set(Calendar.HOUR_OF_DAY, 23)
			set(Calendar.MINUTE, 59)
		}.timeInMillis
		
		val dateRange = optimizedStartDate..optimizedEndDate
		val filteredFinancials = financials
			.asSequence()
			.filter { it.dateCreated in dateRange }
			.filter {
				getMonthIndex(
					monthFormatter.format(it.dateCreated)
				) in selectedMonth
			}.toList()
		
		val data = group(sortType, groupType, filteredFinancials)
		
		return FinancialGroupData(
			type = groupType,
			data = data
		)
	}
	
	private fun group(
		sortType: SortType,
		groupType: GroupType,
		financials: List<Financial>
	): FinancialGroup {
		return when (groupType) {
			GroupType.DEFAULT -> FinancialGroupDefault(sort(sortType, financials))
			GroupType.DAY -> {
				val result = arrayListOf<FinancialGroupDayItem>()
				val groupedFinancial = financials.groupBy {
					dateFormatter.format(it.dateCreated)
				}
				
				groupedFinancial.forEachMap { day, value ->
					result.add(
						FinancialGroupDayItem(
							timeInMillis = dateFormatter.parse(day)?.time ?: 0,
							financials = sort(sortType, value)
						)
					)
				}
				
				FinancialGroupDay(result, financials)
			}
			// TODO: grup lain
			else -> FinancialGroupDefault(financials)
		}
	}
	
	private fun sort(
		sortType: SortType,
		financials: List<Financial>
	): List<Financial> {
		return when (sortType) {
			SortType.A_TO_Z -> financials.sortedBy { it.name }
			SortType.Z_TO_A -> financials.sortedByDescending { it.name }
			SortType.LATEST -> financials.sortedByDescending { it.dateCreated }
			SortType.LONGEST -> financials.sortedBy { it.dateCreated }
			SortType.LOWEST_AMOUNT -> financials.sortedBy { it.amount }
			SortType.HIGHEST_AMOUNT -> financials.sortedByDescending { it.amount }
			else -> financials
		}
	}
	
	private fun getMonthIndex(longMonthString: String): Int {
		Timber.i("mnths: $longMonthString")
		return when (longMonthString.uppercase()) {
			months[0].uppercase() -> 0
			months[1].uppercase() -> 1
			months[2].uppercase() -> 2
			months[3].uppercase() -> 3
			months[4].uppercase() -> 4
			months[5].uppercase() -> 5
			months[6].uppercase() -> 6
			months[7].uppercase() -> 7
			months[8].uppercase() -> 8
			months[9].uppercase() -> 9
			months[10].uppercase() -> 10
			months[11].uppercase() -> 11
			else -> -1
		}
	}
}