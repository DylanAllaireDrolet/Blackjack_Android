package com.example.blackjack.repository

import android.content.Context
import com.example.blackjack.db.AppDatabase
import com.example.blackjack.model.CardStatistic

class StatisticsRepository(context: Context) {

    private val cardStatisticsDao = AppDatabase.getDatabase(context).cardStatisticsDao

    suspend fun initializeStatistics() {
        val initialStatistics = listOf(
            CardStatistic(cardValue = "ACE", remaining = 28, drawn = 0),
            CardStatistic(cardValue = "2", remaining = 28, drawn = 0),
            CardStatistic(cardValue = "3", remaining = 28, drawn = 0),
            CardStatistic(cardValue = "4", remaining = 28, drawn = 0),
            CardStatistic(cardValue = "5", remaining = 28, drawn = 0),
            CardStatistic(cardValue = "6", remaining = 28, drawn = 0),
            CardStatistic(cardValue = "7", remaining = 28, drawn = 0),
            CardStatistic(cardValue = "8", remaining = 28, drawn = 0),
            CardStatistic(cardValue = "9", remaining = 28, drawn = 0),
            CardStatistic(cardValue = "10", remaining = 112, drawn = 0)
        )
        initialStatistics.forEach { stat ->
            cardStatisticsDao.insertCardStatistic(stat)
        }
    }

    suspend fun updateCardStatistics(cardValue: String, count: Int) {
        val rank = when (cardValue) {
            "1" -> "ACE"
            "11" -> "10"
            "12" -> "10"
            "13" -> "10"
            else -> cardValue
        }
        cardStatisticsDao.updateDrawnCards(rank, count)
        cardStatisticsDao.updateRemainingCards(rank, count)
    }

    suspend fun getCardProbabilities(): Map<CardStatistic, Float> {
        val cardStatistics = cardStatisticsDao.getAllCardStatistics()
        val totalRemainingCards = cardStatistics.sumOf { it.remaining }

        return cardStatistics.associate { stat ->
            stat to stat.remaining.toFloat() / totalRemainingCards * 100
        }
    }

    suspend fun getRemainingCards(): Int {
        return cardStatisticsDao.getAllCardStatistics().sumOf { it.remaining }
    }

    suspend fun resetStatistics() {
        cardStatisticsDao.resetStatistics()
        initializeStatistics()
    }
}
