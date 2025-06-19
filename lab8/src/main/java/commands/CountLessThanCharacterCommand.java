package commands;

import classes.Dragon;
import classes.DragonCharacter;
import exceptions.NullArgsForbiddenException;
import managers.CollectionManager;
import utils.InputChecker;

import java.util.Map;

/**
 * Outputs amount of elements in which character field contains value less than given
 */
public class CountLessThanCharacterCommand extends BasicCommand {
    public CountLessThanCharacterCommand(CollectionManager collectionManager){
        super("count_less_than_character", "count_less_than_character character : вывести количество элементов, значение поля character которых меньше заданного", collectionManager);
        }

    /**
     * Checks your input with {@link InputChecker}, goes through an entrySet and outputs a number.
     * @param args an array of strings (words that were separated by spaces). Usually it is ignored in commands that do not need any args, and those who need, get only as much as they need (others are being ignored)
     * @throws NullArgsForbiddenException thrown when no args were given when needed.
     * @throws IllegalArgumentException thrown if such character enum type does not exist. Check {@link DragonCharacter}
     */
    @Override
    public void execute(String[] args) throws NullArgsForbiddenException, IllegalArgumentException {

        if(args.length == 0){
            throw new NullArgsForbiddenException();
        }
        if (args[0].trim().isBlank()) {
            throw new NullArgsForbiddenException();
        }


        DragonCharacter character = InputChecker.getEnum(args[0], DragonCharacter.class);

        int amount = 0;
        Map<String, Dragon> map = collectionManager.getCollection();
        for(var entry : map.entrySet()){
            if(entry.getValue().getCharacter().compareTo(character) < 0){
                amount++;
            }
        }
        System.out.println(amount);
    }

}
