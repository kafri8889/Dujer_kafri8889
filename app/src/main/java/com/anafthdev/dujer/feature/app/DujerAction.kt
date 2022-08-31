package com.anafthdev.dujer.feature.app

import com.anafthdev.dujer.data.model.Category
import com.anafthdev.dujer.data.model.Financial
import com.anafthdev.dujer.data.model.Wallet
import com.anafthdev.dujer.feature.app.data.DujerController
import com.anafthdev.dujer.feature.app.data.UndoType

sealed interface DujerAction {
	data class Undo(val type: UndoType): DujerAction
	data class InsertWallet(val wallet: Wallet): DujerAction
	data class SetController(val controller: DujerController): DujerAction
	data class DeleteFinancial(val financials: Array<out Financial>): DujerAction {
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
	
	data class DeleteCategory(val categories: Array<out Category>, val financials: Array<out Financial>): DujerAction {
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
	
	data class DeleteWallet(val wallets: Array<out Wallet>): DujerAction {
		override fun equals(other: Any?): Boolean {
			if (this === other) return true
			if (javaClass != other?.javaClass) return false
			
			other as DeleteWallet
			
			if (!wallets.contentEquals(other.wallets)) return false
			
			return true
		}
		
		override fun hashCode(): Int {
			return wallets.contentHashCode()
		}
	}
}
