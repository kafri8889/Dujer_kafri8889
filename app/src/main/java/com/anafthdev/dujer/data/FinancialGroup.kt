package com.anafthdev.dujer.data

import com.anafthdev.dujer.data.db.model.Category
import com.anafthdev.dujer.data.db.model.Financial

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
