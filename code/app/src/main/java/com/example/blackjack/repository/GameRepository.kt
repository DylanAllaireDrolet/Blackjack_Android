package com.example.blackjack.repository

import com.example.blackjack.model.Card

class GameRepository {

    // Simuler l'appel à une API pour tirer les cartes initiales
    fun drawInitialCards(): List<Card> {
        return listOf(
            Card("2H", "https://example.com/2H.png", "2", "HEARTS"),
            Card("5C", "https://example.com/5C.png", "5", "CLUBS")
        )
    }

    fun drawInitialDealerCards(): List<Card> {
        return listOf(
            Card("KH", "https://example.com/KH.png", "KING", "HEARTS"),
            Card("??", "https://example.com/back.png", "BACK", "UNKNOWN")
        )
    }

    // Simuler l'appel pour tirer une carte supplémentaire
    fun drawCard(): Card {
        return Card("8D", "https://example.com/8D.png", "8", "DIAMONDS")
    }
}
