package com.anafthdev.dujer.feature.category

import com.anafthdev.dujer.data.FinancialType
import com.anafthdev.dujer.data.model.Category

sealed class CategoryAction {
	data class Edit(val category: Category): CategoryAction()
	data class ChangeCategoryAction(val action: String): CategoryAction()
	data class ChangeCategoryName(val name: String): CategoryAction()
	data class ChangeCategoryIconID(val iconID: Int): CategoryAction()
	data class ChangeFinancialType(val type: FinancialType): CategoryAction()
	data class ChangeFinancialTypeForEdit(val type: FinancialType): CategoryAction()
	data class Update(val categories: Array<out Category>): CategoryAction() {
		override fun equals(other: Any?): Boolean {
			if (this === other) return true
			if (javaClass != other?.javaClass) return false
			
			other as Update
			
			if (!categories.contentEquals(other.categories)) return false
			
			return true
		}
		
		override fun hashCode(): Int {
			return categories.contentHashCode()
		}
	}
	data class Insert(val categories: Array<out Category>): CategoryAction() {
		override fun equals(other: Any?): Boolean {
			if (this === other) return true
			if (javaClass != other?.javaClass) return false
			
			other as Update
			
			if (!categories.contentEquals(other.categories)) return false
			
			return true
		}
		
		override fun hashCode(): Int {
			return categories.contentHashCode()
		}
	}
}
