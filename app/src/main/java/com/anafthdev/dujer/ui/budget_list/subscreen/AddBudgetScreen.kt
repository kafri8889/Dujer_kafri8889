package com.anafthdev.dujer.ui.budget_list.subscreen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.anafthdev.dujer.data.FinancialType
import com.anafthdev.dujer.data.db.model.Budget
import com.anafthdev.dujer.data.db.model.Category
import com.anafthdev.dujer.foundation.window.dpScaled
import com.anafthdev.dujer.ui.app.LocalDujerState
import com.anafthdev.dujer.ui.budget_list.component.SelectBudgetCategoryCard
import com.anafthdev.dujer.ui.theme.black04
import com.anafthdev.dujer.uicomponent.TopAppBar

@Composable
fun AddBudgetScreen(
	averagePerMonthCategory: List<Pair<Double, Category>>,
	onBack: () ->Unit,
	onAdded: (Budget) -> Unit
) {
	
	val dujerState = LocalDujerState.current
	
	val allExpenseTransaction = dujerState.allExpenseTransaction
	
	val categories = remember(dujerState.allCategory) {
		dujerState.allCategory
			.filter { it.type == FinancialType.EXPENSE }
			.sortedBy { it.name }
	}
	
	var selectedCategory by rememberSaveable { mutableStateOf(Category.otherExpense.id) }
	
	Column(
		modifier = Modifier
			.fillMaxSize()
			.background(MaterialTheme.colorScheme.background)
			.systemBarsPadding()
			.verticalScroll(rememberScrollState())
	) {
		TopAppBar {
			IconButton(
				onClick = onBack,
				modifier = Modifier
					.padding(start = 8.dpScaled)
					.align(Alignment.CenterStart)
			) {
				Icon(
					imageVector = Icons.Rounded.ArrowBack,
					tint = black04,
					contentDescription = null
				)
			}
		}
		
		categories.forEach { category ->
			
			val averagePerMonth = remember {
				averagePerMonthCategory.find { it.second.id == category.id }?.first ?: 0.0
			}
			
			SelectBudgetCategoryCard(
				selected = category.id == selectedCategory,
				category = category,
				averagePerMonth = averagePerMonth,
				onClick = {
					selectedCategory = category.id
				},
				modifier = Modifier
					.padding(
						vertical = 8.dpScaled,
						horizontal = 16.dpScaled
					)
			)
		}
	}
	
}
