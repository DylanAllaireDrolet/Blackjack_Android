import com.example.blackjack.model.Card
import com.example.blackjack.viewmodel.GameViewModel
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class GameViewModelTest {

    private lateinit var gameViewModel: GameViewModel

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

    @Test
    fun calculateTotal_playerCards() {
        val playerCards = listOf(
            Card("11d", "/static/11d.svg", "11", "d"),
            Card("8s", "/static/8s.svg", "8", "s"),
            Card("2c", "/static/2c.svg", "2", "c")
        )
        val total = calculateTotal(playerCards)
        assertEquals(20, total)
    }

    @Test
    fun calculateTotal_dealerCards_withHiddenCard() {
        val dealerCards = listOf(
            Card("11d", "/static/11d.svg", "11", "d"),
            Card("8s", "/static/8s.svg", "8", "s")
        )
        val total = calculateTotal(dealerCards, revealFirstCard = false)
        assertEquals(8, total)
    }
}
