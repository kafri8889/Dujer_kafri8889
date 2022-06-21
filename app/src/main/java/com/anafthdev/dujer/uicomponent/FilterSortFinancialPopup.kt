package com.anafthdev.dujer.uicomponent

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import com.anafthdev.dujer.R
import com.anafthdev.dujer.data.SortType
import com.anafthdev.dujer.foundation.common.TextFieldDateFormatter
import com.anafthdev.dujer.foundation.common.showDatePicker
import com.anafthdev.dujer.foundation.extension.gridItems
import com.anafthdev.dujer.foundation.extension.isLightTheme
import com.anafthdev.dujer.foundation.ui.LocalUiColor
import com.anafthdev.dujer.foundation.uimode.data.LocalUiMode
import com.anafthdev.dujer.foundation.window.dpScaled
import com.anafthdev.dujer.foundation.window.spScaled
import com.anafthdev.dujer.ui.MainActivity
import com.anafthdev.dujer.ui.theme.medium_shape
import com.anafthdev.dujer.util.AppUtil
import timber.log.Timber
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FilterSortFinancialPopup(
	isVisible: Boolean,
	sortType: SortType,
	filterDate: Pair<Long, Long>,
	monthsSelected: List<Int>,
	modifier: Modifier = Modifier,
	onClose: () -> Unit,
	onApply: (monthsSelected: List<Int>, sortBy: SortType, filterDate: Pair<Long, Long>?) -> Unit,
	onClickOutside: () -> Unit
) {
	val activity = (LocalContext.current as MainActivity)
	
	val sortSelection = listOf(
		SortType.A_TO_Z to stringResource(id = R.string.a_to_z),
		SortType.Z_TO_A to stringResource(id = R.string.z_to_a),
		SortType.LATEST to stringResource(id = R.string.latest),
		SortType.LONGEST to stringResource(id = R.string.longest),
		SortType.LOWEST_AMOUNT to stringResource(id = R.string.lowest_amount),
		SortType.HIGHEST_AMOUNT to stringResource(id = R.string.highest_amount),
	)
	
	val uiMode = LocalUiMode.current
	
	val calendar = remember { Calendar.getInstance() }
	val months = remember { AppUtil.shortMonths }
	
	var isFilterDateValid by rememberSaveable { mutableStateOf(false) }
	
	var startDate by remember {
		mutableStateOf(
			TextFieldValue(
				text = "dd-MM-yyyy"
			)
		)
	}
	Timber.i("star det: ${startDate.text}")
	
	var endDate by remember {
		mutableStateOf(
			TextFieldValue(
				text = "dd-MM-yyyy"
			)
		)
	}
	
	val startDateLong = remember(startDate) {
		if (TextFieldDateFormatter.isValid(startDate.text)) {
			TextFieldDateFormatter.parse(startDate.text)
		} else null
	}
	
	val endDateLong = remember(endDate) {
		if (TextFieldDateFormatter.isValid(endDate.text)) {
			TextFieldDateFormatter.parse(endDate.text)
		} else null
	}
	
	val selectedFilterDate = remember(startDate, endDate) {
		val date = if (startDateLong == null || endDateLong == null) null
		else startDateLong to endDateLong
		
		isFilterDateValid = date != null
		
		onApply(
			monthsSelected,
			sortType,
			date
		)
		
		date
	}
	
	LaunchedEffect(filterDate) {
		startDate = startDate.copy(
			text = TextFieldDateFormatter.format(filterDate.first)
		)
		
		endDate = endDate.copy(
			text = TextFieldDateFormatter.format(filterDate.second)
		)
		Timber.i("filter det: ${startDate.text}")
	}
	
	BackHandler(isVisible) {
		onClose()
	}
	
	Column(
		horizontalAlignment = Alignment.CenterHorizontally,
		verticalArrangement = Arrangement.Center,
		modifier = Modifier
			.fillMaxSize()
			.background(
				if (uiMode.isLightTheme()) Color.Black.copy(alpha = 0.24f)
				else Color.White.copy(alpha = 0.24f)
			)
			.clickable(
				enabled = true,
				interactionSource = MutableInteractionSource(),
				indication = null,
				onClick = {
					onClickOutside()
				}
			)
	) {
		Box(
			modifier = modifier
				.padding(horizontal = 24.dpScaled)
				.fillMaxWidth()
				.clip(medium_shape)
				.background(MaterialTheme.colorScheme.background)
				.clickable(
					enabled = true,
					interactionSource = MutableInteractionSource(),
					indication = null,
					onClick = {}
				)
		) {
			LazyColumn(
				modifier = Modifier
					.padding(bottom = ButtonDefaults.MinHeight + 8.dpScaled)
			) {
				item {
					Column(
						modifier = Modifier
							.fillMaxWidth()
					) {
						Text(
							text = stringResource(id = R.string.filter),
							style = MaterialTheme.typography.titleMedium.copy(
								color = LocalUiColor.current.normalText,
								fontWeight = FontWeight.SemiBold,
								fontSize = MaterialTheme.typography.titleMedium.fontSize.spScaled
							),
							modifier = Modifier
								.padding(
									top = 16.dpScaled,
									start = 16.dpScaled
								)
						)
						
						Text(
							text = stringResource(id = R.string.start),
							style = MaterialTheme.typography.titleSmall.copy(
								color = LocalUiColor.current.normalText,
								fontWeight = FontWeight.Medium,
								fontSize = MaterialTheme.typography.titleSmall.fontSize.spScaled
							),
							modifier = Modifier
								.padding(
									top = 16.dpScaled,
									start = 24.dpScaled
								)
						)
						
						DateTextField(
							value = startDate,
							onValueChange = {
								startDate = it
							},
							trailingIcon = {
								IconButton(
									onClick = {
										activity.showDatePicker(
											min = calendar.apply {
												set(Calendar.YEAR, 2000)
											}.timeInMillis
										) { timeInMillis ->
											startDate = startDate.copy(
												text = TextFieldDateFormatter.format(timeInMillis)
											)
										}
									}
								) {
									Icon(
										painter = painterResource(id = R.drawable.ic_calendar),
										contentDescription = null
									)
								}
							},
							modifier = Modifier
								.padding(
									top = 8.dpScaled,
									start = 24.dpScaled
								)
						)
						
						Text(
							text = stringResource(id = R.string.end),
							style = MaterialTheme.typography.titleSmall.copy(
								color = LocalUiColor.current.normalText,
								fontWeight = FontWeight.Medium,
								fontSize = MaterialTheme.typography.titleSmall.fontSize.spScaled
							),
							modifier = Modifier
								.padding(
									top = 16.dpScaled,
									start = 24.dpScaled
								)
						)
						
						DateTextField(
							value = endDate,
							onValueChange = {
								endDate = it
							},
							trailingIcon = {
								IconButton(
									onClick = {
										activity.showDatePicker(
											min = calendar.apply {
												set(Calendar.YEAR, 2000)
											}.timeInMillis
										) { timeInMillis ->
											endDate = endDate.copy(
												text = TextFieldDateFormatter.format(timeInMillis)
											)
										}
									}
								) {
									Icon(
										painter = painterResource(id = R.drawable.ic_calendar),
										contentDescription = null
									)
								}
							},
							modifier = Modifier
								.padding(
									top = 8.dpScaled,
									start = 24.dpScaled
								)
						)
						
						AnimatedVisibility(
							visible = !isFilterDateValid,
							modifier = Modifier
								.padding(
									vertical = 8.dpScaled,
									horizontal = 24.dpScaled
								)
						) {
							Text(
								text = stringResource(id = R.string.invalid_format),
								style = MaterialTheme.typography.titleSmall.copy(
									color = MaterialTheme.colorScheme.error,
									fontWeight = FontWeight.Medium,
									fontSize = MaterialTheme.typography.titleSmall.fontSize.spScaled
								)
							)
						}
					}
				}
				
				gridItems(
					items = months,
					nColumns = 2
				) { month ->
					val monthIndex = remember {
						when (month.uppercase()) {
							months[0].uppercase() -> 0
							months[1].uppercase() -> 1
							months[2].uppercase() -> 2
							months[3].uppercase() -> 3
							months[4].uppercase() -> 4
							months[5].uppercase() -> 5
							months[6].uppercase() -> 6
							months[7].uppercase() -> 7
							months[8].uppercase() -> 8
							months[9].uppercase() -> 9
							months[10].uppercase() -> 10
							months[11].uppercase() -> 11
							else -> -1
						}
					}
					
					Row(
						verticalAlignment = Alignment.CenterVertically,
						modifier = Modifier
							.fillMaxWidth()
							.clickable {
								val isChecked = monthIndex !in monthsSelected
								
								onApply(
									monthsSelected
										.toMutableList()
										.apply {
											if (isChecked) add(monthIndex)
											else remove(monthIndex)
										},
									sortType,
									selectedFilterDate
								)
							}
							.padding(
								horizontal = 16.dpScaled
							)
					) {
						Checkbox(
							checked = monthIndex in monthsSelected,
							onCheckedChange = { isChecked ->
								onApply(
									monthsSelected.toMutableList().apply {
										if (isChecked) add(monthIndex)
										else remove(monthIndex)
									},
									sortType,
									selectedFilterDate
								)
							}
						)
						
						Text(
							text = month,
							style = MaterialTheme.typography.titleSmall.copy(
								color = LocalUiColor.current.normalText,
								fontSize = MaterialTheme.typography.titleSmall.fontSize.spScaled
							),
							modifier = Modifier
								.padding(start = 8.dpScaled)
						)
					}
				}

				item {
					Text(
						text = stringResource(id = R.string.sort),
						style = MaterialTheme.typography.titleMedium.copy(
							color = LocalUiColor.current.titleText,
							fontWeight = FontWeight.SemiBold,
							fontSize = MaterialTheme.typography.titleMedium.fontSize.spScaled
						),
						modifier = Modifier
							.padding(
								top = 16.dpScaled,
								start = 16.dpScaled
							)
					)
				}

				items(
					items = sortSelection
				) { selection ->
					Row(
						verticalAlignment = Alignment.CenterVertically,
						modifier = Modifier
							.fillMaxWidth()
							.clickable {
								onApply(
									monthsSelected,
									selection.first,
									selectedFilterDate
								)
							}
							.padding(
								horizontal = 16.dpScaled
							)
					) {
						RadioButton(
							selected = sortType == selection.first,
							onClick = {
								onApply(
									monthsSelected,
									selection.first,
									selectedFilterDate
								)
							}
						)

						Text(
							text = selection.second,
							style = MaterialTheme.typography.titleSmall.copy(
								color = LocalUiColor.current.normalText,
								fontSize = MaterialTheme.typography.titleSmall.fontSize.spScaled
							),
							modifier = Modifier
								.padding(start = 8.dpScaled)
						)
					}
				}
			}
			
			Row(
				horizontalArrangement = Arrangement.End,
				modifier = Modifier
					.fillMaxWidth()
					.height(
						ButtonDefaults.MinHeight + 16.dpScaled
					)
					.align(Alignment.BottomCenter)
			) {
				Button(
					shape = MaterialTheme.shapes.medium,
					onClick = onClose,
					modifier = Modifier
						.padding(
							horizontal = 8.dpScaled
						)
				) {
					Text(
						text = stringResource(id = R.string.close),
						style = LocalTextStyle.current.copy(
							color = Color.White
						)
					)
				}
			}
		}
	}
}
