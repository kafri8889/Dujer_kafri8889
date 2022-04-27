package com.anafthdev.dujer.ui.search

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.anafthdev.dujer.R
import com.anafthdev.dujer.data.DujerDestination
import com.anafthdev.dujer.data.db.model.Category
import com.anafthdev.dujer.data.db.model.Financial
import com.anafthdev.dujer.foundation.window.dpScaled
import com.anafthdev.dujer.foundation.window.spScaled
import com.anafthdev.dujer.ui.category.component.CategoryCard
import com.anafthdev.dujer.ui.category.data.CategorySwipeAction
import com.anafthdev.dujer.ui.theme.Inter
import com.anafthdev.dujer.ui.theme.Typography
import com.anafthdev.dujer.ui.theme.semi_large_shape
import com.anafthdev.dujer.ui.theme.shapes
import com.anafthdev.dujer.uicomponent.FinancialCard
import com.anafthdev.dujer.uicomponent.SwipeSearch
import com.anafthdev.dujer.uicomponent.SwipeSearchValue
import com.anafthdev.dujer.uicomponent.rememberSwipeSearchState
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun SearchScreen(
	navController: NavController,
	canBack: Boolean = true,
	content: @Composable () -> Unit,
	onFinancialClicked: (Financial) -> Unit
) {
	
	val focusManager = LocalFocusManager.current
	
	val searchViewModel = hiltViewModel<SearchViewModel>()
	
	var swipeSearchValue by remember { mutableStateOf(SwipeSearchValue.Closed) }
	
	val searchFocusRequester = remember { FocusRequester() }
	
	val swipeSearchState = rememberSwipeSearchState(swipeSearchValue)
	
	val searchState by searchViewModel.state.collectAsState()
	val searchTextQuery = searchState.textQuery
	val resultFinancial = searchState.resultFinancial
	val resultCategory = searchState.resultCategory
	
	val closeSearch = { swipeSearchValue = SwipeSearchValue.Closed }
	
	if (swipeSearchState.currentValue == SwipeSearchValue.Opened) {
		BackHandler(
			enabled = canBack,
			onBack = closeSearch
		)
	}
	
	LaunchedEffect(swipeSearchState.currentValue) {
		if (swipeSearchState.currentValue == SwipeSearchValue.Opened) {
			searchFocusRequester.requestFocus()
		} else {
			focusManager.clearFocus(force = true)
		}
	}
	
	SwipeSearch(
		state = swipeSearchState,
		onFling = {
			swipeSearchValue = it
		},
		onSearchAreaClick = closeSearch,
		content = content,
		searchSection = {
			SearchSection(
				value = searchTextQuery,
				focusRequester = searchFocusRequester,
				onTextChanged = { s ->
					searchViewModel.dispatch(
						SearchAction.Search(s)
					)
				},
				onCancel = closeSearch
			)
		},
		searchBody = {
			SearchBody(
				navController = navController,
				resultFinancial = resultFinancial,
				resultCategory = resultCategory,
				onFinancialClicked = onFinancialClicked
			)
		},
		modifier = Modifier
			.fillMaxSize()
			.background(MaterialTheme.colorScheme.background)
	)
}

@Composable
internal fun SearchSection(
	value: String,
	focusRequester: FocusRequester,
	onTextChanged: (String) -> Unit,
	onCancel: () -> Unit
) {
	
	val focusManager = LocalFocusManager.current
	
	Row(
		verticalAlignment = Alignment.CenterVertically,
		modifier = Modifier
			.fillMaxSize()
			.background(MaterialTheme.colorScheme.background)
			.padding(start = 16.dpScaled, end = 16.dpScaled)
			.statusBarsPadding()
	) {
		OutlinedTextField(
			shape = semi_large_shape,
			singleLine = true,
			value = value,
			onValueChange = onTextChanged,
			textStyle = LocalTextStyle.current.copy(
				fontFamily = Inter
			),
			leadingIcon = {
				Icon(
					imageVector = Icons.Rounded.Search,
					contentDescription = null
				)
			},
			keyboardOptions = KeyboardOptions(
				imeAction = ImeAction.Done
			),
			keyboardActions = KeyboardActions(
				onDone = {
					focusManager.clearFocus(force = true)
				}
			),
			modifier = Modifier
				.padding(
					vertical = 4.dpScaled
				)
				.weight(0.7f)
				.focusRequester(focusRequester)
		)
		
		TextButton(
			onClick = onCancel,
			shape = shapes.medium,
			modifier = Modifier
				.padding(
					start = 8.dpScaled
				)
				.weight(0.3f)
		) {
			Text(
				text = stringResource(id = R.string.cancel),
				style = Typography.bodyMedium.copy(
					fontWeight = FontWeight.Medium,
					fontSize = Typography.bodyMedium.fontSize.spScaled
				)
			)
		}
	}
}

@OptIn(ExperimentalAnimationApi::class, ExperimentalComposeUiApi::class)
@Composable
internal fun SearchBody(
	navController: NavController,
	resultFinancial: List<Financial>,
	resultCategory: List<Category>,
	onFinancialClicked: (Financial) -> Unit
) {
	
	val keyboardController = LocalSoftwareKeyboardController.current
	
	val scope = rememberCoroutineScope()
	
	Box(
		modifier = Modifier
			.fillMaxSize()
			.background(MaterialTheme.colorScheme.background)
	) {
		LazyColumn(
			modifier = Modifier
				.fillMaxSize()
		) {
			items(
				items = resultFinancial,
				key = { item: Financial -> item.id }
			) { financial ->
				FinancialCard(
					financial = financial,
					onClick = {
						keyboardController?.hide()
						onFinancialClicked(financial)
					},
					modifier = Modifier
						.padding(
							vertical = 4.dpScaled,
							horizontal = 8.dpScaled
						)
				)
			}
			
			item {
				if (resultFinancial.isNotEmpty()) {
					Divider(
						modifier = Modifier
							.fillMaxWidth()
							.padding(
								vertical = 12.dpScaled
							)
					)
				}
			}
			
			items(
				items = resultCategory,
				key = { item: Category -> item.id }
			) { category ->
				CategoryCard(
					category = category,
					modifier = Modifier
						.padding(
							vertical = 4.dpScaled,
							horizontal = 8.dpScaled
						),
					onClick = {
						scope.launch {
							keyboardController?.hide()
							delay(300)
							navController.navigate(
								DujerDestination.Category.createRoute(
									id = category.id,
									action = CategorySwipeAction.EDIT
								)
							)
						}
					}
				)
			}
		}
		
		AnimatedVisibility(
			visible = resultFinancial.isEmpty() and resultCategory.isEmpty(),
			enter = scaleIn(
				animationSpec = tween(
					durationMillis = 500
				)
			),
			exit = scaleOut(
				animationSpec = tween(
					durationMillis = 500
				)
			),
			modifier = Modifier
				.align(Alignment.Center)
		) {
			Text(
				text = stringResource(id = R.string.no_result),
				style = Typography.bodyLarge.copy(
					fontWeight = FontWeight.Normal,
					fontSize = Typography.bodyLarge.fontSize.spScaled
				)
			)
		}
	}
	
}
