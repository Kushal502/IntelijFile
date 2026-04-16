import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import java.util.ArrayList;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Bonus Tests")
public class BonusTest {

    // ── Helpers ───────────────────────────────────────────────────

    /** Creates a player and gives them specific cards by rank + suit strings. */
    private Player playerWith(String name, String[][] cards) {
        Player p = new Player(name);
        for (String[] c : cards) {
            p.collectCard(new Card(c[0], c[1]));
        }
        return p;
    }

    private ArrayList<Player> list(Player... players) {
        ArrayList<Player> result = new ArrayList<>();
        for (Player p : players) result.add(p);
        return result;
    }

    // ════════════════════════════════════════════════════════════
    //  BONUS 1 – Longest Consecutive Rank Sequence
    // ════════════════════════════════════════════════════════════

    @Test
    @DisplayName("Bonus 1: sole player with sequence of 2 gets +5")
    void sequence_soloWinner_getsPlus5() {
        // Q(10) + K(11) → sequence of 2
        Player p = playerWith("Alice", new String[][]{{"Q","Clubs"},{"K","Hearts"}});
        Bonus.applyLongestSequenceBonus(list(p));
        assertEquals(5, p.getScore());
    }

    @Test
    @DisplayName("Bonus 1: sole player with sequence of 3 gets +5")
    void sequence_threeConsecutive_getsPlus5() {
        // J(9) + Q(10) + K(11)
        Player p = playerWith("Alice",
                new String[][]{{"J","Clubs"},{"Q","Hearts"},{"K","Spades"}});
        Bonus.applyLongestSequenceBonus(list(p));
        assertEquals(5, p.getScore());
    }

    @Test
    @DisplayName("Bonus 1: no player has sequence of 2 — no bonus awarded")
    void sequence_noConsecutive_noBonusAwarded() {
        // A(12) and 2(0) — not consecutive
        Player p1 = playerWith("Alice", new String[][]{{"A","Spades"}});
        Player p2 = playerWith("Bob",   new String[][]{{"2","Clubs"}});
        Bonus.applyLongestSequenceBonus(list(p1, p2));
        assertEquals(0, p1.getScore());
        assertEquals(0, p2.getScore());
    }

    @Test
    @DisplayName("Bonus 1: two players tied for longest sequence each get +2")
    void sequence_tie_eachGetsPlus2() {
        // Both have Q+K (length 2)
        Player p1 = playerWith("Alice", new String[][]{{"Q","Clubs"},  {"K","Hearts"}});
        Player p2 = playerWith("Bob",   new String[][]{{"Q","Spades"}, {"K","Diamonds"}});
        Bonus.applyLongestSequenceBonus(list(p1, p2));
        assertEquals(2, p1.getScore());
        assertEquals(2, p2.getScore());
    }

    @Test
    @DisplayName("Bonus 1: player with longer sequence beats shorter sequence")
    void sequence_longerWins() {
        // p1: J+Q+K = 3; p2: 9+10 = 2
        Player p1 = playerWith("Alice",
                new String[][]{{"J","Clubs"},{"Q","Hearts"},{"K","Spades"}});
        Player p2 = playerWith("Bob",
                new String[][]{{"9","Clubs"},{"10","Diamonds"}});
        Bonus.applyLongestSequenceBonus(list(p1, p2));
        assertEquals(5, p1.getScore());
        assertEquals(0, p2.getScore());
    }

    @Test
    @DisplayName("Bonus 1: duplicate rank values are ignored in sequence calculation")
    void sequence_duplicateRanks_counted_once() {
        // Two Kings and a Queen — sequence is still 2 (Q,K), not 3
        Player p = playerWith("Alice",
                new String[][]{{"K","Clubs"},{"K","Hearts"},{"Q","Spades"}});
        Bonus.applyLongestSequenceBonus(list(p));
        assertEquals(5, p.getScore()); // still wins with length-2 sequence
    }

    @Test
    @DisplayName("Bonus 1: player with no cards gets sequence length 0")
    void sequence_noCards_lengthZero() {
        Player p1 = new Player("Alice");                         // 0 cards
        Player p2 = playerWith("Bob", new String[][]{{"K","Clubs"},{"Q","Hearts"}});
        Bonus.applyLongestSequenceBonus(list(p1, p2));
        assertEquals(0, p1.getScore());
        assertEquals(5, p2.getScore());
    }

    // ════════════════════════════════════════════════════════════
    //  BONUS 2 – Highest Suit Count
    // ════════════════════════════════════════════════════════════

    @Test
    @DisplayName("Bonus 2: sole player with highest suit count gets +5")
    void suitCount_soloWinner_getsPlus5() {
        // p1 has 3 Diamonds, p2 has 1 Spade
        Player p1 = playerWith("Alice",
                new String[][]{{"A","Diamonds"},{"8","Diamonds"},{"10","Diamonds"}});
        Player p2 = playerWith("Bob", new String[][]{{"A","Spades"}});
        Bonus.applyHighestSuitCountBonus(list(p1, p2));
        assertEquals(5, p1.getScore());
        assertEquals(0, p2.getScore());
    }

    @Test
    @DisplayName("Bonus 2: two players tied for highest suit count each get +2")
    void suitCount_tie_eachGetsPlus2() {
        // Both have 2 Spades
        Player p1 = playerWith("Alice",
                new String[][]{{"A","Spades"},{"K","Spades"}});
        Player p2 = playerWith("Bob",
                new String[][]{{"Q","Spades"},{"J","Spades"}});
        Bonus.applyHighestSuitCountBonus(list(p1, p2));
        assertEquals(2, p1.getScore());
        assertEquals(2, p2.getScore());
    }

    @Test
    @DisplayName("Bonus 2: no cards collected — no bonus awarded")
    void suitCount_noCards_noBonusAwarded() {
        Player p1 = new Player("Alice");
        Player p2 = new Player("Bob");
        Bonus.applyHighestSuitCountBonus(list(p1, p2));
        assertEquals(0, p1.getScore());
        assertEquals(0, p2.getScore());
    }

    @Test
    @DisplayName("Bonus 2: best suit count uses the player's highest single-suit count")
    void suitCount_usesBestSuit() {
        // p1 has 2 Hearts, 1 Spade → best = 2
        // p2 has 1 of each suit → best = 1
        Player p1 = playerWith("Alice",
                new String[][]{{"A","Hearts"},{"K","Hearts"},{"Q","Spades"}});
        Player p2 = playerWith("Bob",
                new String[][]{{"2","Spades"},{"3","Clubs"},{"4","Diamonds"},{"5","Hearts"}});
        Bonus.applyHighestSuitCountBonus(list(p1, p2));
        assertEquals(5, p1.getScore());
        assertEquals(0, p2.getScore());
    }

    @Test
    @DisplayName("Bonus 2: three-way tie each player gets +2")
    void suitCount_threeWayTie_eachGetsPlus2() {
        Player p1 = playerWith("A", new String[][]{{"A","Spades"},{"K","Spades"}});
        Player p2 = playerWith("B", new String[][]{{"A","Hearts"},{"K","Hearts"}});
        Player p3 = playerWith("C", new String[][]{{"A","Clubs"}, {"K","Clubs"}});
        Bonus.applyHighestSuitCountBonus(list(p1, p2, p3));
        assertEquals(2, p1.getScore());
        assertEquals(2, p2.getScore());
        assertEquals(2, p3.getScore());
    }

    // ════════════════════════════════════════════════════════════
    //  Both bonuses applied together
    // ════════════════════════════════════════════════════════════

    @Test
    @DisplayName("Both bonuses applied: scores accumulate correctly")
    void bothBonuses_scoresAccumulate() {
        // p1 wins sequence bonus (+5); p2 wins suit bonus (+5)
        Player p1 = playerWith("Alice",
                new String[][]{{"J","Clubs"},{"Q","Hearts"},{"K","Spades"}});
        Player p2 = playerWith("Bob",
                new String[][]{{"A","Diamonds"},{"8","Diamonds"},{"10","Diamonds"}});
        ArrayList<Player> players = list(p1, p2);
        Bonus.applyLongestSequenceBonus(players);
        Bonus.applyHighestSuitCountBonus(players);
        assertEquals(5, p1.getScore());
        assertEquals(5, p2.getScore());
    }
}
