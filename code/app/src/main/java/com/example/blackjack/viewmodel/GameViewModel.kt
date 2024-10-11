package com.example.blackjack.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.blackjack.model.Card
import com.example.blackjack.model.DeckStats
import com.example.blackjack.repository.FirebaseRepository
import com.example.blackjack.repository.GameRepository
import com.example.blackjack.repository.StatisticsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

enum class GameStatus {
    IN_PROGRESS, WON, LOST, DRAW
}

class GameViewModel(context: Context) : ViewModel() {

    private val deckStats = MutableStateFlow(mutableMapOf<String, DeckStats>())
    private var startTime: Long = 0L
    private var currentDeckStartTime: Long = 0L

    private val _currentBet = MutableStateFlow(0f)
    val currentBet: StateFlow<Float> = _currentBet

    private val _playerCards = MutableStateFlow<List<Card>>(emptyList())
    val playerCards: StateFlow<List<Card>> = _playerCards

    private val _dealerCards = MutableStateFlow<List<Card>>(emptyList())
    val dealerCards: StateFlow<List<Card>> = _dealerCards

    private val gameRepository = GameRepository()
    private val _deckId = MutableStateFlow<String?>(null)
    val deckId: StateFlow<String?> = _deckId

    private val remain = MutableStateFlow<Int>(0)

    private val balance = MutableStateFlow(1000f)
    val currentBalance: StateFlow<Float> = balance

    private val _gameStatus = MutableStateFlow(GameStatus.IN_PROGRESS)
    val gameStatus: StateFlow<GameStatus> = _gameStatus

    private val _dealerCardRevealed = MutableStateFlow(false)
    val dealerCardRevealed: StateFlow<Boolean> = _dealerCardRevealed

    private val statisticsRepository = StatisticsRepository(context)
    private val firebaseRepository = FirebaseRepository()

    suspend fun createNewDeck(deckCount: Int) {
        endCurrentDeckSession()
        try {
            val deckResponse = gameRepository.createNewDeck(deckCount)
            _deckId.value = deckResponse.deckId
            remain.value = deckResponse.remaining

            val stats = DeckStats(
                cardsDrawnByUser = 0,
                stopCount = 0,
                totalCards = 52 * deckCount,
                remainingCards = deckResponse.remaining,
                hoursPlayed = 0.0
            )
            deckStats.value[deckResponse.deckId] = stats

            statisticsRepository.resetStatistics()


            currentDeckStartTime = System.currentTimeMillis()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun setBet(bet: Float) {
        if (bet <= balance.value) {
            _currentBet.value = bet
        }
    }

    fun startGame() {
        _dealerCardRevealed.value = false
        _playerCards.value = emptyList()
        _dealerCards.value = emptyList()
        _gameStatus.value = GameStatus.IN_PROGRESS
        viewModelScope.launch {
            try {
                if (deckId.value == null || remain.value <= 0) {
                    createNewDeck(7)
                }
                drawCardForPlayer(2)
                drawCardForDealer(2)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun startSession() {
        if (deckId.value == null) {
            viewModelScope.launch {
                createNewDeck(7)
            }
        } else {
            startTime = System.currentTimeMillis()
            currentDeckStartTime = startTime
        }
    }

    fun endCurrentDeckSession() {
        val endTime = System.currentTimeMillis()
        val sessionDuration = (endTime - currentDeckStartTime) / (1000 * 60 * 60.0)

        viewModelScope.launch {
            deckId.value?.let { id ->
                deckStats.value[id]?.let { stats ->
                    stats.hoursPlayed += sessionDuration
                    firebaseRepository.saveDeckStats(id, stats)
                }
            }
        }

        currentDeckStartTime = System.currentTimeMillis()
    }

    suspend fun drawCardForPlayer(nbr: Int) {
        if (deckId.value == null || remain.value <= 0) {
            withContext(Dispatchers.IO) {
                createNewDeck(7)
            }
        }

        val newCard = gameRepository.drawCards(deckId.value!!, nbr)
        remain.value = newCard.remaining

        deckId.value?.let {
            val currentStats = deckStats.value[it] ?: DeckStats(0, 0, 0, 0, 0.0)
            currentStats.cardsDrawnByUser += nbr
            currentStats.remainingCards = remain.value
            deckStats.value[it] = currentStats
        }
        updateStats(newCard.cards)
        if (remain.value <= 0) {
            withContext(Dispatchers.IO) {
                createNewDeck(7)
            }
        }

        _playerCards.value += newCard.cards
        if (calculateTotal(playerCards.value) >= 21) {
            stopGame()
        }
    }

    fun drawCardButtonPressed() {
        viewModelScope.launch {
            drawCardForPlayer(1)
        }
    }

    suspend fun drawCardForDealer(nbr: Int) {
        if (deckId.value == null || remain.value <= 0) {
            withContext(Dispatchers.IO) {
                createNewDeck(7)
            }
        }
        val newCard = gameRepository.drawCards(deckId.value!!, nbr)
        remain.value = newCard.remaining

        deckId.value?.let {
            val currentStats = deckStats.value[it] ?: DeckStats(0, 0, 0, 0, 0.0)
            currentStats.remainingCards = remain.value
            deckStats.value[it] = currentStats
        }
        updateStats(newCard.cards)
        if (remain.value <= 0) {
            withContext(Dispatchers.IO) {
                createNewDeck(7)
            }
        }

        _dealerCards.value += newCard.cards
    }

    fun stopButtonPressed() {
        deckId.value?.let {
            val currentStats = deckStats.value[it] ?: DeckStats(0, 0, 0, 0, 0.0)
            currentStats.stopCount += 1
            deckStats.value[it] = currentStats
        }
        stopGame()
    }

    fun stopGame() {
        _dealerCardRevealed.value = true
        viewModelScope.launch {
            if (calculateTotal(playerCards.value) == 21) {
                balance.value += (currentBet.value * 2)
                _gameStatus.value = GameStatus.WON
            } else if (calculateTotal(playerCards.value) > 21) {
                balance.value -= currentBet.value
                _gameStatus.value = GameStatus.LOST
            } else {
                while (calculateTotal(dealerCards.value) < 17) {
                    drawCardForDealer(1)
                }
                if (calculateTotal(dealerCards.value) > 21) {
                    balance.value += currentBet.value
                    _gameStatus.value = GameStatus.WON
                } else if (calculateTotal(playerCards.value) > calculateTotal(dealerCards.value)) {
                    balance.value += currentBet.value
                    _gameStatus.value = GameStatus.WON
                } else if (calculateTotal(playerCards.value) == calculateTotal(dealerCards.value)) {
                    _gameStatus.value = GameStatus.DRAW
                } else {
                    balance.value -= currentBet.value
                    _gameStatus.value = GameStatus.LOST
                }
            }
        }
    }

    fun calculateTotal(cards: List<Card>, revealFirstCard: Boolean = true): Int {
        var total = 0
        var aceCount = 0
        for ((index, card) in cards.withIndex()) {
            if (!revealFirstCard && index == 0) {
                continue
            }
            when (card.rank) {
                "1" -> {
                    total += 11
                    aceCount += 1
                }
                "11", "12", "13" -> {
                    total += 10
                }
                else -> {
                    total += card.rank.toInt()
                }
            }
        }
        while (total > 21 && aceCount > 0) {
            total -= 10
            aceCount -= 1
        }
        return total
    }

    fun updateStats(cards: List<Card>) {
        viewModelScope.launch {
            cards.forEach { card ->
                statisticsRepository.updateCardStatistics(card.rank, 1)
            }
        }
    }
}
