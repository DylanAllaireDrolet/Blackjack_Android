package com.example.blackjack.repository

import com.example.blackjack.api.BlackjackAPI
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import com.example.blackjack.model.DeckResponse
import com.example.blackjack.model.CardDrawResponse

class GameRepository {

    private val api: BlackjackAPI
    init {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://420C56.drynish.synology.me/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        api = retrofit.create(BlackjackAPI::class.java)
    }

    suspend fun createNewDeck(deckCount : Int) : DeckResponse {

        return api.createNewDeck(deckCount)
    }

    suspend fun drawCards(deckId : String, count : Int) : CardDrawResponse {
        return api.drawCards(deckId, count)
    }
}
