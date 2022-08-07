package com.anafthdev.dujer.foundation.extension

import com.anafthdev.dujer.data.model.Category
import com.anafthdev.dujer.data.model.CategoryDb
import com.anafthdev.dujer.data.model.Financial
import com.anafthdev.dujer.data.model.FinancialDb

/**
 * convert to financial with empty `financials` in [Category]
 */
fun Collection<FinancialDb>.toFinancial(categoryDb: CategoryDb): List<Financial> {
	return map { financialDb ->
		Financial(
			id = financialDb.id,
			name = financialDb.name,
			amount = financialDb.amount,
			type = financialDb.type,
			walletID = financialDb.walletID,
			category = categoryDb.toCategoryWithEmptyFinancial(),
			currency = financialDb.currency,
			dateCreated = financialDb.dateCreated
		)
	}
}
