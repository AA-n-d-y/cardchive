/**
 * This class is used to model a list that contains objects of the Card class
 * @author Andy Huang
 */

package com.card_application.cardchive.models;

import java.util.ArrayList;
import java.util.List;

public class CardList {
    // Attributes
    private List<Card> cards = null;


    // Constructors
    public CardList() {
        this.cards = new ArrayList<>();
    }

    public CardList(List<Card> cards) {
        this.cards = new ArrayList<>();
        for (Card card : cards) {
            this.cards.add(new Card(card.getID(), card.getName(), card.getType(), card.getRarity(),
                    card.getPictureURL(), card.getHealthPoints(), card.getLevel(), card.getAttackPoints(), card.getDefensePoints()));
        }
    }


    // Functions

    // Function to return the cards
    public List<Card> getCards() {
        return this.cards;
    }


    // Function to return a card with the given ID
    public Card getCard(int ID) {
        for (Card card : cards) {
            // If there is a card with the ID, return it
            if (card.getID() == ID) {
                return card;
            }
        }
        // If there is no card with the ID, return null
        return null;
    }


    // Function to add more cards to the list
    public void addCard(Card card) {
        if (this.cards == null) {
            this.cards = new ArrayList<>();
        }
        this.cards.add(card);
    }


    // Function to update a card with the given ID
    public void editCard(int ID, String name, String type, int rarity, String pictureURL,
                         int healthPoints, int level, int attackPoints, int defensePoints) {
        for (Card card : cards) {
            // If there is a card with the ID, update it
            if (card.getID() == ID) {
                card.setName(name);
                card.setType(type);
                card.setRarity(rarity);
                card.setPictureURL(pictureURL);
                card.setHealthPoints(healthPoints);
                card.setLevel(level);
                card.setAttackPoints(attackPoints);
                card.setDefensePoints(defensePoints);
            }
        }
    }


    // Function to remove a card with the given ID
    public void removeCard(int ID) {
        for (Card card : cards) {
            // If there is a card with the ID, remove it
            if (card.getID() == ID) {
                cards.remove(card);
                return;
            }
        }
    }

}
