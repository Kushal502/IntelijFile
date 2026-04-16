import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Player Tests")
public class PlayerTest {

    private Player player;
    private Deck   deck;

    @BeforeEach
    void setUp() {
        player = new Player("Alice");
        deck   = new Deck();
        deck.shuffle();
    }

    // ── Constructor / identity ────────────────────────────────────

    @Test
    @DisplayName("Player name is stored correctly")
    void constructor_storesName() {
        assertEquals("Alice", player.getName());
    }

    @Test
    @DisplayName("New player starts with 0 points")
    void constructor_zeroPoints() {
        assertEquals(0, player.getScore());
    }

    @Test
    @DisplayName("New player starts with empty hand")
    void constructor_emptyHand() {
        assertEquals(0, player.getHandSize());
        assertTrue(player.getCollectedCards().isEmpty());
    }

    @Test
    @DisplayName("isComputer() returns false for a regular player")
    void isComputer_regularPlayer_false() {
        assertFalse(player.isComputer());
    }

    // ── Points ────────────────────────────────────────────────────

    @Test
    @DisplayName("addPoints increases score correctly")
    void addPoints_increasesScore() {
        player.addPoints(3);
        assertEquals(3, player.getScore());
    }

    @Test
    @DisplayName("addPoints accumulates across multiple calls")
    void addPoints_accumulates() {
        player.addPoints(3);
        player.addPoints(1);
        player.addPoints(5);
        assertEquals(9, player.getScore());
    }

    // ── Collecting cards ──────────────────────────────────────────

    @Test
    @DisplayName("collectCard adds a card to the hand")
    void collectCard_addsCard() {
        player.collectCard(deck.deal());
        assertEquals(1, player.getHandSize());
    }

    @Test
    @DisplayName("collectCard with null does not grow the hand")
    void collectCard_null_ignored() {
        player.collectCard(null);
        assertEquals(0, player.getHandSize());
    }

    @Test
    @DisplayName("Collecting multiple cards grows hand correctly")
    void collectCard_multiple() {
        player.collectCard(deck.deal());
        player.collectCard(deck.deal());
        player.collectCard(deck.deal());
        assertEquals(3, player.getHandSize());
    }

    // ── Removing cards ────────────────────────────────────────────

    @Test
    @DisplayName("removeCard shrinks hand by 1")
    void removeCard_shrinkHand() {
        Card card = deck.deal();
        player.collectCard(card);
        player.removeCard(card);
        assertEquals(0, player.getHandSize());
    }

    @Test
    @DisplayName("removeCard removes the correct card")
    void removeCard_correctCard() {
        Card first  = deck.deal();
        Card second = deck.deal();
        player.collectCard(first);
        player.collectCard(second);
        player.removeCard(first);
        assertFalse(player.getCollectedCards().contains(first));
        assertTrue(player.getCollectedCards().contains(second));
    }
}
