package com.example.blackjack.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.blackjack.repository.StatisticsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class StatsViewModel(context: Context) : ViewModel() {

    private val statisticsRepository = StatisticsRepository(context)
    private val _cardProbabilities = MutableStateFlow<Map<String, Float>>(emptyMap())
    val cardProbabilities: StateFlow<Map<String, Float>> = _cardProbabilities

    init {
        calculateProbabilities()
    }

    fun calculateProbabilities() {
        viewModelScope.launch {
            val probabilities = statisticsRepository.getCardProbabilities()
            _cardProbabilities.value = probabilities
        }
    }
}
