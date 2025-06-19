package classes;
import java.time.LocalDate;

/**
 * Main object in our story.
 */
public class Dragon implements Comparable<Dragon> {
    private static int idCreator = 0;
    private int id; //Значение поля должно быть больше 0, Значение этого поля должно быть уникальным, Значение этого поля должно генерироваться автоматически
    private String name; //Поле не может быть null, Строка не может быть пустой
    private Coordinates coordinates; //Поле не может быть null
    private java.time.LocalDate creationDate; //Поле не может быть null, Значение этого поля должно генерироваться автоматически
    private int age; //Значение поля должно быть больше 0
    private Color color; //Поле не может быть null
    private DragonType type; //Поле не может быть null
    private DragonCharacter character; //Поле может быть null
    private DragonCave cave; //Поле может быть null

    public DragonType getType() {
        return type;
    }

    public Dragon(){
        this.id = ++idCreator;
    }

    public Dragon(String name, Coordinates coordinates, int age, Color color, DragonType type){
        this.id = ++idCreator;
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

    public DragonCharacter getCharacter() {
        return character;
    }

    @Override
    public String toString(){
        String characterLocal= "null";
        String caveLocal = "null";
        if(this.character != null){
            characterLocal = this.character.toString();
        }
        if(this.cave != null){
            caveLocal = this.cave.toString();
        }

        return  "Id: " + id + "\n" +
                "Name: " + this.name + "\n" +
                "Coordinates: " + this.coordinates.toString() + "\n"+
                "Creation date: " + this.creationDate.toString() + "\n"+
                "Age: " + this.age + "\n"+
                "Color: " + this.color.toString() + "\n" +
                "Type: " + this.type.toString() + "\n" +
                "Character: " + characterLocal + "\n"+
                "Cave:" + caveLocal
                ;
    }

    public int getId(){
        return this.id;
    }

    public String getName(){
        return this.name;
    }

    public static int getIdCreator() {
        return idCreator;
    }

    public static void setIdCreator(int idCreator) {
        Dragon.idCreator = idCreator;
    }

    public void setId(int id) {
        this.id = id;
        if(id > idCreator){
            Dragon.idCreator = id;
        }
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

    public void setType(DragonType type) {
        this.type = type;
    }

    public void setCharacter(DragonCharacter character) {
        this.character = character;
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

    public boolean equals(Dragon d){
        return id == d.getId();
    }

}
