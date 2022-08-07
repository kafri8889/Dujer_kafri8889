package com.anafthdev.dujer.feature.app

import com.anafthdev.dujer.data.model.Category
import com.anafthdev.dujer.data.model.Financial
import com.anafthdev.dujer.data.model.Wallet

sealed class DujerEffect {
	object Nothing: DujerEffect()
	data class DeleteFinancial(val financial: Financial): DujerEffect()
	data class DeleteCategory(val category: Category): DujerEffect()
	data class DeleteWallet(val wallet: Wallet): DujerEffect()
}
