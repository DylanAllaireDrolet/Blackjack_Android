package com.example.blackjack

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.assertIsDisplayed
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.blackjack.model.CardStatistic
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class StatisticsScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun testStatisticsScreenDisplaysTitleAndRemainingCards() {
        val cards = mapOf(
            CardStatistic("ACE", 4, 0) to 2.5f,
            CardStatistic("10", 4, 1) to 3.0f
        )

        composeTestRule.setContent {
            StatisticsScreen(cards = cards, remainingCards = 360)
        }

        composeTestRule.onNodeWithText("Card Probabilities")
            .assertIsDisplayed()

        composeTestRule.onNodeWithText("Remaining cards: 360 / 364")
            .assertIsDisplayed()
    }
}
