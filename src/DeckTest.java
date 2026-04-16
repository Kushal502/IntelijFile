import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class DeckTest {

    private Deck deck;

    @BeforeEach
    public void setUp() {
        deck = new Deck();
    }

    @Test
    public void testDeckInitializationSize() {
        assertEquals(52, deck.size(), "A new deck should contain 52 cards.");
    }

    @Test
    public void testDealCardReducesSize() {
        int initialSize = deck.size();
        deck.dealCard();
        assertEquals(initialSize - 1, deck.size(), "Dealing a card should reduce the deck size by 1.");
    }

    @Test
    public void testDealFromEmptyDeckReturnsNull() {
        for (int i = 0; i < 52; i++) {
            deck.dealCard();
        }

        assertEquals(0, deck.size(), "Deck should be empty after drawing 52 cards.");
        assertNull(deck.dealCard(), "Drawing from an empty deck should return null.");
    }

    @Test
    public void testReturnCardIncreasesSize() {
        Card topCard = deck.dealCard();
        int sizeAfterDeal = deck.size();

        deck.returnCard(topCard);

        assertEquals(sizeAfterDeal + 1, deck.size(), "Returning a card should increase the deck size by 1.");
    }
}
