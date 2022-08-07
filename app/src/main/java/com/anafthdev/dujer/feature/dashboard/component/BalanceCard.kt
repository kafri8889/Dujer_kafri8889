package com.anafthdev.dujer.feature.dashboard.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.anafthdev.dujer.R
import com.anafthdev.dujer.data.model.Wallet
import com.anafthdev.dujer.feature.theme.*
import com.anafthdev.dujer.foundation.uicomponent.WalletCard
import com.anafthdev.dujer.foundation.uicomponent.WormHorizontalPagerIndicator
import com.anafthdev.dujer.foundation.window.dpScaled
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState

@OptIn(ExperimentalMaterial3Api::class, ExperimentalPagerApi::class)
@Composable
fun BalanceCard(
	wallets: List<Wallet>,
	modifier: Modifier = Modifier,
	onAddWallet: () -> Unit,
	onWalletClicked: (Wallet) -> Unit
) {
	
	val walletPagerState = rememberPagerState()
	
	Card(
		shape = large_shape,
		colors = CardDefaults.cardColors(
			containerColor = balance_card_background
		),
		modifier = modifier
			.fillMaxWidth()
	) {
		Column(
			modifier = Modifier
				.fillMaxSize()
				.padding(
					vertical = 24.dpScaled,
					horizontal = 16.dp
				)
		) {
			Text(
				text = stringResource(id = R.string.wallet),
				style = Typography.bodyMedium.copy(
					color = Color.White,
					fontWeight = FontWeight.SemiBold
				)
			)
			
			HorizontalPager(
				count = wallets.size,
				state = walletPagerState,
				modifier = Modifier
					.padding(
						top = 8.dpScaled
					)
					.fillMaxWidth()
			) { page ->
				for (i in wallets.indices) {
					WalletCard(
						wallet = wallets[page],
						onClick = {
							onWalletClicked(wallets[page])
						},
						modifier = Modifier
							.padding(4.dpScaled)
							.fillMaxWidth()
					)
				}
			}
			
			WormHorizontalPagerIndicator(
				pagerState = walletPagerState,
				activeColor = md_theme_light_tertiaryContainer,
				inactiveColor = md_theme_light_onTertiary,
				modifier = Modifier
					.padding(top = 8.dpScaled)
					.align(Alignment.CenterHorizontally)
			)
			
			TextButton(
				onClick = onAddWallet,
				modifier = Modifier
					.padding(top = 8.dpScaled)
			) {
				Icon(
					imageVector = Icons.Rounded.Add,
					tint = md_theme_light_primaryContainer,
					contentDescription = stringResource(id = R.string.add_wallet)
				)
				
				Text(
					text = stringResource(id = R.string.add_wallet),
					style = Typography.bodyMedium.copy(
						color = Color.White,
						fontWeight = FontWeight.Normal
					)
				)
			}
		}
	}
}
