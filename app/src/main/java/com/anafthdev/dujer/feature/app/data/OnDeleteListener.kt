package com.anafthdev.dujer.feature.app.data

import com.anafthdev.dujer.data.model.Category
import com.anafthdev.dujer.data.model.Financial

interface OnDeleteListener {
	
	fun onDelete(financial: Financial)
	
	fun onDelete(category: Category)
	
}