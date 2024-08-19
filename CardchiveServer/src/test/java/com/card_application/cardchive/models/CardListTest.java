/**
 * This class is used to test the CardList class
 * @author Andy Huang
 */

package com.card_application.cardchive.models;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CardListTest {

    CardList cardList;

    // Before each test
    @BeforeEach
    void setUp() {
        cardList = new CardList();
    }

    // After each test
    @AfterEach
    void tearDown() {
        cardList = null;
    }


    // Testing the methods
    @Test
    void cardListMethodsTest() {
        // Getting a card that doesn't exist
        assertEquals(null, cardList.getCard(0));

        // Adding and getting a card
        Card tempCard = new Card(1, "Card1", "Light", 9, "Link", 100, 10, 1000, 1000);
        cardList.addCard(tempCard);
        assertEquals(tempCard, cardList.getCards().get(0));
        assertEquals(tempCard, cardList.getCard(1));

        // Editing a card
        tempCard.setName("CardEdit");
        tempCard.setType("Dark");
        cardList.editCard(1, "CardEdit", "Dark", 9, "Link", 100, 10, 1000, 1000);
        assertEquals(tempCard, cardList.getCard(1));

        // Removing a card
        cardList.removeCard(1);
        assertEquals(null, cardList.getCard(1));
        assertEquals(true, cardList.getCards().isEmpty());

        // Testing parameterized constructor
        CardList copyCardList = new CardList(cardList.getCards());
        for (int i = 0; i < copyCardList.getCards().size(); i++) {
            assertEquals(copyCardList.getCards().get(i).getID(), cardList.getCards().get(i).getID());
            assertEquals(copyCardList.getCards().get(i).getName(), cardList.getCards().get(i).getName());
            assertEquals(copyCardList.getCards().get(i).getType(), cardList.getCards().get(i).getType());
            assertEquals(copyCardList.getCards().get(i).getRarity(), cardList.getCards().get(i).getRarity());
            assertEquals(copyCardList.getCards().get(i).getPictureURL(), cardList.getCards().get(i).getPictureURL());
            assertEquals(copyCardList.getCards().get(i).getHealthPoints(), cardList.getCards().get(i).getHealthPoints());
            assertEquals(copyCardList.getCards().get(i).getLevel(), cardList.getCards().get(i).getLevel());
            assertEquals(copyCardList.getCards().get(i).getAttackPoints(), cardList.getCards().get(i).getAttackPoints());
            assertEquals(copyCardList.getCards().get(i).getDefensePoints(), cardList.getCards().get(i).getDefensePoints());
        }
    }

}