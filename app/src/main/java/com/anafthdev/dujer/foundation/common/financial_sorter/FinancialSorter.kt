package com.anafthdev.dujer.foundation.common.financial_sorter

import com.anafthdev.dujer.data.SortType
import com.anafthdev.dujer.data.db.model.Financial
import com.anafthdev.dujer.foundation.extension.deviceLocale
import com.anafthdev.dujer.util.AppUtil
import timber.log.Timber
import java.text.SimpleDateFormat
import javax.inject.Inject

class FinancialSorter @Inject constructor() {
	
	private val monthFormatter = SimpleDateFormat("MMM", deviceLocale)
	private val months = AppUtil.shortMonths
	
	fun beginSort(
		sortType: SortType,
		filterDate: Pair<Long, Long>,
		selectedMonth: List<Int>,
		financials: List<Financial>
	): List<Financial> {
		val dateRange = filterDate.first..filterDate.second
		val filteredFinancials = financials
			.asSequence()
			.filter { it.dateCreated in dateRange }
			.filter {
				getMonthIndex(
					monthFormatter.format(it.dateCreated)
				) in selectedMonth
			}.toList()
		
		return when (sortType) {
			SortType.A_TO_Z -> filteredFinancials.sortedBy { it.name }
			SortType.Z_TO_A -> filteredFinancials.sortedByDescending { it.name }
			SortType.LATEST -> filteredFinancials.sortedByDescending { it.dateCreated }
			SortType.LONGEST -> filteredFinancials.sortedBy { it.dateCreated }
			SortType.LOWEST_AMOUNT -> filteredFinancials.sortedBy { it.amount }
			SortType.HIGHEST_AMOUNT -> filteredFinancials.sortedByDescending { it.amount }
			else -> filteredFinancials
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