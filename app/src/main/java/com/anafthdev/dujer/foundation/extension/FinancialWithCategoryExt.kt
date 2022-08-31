package com.anafthdev.dujer.foundation.extension

import com.anafthdev.dujer.data.model.Category
import com.anafthdev.dujer.data.model.Financial
import com.anafthdev.dujer.data.model.relation.FinancialWithCategory

fun Collection<FinancialWithCategory>.toFinancial(): List<Financial> {
	return map { financialWithCategoryAndWallet ->
		Financial(
			id = financialWithCategoryAndWallet.financialDb.id,
			name = financialWithCategoryAndWallet.financialDb.name,
			amount = financialWithCategoryAndWallet.financialDb.amount,
			type = financialWithCategoryAndWallet.financialDb.type,
			walletID = financialWithCategoryAndWallet.financialDb.walletID,
			category = financialWithCategoryAndWallet.categoryDb?.toCategoryWithEmptyFinancial() ?: if (financialWithCategoryAndWallet.financialDb.type.isIncome()) Category.otherIncome else Category.otherExpense,
			currency = financialWithCategoryAndWallet.financialDb.currency,
			dateCreated = financialWithCategoryAndWallet.financialDb.dateCreated
		)
	}
}

fun FinancialWithCategory.toFinancial(): Financial {
	return Financial(
		id = financialDb.id,
		name = financialDb.name,
		amount = financialDb.amount,
		type = financialDb.type,
		walletID = financialDb.walletID,
		category = categoryDb?.toCategoryWithEmptyFinancial() ?: if (financialDb.type.isIncome()) Category.otherIncome else Category.otherExpense,
		currency = financialDb.currency,
		dateCreated = financialDb.dateCreated
	)
}
