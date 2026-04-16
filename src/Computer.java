import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

/**
 * Computer-controlled player for High Card Series.
 *
 * Behaviour during the optional adjustment stage (Level 9):
 *   - 0 cards in hand  -> always skips
 *   - 1 card in hand   -> 50 % chance to swap it, 50 % chance to skip
 *   - 2+ cards in hand -> randomly chooses to discard 0, 1, or 2 cards
 *
 * Card selection is uniformly random with no strategic evaluation.
 * All automated decisions are printed to the console so the human
 * players can follow along.
 */
public class Computer extends Player {

    private static final Random RANDOM = new Random();

    public Computer(String name) {
        super(name);
    }

    // Identity

    @Override
    public boolean isComputer() {
        return true;
    }

    // Adjustment-stage decisions

    /**
     * Decides how many cards to discard (0, 1, or 2).
     * The count is clamped to the current hand size so it is always safe
     * to call {@link #chooseDiscardIndices(int)} with the returned value.
     */
    public int decideDiscardCount() {
        int handSize = getCollectedCards().size();
        if (handSize == 0) return 0;
        if (handSize == 1) return RANDOM.nextBoolean() ? 0 : 1;
        return RANDOM.nextInt(3); // 0, 1, or 2
    }

    /**
     * Randomly selects {@code count} distinct card indices from the
     * current hand. Returns an empty list if {@code count} is 0.
     */
    public ArrayList<Integer> chooseDiscardIndices(int count) {
        ArrayList<Integer> available = new ArrayList<>();
        for (int i = 0; i < getCollectedCards().size(); i++) {
            available.add(i);
        }
        Collections.shuffle(available);

        ArrayList<Integer> chosen = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            chosen.add(available.get(i));
        }
        return chosen;
    }
}
