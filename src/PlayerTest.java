package src;

import static org.junit.Assert.*;
import org.junit.Ignore;
import org.junit.Test;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class PlayerTest {

    @Test
    public void testPlayer() {
        CardDeck testDeck1 = new CardDeck(1);
        CardDeck testDeck2 = new CardDeck(2);
        Player player = new Player(1, testDeck1, testDeck2, 2);
        assertEquals(1, player.getPlayerNumber());
        assertEquals(testDeck1, player.getLeftDeck());
        assertEquals(testDeck2, player.getRightDeck());
    }

    @Test
    public void testAddCard() {
        Player player = new Player(0, new CardDeck(1), new CardDeck(2), 2);
        Card card = new Card(0);
        player.addCard(card);
        assertEquals(card, player.getHand().get(0));
    }

    @Test
    public void testDiscardCard() {
        Player player = new Player(0, new CardDeck(1), new CardDeck(2), 2);
        Card preferredCard = new Card(0);
        Card nonPreferredCard = new Card(1);
        player.addCard(preferredCard);
        player.addCard(nonPreferredCard);
        int handSize = player.getHand().size();
        Card discardedCard = player.discardCard();
        assertEquals(nonPreferredCard, discardedCard);
        assertTrue(player.getHand().size() == handSize - 1);
    }

    @Test
    public void testCheckWin() {
        Player player = new Player(0, new CardDeck(1), new CardDeck(2), 2);
        for (int i = 0; i < 4; i++) {
            player.addCard(new Card(0));
        }
        assertTrue(player.checkWin());
    }

    @Test
    public void testCreatePlayerFile() {
        Player player = new Player(0, new CardDeck(1), new CardDeck(2), 2);
        player.createPlayerFile();
        Path filePath = Paths.get("player0_output.txt");
        assertTrue(Files.exists(filePath));
    }

    @Test
    public void testWriteToPlayerFile() {
        Player player = new Player(2, new CardDeck(1), new CardDeck(2), 2);
        File file = player.createPlayerFile();
        player.writeToPlayerFile(file, "test");
        assertTrue(file.length() != 0);
    }

    @Test
    public void testWriteInitialHand() {
      Player player = new Player(2, new CardDeck(1), new CardDeck(2), 2);
      File file = player.createPlayerFile();
        for (int i = 0; i < 4; i++) {
            player.addCard(new Card(0));
        }
      player.writeInitialHand(file);
      assertTrue(file.length() != 0);
    }

    @Test
    public void testWriteTurn() {
      Player player = new Player(3, new CardDeck(1), new CardDeck(2), 2);
      File file = player.createPlayerFile();
        for (int i = 0; i < 4; i++) {
            player.addCard(new Card(0));
        }
      player.writeTurn(file,new Card(1), new Card(2));
      assertTrue(file.length() != 0);
    }

    @Test
    public void testWriteWinner() {
      Player player = new Player(4, new CardDeck(1), new CardDeck(2), 2);
      File file = player.createPlayerFile();
        for (int i = 0; i < 4; i++) {
            player.addCard(new Card(0));
        }
      player.writeWinner(file);
      assertTrue(file.length() != 0);
    }

    @Test
    public void testWriteNonWinner() {
      Player player = new Player(5, new CardDeck(1), new CardDeck(2), 2);
      File file = player.createPlayerFile();
        for (int i = 0; i < 4; i++) {
            player.addCard(new Card(0));
        }
      player.writeNonWinner(file);
      assertTrue(file.length() != 0);
    }
}