package com.example.blackjack.model

import com.google.gson.annotations.SerializedName

data class DeckResponse(
    @SerializedName("deck_id") val deckId: String,
    @SerializedName("remaining") val remaining: Int
)

data class CardDrawResponse(
    @SerializedName("deck_id") val deckId: String,
    @SerializedName("cards") val cards: List<Card>,
    @SerializedName("remaining") val remaining: Int
)
