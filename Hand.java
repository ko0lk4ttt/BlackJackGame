import java.util.ArrayList;
import java.util.List;

public class Hand {
    private List<Card> cards;

    public Hand() {
        cards = new ArrayList<>();
    }

    public void addCard(Card card) {
        cards.add(card);
    }
    public void reset() {
        cards.clear();
    }
    public int getHandValue() {
        int value = 0;
        int numAces = 0;

        for (Card card : cards) {
            switch (card.getRank()) { // Use the getRank() method
                case "A":
                    numAces++;
                    value += 11;
                    break;
                case "K":
                case "Q":
                case "J":
                    value += 10;
                    break;
                default:
                    value += Integer.parseInt(card.getRank()); // Use the getRank() method
            }
        }

        while (numAces > 0 && value > 21) {
            value -= 10;
            numAces--;
        }
        return value;
    }

    @Override
    public String toString() {
        StringBuilder handString = new StringBuilder();
        for (Card card : cards) {
            handString.append(card.toString()).append(", ");
        }
        return handString.toString();
    }
}
