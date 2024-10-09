package com.example.blackjack

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BetScreen(
    currentBet: Float,
    onBetPlaced: (Float) -> Unit,
    balance: Float
) {
    var betAmount by remember { mutableStateOf(currentBet) }
    var betAmountText by remember { mutableStateOf(currentBet.toString()) }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        item {
            Text(text = "Place Your Bet", style = MaterialTheme.typography.headlineSmall)
        }

        item {
            Spacer(modifier = Modifier.height(16.dp))
        }

        item {
            TextField(
                value = betAmountText,
                onValueChange = { text ->
                    betAmountText = text
                    betAmount = text.toFloatOrNull() ?: 0f
                },
                label = { Text("Bet Amount") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth()
            )
        }

        item {
            Spacer(modifier = Modifier.height(16.dp))
        }

        item {
            if (betAmount > balance) {
                Text("Insufficient balance", style = MaterialTheme.typography.bodyMedium)
            }
            else {
                Button(
                    onClick = { onBetPlaced(betAmount) },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Start Game")
                }
            }

        }
    }
}