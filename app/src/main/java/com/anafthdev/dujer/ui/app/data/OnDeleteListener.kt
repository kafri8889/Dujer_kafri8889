package com.anafthdev.dujer.ui.app.data

import com.anafthdev.dujer.data.db.model.Category
import com.anafthdev.dujer.data.db.model.Financial

interface OnDeleteListener {
	
	fun onDelete(financial: Financial)
	
	fun onDelete(category: Category)
	
}