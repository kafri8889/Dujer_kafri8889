package com.anafthdev.dujer.ui.category

import com.anafthdev.dujer.data.db.model.Category

sealed class CategoryAction {
	data class Get(val id: Int, val action: (Category) -> Unit): CategoryAction()
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
