package com.anafthdev.dujer.ui.budget_list.environment

import com.anafthdev.dujer.foundation.di.DiName
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject
import javax.inject.Named

class BudgetListEnvironment @Inject constructor(
	@Named(DiName.DISPATCHER_IO) override val dispatcher: CoroutineDispatcher
): IBudgetListEnvironment {



}