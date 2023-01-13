package src;

/**
 * The Card class contains the value of a card in the pack.
 *
 * @version 1.0
 * @author Thomas Farrar
 * @author Owen Gibson
 */
public class Card {
    private int value;

    /**
     * Constructor for the Card class. Assigns the value to the card.
     * @param value The card value.
     */
    public Card(int value) {
    this.value = value;
    }

    /**
     * Gets the value of the card.
     * @return The value of the card.
     */
    public int getValue() {
        return value;
    }
}