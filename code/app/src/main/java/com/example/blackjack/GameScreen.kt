package com.example.blackjack

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.ImageLoader
import com.example.blackjack.model.Card
import coil.compose.rememberAsyncImagePainter
import coil.decode.SvgDecoder
import com.example.blackjack.viewmodel.GameStatus
import com.example.blackjack.viewmodel.GameViewModel
import kotlinx.coroutines.flow.StateFlow

@Composable
fun GameScreen(gameViewModel: GameViewModel, navController: NavController) {

    val playerCards by gameViewModel.playerCards.collectAsState()
    val dealerCards by gameViewModel.dealerCards.collectAsState()
    val gameStatus by gameViewModel.gameStatus.collectAsState()

    var showDialog by remember { mutableStateOf(false) }
    var dialogMessage by remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        if (playerCards.isEmpty() && dealerCards.isEmpty()) {
            gameViewModel.startGame()
        }
    }

    LaunchedEffect(gameStatus) {
        when (gameStatus) {
            GameStatus.WON -> {
                dialogMessage = "You won!"
                showDialog = true
            }
            GameStatus.LOST -> {
                dialogMessage = "You lost!"
                showDialog = true
            }
            GameStatus.DRAW -> {
                dialogMessage = "It's a draw!"
                showDialog = true
            }
            GameStatus.IN_PROGRESS -> {
                showDialog = false
            }
        }
    }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { },
            title = { Text(text = "Résultat de la partie") },
            text = { Text(text = dialogMessage) },
            confirmButton = {
                Button(onClick = {
                    showDialog = false
                    navController.navigate("bet")
                }) {
                    Text("Fermer")
                }
            }
        )
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Afficher les cartes du croupier
        item {
            Text("Dealer's Cards", style = MaterialTheme.typography.headlineSmall)
        }

        item {
            CardRow(cards = dealerCards, isDealer = true, gameViewModel.dealerCardRevealed.collectAsState().value)
        }

        // Afficher les cartes du joueur
        item {
            Text("Your Cards", style = MaterialTheme.typography.headlineSmall)
        }

        item {
            CardRow(cards = playerCards, isDealer = false)
        }

        // Boutons pour les actions
        item {
            Row(
                horizontalArrangement = Arrangement.SpaceEvenly,
                modifier = Modifier.fillMaxWidth()
            ) {
                Button(onClick = {
                    gameViewModel.drawCardForPlayer()
                }) {
                    Text("Draw Card")
                }
                Button(onClick = { gameViewModel.stopGame() }) {
                    Text("Stop")
                }
            }
        }
    }
}

@Composable
fun CardRow(cards: List<Card>, isDealer: Boolean, isRevealed : Boolean = false) {
    LazyRow(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        val baseUrl = "https://420C56.drynish.synology.me" //
        items(cards.size) { index ->
            val cardImageUrl = if (isDealer && index == 0) {
                if (isRevealed)
                    "$baseUrl${cards[index].image}"
                else
                    "$baseUrl/static/back.svg"
            } else {
                "$baseUrl${cards[index].image}"
            }
            ImageCard(imageUrl = cardImageUrl)
        }
    }
}

@Composable
fun ImageCard(imageUrl: String) {
    val imageLoader = ImageLoader.Builder(LocalContext.current)
        .components {
            add(SvgDecoder.Factory())
        }
        .build()

    Image(
        painter = rememberAsyncImagePainter(
            model = imageUrl,
            imageLoader = imageLoader
        ),
        contentDescription = null,
        contentScale = ContentScale.FillBounds,
        modifier = Modifier
            .size(130.dp)
            .padding(4.dp)
    )
}
