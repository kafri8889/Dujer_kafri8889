package com.anafthdev.dujer.data

sealed class DujerDestination(val route: String) {
	
	object Dashboard: DujerDestination("dashboard")
	
	object Income: DujerDestination("income")
	
	object Expense: DujerDestination("expense")
}