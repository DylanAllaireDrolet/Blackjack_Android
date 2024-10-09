package com.example.blackjack

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.ImageLoader
import com.example.blackjack.model.Card
import coil.compose.rememberAsyncImagePainter
import coil.decode.SvgDecoder
import com.example.blackjack.viewmodel.GameStatus
import com.example.blackjack.viewmodel.GameViewModel

@Composable
fun GameScreen(gameViewModel: GameViewModel, navController: NavController) {
    val playerCards by gameViewModel.playerCards.collectAsState()
    val dealerCards by gameViewModel.dealerCards.collectAsState()
    val gameStatus by gameViewModel.gameStatus.collectAsState()
    val dealerRevealed by gameViewModel.dealerCardRevealed.collectAsState()

    var showMessage by remember { mutableStateOf(false) }
    var messageText by remember { mutableStateOf("") }
    var messageColor by remember { mutableStateOf(Color.Gray) } // Couleur par défaut

    val balance by gameViewModel.currentBalance.collectAsState()
    val currentBet by gameViewModel.currentBet.collectAsState()

    // Gestion du chargement des images
    var loadedImages by remember { mutableStateOf(0) }
    val totalImages = playerCards.size + dealerCards.size
    val allImagesLoaded = totalImages in 1..loadedImages

    LaunchedEffect(gameStatus) {
        when (gameStatus) {
            GameStatus.WON -> {
                messageText = "You won!"
                messageColor = Color.Green
                showMessage = true
            }
            GameStatus.LOST -> {
                messageText = "You lost!"
                messageColor = Color.Red
                showMessage = true
            }
            GameStatus.DRAW -> {
                messageText = "It's a draw!"
                messageColor = Color.Gray
                showMessage = true
            }
            GameStatus.IN_PROGRESS -> {
                showMessage = false
            }
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            item {
                Text("Dealer's Cards", style = MaterialTheme.typography.headlineSmall)
                Text("Total: ${gameViewModel.calculateTotal(dealerCards, dealerRevealed)}")
                CardGrid(cards = dealerCards, isDealer = true, isRevealed = dealerRevealed, onImageLoaded = { loadedImages++ })
            }

            item {
                Text("Your Cards", style = MaterialTheme.typography.headlineSmall)
                Text("Total: ${gameViewModel.calculateTotal(playerCards)}")
                CardGrid(cards = playerCards, isDealer = false, onImageLoaded = { loadedImages++ })
            }

            item {
                Row(
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Button(
                        onClick = { gameViewModel.drawCardButtonPressed() },
                        enabled = gameStatus == GameStatus.IN_PROGRESS && allImagesLoaded
                    ) {
                        Text("Draw Card")
                    }
                    Button(
                        onClick = { gameViewModel.stopGame() },
                        enabled = gameStatus == GameStatus.IN_PROGRESS && allImagesLoaded
                    ) {
                        Text("Stop")
                    }
                }
            }
        }

        if (showMessage) {
            Box(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .padding(16.dp)
                    .background(messageColor.copy(alpha = 0.8f))
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = messageText,
                        color = MaterialTheme.colorScheme.onPrimary,
                        style = MaterialTheme.typography.bodyLarge,
                        fontSize = 20.sp
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        Button(onClick = {
                            showMessage = false
                            navController.navigate("bet")
                        }) {
                            Text("Close")
                        }

                        if (balance >= currentBet) {
                            Button(onClick = {
                                showMessage = false
                                gameViewModel.startGame()
                            }) {
                                Text("Replay")
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun CardGrid(cards: List<Card>, isDealer: Boolean, isRevealed: Boolean = false, onImageLoaded: () -> Unit) {
    val baseUrl = "https://420C56.drynish.synology.me"
    val rows = cards.chunked(3)

    Column(modifier = Modifier.fillMaxWidth()) {
        rows.forEach { rowCards ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                rowCards.forEachIndexed { index, card ->
                    val cardImageUrl = if (isDealer && index == 0 && !isRevealed) {
                        "$baseUrl/static/back.svg"
                    } else {
                        "$baseUrl${card.image}"
                    }
                    ImageCard(imageUrl = cardImageUrl, onImageLoaded = onImageLoaded)
                }
            }
        }
    }
}

@Composable
fun ImageCard(imageUrl: String, onImageLoaded: () -> Unit) {
    val imageLoader = ImageLoader.Builder(LocalContext.current)
        .components {
            add(SvgDecoder.Factory())
        }
        .build()

    Image(
        painter = rememberAsyncImagePainter(
            model = imageUrl,
            imageLoader = imageLoader,
            onSuccess = { onImageLoaded() }
        ),
        contentDescription = null,
        contentScale = ContentScale.FillBounds,
        modifier = Modifier
            .size(130.dp)
            .padding(4.dp)
    )
}
