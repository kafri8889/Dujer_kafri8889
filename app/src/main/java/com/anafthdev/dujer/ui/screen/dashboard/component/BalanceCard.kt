package com.anafthdev.dujer.ui.screen.dashboard.component

import androidx.compose.animation.core.tween
import androidx.compose.foundation.gestures.ScrollableDefaults
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import com.anafthdev.dujer.R
import com.anafthdev.dujer.common.EventCountdownTimer
import com.anafthdev.dujer.foundation.extension.toDp
import com.anafthdev.dujer.foundation.window.dpScaled
import com.anafthdev.dujer.model.Currency
import com.anafthdev.dujer.ui.theme.Inter
import com.anafthdev.dujer.ui.theme.Typography
import com.anafthdev.dujer.ui.theme.balance_card_background
import com.anafthdev.dujer.ui.theme.big_shape
import com.anafthdev.dujer.util.AppUtil
import com.anafthdev.dujer.util.CurrencyFormatter
import kotlinx.coroutines.launch
import timber.log.Timber

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BalanceCard(
	balance: Double,
	currency: Currency
) {
	
	val density = LocalDensity.current
	
	val scope = rememberCoroutineScope()
	val textAmountScrollState = rememberScrollState()
	
	var cardHeight by remember { mutableStateOf(0.dpScaled) }
	val eventCountdownTimer by remember { mutableStateOf(EventCountdownTimer()) }
	
	DisposableEffect(textAmountScrollState.isScrollInProgress) {
		if (!eventCountdownTimer.isTimerRunning) {
			eventCountdownTimer.startTimer(
				millisInFuture = 3000,
				onTick = {},
				onFinish = {
					scope.launch {
						textAmountScrollState.animateScrollTo(
							value = 0,
							animationSpec = tween(600)
						)
					}
				}
			)
		}
		onDispose {}
	}
	
	Card(
		shape = big_shape,
		containerColor = balance_card_background,
		elevation = CardDefaults.cardElevation(
			defaultElevation = 4.dp
		),
		modifier = Modifier
			.fillMaxWidth()
			.onGloballyPositioned {
				cardHeight = with(density) {
					it.size.width
						.div(2f)
						.toDp()
				}
			}
			.height(cardHeight)
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
				text = stringResource(id = R.string.available_balance),
				style = Typography.bodyMedium.copy(
					color = Color.White,
					fontWeight = FontWeight.Normal
				)
			)
			
			Text(
				text = CurrencyFormatter.format(AppUtil.deviceLocale, balance),
				style = Typography.headlineMedium.copy(
					color = Color.White,
					fontWeight = FontWeight.SemiBold
				),
				modifier = Modifier
					.padding(top = 16.dpScaled)
					.horizontalScroll(
						textAmountScrollState
					)
			)
			
			Spacer(
				modifier = Modifier
					.weight(1f)
			)
			
			Text(
				text = stringResource(id = R.string.see_details),
				style = Typography.bodyMedium.copy(
					color = Color.White,
					fontWeight = FontWeight.SemiBold
				)
			)
		}
	}
}
