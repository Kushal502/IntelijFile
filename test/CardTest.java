import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Card Tests")
public class CardTest {

    // ── Constructor: index-based ──────────────────────────────────

    @Test
    @DisplayName("Index constructor: rank 0 maps to '2'")
    void indexConstructor_lowestRank() {
        Card card = new Card(0, 0);
        assertEquals("2", card.getRank());
    }

    @Test
    @DisplayName("Index constructor: rank 12 maps to 'Ace'")
    void indexConstructor_highestRank() {
        Card card = new Card(12, 3);
        assertEquals("Ace", card.getRank());
        assertEquals("Spades", card.getSuit());
    }

    @Test
    @DisplayName("Index constructor: invalid rank index throws exception")
    void indexConstructor_invalidRank_throws() {
        assertThrows(IllegalArgumentException.class, () -> new Card(13, 0));
    }

    @Test
    @DisplayName("Index constructor: negative rank index throws exception")
    void indexConstructor_negativeRank_throws() {
        assertThrows(IllegalArgumentException.class, () -> new Card(-1, 0));
    }

    @Test
    @DisplayName("Index constructor: invalid suit index throws exception")
    void indexConstructor_invalidSuit_throws() {
        assertThrows(IllegalArgumentException.class, () -> new Card(0, 4));
    }

    // ── Constructor: string-based with normalisation ──────────────

    @Test
    @DisplayName("String constructor: 'A' normalises to 'Ace'")
    void stringConstructor_normalisesAce() {
        Card card = new Card("A", "Spades");
        assertEquals("Ace", card.getRank());
    }

    @Test
    @DisplayName("String constructor: 'K' normalises to 'King'")
    void stringConstructor_normalisesKing() {
        Card card = new Card("K", "Hearts");
        assertEquals("King", card.getRank());
    }

    @Test
    @DisplayName("String constructor: 'Q' normalises to 'Queen'")
    void stringConstructor_normalisesQueen() {
        Card card = new Card("Q", "Diamonds");
        assertEquals("Queen", card.getRank());
    }

    @Test
    @DisplayName("String constructor: 'J' normalises to 'Jack'")
    void stringConstructor_normalisesJack() {
        Card card = new Card("J", "Clubs");
        assertEquals("Jack", card.getRank());
    }

    @Test
    @DisplayName("String constructor: numeric rank '10' is accepted as-is")
    void stringConstructor_numericRank() {
        Card card = new Card("10", "Clubs");
        assertEquals("10", card.getRank());
    }

    @Test
    @DisplayName("String constructor: invalid rank throws exception")
    void stringConstructor_invalidRank_throws() {
        assertThrows(IllegalArgumentException.class, () -> new Card("Z", "Spades"));
    }

    @Test
    @DisplayName("String constructor: invalid suit throws exception")
    void stringConstructor_invalidSuit_throws() {
        assertThrows(IllegalArgumentException.class, () -> new Card("Ace", "Stars"));
    }

    // ── Rank ordering ─────────────────────────────────────────────

    @Test
    @DisplayName("Ace ranks higher than King")
    void rankOrder_aceBeatsKing() {
        Card ace  = new Card("A", "Spades");
        Card king = new Card("K", "Hearts");
        assertTrue(ace.isBiggerThan(king));
    }

    @Test
    @DisplayName("King does not rank higher than Ace")
    void rankOrder_kingLosesToAce() {
        Card ace  = new Card("A", "Spades");
        Card king = new Card("K", "Hearts");
        assertFalse(king.isBiggerThan(ace));
    }

    @Test
    @DisplayName("Two cards of equal rank are not bigger than each other")
    void rankOrder_equalRanks() {
        Card a = new Card("K", "Spades");
        Card b = new Card("K", "Clubs");
        assertFalse(a.isBiggerThan(b));
        assertFalse(b.isBiggerThan(a));
    }

    @Test
    @DisplayName("2 has the lowest rank value (0)")
    void rankValue_two_isZero() {
        Card two = new Card("2", "Clubs");
        assertEquals(0, two.getRankValue());
    }

    @Test
    @DisplayName("Ace has the highest rank value (12)")
    void rankValue_ace_isTwelve() {
        Card ace = new Card("A", "Spades");
        assertEquals(12, ace.getRankValue());
    }

    // ── toString ──────────────────────────────────────────────────

    @Test
    @DisplayName("toString returns 'Ace of Spades'")
    void toString_format() {
        Card card = new Card("A", "Spades");
        assertEquals("Ace of Spades", card.toString());
    }
}
