package com.anafthdev.dujer.ui.app

import com.anafthdev.dujer.data.db.model.Category
import com.anafthdev.dujer.data.db.model.Financial
import com.anafthdev.dujer.data.db.model.Wallet

sealed class DujerEffect {
	object Nothing: DujerEffect()
	data class DeleteFinancial(val financial: Financial): DujerEffect()
	data class DeleteCategory(val category: Category): DujerEffect()
	data class DeleteWallet(val wallet: Wallet): DujerEffect()
}
