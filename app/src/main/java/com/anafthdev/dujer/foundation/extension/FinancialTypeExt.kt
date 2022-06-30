package com.anafthdev.dujer.foundation.extension

import com.anafthdev.dujer.data.FinancialType

fun FinancialType.isIncome() = this == FinancialType.INCOME
fun FinancialType.isExpense() = this == FinancialType.EXPENSE
fun FinancialType.isNothing() = this == FinancialType.NOTHING
