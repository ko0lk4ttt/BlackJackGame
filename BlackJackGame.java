import java.util.InputMismatchException;
import java.util.Scanner;

public class BlackJackGame {

    private Deck deck;
    private Player player;
    private Player dealer;
    private int playerChips;
    private int dealerChips;
    private int currentBet;

    private BlackJackGame(int initialChips) {
        deck = new Deck();
        deck.shuffle();
        this.playerChips = initialChips;
        this.dealerChips = initialChips;
        this.player = new Player(initialChips);
        this.dealer = new Player(initialChips);
    }

    private void dealInitialCards() {
        player.getHand().addCard(deck.drawCard());
        dealer.getHand().addCard(deck.drawCard());
        player.getHand().addCard(deck.drawCard());
        dealer.getHand().addCard(deck.drawCard());
    }

    private void reloadChips() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Your chips are at 0. Do you want to reload? (yes/no): ");

        String reloadChoice;
        while (true) {
            reloadChoice = scanner.nextLine().toLowerCase();
            if (reloadChoice.equals("yes") || reloadChoice.equals("no")) {
                break;
            } else {
                System.out.println("Invalid choice. Please enter 'yes' or 'no'.");
            }
        }

        if (reloadChoice.equals("yes")) {
            System.out.println("How many chips do you want to reload?");
            int reloadAmount = scanner.nextInt();
            scanner.nextLine(); // Consume newline
            player.addChips(reloadAmount);
            System.out.println("You have reloaded " + reloadAmount + " chips.");
        }
    }



    private void resetDeck() {
        deck = new Deck();
        deck.shuffle();
    }

    private void playPlayerTurn() {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("Your hand: " + player.getHand().toString());

            System.out.print("Do you want to hit or stand? ");
            String choice = scanner.nextLine().toLowerCase();

            if (choice.equals("hit")) {
                player.getHand().addCard(deck.drawCard());
                int handValue = player.getHand().getHandValue();
                if (handValue > 21) {
                    System.out.println("Your hand: " + player.getHand().toString());
                    System.out.println("Your hand value: " + handValue);
                    System.out.println("Bust! You lose!");
                    break;
                }
            } else if (choice.equals("stand")) {
                break;
            } else {
                System.out.println("Invalid choice. Please enter 'hit' or 'stand'.");
            }
        }
    }

    private void playDealerTurn() {
        while (dealer.getHand().getHandValue() < 17) {
            dealer.getHand().addCard(deck.drawCard());
        }
    }

    private void determineWinner() {
        int playerValue = player.getHand().getHandValue();
        int dealerValue = dealer.getHand().getHandValue();

        System.out.println("Player's hand: " + player.getHand().toString() + " (Value: " + playerValue + ")");
        System.out.println("Dealer's hand: " + dealer.getHand().toString() + " (Value: " + dealerValue + ")");

        if (playerValue > 21) {
            System.out.println("Player Bust! Dealer wins!");
            dealer.addChips(currentBet * 2);
        } else if (dealerValue > 21 || playerValue > dealerValue) {
            System.out.println("Player wins!");
            player.addChips(currentBet * 2);
        } else if (playerValue < dealerValue) {
            System.out.println("Dealer wins!");
        } else {
            System.out.println("It's a tie!");
            player.addChips(currentBet);
        }
    }

    public void playRound() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Available chips: " + player.getChips());
        System.out.print("Enter your bet: ");
        int bet = scanner.nextInt();
        scanner.nextLine();
        if (bet > 0 && bet <= player.getChips()) {
            currentBet = bet;
            player.placeBet(currentBet);
        } else {
            System.out.println("Invalid bet amount. Betting canceled.");
            return;
        }

        dealInitialCards();

        playPlayerTurn();

        playDealerTurn();

        determineWinner();

        resetDeck();


        System.out.println("Your chips: " + player.getChips());

        player.getHand().reset();
        dealer.getHand().reset();
    }


    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Welcome to Blackjack!");
        int initialChips = 0;
        boolean validInput = false;

        while (!validInput) {
            try {
                System.out.print("Enter your initial chips: ");
                initialChips = scanner.nextInt();
                scanner.nextLine(); // Consume newline
                validInput = true;
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a valid number.");
                scanner.nextLine(); // Clear the invalid input from the buffer
            }
        }

        BlackJackGame game = new BlackJackGame(initialChips);

        while (true) {
            if (game.player.getChips() == 0) {
                game.reloadChips();
                if (game.player.getChips() == 0) {
                    System.out.println("You don't have enough chips to play. Goodbye!");
                    break;
                }
            }

            System.out.println("<----- New Round ----->");
            game.playRound();

            String playAgain;
            while (true) {
                System.out.print("Do you want to play another round? (yes/no): ");
                playAgain = scanner.nextLine().toLowerCase();
                if (playAgain.equals("yes") || playAgain.equals("no")) {
                    break;
                } else {
                    System.out.println("Invalid choice. Please enter 'yes' or 'no'.");
                }
            }

            if (!playAgain.equals("yes")) {
                int finalChips = game.player.getChips();
                int chipsChange = finalChips - initialChips;
                String resultMessage = chipsChange > 0 ? "won" : "lost";
                chipsChange = Math.abs(chipsChange);

                System.out.println("You're leaving the table with " + finalChips + " chips.");
                System.out.println("You " + resultMessage + " " + chipsChange + " chips.");

                break;
            }
        }

        System.out.println("Thank you for playing!");
        scanner.close();
    }
}