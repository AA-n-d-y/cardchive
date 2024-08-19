/**
 * This class is used to manage our server's endpoints
 * @author Andy Huang
 */

package com.card_application.cardchive.controllers;

import com.card_application.cardchive.models.Card;
import com.card_application.cardchive.models.CardList;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;

@RestController
public class CardController {

    /// Setup
    CardList cardList = null;
    Gson gson = new GsonBuilder().setPrettyPrinting().create();


    /// Endpoints

    // Get request (returning the list of all the cards)
    @GetMapping("/api/card/all")
    public List<Card> getAllCards(HttpServletResponse response) {
        try {
            // Parsing the json file to insert the cards into cardList
            FileReader reader = new FileReader("src/main/resources/data/cards.json");
            cardList = gson.fromJson(reader, CardList.class);
            reader.close();

            // Returning the cards if the list is not empty
            if (cardList != null && cardList.getCards() != null) {
                response.setStatus(200);
                return cardList.getCards();
            }
            // Else return null
            else {
                System.out.println("No cards found");
                response.setStatus(200);
                return null;
            }
        }

        catch (Exception obj) {
            // If an exception occurs
            System.out.println(obj);
            response.setStatus(500);
            return null;
        }
    }


    // Get request (returning the card with the given ID)
    @GetMapping("/api/card/{id}")
    public Card getCardById(@PathVariable int id, HttpServletResponse response) {
        try {
            // Parsing the json file to insert the cards into cardList
            FileReader reader = new FileReader("src/main/resources/data/cards.json");
            cardList = gson.fromJson(reader, CardList.class);
            reader.close();

            // If the list is not empty
            if (cardList != null && cardList.getCards() != null) {
                // If the card with the ID is found, return it
                for (Card card : cardList.getCards()) {
                    if (card.getID() == id) {
                        response.setStatus(200);
                        return card;
                    }
                }
            }

            // Else return null
            if (cardList == null) {
                cardList = new CardList();
            }
            System.out.println("Card with ID " + id + " not found");
            response.setStatus(404);
            return null;
        }

        catch (Exception obj) {
            // If an exception occurs
            System.out.println(obj);
            response.setStatus(500);
            return null;
        }
    }


    // Post request (adding a new card)
    @PostMapping("/api/card/add")
    public void addCard(@RequestBody Card card, HttpServletResponse response) {
        try {
            // Parsing the json file to insert the cards into cardList
            FileReader reader = new FileReader("src/main/resources/data/cards.json");
            cardList = gson.fromJson(reader, CardList.class);
            reader.close();
            if (cardList == null) {
                cardList = new CardList();
            }

            // Checking to see if a card with the same ID already exists and returning if it does
            if (cardList != null && !cardList.getCards().isEmpty()) {
                if (cardList.getCard(card.getID()) != null) {
                    response.setStatus(409);
                    return;
                }
            }

            // Else add the card to the list and write it to the file
            cardList.addCard(card);
            FileWriter writer = new FileWriter("src/main/resources/data/cards.json");
            gson.toJson(cardList, writer);
            writer.close();
            response.setStatus(201);
            return;
        }

        catch (Exception obj) {
            // If an exception occurs
            System.out.println(obj);
            response.setStatus(500);
            return;
        }
    }


    // Put request (editing the card with the given ID)
    @PutMapping("/api/card/edit/{id}")
    public void editCard(@PathVariable int id, @RequestBody Card card, HttpServletResponse response) {
        try {
            // Parsing the json file to insert the cards into cardList
            FileReader reader = new FileReader("src/main/resources/data/cards.json");
            cardList = gson.fromJson(reader, CardList.class);
            reader.close();

            // If the list is null, return
            if (cardList == null) {
                System.out.println("Cards list is empty");
                response.setStatus(404);
                return;
            }

            // Checking to see if a card with the same ID already exists and returning if there is not
            if (cardList.getCard(card.getID()) == null) {
                System.out.println("Card with ID " + id + " not found");
                response.setStatus(404);
                return;
            }

            // Else update the card and write to the file
            int index = 0;
            for (int i = 0; i < cardList.getCards().size(); i++) {
                if (cardList.getCards().get(i).getID() == id) {
                    index = i;
                }
            }
            cardList.getCards().set(index, card);
            FileWriter writer = new FileWriter("src/main/resources/data/cards.json");
            gson.toJson(cardList, writer);
            writer.close();
            response.setStatus(200);
            return;
        }

        catch (Exception obj) {
            // If an exception occurs
            System.out.println(obj);
            response.setStatus(500);
            return;
        }
    }


    // Delete request (deleting the card with the given ID)
    @DeleteMapping("/api/card/{id}")
    public void deleteCard(@PathVariable int id, HttpServletResponse response) {
        try {
            // Parsing the json file to insert the cards into cardList
            FileReader reader = new FileReader("src/main/resources/data/cards.json");
            cardList = gson.fromJson(reader, CardList.class);
            reader.close();

            // If the list is null, return
            if (cardList == null) {
                System.out.println("Cards list is empty");
                response.setStatus(404);
                return;
            }

            // Checking to see if a card with the same ID already exists and returning if there is not
            if (cardList.getCard(id) == null) {
                System.out.println("Card with ID " + id + " not found");
                response.setStatus(404);
                return;
            }

            // Else delete the card and write to the file
            int index = 0;
            for (int i = 0; i < cardList.getCards().size(); i++) {
                if (cardList.getCards().get(i).getID() == id) {
                    index = i;
                }
            }
            cardList.getCards().remove(index);
            FileWriter writer = new FileWriter("src/main/resources/data/cards.json");
            gson.toJson(cardList, writer);
            writer.close();
            response.setStatus(204);
            return;
        }

        catch (Exception obj) {
            // If an exception occurs
            System.out.println(obj);
            response.setStatus(500);
            return;
        }
    }

}
