package com.anafthdev.dujer.foundation.common.financial_sorter

import com.anafthdev.dujer.data.*
import com.anafthdev.dujer.data.model.Category
import com.anafthdev.dujer.data.model.Financial
import com.anafthdev.dujer.foundation.common.AppUtil
import com.anafthdev.dujer.foundation.common.AppUtil.dateFormatter
import com.anafthdev.dujer.foundation.common.AppUtil.monthFormatter
import com.anafthdev.dujer.foundation.common.AppUtil.monthYearFormatter
import com.anafthdev.dujer.foundation.common.AppUtil.yearFormatter
import com.anafthdev.dujer.foundation.extension.forEachMap
import timber.log.Timber
import java.util.*
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
				
				val sortedResult = when (sortType) {
					SortType.A_TO_Z -> result.sortedByDescending { it.timeInMillis }
					SortType.Z_TO_A -> result.sortedByDescending { it.timeInMillis }
					SortType.LATEST -> result.sortedByDescending { it.timeInMillis }
					SortType.LONGEST -> result.sortedBy { it.timeInMillis }
					SortType.LOWEST_AMOUNT -> result.sortedByDescending { it.timeInMillis }
					SortType.HIGHEST_AMOUNT -> result.sortedByDescending { it.timeInMillis }
					else -> result
				}
				
				FinancialGroupDate(sortedResult, financials)
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
				
				val sortedResult = when (sortType) {
					SortType.A_TO_Z -> result.sortedBy { it.category.name }
					SortType.Z_TO_A -> result.sortedByDescending { it.category.name }
					SortType.LATEST -> result.sortedBy { it.category.name }
					SortType.LONGEST -> result.sortedBy { it.category.name }
					SortType.LOWEST_AMOUNT -> result.sortedBy { it.category.name }
					SortType.HIGHEST_AMOUNT -> result.sortedBy { it.category.name }
					else -> result
				}
				
				FinancialGroupCategory(sortedResult, financials)
			}
//			GroupType.WEEK -> {
//				val result = arrayListOf<FinancialGroupWeekItem>()
//
//				// Start date, Stop date
//				var daysInWeekList = arrayListOf<Pair<Long, Long>>()
//				val firstDayOfMonthCalendar = Calendar.getInstance().apply {
//					this.timeInMillis = financials.minOf { it.dateCreated }
//					set(Calendar.DAY_OF_MONTH, 1)
//				}
//
//				val firstOfMonth = LocalDate.fromCalendarFields(firstDayOfMonthCalendar)
//				val firstOfNextMonth = firstOfMonth.plusMonths(1)
//				var firstDateInGrid = firstOfMonth.withDayOfWeek(DateTimeConstants.SUNDAY)
//
//				// If getting the next start of week instead of desired week's start, adjust backwards.
//				if ( firstDateInGrid.isAfter(firstOfMonth) ) {
//					firstDateInGrid = firstDateInGrid.minusWeeks(1)
//				}
//
//				var weekStart = firstDateInGrid
//				var weekStop: LocalDate
//				var weekNumber = 0
//
//				do {
//					weekNumber += 1
//					weekStop = weekStart.plusDays(6)
//					daysInWeekList.add(weekStart.toDate().time to weekStop.toDate().time)
//					weekStart = weekStop.plusDays(1)
//				} while (weekStop.isBefore(firstOfNextMonth))
//
//				result.apply {
//					for (week in 0..4) {
//						val startDate = daysInWeekList[week].first
//						val stopDate = daysInWeekList[week].second
//						val dateRange = startDate..stopDate
//						val value = financials
//							.map {
//								it to Calendar.getInstance().apply {
//									this.timeInMillis = it.dateCreated
//									set(Calendar.HOUR_OF_DAY, 23)
//									set(Calendar.MINUTE, 59)
//									set(Calendar.SECOND, 59)
//								}.timeInMillis
//							}
//							.filter {
//								it.second in dateRange
//							}
//
//						Timber.i("valyu: $value")
//						Timber.i("valyu formt: ${
//							financials.map { dateFormatter.format(it.dateCreated) }
//						} in ${dateFormatter.format(startDate)}..${dateFormatter.format(stopDate)}")
//						Timber.i("valyu ril: ${financials.map { it.dateCreated }} in $startDate..$stopDate")
//
//						add(
//							FinancialGroupWeekItem(
//								week = week,
//								from = startDate,
//								to = stopDate,
//								financials = value.map { it.first }
//							)
//						)
//					}
//				}
//
//				return FinancialGroupWeek(result, financials)
//			}
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