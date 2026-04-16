import java.util.ArrayList;
import java.util.Collections;

/**
 * Calculates and awards the two end-of-game bonus rounds for High Card Series.
 *
 * <p><b>Bonus 1 - Longest Consecutive Rank Sequence</b><br>
 * Each player's collected cards are sorted by rank value and the length of the
 * longest run of consecutive rank values is found. The player with the longest
 * run receives +5 points. If two or more players are tied, each receives +2.
 * No bonus is awarded when no player has a run of two or more.
 *
 * <p><b>Bonus 2 - Highest Suit Count</b><br>
 * Each player counts how many cards they hold per suit; their best (highest)
 * single-suit count is noted. The player with the overall highest count
 * receives +5 points. If two or more players are tied, each receives +2.
 */
public class Bonus {

    // Public API

    /**
     * Evaluates Bonus 1 and applies points to the relevant players.
     *
     * @param players all players in the game
     */
    public static void applyLongestSequenceBonus(ArrayList<Player> players) {
        System.out.println("\n" + line());
        System.out.println("  BONUS 1: Longest Consecutive Rank Sequence");
        System.out.println(line());

        int highestLength = 0;
        ArrayList<Player> bonusWinners = new ArrayList<>();

        for (Player player : players) {
            int seqLen = longestConsecutiveSequence(player.getCollectedCards());
            System.out.printf("  %-15s longest sequence = %d%n",
                    player.getName() + ":", seqLen);

            if (seqLen > highestLength) {
                highestLength = seqLen;
                bonusWinners.clear();
                bonusWinners.add(player);
            } else if (seqLen == highestLength) {
                bonusWinners.add(player);
            }
        }

        System.out.println();

        if (highestLength < 2) {
            System.out.println("  No consecutive sequence of 2 or more found. No bonus awarded.");
            return;
        }

        if (bonusWinners.size() == 1) {
            bonusWinners.get(0).addPoints(5);
            System.out.println("  Bonus: " + bonusWinners.get(0).getName() + " receives +5 points!");
        } else {
            System.out.print("  Tie! ");
            for (Player p : bonusWinners) {
                p.addPoints(2);
                System.out.print(p.getName() + " ");
            }
            System.out.println("each receive +2 points.");
        }
    }

    /**
     * Evaluates Bonus 2 and applies points to the relevant players.
     *
     * @param players all players in the game
     */
    public static void applyHighestSuitCountBonus(ArrayList<Player> players) {
        System.out.println("\n" + line());
        System.out.println("  BONUS 2: Highest Suit Count");
        System.out.println(line());

        int highestCount = 0;
        ArrayList<Player> bonusWinners = new ArrayList<>();

        for (Player player : players) {
            int[] c = suitCounts(player.getCollectedCards());
            int spades = c[0], hearts = c[1], diamonds = c[2], clubs = c[3];
            int maxSuit = Math.max(Math.max(spades, hearts), Math.max(diamonds, clubs));

            System.out.printf("  %-15s Spades=%-2d Hearts=%-2d Diamonds=%-2d Clubs=%-2d  best=%d%n",
                    player.getName() + ":", spades, hearts, diamonds, clubs, maxSuit);

            if (maxSuit > highestCount) {
                highestCount = maxSuit;
                bonusWinners.clear();
                bonusWinners.add(player);
            } else if (maxSuit == highestCount) {
                bonusWinners.add(player);
            }
        }

        System.out.println();

        if (highestCount == 0) {
            System.out.println("  No cards collected. No bonus awarded.");
            return;
        }

        if (bonusWinners.size() == 1) {
            bonusWinners.get(0).addPoints(5);
            System.out.println("  Bonus: " + bonusWinners.get(0).getName() + " receives +5 points!");
        } else {
            System.out.print("  Tie! ");
            for (Player p : bonusWinners) {
                p.addPoints(2);
                System.out.print(p.getName() + " ");
            }
            System.out.println("each receive +2 points.");
        }
    }

    // Private helpers

    /**
     * Returns the length of the longest run of consecutive rank values
     * in the given card list. Duplicate rank values are ignored.
     * Returns 0 for an empty list and 1 when no two cards have adjacent ranks.
     */
    private static int longestConsecutiveSequence(ArrayList<Card> cards) {
        if (cards.isEmpty()) return 0;

        // Collect unique rank values
        ArrayList<Integer> rankValues = new ArrayList<>();
        for (Card card : cards) {
            int val = card.getRankValue();
            if (!rankValues.contains(val)) {
                rankValues.add(val);
            }
        }
        Collections.sort(rankValues);

        int longest = 1;
        int current = 1;
        for (int i = 1; i < rankValues.size(); i++) {
            if (rankValues.get(i) == rankValues.get(i - 1) + 1) {
                current++;
                if (current > longest) longest = current;
            } else {
                current = 1;
            }
        }
        return longest;
    }

    /**
     * Returns suit counts as [spades, hearts, diamonds, clubs].
     */
    private static int[] suitCounts(ArrayList<Card> cards) {
        int spades = 0, hearts = 0, diamonds = 0, clubs = 0;
        for (Card card : cards) {
            switch (card.getSuit()) {
                case "Spades":   spades++;   break;
                case "Hearts":   hearts++;   break;
                case "Diamonds": diamonds++; break;
                case "Clubs":    clubs++;    break;
                default:                     break;
            }
        }
        return new int[]{spades, hearts, diamonds, clubs};
    }

    private static String line() {
        return "  " + "-".repeat(38);
    }
}
