import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import java.util.ArrayList;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Computer Tests")
public class ComputerTest {

    private Computer computer;
    private Deck     deck;

    @BeforeEach
    void setUp() {
        computer = new Computer("Computer");
        deck     = new Deck();
        deck.shuffle();
    }

    // ── Identity ──────────────────────────────────────────────────

    @Test
    @DisplayName("isComputer() returns true")
    void isComputer_returnsTrue() {
        assertTrue(computer.isComputer());
    }

    @Test
    @DisplayName("getName() returns 'Computer'")
    void getName_returnsComputer() {
        assertEquals("Computer", computer.getName());
    }

    @Test
    @DisplayName("Computer is an instance of Player")
    void computer_isInstanceOfPlayer() {
        assertInstanceOf(Player.class, computer);
    }

    // ── Inherited Player behaviour ────────────────────────────────

    @Test
    @DisplayName("Computer starts with 0 points")
    void initialScore_isZero() {
        assertEquals(0, computer.getScore());
    }

    @Test
    @DisplayName("Computer can collect cards")
    void collectCard_works() {
        computer.collectCard(deck.deal());
        assertEquals(1, computer.getHandSize());
    }

    @Test
    @DisplayName("Computer can accumulate points")
    void addPoints_works() {
        computer.addPoints(3);
        computer.addPoints(1);
        assertEquals(4, computer.getScore());
    }

    // ── decideDiscardCount ────────────────────────────────────────

    @Test
    @DisplayName("decideDiscardCount returns 0 when hand is empty")
    void decideDiscardCount_emptyHand_alwaysZero() {
        assertEquals(0, computer.decideDiscardCount());
    }

    @RepeatedTest(20)
    @DisplayName("decideDiscardCount with 1 card returns 0 or 1 only")
    void decideDiscardCount_oneCard_zeroOrOne() {
        computer.collectCard(deck.deal());
        int result = computer.decideDiscardCount();
        assertTrue(result == 0 || result == 1,
                "Expected 0 or 1, got: " + result);
    }

    @RepeatedTest(20)
    @DisplayName("decideDiscardCount with 2+ cards returns 0, 1, or 2 only")
    void decideDiscardCount_twoCards_zeroOneOrTwo() {
        computer.collectCard(deck.deal());
        computer.collectCard(deck.deal());
        int result = computer.decideDiscardCount();
        assertTrue(result >= 0 && result <= 2,
                "Expected 0-2, got: " + result);
    }

    // ── chooseDiscardIndices ──────────────────────────────────────

    @Test
    @DisplayName("chooseDiscardIndices(0) returns empty list")
    void chooseDiscardIndices_zero_isEmpty() {
        computer.collectCard(deck.deal());
        computer.collectCard(deck.deal());
        ArrayList<Integer> indices = computer.chooseDiscardIndices(0);
        assertTrue(indices.isEmpty());
    }

    @Test
    @DisplayName("chooseDiscardIndices(1) returns exactly 1 index")
    void chooseDiscardIndices_one_returnsOneIndex() {
        computer.collectCard(deck.deal());
        computer.collectCard(deck.deal());
        ArrayList<Integer> indices = computer.chooseDiscardIndices(1);
        assertEquals(1, indices.size());
    }

    @Test
    @DisplayName("chooseDiscardIndices(2) returns exactly 2 distinct indices")
    void chooseDiscardIndices_two_returnsTwoDistinctIndices() {
        computer.collectCard(deck.deal());
        computer.collectCard(deck.deal());
        computer.collectCard(deck.deal());
        ArrayList<Integer> indices = computer.chooseDiscardIndices(2);
        assertEquals(2, indices.size());
        assertNotEquals(indices.get(0), indices.get(1));
    }

    @Test
    @DisplayName("chooseDiscardIndices returns valid indices within hand bounds")
    void chooseDiscardIndices_indicesWithinBounds() {
        computer.collectCard(deck.deal());
        computer.collectCard(deck.deal());
        ArrayList<Integer> indices = computer.chooseDiscardIndices(2);
        for (int idx : indices) {
            assertTrue(idx >= 0 && idx < computer.getHandSize(),
                    "Index " + idx + " out of bounds for hand size " + computer.getHandSize());
        }
    }
}
