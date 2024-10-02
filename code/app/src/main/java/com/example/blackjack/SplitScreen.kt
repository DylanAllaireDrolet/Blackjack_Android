package com.example.blackjack


import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.example.blackjack.model.Card


@Composable
fun SplitScreen(
    handOne: List<Card>,
    handTwo: List<Card>,
    onDrawCardHandOne: () -> Unit,
    onDrawCardHandTwo: () -> Unit,
    onStopHandOne: () -> Unit,
    onStopHandTwo: () -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Section pour la première main
        item {
            Text("Hand 1", style = MaterialTheme.typography.headlineSmall)
        }

        // Afficher les cartes de la première main
        item {
            CardRow(cards = handOne)
        }

        // Boutons d'action pour la première main
        item {
            Row(
                horizontalArrangement = Arrangement.SpaceEvenly,
                modifier = Modifier.fillMaxWidth()
            ) {
                Button(onClick = onDrawCardHandOne) {
                    Text("Draw Card")
                }
                Button(onClick = onStopHandOne) {
                    Text("Stop")
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(16.dp))
        }

        // Section pour la deuxième main
        item {
            Text("Hand 2", style = MaterialTheme.typography.headlineSmall)
        }

        // Afficher les cartes de la deuxième main
        item {
            CardRow(cards = handTwo)
        }

        // Boutons d'action pour la deuxième main
        item {
            Row(
                horizontalArrangement = Arrangement.SpaceEvenly,
                modifier = Modifier.fillMaxWidth()
            ) {
                Button(onClick = onDrawCardHandTwo) {
                    Text("Draw Card")
                }
                Button(onClick = onStopHandTwo) {
                    Text("Stop")
                }
            }
        }
    }
}

@Composable
fun CardRow(cards: List<Card>) {
    LazyRow(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(cards.size) { index ->
            ImageCardSplit(imageUrl = cards[index].image)
        }
    }
}

@Composable
fun ImageCardSplit(imageUrl: String) {
    Image(
        painter = rememberAsyncImagePainter(model = imageUrl),
        contentDescription = null,
        modifier = Modifier
            .size(100.dp)
            .padding(4.dp)
    )
}
