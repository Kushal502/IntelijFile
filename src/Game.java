import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

public class Game {
    private static final int MINIMUM_CARDS_FOR_ADJUSTMENT = 3;
    private Deck deck;
    private final ArrayList<Player> players;
    private final Scanner scanner;
    private int numRounds;

    public Game() {
        deck = null;
        players = new ArrayList<>();
        scanner = new Scanner(System.in);
        numRounds = 0;
    }

    private void setupPlayers() {
        players.clear();

        int numPlayers = 0;
        while (numPlayers < 2) {
            System.out.print("How many players will be playing? (Minimum 2 or press Enter for 2 default): ");
            String input = scanner.nextLine().trim();

            if (input.isEmpty()) {
                System.out.println("Using 2 default players.");
                players.add(new Player("Player 1"));
                players.add(new Player("Player 2"));
                numRounds = promptForRounds();
                return;
            }

            try {
                numPlayers = Integer.parseInt(input.replaceAll("[^0-9]", ""));
                if (numPlayers < 2) {
                    System.out.println("You need at least 2 players.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number.");
            }
        }

        for (int i = 1; i <= numPlayers; i++) {
            System.out.print("Enter name for Player " + i + ": ");
            String name = scanner.nextLine().trim();
            if (name.isEmpty()) {
                name = "Player " + i;
            }
            players.add(new Player(name));
        }

        numRounds = promptForRounds();
    }

    private int promptForRounds() {
        final int defaultRounds = 5;

        while (true) {
            System.out.print("Enter number of rounds (5-10, or press Enter for default 5): ");
            String input = scanner.nextLine().trim();

            if (input.isEmpty()) {
                System.out.println("Using default of " + defaultRounds + " rounds.");
                return defaultRounds;
            }

            try {
                int rounds = Integer.parseInt(input);
                if (rounds >= 5 && rounds <= 10) {
                    return rounds;
                }
            } catch (NumberFormatException e) {
                // Fall through to validation message
            }

            System.out.println("Please enter a whole number from 5 to 10.");
        }
    }

    private int getRankValue(String rank) {
        switch (rank) {
            case "A":
                return 14;
            case "K":
                return 13;
            case "Q":
                return 12;
            case "J":
                return 11;
            default:
                return Integer.parseInt(rank);
        }
    }

    private void playRounds() {
        for (int round = 1; round <= numRounds; round++) {
            System.out.println("\n--- Round " + round + " ---");

            ArrayList<Card> dealtCards = new ArrayList<>();
            for (Player player : players) {
                Card card = deck.dealCard();
                dealtCards.add(card);
                player.collectCard(card);
                System.out.println(player.getName() + " was dealt: " + card);
            }

            int highestValue = 0;
            for (Card card : dealtCards) {
                int value = getRankValue(card.getRank());
                if (value > highestValue) {
                    highestValue = value;
                }
            }

            ArrayList<Player> roundWinners = new ArrayList<>();
            for (int i = 0; i < players.size(); i++) {
                if (getRankValue(dealtCards.get(i).getRank()) == highestValue) {
                    roundWinners.add(players.get(i));
                }
            }

            if (roundWinners.size() == 1) {
                Player winner = roundWinners.get(0);
                winner.addPoints(3);

                if (winner.isComputer()) {
                    System.out.println("Winner: Computer (+3 points)");
                } else {
                    System.out.println("Winner: " + winner.getName() + " (+3 points)");
                }
            } else {
                System.out.print("Tie between: ");
                for (Player winner : roundWinners) {
                    winner.addPoints(1);
                    if (winner.isComputer()) {
                        System.out.print("Computer ");
                    } else {
                        System.out.print(winner.getName() + " ");
                    }
                }
                System.out.println("(+1 point each)");
            }

            System.out.println("\nCurrent Scores:");
            for (Player player : players) {
                System.out.println("  " + player);
            }

            adjustmentStage();

            if (round < numRounds) {
                System.out.print("\nPress [Enter] to start Round " + (round + 1) + "...");
                scanner.nextLine();
            } else {
                System.out.print("\nPress [Enter] to finish rounds and see results...");
                scanner.nextLine();
            }
        }
    }

    private void displayCollectedCards() {
        System.out.println("\n=== Cards Collected After All Rounds ===");
        for (Player player : players) {
            System.out.print(player.getName() + ": ");
            if (player.getCollectedCards().isEmpty()) {
                System.out.print("No cards collected");
            } else {
                for (Card card : player.getCollectedCards()) {
                    System.out.print(card + " ");
                }
            }
            System.out.println(" | Points so far: " + player.getScore());
        }
    }

    private void adjustmentStage() {
        boolean hasEligiblePlayer = false;
        for (Player player : players) {
            if (!player.isComputer() && player.getCollectedCards().size() >= MINIMUM_CARDS_FOR_ADJUSTMENT) {
                hasEligiblePlayer = true;
                break;
            }
        }

        if (!hasEligiblePlayer) {
            return;
        }

        System.out.println("\n=== Optional Adjustment Stage ===");

        for (Player player : players) {
            ArrayList<Card> hand = player.getCollectedCards();

            if (player.isComputer() || hand.size() < MINIMUM_CARDS_FOR_ADJUSTMENT) {
                continue;
            }

            System.out.println("\n" + player.getName() + "'s collected cards:");

            for (int i = 0; i < hand.size(); i++) {
                System.out.println("  " + (i + 1) + ": " + hand.get(i));
            }

            System.out.print("Do you want to discard 1 or 2 cards? (Enter 1, 2, or press Enter to skip): ");
            int choice = -1;
            while (choice < 0 || choice > 2) {
                String input = scanner.nextLine().trim();

                if (input.isEmpty() || input.equals("0")) {
                    System.out.println(player.getName() + " chose to skip adjustment.");
                    choice = 0;
                    break;
                }

                try {
                    choice = Integer.parseInt(input);
                    if (choice < 0 || choice > 2) {
                        System.out.print("Please enter 0, 1 or 2: ");
                    }
                } catch (NumberFormatException e) {
                    System.out.print("Please enter 0, 1 or 2: ");
                    choice = -1;
                }
            }

            if (choice == 0) {
                continue;
            }

            ArrayList<Card> toDiscard = new ArrayList<>();
            boolean canceledAdjustment = false;
            for (int i = 0; i < choice; i++) {
                int cardIndex = -1;
                while (cardIndex < 1 || cardIndex > hand.size()) {
                    System.out.print("Enter card number to discard (or press Enter to cancel): ");
                    String input = scanner.nextLine().trim();

                    if (input.isEmpty()) {
                        System.out.println("Ending adjustment for " + player.getName() + ".");
                        toDiscard.clear();
                        canceledAdjustment = true;
                        break;
                    }

                    try {
                        cardIndex = Integer.parseInt(input);
                        if (cardIndex < 1 || cardIndex > hand.size()) {
                            System.out.print("Invalid choice. ");
                        }
                    } catch (NumberFormatException e) {
                        System.out.print("Please enter a valid number: ");
                    }
                }

                if (cardIndex == -1 || canceledAdjustment) {
                    break;
                }

                Card chosen = hand.get(cardIndex - 1);
                if (toDiscard.contains(chosen)) {
                    System.out.println("You already chose that card, pick again.");
                    i--;
                } else {
                    toDiscard.add(chosen);
                }
            }

            if (toDiscard.isEmpty()) {
                continue;
            }

            for (Card card : toDiscard) {
                player.removeCard(card);
                deck.returnCard(card);
            }

            for (int i = 0; i < toDiscard.size(); i++) {
                if (deck.size() > 0) {
                    Card newCard = deck.dealCard();
                    player.collectCard(newCard);
                    System.out.println(player.getName() + " drew: " + newCard);
                } else {
                    System.out.println("Deck is empty, no replacement available.");
                }
            }
        }
    }

    private void longestSequenceBonus() {
        System.out.println("\n=== Bonus 1: Longest Consecutive Rank Sequence ===");

        int highestLength = 0;
        ArrayList<Player> bonusWinners = new ArrayList<>();

        for (Player player : players) {
            ArrayList<Card> cards = player.getCollectedCards();

            if (cards.isEmpty()) {
                System.out.println(player.getName() + ": no cards, sequence length = 0");
                continue;
            }

            ArrayList<Integer> rankValues = new ArrayList<>();
            for (Card card : cards) {
                int val = getRankValue(card.getRank());
                if (!rankValues.contains(val)) {
                    rankValues.add(val);
                }
            }
            Collections.sort(rankValues);

            int longestRun = 1;
            int currentRun = 1;
            for (int i = 1; i < rankValues.size(); i++) {
                if (rankValues.get(i) == rankValues.get(i - 1) + 1) {
                    currentRun++;
                    if (currentRun > longestRun) {
                        longestRun = currentRun;
                    }
                } else {
                    currentRun = 1;
                }
            }

            System.out.println(player.getName() + ": longest sequence = " + longestRun);

            if (longestRun > highestLength) {
                highestLength = longestRun;
                bonusWinners.clear();
                bonusWinners.add(player);
            } else if (longestRun == highestLength) {
                bonusWinners.add(player);
            }
        }

        if (bonusWinners.size() == 1) {
            bonusWinners.get(0).addPoints(5);
            System.out.println("Bonus: " + bonusWinners.get(0).getName() + " gets +5 points!");
        } else {
            System.out.print("Tie for longest sequence! ");
            for (Player player : bonusWinners) {
                player.addPoints(2);
                System.out.print(player.getName() + " ");
            }
            System.out.println("each get +2 points!");
        }
    }

    private void highestSuitCountBonus() {
        System.out.println("\n=== Bonus 2: Highest Suit Count ===");

        int highestCount = 0;
        ArrayList<Player> bonusWinners = new ArrayList<>();

        for (Player player : players) {
            ArrayList<Card> cards = player.getCollectedCards();

            int spades = 0;
            int hearts = 0;
            int diamonds = 0;
            int clubs = 0;
            for (Card card : cards) {
                switch (card.getSuit()) {
                    case "♠":
                    case "Spades":
                        spades++;
                        break;
                    case "♥":
                    case "Hearts":
                        hearts++;
                        break;
                    case "♦":
                    case "Diamonds":
                        diamonds++;
                        break;
                    case "♣":
                    case "Clubs":
                        clubs++;
                        break;
                    default:
                        break;
                }
            }

            int maxSuit = Math.max(Math.max(spades, hearts), Math.max(diamonds, clubs));

            System.out.println(player.getName() + ": ♠" + spades
                + " ♥" + hearts + " ♦" + diamonds + " ♣" + clubs
                + " -> highest suit count = " + maxSuit);

            if (maxSuit > highestCount) {
                highestCount = maxSuit;
                bonusWinners.clear();
                bonusWinners.add(player);
            } else if (maxSuit == highestCount) {
                bonusWinners.add(player);
            }
        }

        if (bonusWinners.size() == 1) {
            bonusWinners.get(0).addPoints(5);
            System.out.println("Bonus: " + bonusWinners.get(0).getName() + " gets +5 points!");
        } else {
            System.out.print("Tie for highest suit count! ");
            for (Player player : bonusWinners) {
                player.addPoints(2);
                System.out.print(player.getName() + " ");
            }
            System.out.println("each get +2 points!");
        }
    }

    private void displayFinalScores() {
        System.out.println("\n=== Final Scores ===");

        players.sort((p1, p2) -> p2.getScore() - p1.getScore());

        for (int i = 0; i < players.size(); i++) {
            Player player = players.get(i);
            System.out.println((i + 1) + ". " + player.getName()
                + " - " + player.getScore() + " points");
        }

        int highestScore = players.get(0).getScore();

        ArrayList<Player> winners = new ArrayList<>();
        for (Player player : players) {
            if (player.getScore() == highestScore) {
                winners.add(player);
            }
        }

        System.out.println();
        if (winners.size() == 1) {
            System.out.println("Winner: " + winners.get(0).getName()
                + " with " + highestScore + " points!");
        } else {
            System.out.print("It's a draw between: ");
            for (Player player : winners) {
                System.out.print(player.getName() + " ");
            }
            System.out.println("with " + highestScore + " points each!");
        }
    }

    public void start() {
        setupPlayers();
        deck = new Deck();
        deck.shuffle();
        playRounds();
        displayCollectedCards();
        longestSequenceBonus();
        highestSuitCountBonus();
        displayFinalScores();
        scanner.close();
    }

    public void play() {
        start();
    }

    public static void main(String[] args) {
        Game game = new Game();
        game.start();
    }
}
