package com.anafthdev.dujer.data.model.relation

import androidx.room.Embedded
import androidx.room.Relation
import com.anafthdev.dujer.data.model.FinancialDb
import com.anafthdev.dujer.data.model.WalletDb

data class WalletWithFinancial(
	@Embedded val walletDb: WalletDb,
	
	@Relation(
		entity = FinancialDb::class,
		parentColumn = "wallet_id",
		entityColumn = "financial_walletID"
	)
	val financials: List<FinancialWithCategory>
)
