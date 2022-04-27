package com.anafthdev.dujer.ui.app

import com.anafthdev.dujer.data.db.model.Category
import com.anafthdev.dujer.data.db.model.Financial
import com.anafthdev.dujer.ui.app.data.UndoType

sealed class DujerAction {
	data class Undo(val type: UndoType): DujerAction()
	data class Vibrate(val durationInMillis: Long): DujerAction()
	data class DeleteFinancial(val financials: Array<out Financial>): DujerAction() {
		override fun equals(other: Any?): Boolean {
			if (this === other) return true
			if (javaClass != other?.javaClass) return false
			
			other as DeleteFinancial
			
			if (!financials.contentEquals(other.financials)) return false
			
			return true
		}
		
		override fun hashCode(): Int {
			return financials.contentHashCode()
		}
	}
	
	data class DeleteCategory(val categories: Array<out Category>): DujerAction() {
		override fun equals(other: Any?): Boolean {
			if (this === other) return true
			if (javaClass != other?.javaClass) return false
			
			other as DeleteCategory
			
			if (!categories.contentEquals(other.categories)) return false
			
			return true
		}
		
		override fun hashCode(): Int {
			return categories.contentHashCode()
		}
	}
}
