package com.example.blackjack.api

import retrofit2.http.GET
import retrofit2.http.Path
import com.example.blackjack.model.DeckResponse
import com.example.blackjack.model.CardDrawResponse

interface BlackjackAPI {

    // Créer un nouveau deck
    @GET("deck/new/{deck_count}")
    suspend fun createNewDeck(
        @Path("deck_count") deckCount: Int
    ): DeckResponse

    // Tirer un certain nombre de cartes d'un deck spécifique
    @GET("deck/{deck_id}/draw/{count}")
    suspend fun drawCards(
        @Path("deck_id") deckId: String,
        @Path("count") count: Int
    ): CardDrawResponse
}
