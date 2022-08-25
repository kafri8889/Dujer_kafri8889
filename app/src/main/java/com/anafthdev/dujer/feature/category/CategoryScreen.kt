package com.anafthdev.dujer.feature.category

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.zIndex
import androidx.navigation.NavController
import com.anafthdev.dujer.R
import com.anafthdev.dujer.data.CategoryIcons
import com.anafthdev.dujer.data.DujerDestination
import com.anafthdev.dujer.data.FinancialType
import com.anafthdev.dujer.data.model.Category
import com.anafthdev.dujer.data.model.Financial
import com.anafthdev.dujer.feature.app.LocalDujerState
import com.anafthdev.dujer.feature.category.component.SwipeableCategory
import com.anafthdev.dujer.feature.category.data.CategorySwipeAction
import com.anafthdev.dujer.feature.theme.Inter
import com.anafthdev.dujer.feature.theme.Typography
import com.anafthdev.dujer.feature.theme.shapes
import com.anafthdev.dujer.foundation.common.AppUtil.toast
import com.anafthdev.dujer.foundation.extension.*
import com.anafthdev.dujer.foundation.ui.LocalUiColor
import com.anafthdev.dujer.foundation.uicomponent.FinancialTypeSelector
import com.anafthdev.dujer.foundation.uicomponent.OutlinedTextField
import com.anafthdev.dujer.foundation.uicomponent.TopAppBar
import com.anafthdev.dujer.foundation.uimode.data.LocalUiMode
import com.anafthdev.dujer.foundation.window.dpScaled
import com.anafthdev.dujer.foundation.window.spScaled
import com.anafthdev.dujer.model.CategoryTint
import com.google.accompanist.flowlayout.FlowCrossAxisAlignment
import com.google.accompanist.flowlayout.FlowMainAxisAlignment
import com.google.accompanist.flowlayout.FlowRow
import com.google.accompanist.flowlayout.MainAxisAlignment
import kotlinx.coroutines.launch
import kotlin.random.Random

@OptIn(ExperimentalMaterialApi::class, ExperimentalComposeUiApi::class,
	ExperimentalMaterial3Api::class
)
@Composable
fun CategoryScreen(
	navController: NavController,
	viewModel: CategoryViewModel,
	onDismissToEnd: (Category, List<Financial>) -> Unit
) {
	
	val uiMode = LocalUiMode.current
	val context = LocalContext.current
	val dujerState = LocalDujerState.current
	val focusManager = LocalFocusManager.current
	val keyboardController = LocalSoftwareKeyboardController.current
	
	val categoryNameFocusRequester = remember { FocusRequester() }
	
	val scope = rememberCoroutineScope()
	val sheetState = rememberModalBottomSheetState(
		initialValue = ModalBottomSheetValue.Hidden,
		skipHalfExpanded = true
	)
	
	val state by viewModel.state.collectAsState()
	
	val categories = dujerState.allCategory
	
	val incomeCategories = remember(categories, state.selectedFinancialType) {
		categories.filter { it.type.isIncome() }
	}
	
	val expenseCategories = remember(categories, state.selectedFinancialType) {
		categories.filter { it.type == FinancialType.EXPENSE }
	}
	
	val hideSheet = {
		scope.launch { sheetState.hide() }
		Unit
	}
	val showSheet = {
		scope.launch { sheetState.show() }
		Unit
	}
	
	LaunchedEffect(dujerState.allCategory) {
		val categoryIDs = categories.map { it.id }
		val financialList = dujerState.allIncomeTransaction + dujerState.allExpenseTransaction
		
		viewModel.listenDeletedCategory(financialList, categoryIDs)
		viewModel.listenModifiedCategory(financialList, categories)
	}
	
	DisposableEffect(
		key1 = sheetState.isVisible,
	) {
		onDispose {
			if (sheetState.isVisible) {
				keyboardController?.show()
				categoryNameFocusRequester.requestFocus()
			} else {
				focusManager.clearFocus()
				keyboardController?.hide()
				viewModel.dispatch(
					CategoryAction.ChangeCategoryName("")
				)
				
				viewModel.dispatch(
					CategoryAction.ChangeCategoryIconID(Category.default.iconID)
				)
				
				viewModel.dispatch(
					CategoryAction.ChangeCategoryAction(CategorySwipeAction.NOTHING)
				)
			}
		}
	}
	
	BackHandler {
		when {
			sheetState.isVisible -> hideSheet()
			else -> navController.popBackStack()
		}
	}
	
	ModalBottomSheetLayout(
		sheetState = sheetState,
		sheetShape = RoundedCornerShape(
			topStart = shapes.medium.topStart,
			topEnd = shapes.medium.topEnd,
			bottomEnd = CornerSize(0.dpScaled),
			bottomStart = CornerSize(0.dpScaled)
		),
		sheetContent = {
			Column(
				modifier = Modifier
					.fillMaxSize()
					.background(
						if (uiMode.isLightTheme()) MaterialTheme.colorScheme.background
						else MaterialTheme.colorScheme.surfaceVariant
					)
					.statusBarsPadding()
					.verticalScroll(rememberScrollState())
			) {
				Box(
					modifier = Modifier
						.fillMaxWidth()
				) {
					IconButton(
						onClick = hideSheet,
						modifier = Modifier
							.align(Alignment.CenterStart)
					) {
						Icon(
							imageVector = Icons.Rounded.Close,
							contentDescription = null
						)
					}
					
					Text(
						text = if (state.swipeAction == CategorySwipeAction.EDIT) stringResource(id = R.string.edit_category)
						else stringResource(id = R.string.new_category),
						style = Typography.titleMedium.copy(
							color = LocalUiColor.current.titleText,
							fontWeight = FontWeight.Bold,
							fontSize = Typography.titleMedium.fontSize.spScaled
						),
						modifier = Modifier
							.align(Alignment.Center)
					)
					
					IconButton(
						onClick = {
							when {
								state.categoryName.isBlank() -> context.getString(
									R.string.category_name_cannot_be_empty
								).toast(context)
								else -> {
									if (state.swipeAction == CategorySwipeAction.EDIT) {
										viewModel.dispatch(
											CategoryAction.Update(
												state.category.copy(
													name = state.categoryName.removeFirstAndLastWhitespace(),
													iconID = state.categoryIconID,
													type = state.selectedFinancialTypeForEdit
												).toArray()
											)
										)
									} else {
										viewModel.dispatch(
											CategoryAction.Insert(
												Category(
													id = Random.nextInt(),
													name = state.categoryName.removeFirstAndLastWhitespace(),
													iconID = state.categoryIconID,
													type = state.selectedFinancialType,
													tint = CategoryTint.getRandomTint()
												).toArray()
											)
										)
									}
									
									hideSheet()
								}
							}
						},
						modifier = Modifier
							.align(Alignment.CenterEnd)
					) {
						Icon(
							imageVector = Icons.Rounded.Check,
							contentDescription = null
						)
					}
				}
				
				FinancialTypeSelector(
					selectedFinancialType = state.selectedFinancialTypeForEdit,
					onFinancialTypeChanged = { type ->
						viewModel.dispatch(
							CategoryAction.ChangeFinancialTypeForEdit(type)
						)
					},
					modifier = Modifier
						.padding(8.dpScaled)
				)
				
				OutlinedTextField(
					singleLine = true,
					maxCounter = 30,
					value = state.categoryName,
					onValueChange = { name ->
						viewModel.dispatch(
							CategoryAction.ChangeCategoryName(name)
						)
					},
					textStyle = LocalTextStyle.current.copy(
						fontFamily = Inter
					),
					keyboardOptions = KeyboardOptions(
						imeAction = ImeAction.Done
					),
					keyboardActions = KeyboardActions(
						onDone = {
							focusManager.clearFocus(force = true)
						}
					),
					placeholder = {
						Text(stringResource(id = R.string.category_name))
					},
					modifier = Modifier
						.padding(
							top = 8.dpScaled,
							end = 16.dpScaled,
							start = 16.dpScaled,
							bottom = 16.dpScaled
						)
						.fillMaxWidth()
						.focusRequester(categoryNameFocusRequester)
				)
				
				FlowRow(
					lastLineMainAxisAlignment = FlowMainAxisAlignment.Center,
					crossAxisAlignment = FlowCrossAxisAlignment.Center,
					mainAxisAlignment = MainAxisAlignment.Center,
					modifier = Modifier
						.padding(
							end = 16.dpScaled,
							start = 16.dpScaled,
							bottom = 16.dpScaled
						)
						.fillMaxWidth()
				) {
					for (icon in CategoryIcons.values) {
						Box(
							contentAlignment = Alignment.Center,
							modifier = Modifier
								.padding(4.dpScaled)
								.size(48.dpScaled)
								.clip(shapes.small)
								.background(
									color = if (state.categoryIconID == icon) MaterialTheme.colorScheme.primaryContainer
									else Color.Transparent
								)
								.border(
									width = 1.dpScaled,
									color = MaterialTheme.colorScheme.outline,
									shape = shapes.small
								)
								.clickable {
									viewModel.dispatch(
										CategoryAction.ChangeCategoryIconID(icon)
									)
								}
						) {
							Icon(
								painter = painterResource(id = icon),
								contentDescription = null
							)
						}
					}
				}
			}
		}
	) {
		Box(
			modifier = Modifier
				.fillMaxSize()
				.background(MaterialTheme.colorScheme.background)
				.systemBarsPadding()
		) {
			FloatingActionButton(
				onClick = showSheet,
				modifier = Modifier
					.padding(32.dpScaled)
					.align(Alignment.BottomEnd)
					.zIndex(2f)
			) {
				Icon(
					imageVector = Icons.Rounded.Add,
					contentDescription = null,
				)
			}
			
			LazyColumn {
				item {
					Column(
						horizontalAlignment = Alignment.CenterHorizontally,
						modifier = Modifier
							.fillMaxWidth()
					) {
						TopAppBar {
							IconButton(
								onClick = {
									navController.popBackStack()
								},
								modifier = Modifier
									.padding(start = 8.dpScaled)
									.align(Alignment.CenterStart)
							) {
								Icon(
									imageVector = Icons.Rounded.ArrowBack,
									contentDescription = null
								)
							}
							
							Text(
								text = stringResource(id = R.string.category),
								style = Typography.titleLarge.copy(
									color = LocalUiColor.current.headlineText,
									fontWeight = FontWeight.Bold,
									fontSize = Typography.titleLarge.fontSize.spScaled
								)
							)
						}
						
						FinancialTypeSelector(
							selectedFinancialType = state.selectedFinancialType,
							onFinancialTypeChanged = { type ->
								viewModel.dispatch(
									CategoryAction.ChangeFinancialType(type)
								)
							},
							modifier = Modifier
								.padding(8.dpScaled)
						)
					}
				}
				
				itemsIndexed(
					items = if (state.selectedFinancialType.isIncome()) incomeCategories else expenseCategories,
					key = { _: Int, item: Category -> item.id }
				) { i, item ->
					
					val financials = remember(item) {
						dujerState.allTransaction.filter {
							it.category.id == item.id
						}
					}
					
					SwipeableCategory(
						category = item,
						onClick = {
							navController.navigate(
								DujerDestination.CategoryTransaction.Home.createRoute(item.id)
							)
						},
						onDismissToStart = {
							viewModel.dispatch(
								CategoryAction.Edit(item)
							)
							
							showSheet()
						},
						onDismissToEnd = {
							onDismissToEnd(
								item,
								financials
							)
						},
						modifier = Modifier
							.padding(
								start = 8.dpScaled,
								end = 8.dpScaled,
								top = 4.dpScaled,
								bottom = if (i.lastIndexOf(categories)) 96.dpScaled else 4.dpScaled,
							)
					)
				}
			}
		}
	}
}
