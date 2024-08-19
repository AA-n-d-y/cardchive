/**
 * This class is used to test the Card controller
 * @author Andy Huang
 */

package com.card_application.cardchive.controllers;

import com.card_application.cardchive.models.Card;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import org.springframework.http.MediaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.nio.charset.Charset;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;


@SpringBootTest
@AutoConfigureMockMvc
public class CardControllerTest {
    /// Setup

    public static final MediaType APPLICATION_JSON_UTF8 = new MediaType(MediaType.APPLICATION_JSON.getType(), MediaType.APPLICATION_JSON.getSubtype(), Charset.forName("utf8"));

    // Mock of our server
    @Autowired
    MockMvc mockMvc;

    // Controller
    @Autowired
    private CardController controller;


    /// Functionality tests

    // Testing the server
    @Test
    public void serverTest() throws Exception {
        assertThat(mockMvc).isNotNull();
    }

    // Testing the controller
    @Test
    void controllerTest() throws Exception {
        assertThat(controller).isNotNull();
    }


    // Testing getting all the cards
    @Test
    void getCardsTest() throws Exception {
        // Mock the get request
        mockMvc.perform(MockMvcRequestBuilders.get("/api/card/all"))

                .andExpect(MockMvcResultMatchers.status().isOk());
    }


    // Testing adding a new card, editing the new card, getting the card, and deleting the new card
    @Test
    void addEditGetDeleteCardTest() throws Exception {
        // Creating a json string to send to the endpoint
        Card card = new Card(-999, "Card1", "Light", 9, "Link", 100, 10, 1000, 1000);
        ObjectMapper mapper = new ObjectMapper();
        String jsonCard = mapper.writeValueAsString(card);

        // Mock the post request to add a card
        mockMvc.perform(MockMvcRequestBuilders.post("/api/card/add")
                .content(jsonCard)
                .contentType(MediaType.APPLICATION_JSON))

                .andExpect(MockMvcResultMatchers.status().isCreated());

        // Mock the post request to add the same card
        mockMvc.perform(MockMvcRequestBuilders.post("/api/card/add")
                        .content(jsonCard)
                        .contentType(MediaType.APPLICATION_JSON))

                .andExpect(MockMvcResultMatchers.status().isConflict());


        // Mock the put request to edit the card that exists
        mockMvc.perform(MockMvcRequestBuilders.put("/api/card/edit/-999")
                        .content(jsonCard)
                        .contentType(MediaType.APPLICATION_JSON))

                .andExpect(MockMvcResultMatchers.status().isOk());

        // Mock the put request to edit a card that does not exist
        Card extraCard = new Card(-1000, "Card1", "Light", 9, "Link", 100, 10, 1000, 1000);
        String jsonExtraCard = mapper.writeValueAsString(extraCard);
        mockMvc.perform(MockMvcRequestBuilders.put("/api/card/edit/-1000")
                        .content(jsonExtraCard)
                        .contentType(MediaType.APPLICATION_JSON))

                .andExpect(MockMvcResultMatchers.status().isNotFound());


        // Mock the get request to get the card that exists
        mockMvc.perform(MockMvcRequestBuilders.get("/api/card/-999"))

                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(content().json(jsonCard));

        // Mock the get request to get a card that does not exist
        mockMvc.perform(MockMvcRequestBuilders.get("/api/card/-1000"))

                .andExpect(MockMvcResultMatchers.status().isNotFound());


        // Mock the delete request to delete the card that exists
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/card/-999"))

                .andExpect(MockMvcResultMatchers.status().isNoContent());

        // Mock the delete request to delete a card that does not exist
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/card/-1000"))

                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

}
