package src;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

/**
 * The CardDeck class contains the deck's unique number, a list of cards in 
 * the deck and allows cards to be picked up and discarded from the deck. It 
 * also creates text files with the deck's contents at the end of the game.
 *
 * @version 1.0
 * @author Thomas Farrar
 * @author Owen Gibson
 */
public class CardDeck {
    private int deckNumber;
    private ArrayList<Card> deck;

    /**
     * Constructor for the CardDeck class. Assigns the deck number and creates 
     * an empty deck.
     * @param deckNumber The unique deck number.
     */
    public CardDeck(int deckNumber) {
        this.deckNumber = deckNumber;
        deck = new ArrayList<Card>();
    }

    /**
     * Gets the deck's number.
     * @return The deck number.
     */
    public int getDeckNumber() {
        return deckNumber;
    }

    /**
     * Gets the deck of cards.
     * @return An ArrayList of cards in the deck.
     */
    public synchronized ArrayList<Card> getDeck() {
        return deck;
    }

    public void setDeck(ArrayList<Card> deck) {
        this.deck = deck;
    }

    /**
     * Adds a card to the deck.
     * @param card The card to be added to the deck.
     */
    public synchronized void addCard(Card card) {
        deck.add(card);
    }

    /**
     * Draws the first card in the deck.
     * @return The card drew.
     */
    public synchronized Card drawCard() {
        Card cardDrew = deck.get(0);
        deck.remove(cardDrew);
        return cardDrew;
    }

    /**
     * Creates a new output file for the deck.
     * @return The output file created.
     */
    public File createDeckFile() {
        File deckFile = null;
        try {
            deckFile = new File("deck" + String.valueOf(deckNumber) + "_output.txt");
            deckFile.createNewFile();
        } catch (IOException e) {
            System.out.println("An error occured while creating player output files.");
        }
        return deckFile;
    }

    /**
     * Writes a new line to a text file.
     * @param deckFile The file to where the text will be written to.
     */
    public void writeDeckFile(File deckFile) {
        try {
            FileWriter writer = new FileWriter(deckFile, true);
            writer.write("deck" + String.valueOf(deckNumber) + " contents: " + String.valueOf((deck.get(0)).getValue()) + " " + String.valueOf((deck.get(1)).getValue()) + " " + String.valueOf((deck.get(2)).getValue()) + " " + String.valueOf((deck.get(3)).getValue()));
            writer.close();
        } catch (IOException e) {
            System.out.println("An error occured while writing to file.");
        }
    }
}