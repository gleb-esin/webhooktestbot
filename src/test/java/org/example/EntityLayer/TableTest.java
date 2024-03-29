package org.example.EntityLayer;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TableTest {

    @Test
    void testToString() {
        String expected = "<b>Карты на столе:</b>" + System.lineSeparator() + "Отбитые карты: " + System.lineSeparator()+"Неотбитые карты: " + System.lineSeparator()+"Козырь <b>[♣]</b>";

        Table table = new Table();
        table.setTrump(new Suit("♣", true));

        assertEquals(expected, table.toString());
    }
}