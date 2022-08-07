package com.anafthdev.dujer.foundation.extension

import com.anafthdev.dujer.data.model.Financial
import com.anafthdev.dujer.data.model.FinancialDb

fun Financial.toFinancialDb(): FinancialDb {
	return FinancialDb(
		id = id,
		name = name,
		amount = amount,
		type = type,
		walletID = walletID,
		categoryID = category.id,
		currency = currency,
		dateCreated = dateCreated
	)
}

fun Collection<Financial>.toFinancialDb(): List<FinancialDb> {
	return map { financial ->
		FinancialDb(
			id = financial.id,
			name = financial.name,
			amount = financial.amount,
			type = financial.type,
			walletID = financial.walletID,
			categoryID = financial.category.id,
			currency = financial.currency,
			dateCreated = financial.dateCreated
		)
	}
}

fun Array<out Financial>.toFinancialDb(): Array<out FinancialDb> {
	return map { financial ->
		FinancialDb(
			id = financial.id,
			name = financial.name,
			amount = financial.amount,
			type = financial.type,
			walletID = financial.walletID,
			categoryID = financial.category.id,
			currency = financial.currency,
			dateCreated = financial.dateCreated
		)
	}.toTypedArray()
}
