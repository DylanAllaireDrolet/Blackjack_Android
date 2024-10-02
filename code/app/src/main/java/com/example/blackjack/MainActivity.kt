package com.example.blackjack

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
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
        // Utilise les viewModels pour que leur cycle de vie soit lié à l'activité
        val viewModel: GameViewModel by viewModels()
        val statViewModel: StatsViewModel by viewModels()

        val navController = rememberNavController()

        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Blackjack") },
                    actions = {
                        IconButton(onClick = { navController.navigate("statistics") }) {
                            Icon(
                                Icons.Default.Menu, contentDescription = "Statistics"
                            )
                        }
                    }
                )
            }
        ) { innerPadding ->
            NavHost(
                navController = navController,
                startDestination = "bet",
                modifier = Modifier.padding(innerPadding)
            ) {
                composable("bet") {
                    BetScreen(
                        currentBet = viewModel.currentBet.collectAsState().value,
                        onBetPlaced = { bet ->
                            viewModel.setBet(bet)
                            navController.navigate("game")
                        }
                    )
                }

                composable("game") {
                    GameScreen(
                        playerCards = viewModel.playerCards.collectAsState().value,
                        dealerCards = viewModel.dealerCards.collectAsState().value,
                        onDrawCard = { viewModel.drawCardForPlayer() },
                        onStop = {
                            viewModel.stopGame()
                            navController.navigate("statistics")
                        }
                    )
                }

                composable("statistics") {
                    StatisticsScreen(
                        probabilities = statViewModel.cardProbabilities.collectAsState().value
                    )
                }

                composable("split") {
                    SplitScreen(
                        handOne = viewModel.splitHandOne.collectAsState().value,
                        handTwo = viewModel.splitHandTwo.collectAsState().value,
                        onDrawCardHandOne = { viewModel.drawCardForSplitHandOne() },
                        onDrawCardHandTwo = { viewModel.drawCardForSplitHandTwo() },
                        onStopHandOne = { viewModel.stopSplitHandOne() },
                        onStopHandTwo = { viewModel.stopSplitHandTwo() }
                    )
                }
            }
        }
    }
}
