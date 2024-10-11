package com.example.blackjack.api

import com.example.blackjack.model.DeckStats
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Header

interface FirebaseDB {
    @PUT("BlackJack/decks/{deckId}.json")
    suspend fun updateDeckStats(
        @Path("deckId") deckId: String,
        @Body deckStats: DeckStats,
        @Header("Authorization") apiKey: String
    ): Response<Void>

    @GET("BlackJack/decks/{deckId}.json")
    suspend fun getDeckStats(
        @Path("deckId") deckId: String,
        @Header("Authorization") apiKey: String
    ): Response<DeckStats>
}
