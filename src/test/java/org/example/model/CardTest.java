package org.example.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class CardTest {

    @Test
    void compareTo() {
        Card spades7 = new Card("♠", "7", true);
        Card spades10 = new Card("♠", "10", true);
        Card heatsA = new Card("♥", "A", false);
        Card diamondsK = new Card("♦", "K", false);

        assertTrue(spades7.compareTo(spades10) < 0);
        assertTrue(spades10.compareTo(heatsA) > 0);
        assertTrue(heatsA.compareTo(diamondsK) > 0);



    }


    @Test
    void testToString() {
        Card heartsJ = new Card("♥", "J", false);

        assertEquals("<b>[ J♥]</b>", heartsJ.toString());
    }
}