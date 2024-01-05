package org.example.entities;

import lombok.experimental.FieldDefaults;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
class ValueTest {
    Value J;
    Value K;
    Value J2;


    @BeforeEach
    void setUp() {
         J = new Value("J");
         K = new Value("K");
         J2 = new Value("J");
    }

    @Test
    void equals_whenJisTheSameObject_thenEquals() {

        assertEquals(J, J);
    }

    @Test
    void equals_whenValuesAreTheSame_thenEquals() {

        assertEquals(J, J2);
    }

    @Test
    void equals_whenDifferentValues_thenNotEquals() {

        assertNotEquals(J, K);
    }

    @Test
    void equals_whenOneCardIsNull_thenNotEquals() {
        J = null;

        assertNotEquals(J, K);
    }

    @Test
    void equals_whenOneCardIsNotACardClass_thenNotEquals() {
        String Q = "Q";
        assertNotEquals(J, Q);
    }

    @Test
    void compareTo_whenValuesAreTheSame_then0() {

        assertEquals(0, J.compareTo(J));
    }

    @Test
    void compareTo_whenValuesAreMore_then1() {

        assertEquals(1, K.compareTo(J));
    }

    @Test
    void compareTo_whenValuesAreLess_thenMinus1() {
        assertEquals(-1, J.compareTo(K));
    }
}