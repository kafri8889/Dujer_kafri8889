package com.anafthdev.dujer

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performTouchInput
import androidx.compose.ui.test.swipeRight
import androidx.navigation.compose.rememberNavController
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.anafthdev.dujer.ui.app.viewmodel.FakeDujerViewModel
import com.anafthdev.dujer.ui.screen.dashboard.DashboardScreen
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@HiltAndroidTest
class ExampleInstrumentedTest {
	
	@get:Rule(order = 1)
	val hiltRule = HiltAndroidRule(this)
	
	@get:Rule(order = 2)
	val composeTestRule = createComposeRule()
	
	@Before
	fun setUp() {
		hiltRule.inject()
	}
	
	@Test
	fun swipe_to_delete_financial_card() {
		composeTestRule.setContent {
			DashboardScreen(
				navController = rememberNavController(),
				dujerViewModel = FakeDujerViewModel()
			)
		}
		
		composeTestRule.onNodeWithTag("SwipeableFinancialCard")
			.assertExists()
			.performTouchInput {
				swipeRight(
					durationMillis = 400
				)
			}
	}
	
}