# Cardchive

Cardchive is an all-in-one card collecting application, allowing users to add new cards to their collection, edit cards, delete cards, and display all of their cards. 
The card formatting is inspired from popular trading card games such as Yu-Gi-Oh!, Pokemon, etc. 
<br><br>


## Tech Stack

**Frontend:** JavaFX <br>
**Backend:** Java Spring Boot <br>
**Testing Frameworks/Libraries:** MockMvc, JUnit <br>
**Other Tools:** Gson - used to parse JSON data for the JSON file database <br>
<br>


## Features

### Adding a card
Users can add a card by clicking on the "Add Cards" button. A new window will be created, allowing users to enter the card's details. Certain fields only accept integer inputs, 
so a pop-up alert will be shown if they try to add a card with invalid field types. To add a picture for the card, users will need to click the "Add Picture" button. From there,
users can choose a file from their desktop to upload. When all of the fields have been successfully filled in, users can press the "Add" button to add the card to their collection.
If the card is successfully added, a pop-up will be shown. Likewise, if a card fails to add, a pop-up will be shown as well. Once a card is added, users can press the "Display Cards"
button in the main window to display their updated collection.
<br><br>
![image](https://github.com/user-attachments/assets/d28d05f9-5bb1-4c8b-a209-fbd83a146d42)
<br><br>

### Displaying the cards
Once a user has successfully added a card, they can click the "Display Cards" button to update their view of their collection. Users should press this button whenever they make changes that affect the database.
It will allow users to see the most up to date version of their collection.
<br><br>
![image](https://github.com/user-attachments/assets/a37b225f-ce79-4fc9-bea9-806432d8ef5d)
<br><br>

### Getting a card's details
To get a card's details, click on the "View Details" button after displaying all of the cards. A new window will be opened, allowing users to view their card's details.
<br><br>

### Editing and deleting a card
When viewing a card's details, users can update the card by clicking the "Update" button. To remove the card from the collection, users can press the "Delete Card" button. 
<br><br>
![image](https://github.com/user-attachments/assets/b0fad504-0a98-464e-9bf1-ceed48340602)
<br><br>


