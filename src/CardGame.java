package src;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;

/**
 * The CardGame class executes the Main method for the program, asks for, and 
 * verifies user input for the number of players in the game and the pack that 
 * is used to deal the cards.
 *
 * @version 1.0
 * @author Thomas Farrar
 * @author Owen Gibson
 */
public class CardGame {
    private int numPlayers;
    private Queue<Card> pack = new LinkedList<Card>();
    private ArrayList<CardDeck> decks = new ArrayList<CardDeck>();
    private ArrayList<Player> players = new ArrayList<Player>();
    private ArrayList<Thread> threads = new ArrayList<Thread>();

    /**
     * Constructor for the CardGame class.
     */
    public CardGame() {
    }

    /**
     * Sets the number of players in the game.
     * @param The number of players in the game.
     */
    public void setNumPlayers(int numPlayers) {
        this.numPlayers = numPlayers;
    }

    /**
     * Gets the number of players in the game.
     * @return The number of players in the game.
     */
    public int getNumPlayers() {
        return numPlayers;
    }

    /**
     * Gets the pack for the game.
     * @return The pack for the game.
     */
    public Queue<Card> getPack() {
        return pack;
    }

    /**
     * Adds a card to the pack.
     * @param card The card to be added to the pack.
     */
    public void addToPack(Card card) {
        pack.add(card);
    }

    /**
     * Gets a list of all the decks in the game.
     * @return An ArrayList of all the decks in the game.
     */
    public ArrayList<CardDeck> getDecks() {
        return decks;
    }

    /**
     * Gets a list of all the players in the game.
     * @return An ArrayList of all the players in the game.
     */
    public ArrayList<Player> getPlayers() {
        return players;
    }

    /**
     * Gets a list of all the threads in the game.
     * @return An ArrayList of all the threads in the game.
     */
    public ArrayList<Thread> getThreads() {
        return threads;
    }

    /**
     * Instantiates the decks and adds them to a list of all the decks 
     * in the game.
     * @param numDecks The number of decks to be created.
     */
    public void createDecks(int numDecks) {
        for (int i = 0; i < numDecks; i++) {
            decks.add(new CardDeck(i+1));
        }
    }

    /**
     * Instantiates the players and adds them to a list of all the players 
     * in the game.
     * @param numPlayers The number of players to be created.
     */
    public void createPlayers(int numPlayers) {
        for (int i = 0; i < numPlayers; i++) {
            /* Check if last player because their right deck will be 1. */
            if (i == numPlayers - 1) {
                players.add(new Player(i+1, decks.get(i), decks.get(0), numPlayers));
            } else {
                players.add(new Player(i+1, decks.get(i), decks.get(i+1), numPlayers));
            }
        }
    }

    /**
     * Instantiates the threads for each player and adds them to a list of all 
     * the threads in the game.
     */
    public void createThreads() {
        for (Player player : players) {
            Thread thread = new Thread(player);
            threads.add(thread);
        }
    }

    /**
     * Starts all the threads for the game.
     */
    public void startThreads() {
        for (Thread thread : threads) {
            thread.start();
        }
    }

    /**
     * The main method for the program. Controls the flow of the game and 
     * starts the threads for each player.
     * @param args An array of command-line arguments for the application. 
     */
    public static void main(String[] args) {
        CardGame cardGame = new CardGame();
        cardGame.setNumPlayers(cardGame.requestNumPlayers());
        cardGame.requestPack();
        cardGame.createDecks(cardGame.getNumPlayers());
        cardGame.createPlayers(cardGame.getNumPlayers());
        cardGame.dealCards();
        cardGame.createThreads();
        cardGame.startThreads();
    }  

    /**
     * Requests user input for number of players and checks if it's a valid number.
     * @return The number of players in the game.
     */
    public int requestNumPlayers() {
        Scanner inputPlayers = new Scanner(System.in);
        int numPlayers = 0;
        System.out.println("Please enter the number of players:");
        try {
            numPlayers = inputPlayers.nextInt();
            if (!(validateNumPlayers(numPlayers))) {
                requestNumPlayers();
            }
        } catch (InputMismatchException e) {
            System.out.println("Invalid input, enter an integer");
            requestNumPlayers();
        }
        return numPlayers;
    }

    /**
     * Checks if the number is not below 2.
     * @Param The number to be checked.
     * @return True if the number is not below 2 or false if it is.
     */
    public boolean validateNumPlayers(int numPlayers) {
        if (numPlayers < 2) {
            System.out.println("Invalid number, must be at least 2 players in the game");
            return false;
        } else {
            return true;
        }
    }

    /**
     * Reads the pack line by line, checks the validity of the pack, and 
     * creates a card object and adds it to a pack queue.
     */
    public void requestPack() {
        Scanner packScanner = new Scanner(System.in);
        System.out.println("Please enter location of pack to load:");
        String fileName = packScanner.nextLine();
        /* Check that the file exists */
        if (!(checkFileExistence(fileName))) {
            System.out.println("File not found");
            requestPack();
        } else {
            /* Check that the file has a valid number of rows */
            if (!(checkValidRows(fileName))) {
                System.out.println("Invalid pack, pack contains invalid number of rows");
                requestPack();
            } else {
                try {
                    File file = new File(fileName);
                    Scanner reader = new Scanner(file);
                    while (reader.hasNextLine()) {
                        String line = reader.nextLine();
                        /* Convert the line in file to int */
                        int value = Integer.parseInt(line);
                        /* Check if there are negative numbers in pack. */
                        if (!(checkIfNegative(value))) {
                            System.out.println("Invalid pack, contains negative numbers");
                            requestPack();
                        } else {
                            addToPack(new Card(value));
                        }
                    }
                    reader.close();
                } catch (NumberFormatException | FileNotFoundException e) {
                    System.out.println("Invalid pack, contains data other than numbers");
                    requestPack();
                }
            }
        }
    }

    /**
     * Checks if a file exists.
     * @param The name of the file.
     * @return True if the file exits, false if not.
     */
    public boolean checkFileExistence(String fileName) {
        Path filePath = Paths.get(fileName);
        if (Files.exists(filePath)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Checks that a number isn't negative.
     * @param The number to be checked.
     * @return True if the number is not negative, false if it is.
     */
    public boolean checkIfNegative(int number) {
        if (number > 0) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Check that there are a valid number of rows in a file.
     * @param fileName The name of the file to be checked.
     * @return True if there is a valid number of rows, false if not.
     */
    public boolean checkValidRows(String fileName) {
        long count = 0;
        try {
            Path file = Paths.get(fileName);
            count = Files.lines(file).count();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (count == getNumPlayers() * 8) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Deals the cards out to the player's hands and each deck.
     */
    public void dealCards() {
        /* Deal cards to players in a round-robin fashion */
        for (int i = 1; i <= 4; i++) {
            for (int j = 0; j < numPlayers; j++) {
                players.get(j).addCard(pack.poll());
                if (i == 4) {
                    players.get(j).setHand(players.get(j).getHand());
                }
            } 
        }
        /* Deal remaining cards to decks in a round-robin fashion */
        for (int i = 1; i <= 4; i++) {
            for (int j = 0; j < numPlayers; j++) {
                decks.get(j).addCard(pack.poll());
                if (i == 4) {
                    decks.get(j).setDeck(decks.get(j).getDeck());
                }
            }
        }
    }
}