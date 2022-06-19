package com.anafthdev.dujer.ui.wallet.subscreen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import com.anafthdev.dujer.R
import com.anafthdev.dujer.data.db.model.Wallet
import com.anafthdev.dujer.foundation.extension.isLightTheme
import com.anafthdev.dujer.foundation.ui.LocalUiColor
import com.anafthdev.dujer.foundation.uimode.data.LocalUiMode
import com.anafthdev.dujer.foundation.window.dpScaled
import com.anafthdev.dujer.foundation.window.spScaled
import com.anafthdev.dujer.ui.theme.Typography
import com.anafthdev.dujer.ui.theme.full_shape
import com.anafthdev.dujer.ui.wallet.component.SelectWalletItem

@Composable
fun SelectWalletScreen(
	selectedWallet: Wallet,
	wallets: List<Wallet>,
	onWalletSelected: (Wallet) -> Unit
) {
	
	val uiMode = LocalUiMode.current

	Column(
		modifier = Modifier
			.fillMaxWidth()
			.background(
				if (uiMode.isLightTheme()) MaterialTheme.colorScheme.background
				else MaterialTheme.colorScheme.surfaceVariant
			)
			.padding(
				vertical = 16.dpScaled
			)
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
	}
}
