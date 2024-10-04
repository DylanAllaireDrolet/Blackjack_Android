package com.example.blackjack

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.blackjack.ui.theme.BlackJackTheme
import com.example.blackjack.viewmodel.GameViewModel
import com.example.blackjack.viewmodel.StatsViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            BlackJackTheme {
                AppScreen()
            }
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun AppScreen() {
        val gameViewModel: GameViewModel by viewModels()
        val statViewModel: StatsViewModel by viewModels()
        val balance by gameViewModel.currentBalance.collectAsState()

        val navController = rememberNavController()

        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Blackjack") },
                    actions = {
                        IconButton(onClick = {
                            if (navController.currentDestination?.route == "statistics") {
                                navController.navigate("game")
                            }
                            else if (navController.currentDestination?.route != "bet") {
                                navController.navigate("statistics")
                            }
                        }) {
                            Icon(
                                Icons.Default.Menu, contentDescription = "Statistics"
                            )
                        }
                    }
                )
            },
            bottomBar = {
                // Display the player's balance in the bottom right
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    contentAlignment = Alignment.BottomEnd // Align content to the bottom end
                ) {
                    Text(text = "Balance: $balance", style = MaterialTheme.typography.bodyLarge)
                }
            }

        ) { innerPadding ->
            NavHost(
                navController = navController,
                startDestination = "bet",
                modifier = Modifier.padding(innerPadding)
            ) {
                composable("bet") {
                    BetScreen(
                        currentBet = gameViewModel.currentBet.collectAsState().value,
                        onBetPlaced = { bet ->
                            gameViewModel.setBet(bet)
                            gameViewModel.startGame()
                            navController.navigate("game")
                        }
                    )
                }

                composable("game") {
                    GameScreen(gameViewModel, navController)
                }

                composable("statistics") {
                    StatisticsScreen(
                        probabilities = statViewModel.cardProbabilities.collectAsState().value
                    )
                }

            }
        }
    }
}
