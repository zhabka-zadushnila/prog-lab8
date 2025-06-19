package structs.classes;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * Main object in our story.
 */
public class Dragon implements Comparable<Dragon>, Serializable {
    private static final long serialVersionUID = 1L;
    public static int idCreator = 0;
    private String ownerLogin;
    private String name; //Поле не может быть null, Строка не может быть пустой
    private Coordinates coordinates; //Поле не может быть null
    private java.time.LocalDate creationDate; //Поле не может быть null, Значение этого поля должно генерироваться автоматически
    private int age; //Значение поля должно быть больше 0
    private Color color; //Поле не может быть null
    private DragonType type; //Поле не может быть null
    private DragonCharacter character; //Поле может быть null
    private DragonCave cave; //Поле может быть null

    public Dragon() {
    }

    public Dragon(String name, Coordinates coordinates, int age, Color color, DragonType type) {
        this.name = name;
        this.coordinates = coordinates;
        this.creationDate = java.time.LocalDate.now();
        this.age = age;
        this.color = color;
        this.type = type;
        this.character = null;
        this.cave = null;
    }

    public Dragon(String name, Coordinates coordinates, int age, Color color, DragonType type, DragonCave cave) {
        this(name, coordinates, age, color, type);
        this.cave = cave;
    }

    public Dragon(String name, Coordinates coordinates, int age, Color color, DragonType type, DragonCharacter character) {
        this(name, coordinates, age, color, type);
        this.character = character;
    }

    public Dragon(String name, Coordinates coordinates, int age, Color color, DragonType type, DragonCharacter character, DragonCave cave) {
        this(name, coordinates, age, color, type);
        this.character = character;
        this.cave = cave;
    }


    public DragonType getType() {
        return type;
    }

    public void setType(DragonType type) {
        this.type = type;
    }

    public String getOwnerLogin() {
        return ownerLogin;
    }

    public void setOwnerLogin(String ownerLogin) {
        this.ownerLogin = ownerLogin;
    }

    public DragonCharacter getCharacter() {
        return character;
    }

    public void setCharacter(DragonCharacter character) {
        this.character = character;
    }

    @Override
    public String toString() {
        String characterLocal = "null";
        String caveLocal = "null";
        if (this.character != null) {
            characterLocal = this.character.toString();
        }
        if (this.cave != null) {
            caveLocal = this.cave.toString();
        }

        return "Name: " + this.name + "\n" +
                "Coordinates: " + this.coordinates.toString() + "\n" +
                "Creation date: " + this.creationDate.toString() + "\n" +
                "Age: " + this.age + "\n" +
                "Color: " + this.color.toString() + "\n" +
                "Type: " + this.type.toString() + "\n" +
                "Character: " + characterLocal + "\n" +
                "Cave:" + caveLocal
                ;
    }


    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Coordinates getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(Coordinates coordinates) {
        this.coordinates = coordinates;
    }

    public LocalDate getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(LocalDate creationDate) {
        this.creationDate = creationDate;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public DragonCave getCave() {
        return cave;
    }

    public void setCave(DragonCave cave) {
        this.cave = cave;
    }

    @Override
    public int compareTo(Dragon other) {
        return this.name.compareTo(other.getName());
    }


}
