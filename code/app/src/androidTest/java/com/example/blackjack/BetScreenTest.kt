package com.example.blackjack

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performTextInput
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class BetScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun testBetScreenDisplaysTitleAndBetAmountField() {
        composeTestRule.setContent {
            BetScreen(currentBet = 0f, onBetPlaced = {}, balance = 1000f)
        }

        composeTestRule.onNodeWithText("Place Your Bet")
            .assertIsDisplayed()

        composeTestRule.onNodeWithText("Bet Amount")
            .performTextInput("50")

        composeTestRule.onNodeWithText("Start Game")
            .assertIsDisplayed()
            .performClick()
    }
}
