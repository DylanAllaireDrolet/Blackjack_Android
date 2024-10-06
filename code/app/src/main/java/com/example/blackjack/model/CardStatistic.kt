package com.example.blackjack.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "card_statistics")
data class CardStatistic(
    @PrimaryKey val cardValue: String,
    val remaining: Int,
    val drawn: Int
)