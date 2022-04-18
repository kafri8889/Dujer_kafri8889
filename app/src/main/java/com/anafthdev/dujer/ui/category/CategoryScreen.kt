package com.anafthdev.dujer.ui.category

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.zIndex
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.anafthdev.dujer.R
import com.anafthdev.dujer.data.db.model.Category
import com.anafthdev.dujer.foundation.window.dpScaled
import com.anafthdev.dujer.foundation.window.spScaled
import com.anafthdev.dujer.ui.category.component.SwipeableCategory
import com.anafthdev.dujer.ui.theme.Inter
import com.anafthdev.dujer.ui.theme.Typography
import com.anafthdev.dujer.ui.theme.black04
import com.anafthdev.dujer.ui.theme.shapes
import com.anafthdev.dujer.uicomponent.TopAppBar
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class, ExperimentalComposeUiApi::class)
@Composable
fun CategoryScreen(
	navController: NavController
) {
	
	val ime = WindowInsets.Companion.ime
	val density = LocalDensity.current
	val focusManager = LocalFocusManager.current
	val keyboardController = LocalSoftwareKeyboardController.current
	
	val categoryViewModel = hiltViewModel<CategoryViewModel>()
	
	val scope = rememberCoroutineScope()
	
	val state by categoryViewModel.state.collectAsState()
	
	val categories = state.categories
	
	val sheetState = rememberModalBottomSheetState(
		initialValue = ModalBottomSheetValue.Hidden
	)
	
	var newCategoryName by remember { mutableStateOf("") }
	val categoryNameFocusRequester by remember { mutableStateOf(FocusRequester()) }
	
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
			}
		}
	}
	
	BackHandler(enabled = sheetState.isVisible) {
		scope.launch {
			sheetState.hide()
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
			Box(
				modifier = Modifier
					.fillMaxWidth()
			) {
				IconButton(
					onClick = {
						scope.launch {
							sheetState.hide()
						}
					},
					modifier = Modifier
						.align(Alignment.CenterStart)
				) {
					Icon(
						imageVector = Icons.Rounded.Close,
						contentDescription = null
					)
				}
				
				Text(
					text = stringResource(id = R.string.new_category),
					style = Typography.bodyLarge.copy(
						fontWeight = FontWeight.Medium,
						fontSize = Typography.bodyLarge.fontSize.spScaled
					),
					modifier = Modifier
						.align(Alignment.Center)
				)
				
				IconButton(
					onClick = {
						// TODO: Insert new category
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
						scope.launch {
							sheetState.hide()
						}
					}
				),
				placeholder = {
					Text(stringResource(id = R.string.new_category))
				},
				modifier = Modifier
					.padding(
						top = 8.dpScaled,
						end = 16.dpScaled,
						start = 16.dpScaled,
						bottom = 16.dpScaled
					)
					.fillMaxWidth()
					.imePadding()
					.focusRequester(categoryNameFocusRequester)
			)
		}
	) {
		Box(
			modifier = Modifier
				.fillMaxSize()
				.background(MaterialTheme.colorScheme.background)
				.systemBarsPadding()
		) {
			FloatingActionButton(
				onClick = {
					scope.launch {
						sheetState.show()
					}
				},
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
				
				items(
					items = categories,
					key = { item: Category -> item.id }
				) { category ->
					SwipeableCategory(
						category = category,
						onCanDelete = {},
						onDismissToEnd = {},
						modifier = Modifier
							.padding(
								vertical = 4.dpScaled,
								horizontal = 8.dpScaled
							)
					)
				}
			}
		}
	}
}
