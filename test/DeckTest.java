import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Deck Tests")
public class DeckTest {

    private Deck deck;

    @BeforeEach
    void setUp() {
        deck = new Deck();
    }

    // ── Initial state ─────────────────────────────────────────────

    @Test
    @DisplayName("New deck contains exactly 52 cards")
    void newDeck_hasFiftyTwoCards() {
        assertEquals(52, deck.size());
    }

    // ── Dealing ───────────────────────────────────────────────────

    @Test
    @DisplayName("Dealing a card reduces deck size by 1")
    void deal_reducesSizeByOne() {
        deck.deal();
        assertEquals(51, deck.size());
    }

    @Test
    @DisplayName("Dealt card is not null")
    void deal_returnsCard() {
        assertNotNull(deck.deal());
    }

    @Test
    @DisplayName("Dealing all 52 cards empties the deck")
    void deal_allCards_emptiesDeck() {
        for (int i = 0; i < 52; i++) deck.deal();
        assertEquals(0, deck.size());
    }

    @Test
    @DisplayName("Dealing from an empty deck returns null")
    void deal_emptyDeck_returnsNull() {
        for (int i = 0; i < 52; i++) deck.deal();
        assertNull(deck.deal());
    }

    // ── Returning cards ───────────────────────────────────────────

    @Test
    @DisplayName("Returning a card increases deck size by 1")
    void returnCard_increasesSizeByOne() {
        Card card = deck.deal();
        int sizeAfterDeal = deck.size();
        deck.returnCard(card);
        assertEquals(sizeAfterDeal + 1, deck.size());
    }

    @Test
    @DisplayName("Returning null does not change deck size")
    void returnCard_null_noChange() {
        int before = deck.size();
        deck.returnCard(null);
        assertEquals(before, deck.size());
    }

    @Test
    @DisplayName("Returned card goes to the bottom (dealt last)")
    void returnCard_goesToBottom() {
        // Deal all cards, then return a known card — it should be last out
        Card[] allCards = new Card[52];
        for (int i = 0; i < 52; i++) allCards[i] = deck.deal();

        Card known = allCards[0];
        deck.returnCard(known);

        // Deck now has 1 card — deal it back
        Card dealtBack = deck.deal();
        assertEquals(known.toString(), dealtBack.toString());
    }

    // ── Shuffle ───────────────────────────────────────────────────

    @Test
    @DisplayName("Shuffling does not change the deck size")
    void shuffle_preservesSize() {
        deck.shuffle();
        assertEquals(52, deck.size());
    }

    @Test
    @DisplayName("Shuffling still allows all 52 cards to be dealt")
    void shuffle_allCardsDealable() {
        deck.shuffle();
        int count = 0;
        while (deck.deal() != null) count++;
        assertEquals(52, count);
    }
}
