package com.example.blackjack.model

data class DeckStats(
    var cardsDrawnByUser: Int,
    var stopCount: Int,
    var totalCards: Int,
    var remainingCards: Int,
    var hoursPlayed: Double
)
