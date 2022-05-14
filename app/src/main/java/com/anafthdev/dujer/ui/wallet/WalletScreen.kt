package com.anafthdev.dujer.ui.wallet

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.ArrowDropDown
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.zIndex
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.anafthdev.dujer.R
import com.anafthdev.dujer.foundation.extension.toColor
import com.anafthdev.dujer.foundation.uimode.data.LocalUiMode
import com.anafthdev.dujer.foundation.window.dpScaled
import com.anafthdev.dujer.foundation.window.spScaled
import com.anafthdev.dujer.ui.theme.Typography
import com.anafthdev.dujer.ui.theme.medium_shape
import com.anafthdev.dujer.ui.theme.shapes
import com.anafthdev.dujer.ui.wallet.component.DeleteWalletPopup
import com.anafthdev.dujer.ui.wallet.subscreen.SelectWalletScreen
import com.anafthdev.dujer.uicomponent.TopAppBar
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun WalletScreen(
	walletID: Int,
	navController: NavController
) {
	
	val uiMode = LocalUiMode.current
	
	val walletViewModel = hiltViewModel<WalletViewModel>()
	
	val state by walletViewModel.state.collectAsState()
	
	val scope = rememberCoroutineScope()
	val selectWalletSheetState = rememberModalBottomSheetState(
		initialValue = ModalBottomSheetValue.Hidden,
		skipHalfExpanded = true
	)
	
	var isDeleteConfirmationPopupShowing by remember { mutableStateOf(false) }
	
	val wallet = state.wallet
	val wallets = state.wallets
	
	val hideSelectWalletSheetState = {
		scope.launch { selectWalletSheetState.hide() }
		Unit
	}
	val showSelectWalletSheetState = {
		scope.launch { selectWalletSheetState.show() }
		Unit
	}
	
	BackHandler {
		navController.popBackStack()
	}
	
	LaunchedEffect(walletID) {
		walletViewModel.dispatch(
			WalletAction.GetWallet(walletID)
		)
	}
	
	Box(
		modifier = Modifier
			.fillMaxSize()
			.background(MaterialTheme.colorScheme.background)
	) {
		AnimatedVisibility(
			visible = isDeleteConfirmationPopupShowing,
			enter = fadeIn(
				animationSpec = tween(400)
			),
			exit = fadeOut(
				animationSpec = tween(400)
			),
			modifier = Modifier
				.fillMaxSize()
				.zIndex(2f)
		) {
			DeleteWalletPopup(
				onDelete = {
					walletViewModel.dispatch(
						WalletAction.DeleteWallet(
							wallet
						)
					)
					
					navController.popBackStack()
				},
				onCancel = {
					isDeleteConfirmationPopupShowing = false
				},
				onClickOutside = {
					isDeleteConfirmationPopupShowing = false
				}
			)
		}
		
		ModalBottomSheetLayout(
			sheetState = selectWalletSheetState,
			sheetShape = RoundedCornerShape(
				topStart = shapes.medium.topStart,
				topEnd = shapes.medium.topEnd,
				bottomEnd = CornerSize(0.dpScaled),
				bottomStart = CornerSize(0.dpScaled)
			),
			sheetContent = {
				SelectWalletScreen(
					selectedWallet = wallet,
					wallets = wallets,
					onWalletSelected = { wallet ->
						hideSelectWalletSheetState()
						walletViewModel.dispatch(
							WalletAction.GetWallet(wallet.id)
						)
					}
				)
			}
		) {
			Column(
				modifier = Modifier
					.systemBarsPadding()
			) {
				TopAppBar {
					IconButton(
						onClick = {
							navController.popBackStack()
						},
						modifier = Modifier
							.align(Alignment.CenterStart)
					) {
						Icon(
							imageVector = Icons.Rounded.ArrowBack,
							contentDescription = null
						)
					}
					
					Text(
						text = stringResource(id = R.string.wallet),
						style = Typography.titleLarge.copy(
							fontWeight = FontWeight.Bold,
							fontSize = Typography.titleLarge.fontSize.spScaled
						)
					)
					
					Row(
						modifier = Modifier
							.align(Alignment.CenterEnd)
					) {
						IconButton(
							onClick = {},
							modifier = Modifier
						) {
							Icon(
								painter = painterResource(id = R.drawable.ic_chart),
								contentDescription = null
							)
						}
						
						if (!wallet.defaultWallet) {
							IconButton(
								onClick = {
									isDeleteConfirmationPopupShowing = true
								},
								modifier = Modifier
							) {
								Icon(
									painter = painterResource(id = R.drawable.ic_trash),
									contentDescription = null
								)
							}
						}
					}
				}
				
				Box(
					modifier = Modifier
						.padding(
							top = 16.dpScaled
						)
						.size(72.dpScaled)
						.clip(medium_shape)
						.border(
							width = 1.dpScaled,
							color = MaterialTheme.colorScheme.outline,
							shape = medium_shape
						)
						.background(wallet.tint.backgroundTint.toColor())
						.align(Alignment.CenterHorizontally)
				) {
					Icon(
						painter = painterResource(id = wallet.iconID),
						tint = wallet.tint.iconTint.toColor(),
						contentDescription = null,
						modifier = Modifier
							.size(28.dpScaled)
							.align(Alignment.Center)
					)
				}
				
				Row(
					verticalAlignment = Alignment.CenterVertically,
					modifier = Modifier
						.padding(
							top = 8.dpScaled
						)
						.clip(MaterialTheme.shapes.medium)
						.clickable {
							showSelectWalletSheetState()
						}
						.padding(
							vertical = 4.dpScaled,
							horizontal = 8.dpScaled
						)
						.align(Alignment.CenterHorizontally)
				) {
					Text(
						text = wallet.name,
						style = MaterialTheme.typography.bodyLarge.copy(
							fontWeight = FontWeight.SemiBold,
							fontSize = MaterialTheme.typography.bodyLarge.fontSize.spScaled
						)
					)
					
					Icon(
						imageVector = Icons.Rounded.ArrowDropDown,
						contentDescription = null,
						modifier = Modifier
							.padding(
								start = 8.dpScaled
							)
					)
				}
			}
		}
	}
}
