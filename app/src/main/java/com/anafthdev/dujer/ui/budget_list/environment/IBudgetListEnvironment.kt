package com.anafthdev.dujer.ui.budget_list.environment

import kotlinx.coroutines.CoroutineDispatcher

interface IBudgetListEnvironment {
	
	val dispatcher: CoroutineDispatcher
	
}