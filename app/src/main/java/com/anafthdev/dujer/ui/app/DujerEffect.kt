package com.anafthdev.dujer.ui.app

import com.anafthdev.dujer.data.db.model.Category
import com.anafthdev.dujer.data.db.model.Financial

sealed class DujerEffect {
	object Nothing: DujerEffect()
	data class DeleteFinancial(val financial: Financial): DujerEffect()
	data class DeleteCategory(val category: Category): DujerEffect()
}
