package com.anafthdev.dujer.ui.category

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.zIndex
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.anafthdev.dujer.R
import com.anafthdev.dujer.data.db.model.Category
import com.anafthdev.dujer.foundation.window.dpScaled
import com.anafthdev.dujer.foundation.window.spScaled
import com.anafthdev.dujer.ui.category.component.SwipeableCategory
import com.anafthdev.dujer.uicomponent.TopAppBar
import com.anafthdev.dujer.ui.theme.Typography
import com.anafthdev.dujer.ui.theme.black04

@Composable
fun CategoryScreen(
	navController: NavController
) {
	
	val categoryViewModel = hiltViewModel<CategoryViewModel>()
	
	val categories by categoryViewModel.categories.observeAsState(initial = emptyList())
	
	Box(
		modifier = Modifier
			.fillMaxSize()
			.background(MaterialTheme.colorScheme.background)
	) {
		FloatingActionButton(
			onClick = {
			
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
