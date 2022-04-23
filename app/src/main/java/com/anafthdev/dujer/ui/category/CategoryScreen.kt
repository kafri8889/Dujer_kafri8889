package com.anafthdev.dujer.ui.category

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.anafthdev.dujer.R
import com.anafthdev.dujer.data.CategoryIcons
import com.anafthdev.dujer.data.db.model.Category
import com.anafthdev.dujer.foundation.extension.lastIndexOf
import com.anafthdev.dujer.foundation.extension.removeFirstAndLastWhitespace
import com.anafthdev.dujer.foundation.window.dpScaled
import com.anafthdev.dujer.foundation.window.spScaled
import com.anafthdev.dujer.model.CategoryTint
import com.anafthdev.dujer.ui.app.DujerViewModel
import com.anafthdev.dujer.ui.category.component.SwipeableCategory
import com.anafthdev.dujer.ui.category.data.CategoryAction
import com.anafthdev.dujer.ui.theme.Inter
import com.anafthdev.dujer.ui.theme.Typography
import com.anafthdev.dujer.ui.theme.black04
import com.anafthdev.dujer.ui.theme.shapes
import com.anafthdev.dujer.uicomponent.OutlinedTextField
import com.anafthdev.dujer.uicomponent.TopAppBar
import com.anafthdev.dujer.util.AppUtil.toast
import com.google.accompanist.flowlayout.FlowCrossAxisAlignment
import com.google.accompanist.flowlayout.FlowMainAxisAlignment
import com.google.accompanist.flowlayout.FlowRow
import com.google.accompanist.flowlayout.MainAxisAlignment
import kotlinx.coroutines.launch
import kotlin.random.Random

@OptIn(ExperimentalMaterialApi::class, ExperimentalComposeUiApi::class)
@Composable
fun CategoryScreen(
	id: Int = Category.default.id,
	action: String = CategoryAction.NOTHING,
	navController: NavController,
	dujerViewModel: DujerViewModel
) {
	
	val context = LocalContext.current
	val focusManager = LocalFocusManager.current
	val keyboardController = LocalSoftwareKeyboardController.current
	
	val categoryViewModel = hiltViewModel<CategoryViewModel>()
	
	val scope = rememberCoroutineScope()
	val sheetState = rememberModalBottomSheetState(
		initialValue = ModalBottomSheetValue.Hidden,
		skipHalfExpanded = true
	)
	
	val state by categoryViewModel.state.collectAsState()
	
	val categories = state.categories
	
	var category by remember { mutableStateOf(Category.default) }
	var hasNavigate by remember { mutableStateOf(false) }
	var categoryAction by remember { mutableStateOf(action) }
	var newCategoryName by remember { mutableStateOf("") }
	var selectedCategoryIcon by remember { mutableStateOf(CategoryIcons.other) }
	val categoryNameFocusRequester by remember { mutableStateOf(FocusRequester()) }
	
	val hideSheet = {
		scope.launch { sheetState.hide() }
		Unit
	}
	val showSheet = {
		scope.launch { sheetState.show() }
		Unit
	}
	
	val getCategory = {
		categoryViewModel.get(id) { mCategory ->
			category = mCategory
			newCategoryName = category.name
			selectedCategoryIcon = category.iconID
			
			showSheet()
			
			true.also {
				hasNavigate = it
			}
		}
	}
	
	if (!hasNavigate) {
		if ((id != Category.default.id) and (categoryAction == CategoryAction.EDIT)) {
			getCategory()
		}
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
				newCategoryName = ""
				selectedCategoryIcon = CategoryIcons.other
				categoryAction = CategoryAction.NOTHING
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
					.imePadding()
					.fillMaxWidth()
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
						text = if (categoryAction == CategoryAction.EDIT) stringResource(id = R.string.edit_category)
						else stringResource(id = R.string.new_category),
						style = Typography.bodyLarge.copy(
							fontWeight = FontWeight.Medium,
							fontSize = Typography.bodyLarge.fontSize.spScaled
						),
						modifier = Modifier
							.align(Alignment.Center)
					)
					
					IconButton(
						onClick = {
							when {
								newCategoryName.isBlank() -> context.getString(
									R.string.category_name_cannot_be_empty
								).toast(context)
								else -> {
									if (categoryAction == CategoryAction.EDIT) {
										categoryViewModel.updateCategory(
											category.copy(
												name = newCategoryName.removeFirstAndLastWhitespace(),
												iconID = selectedCategoryIcon
											)
										)
									} else {
										categoryViewModel.insertCategory(
											Category(
												id = Random.nextInt(),
												name = newCategoryName.removeFirstAndLastWhitespace(),
												iconID = selectedCategoryIcon,
												tint = CategoryTint.getRandomTint()
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
				
				OutlinedTextField(
					singleLine = true,
					maxCounter = 30,
					value = newCategoryName,
					onValueChange = { s ->
						newCategoryName = s
					},
					textStyle = LocalTextStyle.current.copy(
						fontFamily = Inter
					),
					keyboardOptions = KeyboardOptions(
						imeAction = ImeAction.Done
					),
					keyboardActions = KeyboardActions(
						onDone = {
							hideSheet()
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
									color = if (selectedCategoryIcon == icon) MaterialTheme.colorScheme.primaryContainer
									else Color.Transparent
								)
								.border(
									width = 1.dpScaled,
									color = MaterialTheme.colorScheme.outline,
									shape = shapes.small
								)
								.clickable {
									selectedCategoryIcon = icon
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
								tint = black04,
								contentDescription = null
							)
						}
						
						Text(
							text = stringResource(id = R.string.category),
							style = Typography.titleLarge.copy(
								fontWeight = FontWeight.Bold,
								fontSize = Typography.titleLarge.fontSize.spScaled
							)
						)
					}
				}
				
				itemsIndexed(
					items = categories,
					key = { _: Int, item: Category -> item.id }
				) { i, item ->
					SwipeableCategory(
						category = item,
						onCanDelete = {
							dujerViewModel.vibrate(100)
						},
						onDismissToStart = {
							categoryViewModel.get(item.id) { mCategory ->
								category = mCategory
								newCategoryName = category.name
								selectedCategoryIcon = category.iconID
								
								categoryAction = CategoryAction.EDIT
								
								showSheet()
							}
						},
						onDismissToEnd = {
							categoryViewModel.deleteCategory(item)
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
