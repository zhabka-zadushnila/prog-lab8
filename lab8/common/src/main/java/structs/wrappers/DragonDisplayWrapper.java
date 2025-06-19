package structs.wrappers;


import structs.classes.Dragon;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.stream.Stream;

public class DragonDisplayWrapper implements Serializable {
    private final String key;
    private final Dragon originalDragon;
    private static final long serialVersionUID = 1L;

    public DragonDisplayWrapper(String key, Dragon dragon) {
        this.key = key;
        this.originalDragon = dragon;
    }

    public String getKey() {
        return key;
    }

    public String getOwner() {
        return originalDragon.getOwnerLogin();
    }

    public String getName() {
        return originalDragon.getName();
    }

    public double getX() {
        return originalDragon.getCoordinates().getX();
    }

    public long getY() {
        return originalDragon.getCoordinates().getY();
    }

    public LocalDate getCreationDate() {
        return originalDragon.getCreationDate();
    }

    public int getAge() {
        return originalDragon.getAge();
    }

    public String getColor() {
        return originalDragon.getColor().name();
    }

    public String getType() {
        return originalDragon.getType().name();
    }

    public String getCharacter() {
        return originalDragon.getCharacter() != null ? originalDragon.getCharacter().name() : "N/A";
    }

    public Integer getDepth() {
        return originalDragon.getCave() != null ? originalDragon.getCave().getDepth() : null;
    }

    public Double getTreasures() {
        return originalDragon.getCave() != null ? originalDragon.getCave().getNumberOfTreasures() : null;
    }

    public Dragon getOriginalDragon() {
        return originalDragon;
    }
    public Dragon getValue(){ return  originalDragon;}


    public Stream<String> getStreamOfFields() {
        return Stream.of(
                getKey(),
                getOwner(),
                getName(),
                String.valueOf(getX()),
                String.valueOf(getY()),
                getCreationDate().toString(),
                String.valueOf(getAge()),
                getColor(),
                getType(),
                getCharacter(),
                getDepth() != null ? String.valueOf(getDepth()) : "",
                getTreasures() != null ? String.valueOf(getTreasures()) : ""
        );
    }
}