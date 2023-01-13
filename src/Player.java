package src;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.InterruptedException;
import java.lang.String;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

/**
 * The Player class contains the player's unique number, their hand of cards, 
 * the turn of which the game is on, and whether the game has been won or not. 
 * It runs threads for each player, each turn drawing a card and discarding a 
 * card from their hand from and to the appropriate decks, and writing a log 
 * of each turn to the player's individual text file until the game has 
 * been won.
 *
 * @version 1.0
 * @author Thomas Farrar
 * @author Owen Gibson
 */
public class Player implements Runnable {
    private int playerNumber;
    private ArrayList<Card> hand;
    private static AtomicBoolean win = new AtomicBoolean();
    private CardDeck rightDeck;
    private CardDeck leftDeck;
    private static CyclicBarrier barrier;
    private static int winningPlayer;

    /**
     * Constructor for the Player class. Assigns the player number, creates
     * an empty hand, assigns the decks left and right of the player and
     * the barrier used for the threads.
     * @param playerNumber The unique player number.
     * @param leftDeck The deck to the left of the player.
     * @param rightDeck The deck to the right of the player.
     * @param numPlayers The number of players in the game.
     */
    public Player(int playerNumber, CardDeck leftDeck, CardDeck rightDeck, int numPlayers) {
        this.playerNumber = playerNumber;
        hand = new ArrayList<Card>();
        this.rightDeck = rightDeck;
        this.leftDeck = leftDeck;
        barrier = new CyclicBarrier(numPlayers);
    }

    /**
     * Gets the player's number.
     * @return The player's number.
     */
    public int getPlayerNumber() {
        return playerNumber;
    }

    /**
     * Gets the player's hand of cards.
     * @return An ArrayList of cards in the player's hand.
     */
    public ArrayList<Card> getHand() {
        return hand;
    }

    /**
     * Gets the deck to the left of the player.
     * @return The deck to the left of the player.
     */
    public CardDeck getLeftDeck() {
        return leftDeck;
    }

    /**
     * Gets the deck to the right of the player.
     * @return The deck to the right of the player.
     */
    public CardDeck getRightDeck() {
        return rightDeck;
    }

    /**
     * Gets whether the game has been won or not.
     * @return True or false if the game has been won.
     */
    public static synchronized boolean getWin() {
        return win.get();
    }

    /**
     * Adds a card to the player's hand.
     * @param card The card to be added.
     */
    public synchronized void addCard(Card card) {
        hand.add(card);
    }

    /**
     * Set's the player's hand.
     * @param The hand to be set.
     */
    public void setHand(ArrayList<Card> hand) {
        this.hand = hand;
    }

    /**
     * Sets the winning player.
     * @param The winning player number.
     */
    public static void setWinningPlayer(int playerNumber) {
        Player.winningPlayer = playerNumber;
    }

    /**
     * Gets the winning player.
     * @return The number of the winning player.
     */
    public static int getWinningPlayer() {
        return winningPlayer;
    }

    /**
     * Discards a random card from the player's hand, unless it is 
     * player's preferred card.
     * @return The discarded card.
     */
    public synchronized Card discardCard() {
        ArrayList<Card> nonPreferredCards = new ArrayList<Card>();
        /* Add cards which value doesn't match the player number to an ArrayList. */
        for (Card card : hand) {
            if (card.getValue() != playerNumber) {
                nonPreferredCards.add(card);
            }
        }
        /* Generate a random index and select that card, then remove it from hand. */
        Random randomNum = new Random();
        Card discardedCard = nonPreferredCards.get(randomNum.nextInt(nonPreferredCards.size()));
        hand.remove(discardedCard);
        return discardedCard;
    }

    /**
     * Checks whether the player has four matching values on cards in their hand.
     * @return true or false whether they meet the win condition.
     */
    public synchronized boolean checkWin() {
        if (((hand.get(0)).getValue() == (hand.get(1)).getValue()) && ((hand.get(1)).getValue() == (hand.get(2)).getValue()) && ((hand.get(2)).getValue() == (hand.get(3)).getValue())) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Controls the flow of the threads and each turn.
     */
    @Override
    public void run() {
        File playerFile = createPlayerFile();
        writeInitialHand(playerFile);
        if (checkWin()) {
            winningPlayer = playerNumber;
            win.set(true);
        }
        while (win.get() == false) {
            Card addedCard = leftDeck.drawCard();
            addCard(addedCard);
            Card discardedCard = discardCard();
            rightDeck.addCard(discardedCard);
            writeTurn(playerFile, addedCard, discardedCard);
            try {
                barrier.await();
                if (checkWin()) {
                    winningPlayer = playerNumber;
                    win.set(true);
                }
                barrier.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (BrokenBarrierException e) {
                e.printStackTrace();
            }
        }
        /* Create the deck files */
        leftDeck.writeDeckFile(leftDeck.createDeckFile());
        /* Check if this thread is the winning player */
        if (playerNumber == winningPlayer) {
            writeWinner(playerFile);
        } else {
            writeNonWinner(playerFile);
        }
    }

    /**
     * Creates a new output file for the player.
     * @return The output file created.
     */
    public synchronized File createPlayerFile() {
        File playerFile = null;
        try {
            playerFile = new File("player" + String.valueOf(playerNumber) + "_output.txt");
            playerFile.createNewFile();
        } catch (IOException e) {
            System.out.println("An error occured while creating player output files.");
        }
        return playerFile;
    }

    /**
     * Writes a new line to a text file.
     * @param playerFile The file to where the text will be written to.
     * @param text The text to be written to the file.
     */
    public synchronized void writeToPlayerFile(File playerFile, String text) {
        try {
            FileWriter writer = new FileWriter(playerFile, true);
            writer.write(text);
            writer.close();
        } catch (IOException e) {
            System.out.println("An error occured while writing to file.");
        }
    }

    /**
     * Writes the initial hand of the player to their text file.
     * @param playerFile The file for the initial hand to be written to.
     */
    public synchronized void writeInitialHand(File playerFile) {
        writeToPlayerFile(playerFile, "player " + String.valueOf(playerNumber) + " initial hand " + String.valueOf((hand.get(0)).getValue()) + " " + String.valueOf((hand.get(1)).getValue()) + " " + String.valueOf((hand.get(2)).getValue()) + " " + String.valueOf((hand.get(3)).getValue()));
    }

    /**
     * Writes the turn for the player to their text file.
     * @param playerFile The file for the turn to be written to.
     * @param addedCard The card that was added to the player's hand that turn.
     * @param discardedCard The card that was discarded from the player's hand that turn.
     */
    public synchronized void writeTurn(File playerFile, Card addedCard, Card discardedCard) {
        writeToPlayerFile(playerFile, "\nplayer " + String.valueOf(playerNumber) + " draws a " + String.valueOf(addedCard.getValue()) + " from deck " + leftDeck.getDeckNumber());
        writeToPlayerFile(playerFile, "\nplayer " + String.valueOf(playerNumber) + " discards a " + String.valueOf(discardedCard.getValue()) + " to deck " + rightDeck.getDeckNumber());
        writeToPlayerFile(playerFile, "\nplayer " + String.valueOf(playerNumber) + " current hand is " + String.valueOf((hand.get(0)).getValue()) + " " + String.valueOf((hand.get(1)).getValue()) + " " + String.valueOf((hand.get(2)).getValue()) + " " + String.valueOf((hand.get(3)).getValue()));
    }

    /**
     * Writes the final lines for the winning player to their text file.
     * @param playerFile The file for the final lines for the winning player to be written to.
     */
    public void writeWinner(File playerFile) {
        System.out.println("player " + String.valueOf(winningPlayer) + " wins");
        writeToPlayerFile(playerFile, "\nplayer " + String.valueOf(winningPlayer) + " wins");
        writeToPlayerFile(playerFile, "\nplayer " + String.valueOf(winningPlayer) + " exits");
        writeToPlayerFile(playerFile, "\nplayer " + String.valueOf(playerNumber) + " final hand: " + String.valueOf((hand.get(0)).getValue()) + " " + String.valueOf((hand.get(1)).getValue()) + " " + String.valueOf((hand.get(2)).getValue()) + " " + String.valueOf((hand.get(3)).getValue()));
    }

    /**
     * Writes the final lines for a non-winning player to their text file.
     * @param playerFile The file for the final lines for a non-winning player to be written to.
     */
    public synchronized void writeNonWinner(File playerFile) {
        writeToPlayerFile(playerFile, "\nplayer " + String.valueOf(winningPlayer) + " has informed player " + String.valueOf(playerNumber) + " that player " + String.valueOf(winningPlayer) + " has won");
        writeToPlayerFile(playerFile, "\nplayer " + String.valueOf(playerNumber) + " exits");
        writeToPlayerFile(playerFile, "\nplayer " + String.valueOf(playerNumber) + " hand: " + String.valueOf((hand.get(0)).getValue()) + " " + String.valueOf((hand.get(1)).getValue()) + " " + String.valueOf((hand.get(2)).getValue()) + " " + String.valueOf((hand.get(3)).getValue()));
    }
}