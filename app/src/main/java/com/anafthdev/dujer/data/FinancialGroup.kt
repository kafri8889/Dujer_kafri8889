package com.anafthdev.dujer.data

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


data class FinancialGroupDay(
	val items: List<FinancialGroupDayItem>,
	override val rawFinancials: List<Financial>
): FinancialGroup

data class FinancialGroupDayItem(
	val timeInMillis: Long,
	val financials: List<Financial>
)
