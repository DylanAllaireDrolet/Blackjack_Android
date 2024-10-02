package com.example.blackjack.repository

class StatisticsRepository {

    // Méthode pour calculer les probabilités des cartes
    fun getCardProbabilities(): Map<String, Float> {
        // Logique pour calculer les probabilités des cartes restantes
        return mapOf(
            "2" to 0.0769f,
            "3" to 0.0769f,
            // ...
        )
    }
}
