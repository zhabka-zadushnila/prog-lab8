package structs.classes;

/**
 * Contains types: <br>
 * <b>EVIL</b><br>
 * <b>CHAOTIC_EVIL</b><br>
 * <b>FICKLE</b><br>
 */
public enum DragonCharacter {
    EVIL,
    CHAOTIC_EVIL,
    FICKLE;

    public int compareTo(Color other) {
        return this.ordinal() - other.ordinal();
    }

}