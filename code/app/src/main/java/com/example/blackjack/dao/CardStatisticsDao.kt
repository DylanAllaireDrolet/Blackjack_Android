package com.example.blackjack.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.blackjack.model.CardStatistic

@Dao
interface CardStatisticsDao {

    @Insert
    suspend fun insertCardStatistic(cardStatistic: CardStatistic)

    @Query("SELECT * FROM card_statistics")
    suspend fun getAllCardStatistics(): List<CardStatistic>

    @Query("UPDATE card_statistics SET drawn = drawn + :count WHERE cardValue = :cardValue")
    suspend fun updateDrawnCards(cardValue: String, count: Int)

    @Query("UPDATE card_statistics SET remaining = remaining - :count WHERE cardValue = :cardValue")
    suspend fun updateRemainingCards(cardValue: String, count: Int)

    @Query("DELETE FROM card_statistics")
    suspend fun resetStatistics()

}
