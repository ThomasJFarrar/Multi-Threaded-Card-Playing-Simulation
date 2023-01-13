package src;

import static org.junit.Assert.*;
import org.junit.Ignore;
import org.junit.Test;

public class CardTest {

    @Test
    public void testCard() {
        Card card = new Card(0);
        assertEquals(0, card.getValue());
    }
}