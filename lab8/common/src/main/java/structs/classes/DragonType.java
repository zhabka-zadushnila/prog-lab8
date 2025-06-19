package structs.classes;

public enum DragonType {
    WATER,
    UNDERGROUND,
    AIR,
    FIRE;

    public int compareTo(Color other) {
        return this.ordinal() - other.ordinal();
    }
}