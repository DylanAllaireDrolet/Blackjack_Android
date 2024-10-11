package com.example.blackjack.repository

import com.example.blackjack.api.FirebaseDB
import com.example.blackjack.model.DeckStats
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class FirebaseRepository {
    private val apiKey = "AIzaSyDXmUI3Ga3zizAMmw9zJWuLo4Kr8xztIaw"
    val retrofit: Retrofit = Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create())
        .baseUrl("https://blackjacktp1-default-rtdb.firebaseio.com/")
        .build()

    val restInterface = retrofit.create(FirebaseDB::class.java)

    suspend fun saveDeckStats(deckId: String, deckStats: DeckStats): Boolean {
        return try {
            val response = restInterface.updateDeckStats(deckId, deckStats, apiKey)
            response.isSuccessful
        } catch (e: Exception) {
            false
        }
    }
}
