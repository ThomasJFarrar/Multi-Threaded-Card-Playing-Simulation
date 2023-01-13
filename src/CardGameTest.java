package src;

import static org.junit.Assert.*;
import org.junit.Ignore;
import org.junit.Test;
import java.io.File;

public class CardGameTest {

    @Test
    public void testValidateNumPlayers() {
        CardGame cardGame = new CardGame();
        assertTrue(cardGame.validateNumPlayers(4));
    }

    @Test
    public void testCheckFileExistance() {
        CardGame cardGame = new CardGame();
        assertFalse(cardGame.checkFileExistence("test.txt"));
        assertTrue(cardGame.checkFileExistence("src/packs/validpacks/4-player-pack.txt"));
    }

    @Test
    public void testCheckIfNegative() {
        CardGame cardGame = new CardGame();
        assertFalse(cardGame.checkIfNegative(-1));
        assertTrue(cardGame.checkIfNegative(1));
    }

    @Test
    public void testCheckValidRows() {
        CardGame cardGame = new CardGame();
        cardGame.setNumPlayers(4);
        File testFile1 = new File("src/packs/validpacks/4-player-pack.txt");
        assertTrue(cardGame.checkValidRows("src/packs/validpacks/4-player-pack.txt"));
    }

    @Test 
    public void testCreateDecks() {
        CardGame cardGame = new CardGame();
        cardGame.createDecks(4);
        assertEquals(4, cardGame.getDecks().size());
    }

    @Test
    public void testCreatePlayers() {
        CardGame cardGame = new CardGame();
        cardGame.setNumPlayers(4);
        cardGame.createDecks(4);
        cardGame.createPlayers(4);
        assertEquals(4, cardGame.getPlayers().size());
    }

    @Test
    public void testDealCards() {
        CardGame cardGame = new CardGame();
        cardGame.setNumPlayers(4);
        for (int i = 0; i < 32; i++) {
            cardGame.addToPack(new Card(i));
        }
        cardGame.createDecks(4);
        cardGame.createPlayers(4);
        cardGame.dealCards();
        assertEquals(0, cardGame.getPack().size());
        assertEquals(4, cardGame.getDecks().get(0).getDeck().size());
        assertEquals(4, cardGame.getPlayers().get(0).getHand().size());
    }

    @Test
    public void testCreateThreads() {
        CardGame cardGame = new CardGame();
        cardGame.createDecks(4);
        cardGame.createPlayers(4);
        cardGame.createThreads();
        assertEquals(4, cardGame.getThreads().size());
    }
}