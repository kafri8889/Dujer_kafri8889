package com.anafthdev.dujer.foundation.extension

import com.anafthdev.dujer.data.FinancialType
import com.anafthdev.dujer.data.model.Wallet
import com.anafthdev.dujer.data.model.relation.WalletWithFinancial

fun Collection<WalletWithFinancial>.toWallet(): List<Wallet> {
	return map { walletWithFinancial ->
		val financials = walletWithFinancial.financials.toFinancial()
		
		val incomeTransaction = financials.filter {
			it.type == FinancialType.INCOME
		}.sumOf { it.amount }
		
		val expenseTransaction = financials.filter {
			it.type == FinancialType.EXPENSE
		}.sumOf { it.amount }
		
		val balance = walletWithFinancial.walletDb.initialBalance + incomeTransaction - expenseTransaction
		
		Wallet(
			id = walletWithFinancial.walletDb.id,
			name = walletWithFinancial.walletDb.name,
			initialBalance = walletWithFinancial.walletDb.initialBalance,
			balance = balance,
			iconID = walletWithFinancial.walletDb.iconID,
			tint = walletWithFinancial.walletDb.tint,
			defaultWallet = walletWithFinancial.walletDb.defaultWallet,
			financials = financials
		)
	}
}

fun WalletWithFinancial.toWallet(): Wallet {
	val financials = financials.toFinancial()
	
	val incomeTransaction = financials.filter {
		it.type == FinancialType.INCOME
	}.sumOf { it.amount }
	
	val expenseTransaction = financials.filter {
		it.type == FinancialType.EXPENSE
	}.sumOf { it.amount }
	
	val balance = walletDb.initialBalance + incomeTransaction - expenseTransaction
	
	
	return Wallet(
		id = walletDb.id,
		name = walletDb.name,
		initialBalance = walletDb.initialBalance,
		balance = balance,
		iconID = walletDb.iconID,
		tint = walletDb.tint,
		defaultWallet = walletDb.defaultWallet,
		financials = financials
	)
}
