package com.anafthdev.dujer.feature.app

import com.anafthdev.dujer.data.model.Category
import com.anafthdev.dujer.data.model.Financial
import com.anafthdev.dujer.data.model.Wallet
import com.anafthdev.dujer.foundation.common.BaseEffect

sealed interface DujerEffect {
	object Nothing: DujerEffect, BaseEffect
	data class DeleteFinancial(val financial: Financial): DujerEffect, BaseEffect
	data class DeleteCategory(val category: Category): DujerEffect, BaseEffect
	data class DeleteWallet(val wallet: Wallet): DujerEffect, BaseEffect
}
