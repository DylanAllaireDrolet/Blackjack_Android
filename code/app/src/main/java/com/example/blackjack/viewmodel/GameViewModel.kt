package com.example.blackjack.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.blackjack.model.Card
import com.example.blackjack.repository.GameRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class GameViewModel : ViewModel() {

    // État pour la mise
    private val _currentBet = MutableStateFlow(0f)
    val currentBet: StateFlow<Float> = _currentBet

    // État pour les cartes du joueur et du croupier
    private val _playerCards = MutableStateFlow<List<Card>>(emptyList())
    val playerCards: StateFlow<List<Card>> = _playerCards

    private val _dealerCards = MutableStateFlow<List<Card>>(emptyList())
    val dealerCards: StateFlow<List<Card>> = _dealerCards

    // État pour les mains après split
    private val _splitHandOne = MutableStateFlow<List<Card>>(emptyList())
    val splitHandOne: StateFlow<List<Card>> = _splitHandOne

    private val _splitHandTwo = MutableStateFlow<List<Card>>(emptyList())
    val splitHandTwo: StateFlow<List<Card>> = _splitHandTwo

    private val gameRepository = GameRepository()
    private val _deckId = MutableStateFlow<String?>(null)
    val deckId: StateFlow<String?> = _deckId

    fun createNewDeck(deckCount: Int) {
        try {
            viewModelScope.launch {
                val deckResponse = gameRepository.createNewDeck(deckCount)
                _deckId.value = deckResponse.deckId
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    // Méthode pour définir la mise
    fun setBet(bet: Float) {
        _currentBet.value = bet
    }

    // Méthode pour démarrer la partie
    fun startGame() {
        viewModelScope.launch {
            val playerHand = gameRepository.drawCards(deckId.value!!, 2)
            val dealerHand = gameRepository.drawCards(deckId.value!!, 2)



            _playerCards.value = playerHand.cards
            _dealerCards.value = dealerHand.cards
        }
    }

    // Méthode pour tirer une carte pour le joueur
    fun drawCardForPlayer() {
        viewModelScope.launch {
            val newCard = gameRepository.drawCards(deckId.value!!, 1)
            _playerCards.value += newCard.cards
        }
    }

    // Méthode pour tirer une carte pour le split
    fun drawCardForSplitHandOne() {
        viewModelScope.launch {
            val newCard = gameRepository.drawCards(deckId.value!!, 1)
            _splitHandOne.value += newCard.cards
        }
    }

    fun drawCardForSplitHandTwo() {
        viewModelScope.launch {
            val newCard = gameRepository.drawCards(deckId.value!!, 1)
            _splitHandTwo.value += newCard.cards
        }
    }

    // Méthode pour arrêter la main
    fun stopGame() {
        // Logique pour arrêter le jeu
    }

    fun stopSplitHandOne() {
        // Logique pour arrêter la première main après split
    }

    fun stopSplitHandTwo() {
        // Logique pour arrêter la deuxième main après split
    }
}
