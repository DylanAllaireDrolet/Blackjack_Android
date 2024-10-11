package com.example.blackjack.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.blackjack.model.CardStatistic
import com.example.blackjack.repository.StatisticsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class StatsViewModel(context: Context) : ViewModel() {

    private val statisticsRepository = StatisticsRepository(context)
    private val _cardProbabilities = MutableStateFlow<Map<CardStatistic, Float>>(emptyMap())
    val cardProbabilities: StateFlow<Map<CardStatistic, Float>> = _cardProbabilities
    private val _remainingCards: MutableStateFlow<Int> = MutableStateFlow(0)
    val remainingCards: StateFlow<Int> = _remainingCards

    init {
        calculateProbabilities()
    }

    fun calculateProbabilities() {
        viewModelScope.launch {
            _remainingCards.value = statisticsRepository.getRemainingCards()
            _cardProbabilities.value = statisticsRepository.getCardProbabilities()
        }
    }
}
