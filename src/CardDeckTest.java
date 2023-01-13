package src;

import static org.junit.Assert.*;
import org.junit.Ignore;
import org.junit.Test;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class CardDeckTest {

    @Test
    public void testCardDeck() {
        CardDeck deck = new CardDeck(0);
        assertEquals(0, deck.getDeckNumber());
    }

    @Test
    public void testAddCard() {
        CardDeck deck = new CardDeck(0);
        Card card = new Card(0);
        deck.addCard(card);
        assertEquals(card, deck.getDeck().get(0));
    }

    @Test
    public void drawCard() {
        CardDeck deck = new CardDeck(0);
        Card card = new Card(0);
        deck.addCard(card);
        int deckSize = deck.getDeck().size();
        Card drawnCard = deck.drawCard();
        assertEquals(card, drawnCard);
        assertTrue(deck.getDeck().size() == deckSize - 1);
    }

    @Test
    public void testCreateDeckFile() {
        CardDeck deck = new CardDeck(0);
        File file = deck.createDeckFile();
        Path filePath = Paths.get("deck0_output.txt");
        assertTrue(Files.exists(filePath));
    }

    @Test
    public void testWriteDeckFile() {
        CardDeck deck = new CardDeck(1);
        deck.addCard(new Card(0));
        deck.addCard(new Card(1));
        deck.addCard(new Card(2));
        deck.addCard(new Card(3));
        File file = deck.createDeckFile();
        deck.writeDeckFile(file);
        assertTrue(file.length() != 0);
    }
}