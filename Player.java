public class Player {
    private Hand hand;
    private int chips;

    public Player(int initialChips) {
        hand = new Hand();
        chips = initialChips;
    }

    public void addChips(int amount) {
        chips += amount;
    }

    public int getChips() {
        return chips;
    }

    public boolean hasChips() {
        return chips > 0;
    }

    public void placeBet(int betAmount) {
        if (betAmount <= chips) {
            chips -= betAmount;
            System.out.println("Bet placed: " + betAmount);
        } else {
            System.out.println("Not enough chips to place this bet.");
        }
    }

    public Hand getHand() {
        return hand;
    }
}
