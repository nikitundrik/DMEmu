package net.lockethefirst.lockesdmemu.ui.home;

public class MonsterResponse {
    String name;
    String alignment;
    int hit_points;
    int armor_class;
    int strength;
    int dexterity;
    int constitution;
    int intelligence;
    int wisdom;
    int charisma;

    @Override
    public String toString() {
        return name + " appeared! HP: " + hit_points + " AC: " + armor_class + " STR: " + strength
                + " DEX: " + dexterity + " CON: " + constitution + " INT: " + intelligence + " WIS: "
                + wisdom + " CHA: " + charisma + ", " + alignment;
    }
}
