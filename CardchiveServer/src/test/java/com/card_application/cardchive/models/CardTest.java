/**
 * This class is used to test the Card class
 * @author Andy Huang
 */

package com.card_application.cardchive.models;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CardTest {

    Card card;

    // Before each test
    @BeforeEach
    void setUp() {
        card = new Card();
    }

    // After each test
    @AfterEach
    void tearDown() {
        card = null;
    }


    // Testing the methods
    @Test
    void cardMethodsTest() {
        card.setID(1);
        assertEquals(1, card.getID());

        card.setName("Card1");
        assertEquals("Card1", card.getName());

        card.setType("Light");
        assertEquals("Light", card.getType());

        card.setRarity(9);
        assertEquals(9, card.getRarity());

        card.setPictureURL("link");
        assertEquals("link", card.getPictureURL());

        card.setHealthPoints(100);
        assertEquals(100, card.getHealthPoints());

        card.setLevel(10);
        assertEquals(10, card.getLevel());

        card.setAttackPoints(1000);
        assertEquals(1000, card.getAttackPoints());

        card.setDefensePoints(1000);
        assertEquals(1000, card.getDefensePoints());
    }

}