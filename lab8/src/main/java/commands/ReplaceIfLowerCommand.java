package commands;

import classes.Dragon;
import exceptions.NoSuchElementException;
import exceptions.NullArgsForbiddenException;
import managers.CollectionManager;
import managers.CommandManager;
import managers.DragonCreationManager;


/**
 * Replaces all the elements that are lower
 */
public class ReplaceIfLowerCommand extends BasicCommand{
    CommandManager commandManager;
    public ReplaceIfLowerCommand(CollectionManager collectionManager, CommandManager commandManager){
        super("replace_if_lowe", "replace_if_lowe null {element} : заменить значение по ключу, если новое значение меньше старого", collectionManager);
        this.commandManager = commandManager;
    }


    /**
     * Uses basic {@link CollectionManager}
     * @param args - an array of strings (words that were separated by spaces). Usually it is ignored in commands that do not need any args, and those who need, get only as much as they need (others are being ignored)
     * @throws NullArgsForbiddenException - basically do not give empty input
     * @throws NoSuchElementException - basically didn't find an element
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
            if(collectionManager.getElement(inputId).compareTo(dragon) < 0) {
                collectionManager.replaceElement(inputId, dragon);
            }
            else{
                System.out.println("New dragon is not less than new one, nothing has changed :)");
            }
        }else{
            throw new NoSuchElementException();
        }
    }
}
