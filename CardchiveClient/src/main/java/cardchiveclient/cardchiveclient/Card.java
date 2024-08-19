/**
 * This class is used to model what a Card is
 * @author Andy Huang
 */

package cardchiveclient.cardchiveclient;

import com.google.gson.annotations.SerializedName;

public class Card {
    // Attributes
    @SerializedName("id")
    public int ID;

    public String name;
    public String type;
    public int rarity;
    public String pictureURL;
    public int healthPoints;
    public int level;
    public int attackPoints;
    public int defensePoints;


    // Constructors
    public Card() {

    }

    public Card(int ID, String name, String type, int rarity, String pictureURL, int healthPoints, int level, int attackPoints, int defensePoints) {
        this.ID = ID;
        this.name = name;
        this.type = type;
        this.rarity = rarity;
        this.pictureURL = pictureURL;
        this.healthPoints = healthPoints;
        this.level = level;
        this.attackPoints = attackPoints;
        this.defensePoints = defensePoints;
    }


    // Getters and setters
    public int getID() {
        return this.ID;
    }
    public void setID(int ID) {
        this.ID = ID;
    }

    public String getName() {
        return this.name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return this.type;
    }
    public void setType(String type) {
        this.type = type;
    }

    public int getRarity() {
        return this.rarity;
    }
    public void setRarity(int rarity) {
        this.rarity = rarity;
    }

    public String getPictureURL() {
        return this.pictureURL;
    }
    public void setPictureURL(String pictureURL) {
        this.pictureURL = pictureURL;
    }

    public int getHealthPoints() {
        return this.healthPoints;
    }
    public void setHealthPoints(int healthPoints) {
        this.healthPoints = healthPoints;
    }

    public int getLevel() {
        return this.level;
    }
    public void setLevel(int level) {
        this.level = level;
    }

    public int getAttackPoints() {
        return this.attackPoints;
    }
    public void setAttackPoints(int attackPoints) {
        this.attackPoints = attackPoints;
    }

    public int getDefensePoints() {
        return this.defensePoints;
    }
    public void setDefensePoints(int defensePoints) {
        this.defensePoints = defensePoints;
    }

}
