package com.anafthdev.dujer.ui.category_transaction

sealed class CategoryTransactionAction {
	data class GetCategory(val id: Int): CategoryTransactionAction()
}
