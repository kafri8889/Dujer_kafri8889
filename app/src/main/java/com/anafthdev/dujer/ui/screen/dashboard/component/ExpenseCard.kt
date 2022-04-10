package com.anafthdev.dujer.ui.screen.dashboard.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.anafthdev.dujer.R
import com.anafthdev.dujer.foundation.extension.sizeBasedWidth
import com.anafthdev.dujer.foundation.window.dpScaled
import com.anafthdev.dujer.model.Currency
import com.anafthdev.dujer.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExpenseCard(
	expense: Double,
	currency: Currency,
	modifier: Modifier = Modifier,
	onClick: () -> Unit
) {
	
	Card(
		shape = big_shape,
		containerColor = expense_card_background,
		onClick = onClick,
		elevation = CardDefaults.cardElevation(
			defaultElevation = 0.dp,
			focusedElevation = 0.dp,
			hoveredElevation = 0.dp
		),
		modifier = modifier
			.fillMaxWidth()
			.sizeBasedWidth(
				enlargement = 1.2f
			)
	) {
		Column(
			modifier = Modifier
				.fillMaxSize()
				.padding(16.dpScaled)
		) {
			
			Box(
				contentAlignment = Alignment.Center,
				modifier = Modifier
					.size(48.dpScaled)
					.clip(RoundedCornerShape(100))
					.background(Color(0xFF836F81))
			) {
				Icon(
					painter = painterResource(id = R.drawable.ic_bxs_wallet),
					tint = Color.White,
					contentDescription = null
				)
			}
			
			Spacer(
				modifier = Modifier
					.weight(1f)
			)
			
			Text(
				text = "${currency.symbol} $expense",
				style = Typography.bodyMedium.copy(
					color = black01,
					fontWeight = FontWeight.Bold
				),
				modifier = Modifier
					.padding(
						vertical = 4.dpScaled
					)
			)
			
			Text(
				text = stringResource(id = R.string.expenses),
				style = Typography.bodySmall.copy(
					color = black05,
					fontWeight = FontWeight.Normal
				),
				modifier = Modifier
					.padding(
						vertical = 4.dpScaled
					)
			)
			
		}
	}
}
