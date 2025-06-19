package commands;

import Interpreters.CommandInterpreter;
import classes.Dragon;
import exceptions.NullArgsForbiddenException;
import managers.CollectionManager;
import managers.CommandManager;
import managers.DragonCreationManager;

import java.util.Arrays;

/**
 * Command that is used for adding some stuff in collection. Operates with both {@link CommandManager} and {@link CollectionManager}.
 */

public class InsertCommand extends BasicCommand{
    CommandManager commandManager;
    public InsertCommand(CollectionManager collectionManager, CommandManager commandManager){
        super("insert", "insert null {element} : добавить новый элемент с заданным ключом", collectionManager);
        this.commandManager = commandManager;
    }


    /**
     * Uses specific {@link DragonCreationManager} and some Iterators magic <i>*being meguka is suffering*</i>
     * @param args an array of strings (words that were separated by spaces). Usually it is ignored in commands that do not need any args, and those who need, get only as much as they need (others are being ignored)
     * @throws NullArgsForbiddenException thrown when no args were provided.
     */
    @Override
    public void execute(String[] args) throws NullArgsForbiddenException {

        if(args.length == 0){
            throw new NullArgsForbiddenException();
        }
        if (args[0].trim().isBlank()) {
            throw new NullArgsForbiddenException();
        }
        String id = args[0];




        if(collectionManager.hasElement(id)){
            System.out.println("Element with such name exists already! If you will continue, it will be overwritten!\nContinue? [y/N]:");
            String input = commandManager.getCommandInterpreter().getInputIterator().next();
            if (input.trim().isBlank() || input.equalsIgnoreCase("n")){
                return;
            }
        }

        Dragon dragon = DragonCreationManager.inputDragon(commandManager.getCommandInterpreter().getInputIterator());
        if(dragon == null){
            return;
        }
        collectionManager.addElement(id, dragon);


    }



}
