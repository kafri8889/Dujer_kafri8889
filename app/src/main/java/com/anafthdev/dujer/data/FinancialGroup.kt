package com.anafthdev.dujer.data

import com.anafthdev.dujer.data.model.Category
import com.anafthdev.dujer.data.model.Financial

sealed interface FinancialGroup {
	
	/**
	 * un grouped financial
	 */
	val rawFinancials: List<Financial>
}

data class FinancialGroupData(
	val type: GroupType,
	val data: FinancialGroup
) {
	companion object {
		val default = FinancialGroupData(
			type = GroupType.DEFAULT,
			data = FinancialGroupDefault(emptyList())
		)
	}
}


data class FinancialGroupDefault(
	override val rawFinancials: List<Financial>
): FinancialGroup


data class FinancialGroupDate(
	val items: List<FinancialGroupDateItem>,
	override val rawFinancials: List<Financial>
): FinancialGroup

data class FinancialGroupDateItem(
	val timeInMillis: Long,
	val financials: List<Financial>
)


data class FinancialGroupCategory(
	val items: List<FinancialGroupCategoryItem>,
	override val rawFinancials: List<Financial>
): FinancialGroup

data class FinancialGroupCategoryItem(
	val category: Category,
	val financials: List<Financial>
)

data class FinancialGroupWeek(
	val items: List<FinancialGroupWeekItem>,
	override val rawFinancials: List<Financial>
): FinancialGroup

/**
 * @param week week in month
 * @param from time in millis
 * @param to time in millis
 * @param financials the financials
 */
data class FinancialGroupWeekItem(
	val week: Int,
	val from: Long,
	val to: Long,
	val financials: List<Financial>
)
