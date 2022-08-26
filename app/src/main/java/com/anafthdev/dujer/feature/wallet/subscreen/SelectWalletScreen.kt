package com.anafthdev.dujer.feature.wallet.subscreen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import com.anafthdev.dujer.R
import com.anafthdev.dujer.data.model.Wallet
import com.anafthdev.dujer.feature.theme.Typography
import com.anafthdev.dujer.feature.theme.full_shape
import com.anafthdev.dujer.feature.wallet.component.SelectWalletItem
import com.anafthdev.dujer.foundation.ui.LocalUiColor
import com.anafthdev.dujer.foundation.window.dpScaled
import com.anafthdev.dujer.foundation.window.spScaled

@Composable
fun SelectWalletScreen(
	selectedWallet: Wallet,
	wallets: List<Wallet>,
	onWalletSelected: (Wallet) -> Unit
) {
	
	Column(
		modifier = Modifier
			.systemBarsPadding()
			.fillMaxWidth()
	) {
		Box(
			modifier = Modifier
				.size(32.dpScaled, 6.dpScaled)
				.clip(full_shape)
				.background(Color.Gray)
				.align(Alignment.CenterHorizontally)
		)
		
		Text(
			text = stringResource(id = R.string.select_wallet),
			style = Typography.titleMedium.copy(
				color = LocalUiColor.current.titleText,
				fontWeight = FontWeight.Bold,
				fontSize = Typography.titleMedium.fontSize.spScaled
			),
			modifier = Modifier
				.padding(
					vertical = 8.dpScaled
				)
				.align(Alignment.CenterHorizontally)
		)
		
		LazyColumn {
			items(wallets) { wallet ->
				SelectWalletItem(
					isSelected = selectedWallet.id == wallet.id,
					wallet = wallet,
					onClick = {
						onWalletSelected(wallet)
					}
				)
			}
		}
		
		Spacer(modifier = Modifier.height(16.dpScaled))
	}
}
