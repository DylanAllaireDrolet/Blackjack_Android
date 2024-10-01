package com.example.blackjack

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import com.example.blackjack.model.Card
import coil.compose.rememberAsyncImagePainter

@Composable
fun GameScreen(
    playerCards: List<Card>,
    dealerCards: List<Card>,
    onDrawCard: () -> Unit,
    onStop: () -> Unit
) {
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
            CardRow(cards = dealerCards, isDealer = true)
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
                Button(onClick = onDrawCard) {
                    Text("Draw Card")
                }
                Button(onClick = onStop) {
                    Text("Stop")
                }
            }
        }
    }
}

@Composable
fun CardRow(cards: List<Card>, isDealer: Boolean) {
    LazyRow(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(cards.size) { index ->
            val cardImageUrl = if (isDealer && index == 0) {
                // Cache la première carte du croupier
                "URL_DE_L'ARRET_DE_LA_CARTE"
            } else {
                cards[index].image
            }
            ImageCard(imageUrl = cardImageUrl)
        }
    }
}

@Composable
fun ImageCard(imageUrl: String) {
    Image(
        painter = rememberAsyncImagePainter(imageUrl),
        contentDescription = null,
        contentScale = ContentScale.FillBounds,
        modifier = Modifier
            .size(100.dp)
            .padding(4.dp)
    )
}