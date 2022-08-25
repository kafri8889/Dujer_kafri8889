package com.anafthdev.dujer.runtime.navigation

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.anafthdev.dujer.data.DujerDestination
import com.anafthdev.dujer.data.model.Financial
import com.anafthdev.dujer.data.model.Wallet
import com.anafthdev.dujer.feature.wallet.WalletScreen
import com.anafthdev.dujer.feature.wallet.WalletViewModel

fun NavGraphBuilder.WalletNavHost(
	navController: NavController,
	onDeleteTransaction: (Financial) -> Unit,
	onDeleteWallet: (Wallet) -> Unit
) {
	navigation(
		startDestination = DujerDestination.Wallet.Home.route,
		route = DujerDestination.Wallet.Root.route
	) {
		composable(
			route = DujerDestination.Wallet.Home.route,
			arguments = DujerDestination.Wallet.Home.arguments
		) { backEntry ->
			val viewModel = hiltViewModel<WalletViewModel>(backEntry)
			
			WalletScreen(
				navController = navController,
				viewModel = viewModel,
				onDeleteTransaction = onDeleteTransaction,
				onDeleteWallet = onDeleteWallet
			)
		}
	}
}
