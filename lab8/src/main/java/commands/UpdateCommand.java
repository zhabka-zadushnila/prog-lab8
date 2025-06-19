package commands;

import classes.Dragon;
import exceptions.NoSuchElementException;
import exceptions.NullArgsForbiddenException;
import managers.CollectionManager;
import managers.CommandManager;
import managers.DragonCreationManager;

/**
 * Used for updating element with specific id
 */
public class UpdateCommand extends BasicCommand{
    CommandManager commandManager;
    public UpdateCommand(CollectionManager collectionManager, CommandManager commandManager){
        super("update", "update id {element} : обновить значение элемента коллекции, id которого равен заданному", collectionManager);
        this.commandManager = commandManager;
    }


    /**
     * Uses both {@link CommandManager} to get iterator and {@link CollectionManager} for access to db
     * @param args - an array of strings (words that were separated by spaces). Usually it is ignored in commands that do not need any args, and those who need, get only as much as they need (others are being ignored)
     * @throws NullArgsForbiddenException - we hate it when you don't know what to put in command
     * @throws NoSuchElementException - we hate it when you put some crap in command
     */

    @Override
    public void execute(String[] args) throws NullArgsForbiddenException, NoSuchElementException {
        if (args.length == 0) {
            throw new NullArgsForbiddenException();
        }

        if (args[0].trim().isBlank()) {
            throw new NullArgsForbiddenException();
        }

        String inputId = args[0];
        if(collectionManager.hasElement(inputId)){
            Dragon dragon = DragonCreationManager.inputDragon(commandManager.getCommandInterpreter().getInputIterator());
            if (dragon == null){
                return;
            }
            collectionManager.replaceElement(inputId, dragon);
        }else{
            throw new NoSuchElementException();
        }
    }
}
