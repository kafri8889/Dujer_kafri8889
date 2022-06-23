package com.anafthdev.dujer.foundation.common.financial_sorter

import android.icu.util.Calendar
import com.anafthdev.dujer.data.*
import com.anafthdev.dujer.data.db.model.Category
import com.anafthdev.dujer.data.db.model.Financial
import com.anafthdev.dujer.foundation.extension.forEachMap
import com.anafthdev.dujer.util.AppUtil
import com.anafthdev.dujer.util.AppUtil.dateFormatter
import com.anafthdev.dujer.util.AppUtil.monthFormatter
import com.anafthdev.dujer.util.AppUtil.monthYearFormatter
import com.anafthdev.dujer.util.AppUtil.yearFormatter
import timber.log.Timber
import javax.inject.Inject

class FinancialSorter @Inject constructor() {
	
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
		
		val data = groupAndSort(sortType, groupType, filteredFinancials)
		
		return FinancialGroupData(
			type = groupType,
			data = data
		)
	}
	
	// TODO: sort juga pas udah di grup
	private fun groupAndSort(
		sortType: SortType,
		groupType: GroupType,
		financials: List<Financial>
	): FinancialGroup {
		return when (groupType) {
			GroupType.DEFAULT -> FinancialGroupDefault(sort(sortType, financials))
			GroupType.DAY, GroupType.MONTH, GroupType.YEAR -> {
				val formatter = when (groupType) {
					GroupType.DAY -> dateFormatter
					GroupType.MONTH -> monthYearFormatter
					GroupType.YEAR -> yearFormatter
					else -> throw RuntimeException("no formatter set")
				}
				
				val result = arrayListOf<FinancialGroupDateItem>()
				val groupedFinancial = financials.groupBy {
					formatter.format(it.dateCreated)
				}
				
				groupedFinancial.forEachMap { key, value ->
					result.add(
						FinancialGroupDateItem(
							timeInMillis = formatter.parse(key)?.time ?: 0,
							financials = sort(sortType, value)
						)
					)
				}
				
				FinancialGroupDate(result, financials)
			}
			GroupType.CATEGORY -> {
				val result = arrayListOf<FinancialGroupCategoryItem>()
				val availableCategory = financials.map { it.category }
				val groupedFinancial = financials.groupBy {
					it.category.id
				}
				
				groupedFinancial.forEachMap { key, value ->
					result.add(
						FinancialGroupCategoryItem(
							category = availableCategory.find { it.id == key } ?: Category.default,
							financials = sort(sortType, value)
						)
					)
				}
				
				FinancialGroupCategory(result, financials)
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