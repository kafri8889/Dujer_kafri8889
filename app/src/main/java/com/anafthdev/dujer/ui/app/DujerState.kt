package com.anafthdev.dujer.ui.app

import com.anafthdev.dujer.model.Currency
import com.anafthdev.dujer.ui.app.data.UndoType

data class DujerState(
	val currentCurrency: Currency = Currency.DOLLAR,
	val dataCanReturned: UndoType = UndoType.Financial
)
