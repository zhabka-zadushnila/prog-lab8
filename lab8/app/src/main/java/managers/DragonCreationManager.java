package managers;

import exceptions.NullForbiddenException;
import exceptions.RangeExceededException;
import structs.classes.*;
import utils.InputChecker;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Basically class that is used only for dragon creation process
 */
public class DragonCreationManager {
    /**
     * Static method that goes through the whole process of dragon creation.
     *
     * @param input {@link Iterator} that would be used to access all the info about dragon
     * @return returns {@link Dragon} object
     */
    static public Dragon inputDragon(Iterator<String> input) {
        String name = null;
        Double coordinateX = null;
        Long coordinateY = null;
        Integer age = null;
        Color color = null;
        DragonType type = null;
        DragonCharacter character = null;
        Integer caveDepth = null;
        Double treasures = null;
        String tmpInput = "";
        System.out.println("Name:");
        while (name == null) {
            try {
                tmpInput = input.next();
            } catch (NoSuchElementException e) {
                System.out.println("Creation process was interrupted, nothing was added");
                return null;
            }
            try {
                name = InputChecker.inputNonNullChecker(tmpInput);
            } catch (NullForbiddenException e) {
                System.out.println(e);
            }

        }
        System.out.println("Coordinate X (double):");
        while (coordinateX == null) {
            try {
                tmpInput = input.next();
            } catch (NoSuchElementException e) {
                System.out.println("Creation process was interrupted, nothing was added");
                return null;
            }
            try {
                coordinateX = Double.parseDouble(tmpInput);
            } catch (NumberFormatException e) {
                System.out.println("Неправильный формат числа");
            } catch (Exception e) {
                System.out.println(e);
            }
        }
        System.out.println("Coordinate Y (Long, must be not null and less than 984):");
        while (coordinateY == null) {
            try {
                tmpInput = input.next();
            } catch (NoSuchElementException e) {
                System.out.println("Creation process was interrupted, nothing was added");
                return null;
            }
            try {
                coordinateY = InputChecker.inputRangeChecker(tmpInput, null, 984.0, true, Long.class);
            } catch (NumberFormatException e) {
                System.out.println("Неправильный формат числа");
            } catch (Exception e) {
                System.out.println(e);
            }
        }
        System.out.println("Age (integer, must be more than 0 and not null):");
        while (age == null) {
            try {
                tmpInput = input.next();
            } catch (NoSuchElementException e) {
                System.out.println("Creation process was interrupted, nothing was added");
                return null;
            }
            try {
                age = InputChecker.inputRangeChecker(tmpInput, 0.0, null, true, Integer.class);
            } catch (NumberFormatException e) {
                System.out.println("Неправильный формат числа");
            } catch (RangeExceededException e) {
                System.out.println(e);
            }
        }
        System.out.println("Color (enum, non null, options are: black, yellow, orange, brown):");
        while (color == null) {
            try {
                tmpInput = input.next();
            } catch (NoSuchElementException e) {
                System.out.println("Creation process was interrupted, nothing was added");
                return null;
            }
            try {
                color = InputChecker.getEnum(tmpInput, Color.class);
            } catch (NoSuchElementException e) {
                System.out.println("Такого элемента нет. Выберите из представленных ранее.");
            } catch (Exception e) {
                System.out.println(e);
            }
        }
        System.out.println("Type (enum, non null, options are: water, underground, air, fire):");
        while (type == null) {
            try {
                tmpInput = input.next();
            } catch (NoSuchElementException e) {
                System.out.println("Creation process was interrupted, nothing was added");
                return null;
            }
            try {
                type = InputChecker.getEnum(tmpInput, DragonType.class);
            } catch (NoSuchElementException e) {
                System.out.println("Такого элемента нет. Выберите из представленных ранее.");
            } catch (Exception e) {
                System.out.println(e);
            }
        }
        System.out.println("Character (enum, non null, options are: evil, chaotic_evil, fickle):");
        while (character == null) {
            try {
                tmpInput = input.next();
            } catch (NoSuchElementException e) {
                System.out.println("Creation process was interrupted, nothing was added");
                return null;
            }
            try {
                character = InputChecker.getEnum(tmpInput, DragonCharacter.class);
            } catch (NoSuchElementException e) {
                System.out.println("Такого элемента нет. Выберите из представленных ранее.");
            } catch (Exception e) {
                System.out.println(e);
            }
        }
        System.out.println("Cave is being created. It might be null. \nIf at least one parameter will be null, cave will be null.");
        System.out.println("Cave depth (integer, might be null):");
        try {
            tmpInput = input.next();
        } catch (NoSuchElementException e) {
            System.out.println("Creation process was interrupted, nothing was added");
            return null;
        }
        try {
            if (!tmpInput.trim().isBlank()) {
                caveDepth = Integer.parseInt(tmpInput);
            }
        } catch (NumberFormatException e) {
            System.out.println("Неправильный формат числа, записан null");
        } catch (Exception e) {
            System.out.println(e);
        }
        if (caveDepth != null) {
            System.out.println("Cave depth (double, might be null, at least 0.0):");
            try {
                tmpInput = input.next();
            } catch (NoSuchElementException e) {
                System.out.println("Creation process was interrupted, nothing was added");
                return null;
            }
            try {
                if (!tmpInput.trim().isBlank()) {
                    treasures = InputChecker.inputRangeChecker(tmpInput, 0.0, null, false, Double.class);
                }
            } catch (NumberFormatException e) {
                System.out.println("Неправильный формат числа, записан null");
            } catch (Exception e) {
                System.out.println(e);
            }
        }
        DragonCave dragonCave;
        if (caveDepth == null || treasures == null) {
            dragonCave = null;
        } else {
            dragonCave = new DragonCave(caveDepth, treasures);
        }


        return new Dragon(name,
                new Coordinates(coordinateX, coordinateY),
                age,
                color,
                type,
                character,
                dragonCave);
    }


}
