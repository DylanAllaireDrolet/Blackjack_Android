package com.example.blackjack

import androidx.compose.runtime.Composable
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.assertIsDisplayed
import androidx.navigation.compose.rememberNavController
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.example.blackjack.GameScreen
import com.example.blackjack.viewmodel.GameViewModel
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class GameScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun testGameScreenDisplaysButtonsAndScore() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        val gameViewModel = GameViewModel(context)

        composeTestRule.setContent {
            val navController = rememberNavController()
            GameScreen(gameViewModel = gameViewModel, navController = navController)
        }

        composeTestRule.onNodeWithText("Dealer's Cards")
            .assertIsDisplayed()
        composeTestRule.onNodeWithText("Your Cards")
            .assertIsDisplayed()
        composeTestRule.onNodeWithText("Draw Card")
            .assertIsDisplayed()
        composeTestRule.onNodeWithText("Stop")
            .assertIsDisplayed()
    }
}